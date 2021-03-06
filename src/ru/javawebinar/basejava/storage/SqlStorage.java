package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.AbstractSection;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.SectionType;
import ru.javawebinar.basejava.sql.SqlHelper;
import ru.javawebinar.basejava.util.JsonParser;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    public final SqlHelper sqlHelper;

    public SqlStorage() {
        this(Config.get().getDbUrl(), Config.get().getDbUser(), Config.get().getDbPassword());
    }

    public SqlStorage(String url, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(url, user, password));
    }

    @Override
    public void clear() {
        sqlHelper.execute(
                "DELETE FROM resume");
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionalExecute(conn -> {
            String uuid = resume.getUuid();
            try (PreparedStatement ps = conn.prepareStatement("" +
                    "UPDATE resume " +
                    "  SET full_name =? " +
                    "  WHERE uuid =?")) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, uuid);
                ps.executeUpdate();
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                }
                deleteContacts(conn, uuid);
                insertContacts(conn, resume);
                deleteSections(conn, uuid);
                insertSections(conn, resume);
            }
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                ps.execute();
            }
            insertContacts(conn, resume);
            insertSections(conn, resume);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionalExecute(conn -> {
            Resume resume;
            try (PreparedStatement preparedStatement = conn.prepareStatement("" +
                    "  SELECT * FROM resume WHERE uuid =?")) {
                preparedStatement.setString(1, uuid);
                ResultSet rs = preparedStatement.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume = new Resume(uuid, rs.getString("full_name"));
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement("" +
                    "  SELECT * FROM contact WHERE resume_uuid =?")) {
                preparedStatement.setString(1, uuid);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    addContact(resume, rs);
                }
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement("" +
                    "  SELECT * FROM section WHERE resume_uuid =?")) {
                preparedStatement.setString(1, uuid);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    addSection(resume, rs);
                }
            }
            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute(
                "DELETE FROM resume WHERE uuid =?",
                preparedStatement -> {
                    preparedStatement.setString(1, uuid);
                    if (preparedStatement.executeUpdate() == 0) {
                        throw new NotExistStorageException(uuid);
                    }
                    return null;
                });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionalExecute(conn -> {
            Map<String, Resume> resumeMap = new LinkedHashMap<>();
            try (PreparedStatement preparedStatement = conn.prepareStatement("" +
                    "  SELECT * FROM resume " +
                    "    ORDER BY full_name,uuid")) {
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    resumeMap.put(uuid, new Resume(uuid, rs.getString("full_name")));
                }
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement("" +
                    "  SELECT * FROM contact")) {
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    Resume resume = resumeMap.get(rs.getString("resume_uuid"));
                    addContact(resume, rs);
                }
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement("" +
                    "  SELECT * FROM section")) {
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    Resume resume = resumeMap.get(rs.getString("resume_uuid"));
                    addSection(resume, rs);
                }
            }
            return new ArrayList<>(resumeMap.values());
        });
    }

    @Override
    public int size() {
        return sqlHelper.execute(
                "SELECT COUNT(*) as cnt FROM resume",
                preparedStatement -> {
                    ResultSet rs = preparedStatement.executeQuery();
                    return rs.next() ? rs.getInt("cnt") : 0;
                });
    }

    private void deleteContacts(Connection connection, String uuid) throws SQLException {
        deleteAttributes(connection, uuid, "DELETE FROM contact WHERE resume_uuid =?");
    }

    private void insertContacts(Connection connection, Resume resume) throws SQLException {
        if (resume.getContacts().size() > 0) {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
                for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
                    ps.setString(1, resume.getUuid());
                    ps.setString(2, e.getKey().name());
                    ps.setString(3, e.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
    }

    private void deleteSections(Connection connection, String uuid) throws SQLException {
        deleteAttributes(connection, uuid, "DELETE FROM section WHERE resume_uuid =?");
    }

    private void deleteAttributes(Connection connection, String uuid, String sql) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, uuid);
            preparedStatement.execute();
        }
    }

    private void insertSections(Connection connection, Resume resume) throws SQLException {
        if (resume.getSections().size() > 0) {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO section (resume_uuid, type, content) VALUES (?,?,?)")) {
                for (Map.Entry<SectionType, AbstractSection> e : resume.getSections().entrySet()) {
                    ps.setString(1, resume.getUuid());
                    ps.setString(2, e.getKey().name());
                    AbstractSection section = e.getValue();
                    ps.setString(3, JsonParser.write(section, AbstractSection.class));
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
    }

    private void addContact(Resume resume, ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        if (type != null) {
            resume.setContact(ContactType.valueOf(type), rs.getString("value"));
        }
    }

    private void addSection(Resume resume, ResultSet rs) throws SQLException {
        String content = rs.getString("content");
        if (content != null) {
            SectionType sectionType = SectionType.valueOf(rs.getString("type"));
            resume.setSection(sectionType, JsonParser.read(content, AbstractSection.class));
        }
    }

}