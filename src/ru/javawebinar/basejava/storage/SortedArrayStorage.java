package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

import static java.lang.System.arraycopy;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer getSearchKey(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected void insertResumeToArray(int index, Resume resume) {
        index = -index;
        arraycopy(storage, index - 1, storage, index, size - (index - 1));
        storage[index - 1] = resume;
    }

    @Override
    protected void deleteResumeFromArray(int index) {
        arraycopy(storage, index + 1, storage, index, size - (index + 1));
    }

}