package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SortedArrayStorageTest extends AbstractArrayStorageTest {

    public SortedArrayStorageTest() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        super(SortedArrayStorage.class);
    }

    @Test
    public void getIndex() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = storage.getClass().getDeclaredMethod("getIndex", String.class);
        Assert.assertEquals(-4, method.invoke(storage, UUID_4));
    }

    @Test
    public void insertResume() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Method method = storage.getClass().getDeclaredMethod("insertResume", int.class, Resume.class);
        method.invoke(storage, -4, new Resume(UUID_4));
        Field field = storage.getClass().getSuperclass().getDeclaredField("size");
        field.setAccessible(true);
        field.set(storage, 4);
        Assert.assertEquals(UUID_4, storage.get(UUID_4).getUuid());
    }

    @Test
    public void deleteResume() {
        storage.delete(UUID_1);
        Assert.assertEquals(2, storage.size());
    }

}
