package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.basejava.storage.AbstractArrayStorage.STORAGE_LIMIT;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {


    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test
    public void getAllSorted() {
        List<Resume> checkList = new ArrayList<>();
        checkList.add(RESUME_1);
        checkList.add(RESUME_2);
        checkList.add(RESUME_3);
        List<Resume> actualList = storage.getAllSorted();
        Assert.assertEquals(checkList, actualList);
    }

    @Test(expected = StorageException.class)
    public void saveOverflow() {
        try {
            for (int i = 3; i < STORAGE_LIMIT; i++) {                                   // Error if i < 3
                Resume expectedResumes = new Resume("uuid" + Integer.toString(i + 1),"FullName" + Integer.toString(i + 1));
                storage.save(expectedResumes);
            }
        } catch (Exception e) {
            Assert.fail("Error has been before stop filling array: " + e.getMessage());
        }
        storage.save(new Resume("uuid10001","FullName10001" ));
    }

}