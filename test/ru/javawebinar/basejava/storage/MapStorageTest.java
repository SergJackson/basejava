package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

import static ru.javawebinar.basejava.storage.AbstractArrayStorage.STORAGE_LIMIT;

public class MapStorageTest extends AbstractArrayStorageTest {

    public MapStorageTest() {
        super(new MapStorage());
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

    @Test
    public void getAll() {
        Resume[] expectedResumes = {new Resume(UUID_1), new Resume(UUID_2), new Resume(UUID_3)};
        Resume[] resultResumes = storage.getAll();
        Arrays.sort(resultResumes);
        Assert.assertArrayEquals(expectedResumes, resultResumes);
    }

}