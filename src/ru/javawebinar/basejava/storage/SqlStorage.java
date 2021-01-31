package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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
        String uuid = resume.getUuid();
        Resume old_resume = get(uuid);

        sqlHelper.transactionalExecute(conn -> {
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
            }
            try (PreparedStatement ps = conn.prepareStatement("" +
                    "INSERT " +
                    "  INTO contact (resume_uuid, type, value) " +
                    "  VALUES (?,?,?) " +
                    "ON CONFLICT (resume_uuid, type) DO UPDATE SET " +
                    "  value = EXCLUDED.value")) {
                for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
                    String type_name = e.getKey().name();
                    String value = e.getValue();
                    ps.setString(1, uuid);
                    ps.setString(2, type_name);
                    ps.setString(3, value);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            try (PreparedStatement ps = conn.prepareStatement("" +
                    "DELETE " +
                    "  FROM contact " +
                    "  WHERE resume_uuid =? AND type =?")) {
                for (ContactType contactType : old_resume.getContacts().keySet()) {
                    if (resume.getContact(contactType) == null) {
                        ps.setString(1, uuid);
                        ps.setString(2, contactType.name());
                        ps.addBatch();
                    }
                }
                ps.executeBatch();
            }
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.transactionalExecute(conn -> {
            String uuid = resume.getUuid();
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, uuid);
                ps.setString(2, resume.getFullName());
                ps.execute();
            }
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
                for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
                    ps.setString(1, uuid);
                    ps.setString(2, e.getKey().name());
                    ps.setString(3, e.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
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
                        String value = rs.getString("value");
                        ContactType type = ContactType.valueOf(rs.getString("type"));
                        resume.setContact(type, value);
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
        return sqlHelper.execute("" +
                        "    SELECT * FROM resume r " +
                        " LEFT JOIN contact c " +
                        "        ON r.uuid = c.resume_uuid " +
                        "     ORDER BY full_name,uuid,type",
                preparedStatement -> {
                    ResultSet rs = preparedStatement.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException("DB is empty");
                    }
                    List<Resume> resumeList = new ArrayList<>();
                    String uuid;
                    String uuid_current = "";
                    Resume resume = null;
                    do {
                        uuid = rs.getString("uuid").trim();
                        if (!uuid.equals(uuid_current)) {
                            if (resume != null) resumeList.add(resume);
                            resume = new Resume(uuid, rs.getString("full_name").trim());
                            uuid_current = uuid;
                        }
                        if (resume != null)
                            resume.setContact(ContactType.valueOf(rs.getString("type").trim()), rs.getString("value").trim());
                    } while (rs.next());
                    resumeList.add(resume);
                    return resumeList;
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
}
