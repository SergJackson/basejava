package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class ListStorage extends AbstractStorage {

    Collection<Resume> storage = new ArrayList<>();

    @Override
    protected int getIndex(String uuid) {
        int index = -1;
        Iterator<Resume> iterator = storage.iterator();
        while (iterator.hasNext()) {
            index++;
            Resume resume = iterator.next();
            if (Objects.equals(resume.getUuid(), uuid)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    protected void insertResume(int index, Resume resume) {
        storage.add(resume);
    }

    @Override
    protected void deleteResume(int index) {
        Resume resume = getResume(index);
        storage.remove(resume);
    }

    @Override
    protected void clearStorage() {
        storage.clear();
    }

    @Override
    protected void updateResume(int index, Resume resume) {
        storage.remove(resume);
        storage.add(resume);
    }

    @Override
    protected boolean checkStorageLimit() {
        return true;
    }

    @Override
    protected Resume getResume(int index) {
        int position = -1;
        Iterator<Resume> iterator = storage.iterator();
        while (iterator.hasNext()) {
            position++;
            if (position == index) {
                return iterator.next();
            }
            iterator.next();
        }
        return null;
    }

    @Override
    protected Resume[] getAllResume() {
        Resume[] resumes = new Resume[size];
        storage.toArray(resumes);
        return resumes;
    }

}
