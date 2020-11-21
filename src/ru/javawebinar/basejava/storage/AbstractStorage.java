package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Collections;
import java.util.List;

public abstract class AbstractStorage implements Storage {

    //private static final Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(resume -> resume.getFullName() + "~" + resume.getUuid());

    public void update(Resume resume) {
        Object searchKey = getSearchKeyIfExists(resume.getUuid());
        updateResume(searchKey, resume);
    }

    public void save(Resume resume) {
        Object searchKey = getSearchKeyIfNotExists(resume.getUuid());
        saveResume(searchKey, resume);
    }

    public Resume get(String uuid) {
        Object searchKey = getSearchKeyIfExists(uuid);
        return getResume(searchKey);
    }

    public void delete(String uuid) {
        Object searchKey = getSearchKeyIfExists(uuid);
        deleteResume(searchKey);
    }

    private Object getSearchKeyIfExists(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object getSearchKeyIfNotExists(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    public List<Resume> getAllSorted() {
        List<Resume> list = getAllResume();
        //Collections.sort(list, RESUME_COMPARATOR);
        Collections.sort(list);
        return list;
    }

    protected abstract List<Resume> getAllResume();

    protected abstract Object getSearchKey(String uuid);

    protected abstract boolean isExist(Object searchKey);

    protected abstract void saveResume(Object searchKey, Resume resume);

    protected abstract void deleteResume(Object searchKey);

    protected abstract void updateResume(Object searchKey, Resume resume);

    protected abstract Resume getResume(Object searchKey);

}
