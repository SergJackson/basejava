package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ListStorage extends AbstractStorage<Integer> {

    private List<Resume> storage = new ArrayList<>();

    public int size() {
        return storage.size();
    }

    public void clear() {
        storage.clear();
    }

    @Override
    protected List<Resume> getAllResume() {
        return storage;
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        return IntStream.range(0, storage.size())
                .filter(index -> storage.get(index).getUuid().equals(uuid))
                .findFirst()
                .orElse(-1);
    }

    @Override
    protected boolean isExist(Integer index) {
        return index > -1;
    }

    @Override
    protected void saveResume(Integer index, Resume resume) {
        storage.add(resume);
    }

    @Override
    protected void deleteResume(Integer index) {
        storage.remove((int) index);
    }

    @Override
    protected void updateResume(Integer index, Resume resume) {
        storage.set(index, resume);
    }

    @Override
    protected Resume getResume(Integer index) {
        return storage.get(index);
    }

}
