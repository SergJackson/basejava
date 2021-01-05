package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }
            for (Map.Entry<SectionType, AbstractSection> entry : r.getSections().entrySet()) {
                writeSection(dos, entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.setContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }
            while (dis.available() > 0) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                resume.setSection(sectionType, readSection(dis, sectionType));
            }
            return resume;
        }
    }

    private void writeSection(DataOutputStream dos, SectionType sectionType, AbstractSection section) {
        try {
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
                        dos.writeUTF(organization.getLink().getName());
                        dos.writeUTF(organization.getLink().getUrl());
                        List<Experience> experiences = organization.getExperiences();
                        dos.writeInt(experiences.size());
                        for (Experience experience : experiences) {
                            dos.writeUTF(experience.getStartDate().toString());
                            dos.writeUTF(experience.getEndDate().toString());
                            dos.writeUTF(experience.getTitle());
                            dos.writeUTF(experience.getDescription());
                        }
                    }
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private AbstractSection readSection(DataInputStream dis, SectionType sectionType) {
        AbstractSection section = null;
        int size;
        try {
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
                        Link link = new Link(dis.readUTF(), dis.readUTF());
                        List<Experience> experiences = new ArrayList<>();
                        int count = dis.readInt();
                        for (int x = 0; x < count; x++) {
                            experiences.add(new Experience(
                                    LocalDate.parse(dis.readUTF()), // StartDate
                                    LocalDate.parse(dis.readUTF()), // EndDate
                                    dis.readUTF(),                  // Title
                                    dis.readUTF()                   // Descriptions
                            ));
                        }
                        organizations.add(new Organization(link, experiences));
                    }
                    section = new OrganizationSection(organizations);
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return section;
    }
}
