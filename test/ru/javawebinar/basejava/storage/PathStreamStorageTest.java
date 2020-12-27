package ru.javawebinar.basejava.storage;

public class PathStreamStorageTest extends AbstractStorageTest {
    public PathStreamStorageTest() {
        super(new PathStreamStorage(STORAGE_DIR.toPath()));
    }
}