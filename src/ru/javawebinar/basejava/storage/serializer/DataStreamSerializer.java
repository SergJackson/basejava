package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class DataStreamSerializer implements StreamSerializer {
    private static final String NULL = "NULL";


    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            writeWithException(dos, r.getContacts().entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            writeWithException(dos, r.getSections().entrySet(), entry -> writeSection(dos, entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        int size;
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readWithException(dis, () -> {
                resume.setContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            });
            readWithException(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                resume.setSection(sectionType, readSection(dis, sectionType));
            });
            return resume;
        }
    }

    private void writeSection(DataOutputStream dos, SectionType sectionType, AbstractSection section) throws IOException {
        dos.writeUTF(sectionType.name());
        switch (sectionType) {
            case PERSONAL:
            case OBJECTIVE:
                dos.writeUTF(((SingleLineSection) section).getContent());
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                List<String> list = ((ListSection) section).getContent();
                dos.writeInt(list.size());
                for (String item : list) {
                    dos.writeUTF(item);
                }
                break;
            case EDUCATION:
            case EXPERIENCE:
                List<Organization> organizations = ((OrganizationSection) section).getContent();
                dos.writeInt(organizations.size());
                for (Organization organization : organizations) {
                    Link link = organization.getLink();
                    dos.writeUTF(link.getName());
                    dos.writeUTF(nullToString(link.getUrl()));
                    List<Experience> experiences = organization.getExperiences();
                    dos.writeInt(experiences.size());
                    for (Experience experience : experiences) {
                        writeDate(dos, experience.getStartDate());
                        writeDate(dos, experience.getEndDate());
                        dos.writeUTF(experience.getTitle());
                        dos.writeUTF(nullToString(experience.getDescription()));
                    }
                }
                break;
        }
    }

    private AbstractSection readSection(DataInputStream dis, SectionType sectionType) throws IOException {
        AbstractSection section = null;
        int size;

        switch (sectionType) {
            case PERSONAL:
            case OBJECTIVE:
                section = new SingleLineSection(dis.readUTF());
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                size = dis.readInt();
                List<String> list = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    list.add(dis.readUTF());
                }
                section = new ListSection(list);
                break;
            case EDUCATION:
            case EXPERIENCE:
                size = dis.readInt();
                List<Organization> organizations = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    Link link = new Link(dis.readUTF(), stringToNull(dis.readUTF()));
                    List<Experience> experiences = new ArrayList<>();
                    int count = dis.readInt();
                    for (int x = 0; x < count; x++) {
                        experiences.add(new Experience(
                                readDate(dis),              // StartDate
                                readDate(dis),              // EndDate
                                dis.readUTF(),              // Title
                                stringToNull(dis.readUTF()) // Descriptions
                        ));
                    }
                    organizations.add(new Organization(link, experiences));
                }
                section = new OrganizationSection(organizations);
                break;
        }
        return section;
    }

    private String nullToString(String string) {
        return string == null ? NULL : string;
    }

    private String stringToNull(String string) {
        return string.equals(NULL) ? null : string;
    }

    private void writeDate(DataOutputStream dos, LocalDate date) throws IOException {
        dos.writeInt(date.getYear());
        dos.writeInt(date.getMonthValue());
        dos.writeInt(date.getDayOfMonth());
    }

    private LocalDate readDate(DataInputStream dis) throws IOException {
        return LocalDate.of(dis.readInt(), dis.readInt(), dis.readInt());
    }

    private interface Writer<T> {
        void write(T t) throws IOException;
    }

    private interface Reader {
        void read() throws IOException;
    }

    private <T> void writeWithException(DataOutputStream dos, Collection<T> collection, Writer<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T t : collection) {
            writer.write(t);
        }
    }

    private void readWithException(DataInputStream dis, Reader reader) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            reader.read();
        }
    }
}



