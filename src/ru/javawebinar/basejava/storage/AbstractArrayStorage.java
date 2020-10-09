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

    protected void updateResume(int index, Resume resume) {
        storage[index] = resume;
    }

    protected Resume getResume(int index) {
        return storage[index];
    }

    protected void deleteResume(int index) {
        deleteResumeFromArray(index);
        storage[size - 1] = null;
        size--;
    }

    protected void insertResume(int index, Resume resume) {
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insertResumeToArray(index, resume);
        size++;
    }

    protected abstract void insertResumeToArray(int index, Resume resume);

    protected abstract void deleteResumeFromArray(int index);

}