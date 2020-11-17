package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ListStorage extends AbstractStorage {

    private List<Resume> storage = new ArrayList<>();

    public int size() {
        return storage.size();
    }

    public void clear() {
        storage.clear();
    }

    public Resume[] getAll() {
        Resume[] resumes = new Resume[storage.size()];
        return storage.toArray(resumes);
    }

    @Override
    protected void getAllResume(List<Resume> list) {
        list.retainAll(storage);
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        return IntStream.range(0, storage.size()).filter(index -> storage.get(index).getUuid().equals(uuid)).findFirst().orElse(-1);
    }

    @Override
    protected boolean isExists(Object index) {
        return (Integer) index > -1;
    }

    @Override
    protected void saveResume(Object index, Resume resume) {
        storage.add(resume);
    }

    @Override
    protected void deleteResume(Object index) {
        storage.remove((int) index);
    }

    @Override
    protected void updateResume(Object index, Resume resume) {
        storage.set((Integer) index, resume);
    }

    @Override
    protected Resume getResume(Object index) {
        return storage.get((Integer) index);
    }

}
