package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {

    private Map<Integer, Resume> storage = new HashMap<>();

    @Override
    protected int getIndex(String uuid) {
        for (Map.Entry<Integer, Resume> entry : storage.entrySet()) {
            if (entry.getValue().getUuid().equals(uuid)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    @Override
    protected void insertResume(int index, Resume resume) {
        if (index > -1) {
            storage.put(index, resume);
        } else {
            storage.put(resume.hashCode(), resume);
        }
    }

    @Override
    protected void deleteResume(int index) {
        storage.remove(index);
    }

    @Override
    protected void updateResume(int index, Resume resume) {
        storage.replace(index, resume);
    }

    @Override
    protected Resume getResume(int index) {
        return storage.get(index);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        Resume[] resumes = new Resume[storage.size()];
        return storage.values().toArray(resumes);
    }

    @Override
    public int size() {
        return storage.size();
    }
}
