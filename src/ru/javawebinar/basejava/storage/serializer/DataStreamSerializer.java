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
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readWithException(dis, () -> resume.setContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
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
                writeWithException(dos, ((ListSection) section).getContent(), dos::writeUTF);
                break;
            case EDUCATION:
            case EXPERIENCE:
                writeWithException(dos, ((OrganizationSection) section).getContent(), organization -> {
                    Link link = organization.getLink();
                    dos.writeUTF(link.getName());
                    dos.writeUTF(nullToString(link.getUrl()));
                    writeWithException(dos, organization.getExperiences(), experience -> {
                        writeDate(dos, experience.getStartDate());
                        writeDate(dos, experience.getEndDate());
                        dos.writeUTF(experience.getTitle());
                        dos.writeUTF(nullToString(experience.getDescription()));
                    });
                });
                break;
        }
    }

    private AbstractSection readSection(DataInputStream dis, SectionType sectionType) throws IOException {
        AbstractSection section = null;
        switch (sectionType) {
            case PERSONAL:
            case OBJECTIVE:
                section = new SingleLineSection(dis.readUTF());
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                List<String> content = new ArrayList<>();
                readWithException(dis, () -> content.add(dis.readUTF()));
                section = new ListSection(content);
                break;
            case EDUCATION:
            case EXPERIENCE:
                List<Organization> organizations = new ArrayList<>();
                readWithException(dis, () -> {
                    Link link = new Link(dis.readUTF(), stringToNull(dis.readUTF()));
                    List<Experience> experiences = new ArrayList<>();
                    readWithException(dis, () -> experiences.add(new Experience(
                            readDate(dis),
                            readDate(dis),
                            dis.readUTF(),
                            stringToNull(dis.readUTF()))));
                    organizations.add(new Organization(link, experiences));
                });
                section = new OrganizationSection(organizations);
                break;
        }
        return section;
    }

    private String nullToString(String source) {
        return source == null ? NULL : source;
    }

    private String stringToNull(String source) {
        return source.equals(NULL) ? null : source;
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



