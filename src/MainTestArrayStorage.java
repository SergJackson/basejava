import com.urise.webapp.modal.Resume;
import com.urise.webapp.storage.ArrayStorage;

/**
 * Test for your com.urise.webapp.storage.ArrayStorage implementation
 */
public class MainTestArrayStorage {
    static final ArrayStorage ARRAY_STORAGE = new ArrayStorage();

    public static void main(String[] args) {
        Resume r1 = new Resume();
        r1.setUuid("uuid1");
        Resume r2 = new Resume();
        r2.setUuid("uuid2");
        Resume r3 = new Resume();
        r3.setUuid("uuid3");

        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r3);

        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.size());

        System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));

        printAll();
        ARRAY_STORAGE.delete(r1.getUuid());
        printAll();
        ARRAY_STORAGE.clear();
        printAll();

        System.out.println("Size: " + ARRAY_STORAGE.size());

        System.out.println("\nFull test:");
        for (int i = 0; i < 10_000; i++) {
            Resume resume = new Resume();
            resume.setUuid("uuid" + Integer.toString(i));
            ARRAY_STORAGE.save(resume);
        }
        System.out.println("Size: " + ARRAY_STORAGE.size());

        System.out.println("\nFull test + 1");
        Resume r100000 = new Resume();
        r100000.setUuid("uuid10000");
        ARRAY_STORAGE.save(r100000);

        System.out.println("\nSave not unique UUID");
        ARRAY_STORAGE.delete(r3.getUuid());
        ARRAY_STORAGE.save(r2);

        System.out.println("\nUpdate not exists UUID");
        ARRAY_STORAGE.update(r100000);

        System.out.println("\nDelete not exists UUID");
        ARRAY_STORAGE.delete(r100000.getUuid());

        ARRAY_STORAGE.clear();
        printAll();
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAll()) {
            System.out.println(r);
        }
    }
}
