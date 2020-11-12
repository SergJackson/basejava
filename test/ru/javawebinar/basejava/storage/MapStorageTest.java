package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class MapStorageTest extends AbstractStorageTest {

    public MapStorageTest() {
        super(new MapStorage());
    }

    @Test
    public void getAll() {
        Resume[] expectedResumes = {RESUME_1, RESUME_2, RESUME_3};
        Resume[] resultResumes = storage.getAll();
        Arrays.sort(resultResumes);
        Assert.assertArrayEquals(expectedResumes, resultResumes);
    }

}