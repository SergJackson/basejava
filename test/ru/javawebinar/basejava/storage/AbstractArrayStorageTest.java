package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.lang.reflect.InvocationTargetException;

import static ru.javawebinar.basejava.storage.AbstractArrayStorage.STORAGE_LIMIT;

public abstract class AbstractArrayStorageTest {

    Storage storage;

    static final String UUID_1 = "uuid1";
    static final String UUID_2 = "uuid2";
    static final String UUID_3 = "uuid3";
    static final String DUMMY = "dummy";

    public AbstractArrayStorageTest(Class clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        storage = (Storage) clazz.getDeclaredConstructor().newInstance();
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void clear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void update() {
        storage.update(new Resume(UUID_2));
        Assert.assertEquals(UUID_2, storage.get(UUID_2).getUuid());
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExists() {
        storage.get(DUMMY);
    }

    @Test
    public void save() {
        storage.save(new Resume(DUMMY));
        Assert.assertEquals(4, storage.size());
    }

    @Test(expected = ExistStorageException.class)
    public void saveAlreadyExists() {
        storage.save(new Resume(UUID_1));
    }

    @Test
    public void get() {
        Assert.assertEquals(UUID_3, storage.get(UUID_3).getUuid());
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExists() {
        storage.get(DUMMY);
    }

    @Test
    public void delete() {
        storage.delete(UUID_2);
        Assert.assertEquals(2, storage.size());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExists() {
        storage.delete(DUMMY);
    }

    @Test
    public void getAll() {
        Resume[] resumes = {new Resume(UUID_1), new Resume(UUID_2), new Resume(UUID_3)};
        Assert.assertArrayEquals(resumes, storage.getAll());
    }

    @Test
    public void size() {
        Assert.assertEquals(3, storage.size());
    }

    @Test //(expected = StorageException.class)
    public void getOverflow() {
        try {
            for (int i = 3; i < STORAGE_LIMIT; i++) {                                   // Error if i < 3
                Resume resume = new Resume("uuid" + Integer.toString(i + 1));
                storage.save(resume);
            }
        } catch (Exception e) {
            Assert.fail("Error has been before stop filling array");
        }
        try {
            storage.save(new Resume("uuid10001"));
        } catch (StorageException e) {
            Assert.assertEquals("Storage overflow", e.getMessage());
        }
    }

}