package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;

public class MapStorage extends AbstractStorage {

    private HashMap<String, Resume> storage = new HashMap<>();

    public int size() {
        return storage.size();
    }

    public void clear() {
        storage.clear();
    }

    public Resume[] getAll() {
        Resume[] resumes = new Resume[storage.size()];
        return storage.values().toArray(resumes);
    }

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExists(Object searchKey) {
        return storage.containsKey(searchKey.toString());
    }

    @Override
    protected void saveResume(Object key, Resume resume) {
        storage.put(key.toString(), resume);
    }

    @Override
    protected void deleteResume(Object key) {
        storage.remove(key.toString());
    }

    @Override
    protected void updateResume(Object key, Resume resume) {
        storage.replace(key.toString(), resume);
    }

    @Override
    protected Resume getResume(Object key) {
        return storage.get(key.toString());
    }

}
