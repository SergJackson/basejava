package com.urise.webapp.storage;

import com.urise.webapp.modal.Resume;

import static java.lang.System.arraycopy;


/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private int size = 0;
    private Resume[] storage = new Resume[10000];

    public void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    public void update(Resume r) {
        if (r.getUuid() != null) {
            int index = find(r.getUuid());
            if ( index == -1) {         // it will be unique value
                System.out.println("ERROR: This UUID is not exists. Update is not possible");
            } else {
                storage[index] = r;
            }
        }
    }

    public void save(Resume r) {
        if (r.getUuid() != null) {
            if (size < storage.length) {               // Control error "Out of range"
                if (find(r.getUuid()) == -1) {         // it will be unique value
                    storage[size] = r;
                    size++;
                } else {
                    System.out.println("ERROR: This UUID already exists. Save was cancelled");
                }
            } else {
                System.out.println("ERROR: Array is full. Save was cancelled");
            }
        }
    }

    /**
     * @return index of UUID in storage
     */
    int find(String uuid) {
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
        int index = find(uuid);
        if (index > -1) {
            return storage[index];
        }
        System.out.println("ERROR: This UUID is not found");
        return null;
    }

    public void delete(String uuid) {
        int index = find(uuid);
        if (index > -1) {
            if (index < size - 1) {
                arraycopy(storage, index + 1, storage, index, size - (index + 1));
            }
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("ERROR: This UUID is not found. Deletion not executed");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        Resume[] resumes = new Resume[size];
        arraycopy(storage, 0, resumes, 0, size);
        return resumes;
    }

    public int size() {
        return size;
    }
}
