package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage {

    protected static final int STORAGE_LIMIT = 10_000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public int size() {
        return size;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    @Override
    protected void updateResume(Object index, Resume resume) {
        storage[(Integer) index] = resume;
    }

    @Override
    protected Resume getResume(Object index) {
        return storage[(Integer) index];
    }

    @Override
    protected void deleteResume(Object index) {
        deleteResumeFromArray((Integer) index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected void saveResume(Object index, Resume resume) {
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insertResumeToArray((Integer) index, resume);
        size++;
    }

    @Override
    protected boolean isExists(Object index) {
        return (Integer) index > -1 && (Integer) index < size;
    }

    protected abstract void insertResumeToArray(int index, Resume resume);

    protected abstract void deleteResumeFromArray(int index);

    protected abstract Integer getSearchKey(String uuid);
}