package ru.javawebinar.basejava.storage;

import com.sun.deploy.util.StringUtils;
import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    public final SqlHelper sqlHelper;

    public SqlStorage() {
        // Не работает WEB без подгрузки драйвера БД
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(
                Config.get().getDbUrl(),
                Config.get().getDbUser(),
                Config.get().getDbPassword()));
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
                resume = new Resume(uuid, rs.getString("full_name").trim());
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
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM contact WHERE resume_uuid =?")) {
            preparedStatement.setString(1, uuid);
            preparedStatement.execute();
        }
    }

    private void insertContacts(Connection connection, Resume resume) throws SQLException {
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

    private void deleteSections(Connection connection, String uuid) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM section WHERE resume_uuid =?")) {
            preparedStatement.setString(1, uuid);
            preparedStatement.execute();
        }
    }

    private void insertSections(Connection connection, Resume resume) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO section (resume_uuid, type, content) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> e : resume.getSections().entrySet()) {
                SectionType type = e.getKey();
                String content = null;
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        content = ((SingleLineSection) e.getValue()).getContent();
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        content = StringUtils.join(((ListSection) e.getValue()).getContent(), "\n");
                        break;
                }
                ps.setString(1, resume.getUuid());
                ps.setString(2, type.name());
                ps.setString(3, content);
                ps.addBatch();
            }
            ps.executeBatch();
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
            AbstractSection section = null;
            switch (sectionType) {
                case PERSONAL:
                case OBJECTIVE:
                    section = new SingleLineSection(content);
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    section = new ListSection(Arrays.asList(content.split("\n")));
                    break;
            }
            resume.setSection(sectionType, section);
        }
    }

}