package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {

    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    public void update(Resume resume) {
        SK searchKey = getSearchKeyIfExists(resume.getUuid());
        updateResume(searchKey, resume);
    }

    public void save(Resume resume) {
        SK searchKey = getSearchKeyIfNotExists(resume.getUuid());
        saveResume(searchKey, resume);
    }

    public Resume get(String uuid) {
        SK searchKey = getSearchKeyIfExists(uuid);
        return getResume(searchKey);
    }

    public void delete(String uuid) {
        SK searchKey = getSearchKeyIfExists(uuid);
        deleteResume(searchKey);
    }

    private SK getSearchKeyIfExists(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private SK getSearchKeyIfNotExists(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    public List<Resume> getAllSorted() {
        List<Resume> list = getAllResume();
        Collections.sort(list);
        return list;
    }

    protected abstract List<Resume> getAllResume();

    protected abstract SK getSearchKey(String uuid);

    protected abstract boolean isExist(SK searchKey);

    protected abstract void saveResume(SK searchKey, Resume resume);

    protected abstract void deleteResume(SK searchKey);

    protected abstract void updateResume(SK searchKey, Resume resume);

    protected abstract Resume getResume(SK searchKey);

}
