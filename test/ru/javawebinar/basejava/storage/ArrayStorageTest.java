package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import static ru.javawebinar.basejava.storage.AbstractArrayStorage.STORAGE_LIMIT;

public class ArrayStorageTest extends AbstractArrayStorageTest {

    public ArrayStorageTest() {
        super(new ArrayStorage());
    }

    @Test(expected = StorageException.class)
    public void getOverflow() {
        try {
            for (int i = 3; i < STORAGE_LIMIT; i++) {                                   // Error if i < 3
                Resume expectedResumes = new Resume("uuid" + Integer.toString(i + 1));
                storage.save(expectedResumes);
            }
        } catch (Exception e) {
            Assert.fail("Error has been before stop filling array");
        }
        storage.save(new Resume("uuid10001"));
    }

}