package ru.javawebinar.basejava.model;

import java.util.List;

public class OrganizationSection extends AbstractSection {

    private final List<Experience> content;

    public OrganizationSection(List<Experience> content) {

        this.content = content;
    }

    public List<Experience> getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganizationSection that = (OrganizationSection) o;

        return content != null ? content.equals(that.content) : that.content == null;
    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }

    @Override
    public String toString() {
        return content.toString();
    }


}
