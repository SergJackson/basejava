package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
        sqlHelper.exe(
                (SqlHelper.SqlCode<Object>) PreparedStatement::execute,
                "DELETE FROM resume");
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.exe(
                preparedStatement -> {
                    preparedStatement.setString(1, resume.getFullName());
                    preparedStatement.setString(2, resume.getUuid());
                    preparedStatement.execute();
                    return null;
                },
                "UPDATE resume SET full_name =? WHERE uuid =?");
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.exe(
                preparedStatement -> {
                    preparedStatement.setString(1, resume.getUuid());
                    preparedStatement.setString(2, resume.getFullName());
                    preparedStatement.execute();

                    return null;
                },
                "INSERT INTO resume (uuid, full_name) VALUES (?,?)");
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.exe(
                preparedStatement -> {
                    preparedStatement.setString(1, uuid);
                    ResultSet rs = preparedStatement.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    return new Resume(uuid.trim(), rs.getString("full_name").trim());
                },
                "SELECT * FROM resume WHERE uuid =?");
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.exe(
                preparedStatement -> {
                    preparedStatement.setString(1, uuid);
                    preparedStatement.execute();
                    if (preparedStatement.executeUpdate() < 1) {
                        throw new NotExistStorageException(uuid);
                    }
                    return null;
                },
                "DELETE FROM resume WHERE uuid =?");
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.exe(
                preparedStatement -> {
                    ResultSet rs = preparedStatement.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException("DB is empty");
                    }
                    List<Resume> resumeList = new ArrayList<>();
                    do {
                        resumeList.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name").trim()));
                    } while (rs.next());
                    return resumeList;
                },
                "SELECT * FROM resume WHERE 1 = 1 ORDER BY full_name, uuid");
    }

    @Override
    public int size() {
        return sqlHelper.exe(
                preparedStatement -> {
                    ResultSet rs = preparedStatement.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException("DB is empty");
                    }
                    return rs.getInt("cnt");
                },
                "SELECT COUNT(*) as cnt FROM resume");
    }
}
