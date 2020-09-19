package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.ArrayStorage;
import ru.javawebinar.basejava.storage.SortedArrayStorage;

/**
 * Test ru.javawebinar.basejava.storage.ArrayStorage
 */
public class MainTestArrayStorage {
    static final SortedArrayStorage ARRAY_SORTSTORAGE = new SortedArrayStorage();
    static final ArrayStorage ARRAY_STORAGE = new ArrayStorage();

    public static void main(String[] args) {
        Resume r1 = new Resume();
        r1.setUuid("uuid1");
        Resume r2 = new Resume();
        r2.setUuid("uuid2");
        Resume r3 = new Resume();
        r3.setUuid("uuid3");

        ARRAY_STORAGE.save(r1);
        ARRAY_SORTSTORAGE.save(r1);
        ARRAY_STORAGE.save(r2);
        ARRAY_SORTSTORAGE.save(r2);
        ARRAY_STORAGE.save(r3);
        ARRAY_SORTSTORAGE.save(r3);

        ARRAY_STORAGE.update(r2);
        ARRAY_SORTSTORAGE.update(r2);

        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.size());

        System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));

        printAll();
        ARRAY_STORAGE.delete(r1.getUuid());
        ARRAY_SORTSTORAGE.delete(r1.getUuid());
        printAll();
        ARRAY_STORAGE.update(r1);
        printAll();
        ARRAY_STORAGE.save(r1);
        ARRAY_SORTSTORAGE.save(r1);
        printAll();
        ARRAY_STORAGE.clear();
        ARRAY_SORTSTORAGE.clear();
        printAll();

        System.out.println("\nSize: " + ARRAY_STORAGE.size());
        System.out.println("Size: " + ARRAY_SORTSTORAGE.size());
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAll()) {
            System.out.println(r);
        }
        System.out.println("\nGet All Sorted");
        for (Resume r : ARRAY_SORTSTORAGE.getAll()) {
            System.out.println(r);
        }
    }
}