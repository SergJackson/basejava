package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

import static ru.javawebinar.basejava.storage.AbstractArrayStorage.STORAGE_LIMIT;

public class ListStorageTest extends AbstractArrayStorageTest {

    public ListStorageTest() {
        super(new ListStorage());
    }

    @Test(timeout = 3000) //As an experiment
    public void getOverflow() {
        try {
            for (int i = 3; i < STORAGE_LIMIT; i++) {
                Resume expectedResumes = new Resume("uuid" + Integer.toString(i + 1));
                storage.save(expectedResumes);
            }
        } catch (Exception e) {
            Assert.fail("Error has been before stop filling array");
        }
        storage.save(new Resume("uuid10001"));
    }

}