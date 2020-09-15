package com.urise.webapp.storage;

import com.urise.webapp.modal.Resume;

import java.util.Arrays;

import static java.lang.System.arraycopy;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private int size = 0;
    private Resume[] storage = new Resume[10_000];

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume resume) {
        if (resume.getUuid() != null) {
            int index = findIndex(resume.getUuid());
            if (index == -1) {                                   // it will be unique value
                System.out.println("ERROR. UUID: " + resume.getUuid() + " is not exists. Update is not possible");
            } else {
                storage[index] = resume;
            }
        }
    }

    public void save(Resume resume) {
        if (resume.getUuid() != null) {
            if (size < storage.length) {                         // Control error "Out of range"
                if (findIndex(resume.getUuid()) == -1) {         // it will be unique value
                    storage[size] = resume;
                    size++;
                } else {
                    System.out.println("ERROR. UUID: " + resume.getUuid() + "  already exists. Save was cancelled");
                }
            } else {
                System.out.println("ERROR. Array is full. Save was cancelled");
            }
        }
    }

    /**
     * @return index of UUID in storage
     */
    private int findIndex(String uuid) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index > -1) {
            return storage[index];
        }
        System.out.println("ERROR. UUID: " + uuid + " is not found");
        return null;
    }

    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index > -1) {
            if (index < size - 1) {
                arraycopy(storage, index + 1, storage, index, size - (index + 1));
            }
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("ERROR. UUID: " + uuid + " is not found. Deletion not executed");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }
}
