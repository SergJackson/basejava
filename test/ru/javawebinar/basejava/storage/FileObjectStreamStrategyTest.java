package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.strategy.ObjectStreamStrategy;

public class FileObjectStreamStrategyTest extends AbstractStorageTest {
    public FileObjectStreamStrategyTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStreamStrategy()));
    }
}