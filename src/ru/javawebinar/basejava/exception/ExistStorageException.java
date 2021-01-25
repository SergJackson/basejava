package ru.javawebinar.basejava.exception;

import java.sql.SQLException;

public class ExistStorageException extends StorageException {

    public ExistStorageException(String uuid, SQLException e) {
        super("Resume " + uuid + " already exist", uuid, e);
    }

    public ExistStorageException(String uuid) {
        super("Resume " + uuid + " already exist", uuid, null);
    }
}
