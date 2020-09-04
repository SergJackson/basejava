import static java.lang.System.arraycopy;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private int sizeStorage = 0;
    Resume[] storage = new Resume[10000];

    void clear() {
        for (int i = 0; i < sizeStorage; i++) {
            storage[i] = null;
        }
        sizeStorage = 0;
    }

    void save(Resume r) {
        if (r.uuid != null) {
            if (sizeStorage < storage.length) {   // Control error "Out of range"
                if (find(r.uuid) == -1) {         // it will be unique value
                    storage[sizeStorage] = r;
                    sizeStorage++;
                }
            }
        }
    }

    /**
     * @return index of UUID in storage
     */
    int find(String uuid) {
        int index = -1;
        for (int i = 0; i < sizeStorage; i++) {
            if (storage[i].uuid.equals(uuid)) {
                index = i;
                break;
            }
        }
        return index;
    }

    Resume get(String uuid) {
        int index = find(uuid);
        if (index > -1) {
            return storage[index];
        }
        return null;
    }

    void delete(String uuid) {
        int index = find(uuid);
        if (index > -1) {
            if (index < sizeStorage - 1) {
                arraycopy(storage, index + 1, storage, index, sizeStorage - (index + 1));
            }
            storage[sizeStorage - 1] = null;
            sizeStorage--;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] resumes = new Resume[sizeStorage];
        arraycopy(storage, 0, resumes, 0, sizeStorage);
        return resumes;
    }

    int size() {
        return sizeStorage;
    }
}
