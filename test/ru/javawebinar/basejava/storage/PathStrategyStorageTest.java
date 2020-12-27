package ru.javawebinar.basejava.storage;

public class PathStrategyStorageTest extends AbstractStorageTest {
    public PathStrategyStorageTest() {
        super(new PathStrategyStorage(STORAGE_DIR.getPath(), new StrategyStorage()));
    }
}