package ru.javawebinar.basejava.storage;

public class FileStrategyStorageTest extends AbstractStorageTest {
    public FileStrategyStorageTest() {
        super(new FileStrategyStorage(STORAGE_DIR, new StrategyStorage()));
    }
}