package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage {

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
        return storage.get(uuid);
    }

    @Override
    protected boolean isExist(Object searchKey) {
        Resume resume = (Resume) searchKey;
        return storage.containsValue(resume);
    }

    @Override
    protected void saveResume(Object key, Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void deleteResume(Object key) {
        storage.remove(((Resume) key).getUuid());
    }

    @Override
    protected void updateResume(Object key, Resume resume) {
        storage.replace(((Resume) key).getUuid(), resume);
    }

    @Override
    protected Resume getResume(Object key) {
        return (Resume) key;
    }

}
