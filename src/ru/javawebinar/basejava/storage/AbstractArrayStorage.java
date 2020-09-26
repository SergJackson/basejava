package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage {

    protected static final int STORAGE_LIMIT = 10_000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];

    protected void clearStorage() {
        Arrays.fill(storage, 0, size, null);
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
    }

    protected Resume[] getAllResume() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    protected boolean checkStorageLimit() {
        return size < STORAGE_LIMIT;
    }

    protected abstract void deleteResumeFromArray(int index);

}