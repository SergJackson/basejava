package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    public final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
     }

    public interface SqlCode<T> {
        T exe(PreparedStatement preparedStatement) throws SQLException;
    }

    public <T> T exe(SqlCode<T> code, String sql) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            return code.exe(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public void exe(String sql) {
        exe(PreparedStatement::execute, sql);
    }

}
