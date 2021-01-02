package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.strategy.ObjectStreamStrategy;
import ru.javawebinar.basejava.storage.strategy.PathStorage;

public class PathObjectStreamStrategyTest extends AbstractStorageTest {
    public PathObjectStreamStrategyTest() {
        super(new PathStorage(STORAGE_DIR.getPath(), new ObjectStreamStrategy()));
    }
}