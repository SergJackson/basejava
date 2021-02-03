package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    public final SqlHelper sqlHelper;

    public SqlStorage() {
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
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("" +
                        "    SELECT * FROM resume r " +
                        " LEFT JOIN contact c " +
                        "        ON r.uuid = c.resume_uuid " +
                        "     WHERE r.uuid =? ",
                preparedStatement -> {
                    preparedStatement.setString(1, uuid);
                    ResultSet rs = preparedStatement.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume resume = new Resume(uuid.trim(), rs.getString("full_name").trim());
                    do {
                        if (rs.getString("type") != null) {
                            addContact(resume, rs);
                        }
                    } while (rs.next());
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

    private void addContact(Resume resume, ResultSet rs) throws SQLException {
        resume.setContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
    }

}