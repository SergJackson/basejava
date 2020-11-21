package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

import static java.lang.System.arraycopy;

public class SortedArrayStorage extends AbstractArrayStorage {

    private static final Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getUuid);

    @Override
    protected Integer getSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid, "");
        return Arrays.binarySearch(storage, 0, size, searchKey, RESUME_COMPARATOR);
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