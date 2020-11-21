package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUuidStorage extends AbstractStorage {

    private Map<String, Resume> storage = new HashMap<>();

    public int size() {
        return storage.size();
    }

    public void clear() {
        storage.clear();
    }

    @Override
    protected List<Resume> getAllResume() {
        return new ArrayList<>(storage.values());
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(Object searchKey) {
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
