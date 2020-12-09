package ru.javawebinar.basejava.model;

import com.sun.deploy.util.StringUtils;

import java.util.List;

public class ListSection extends Section {

    private final List<String> content;


    public ListSection(List<String> content) {

        this.content = content;
    }

    public List<String> getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListSection that = (ListSection) o;

        return content != null ? content.equals(that.content) : that.content == null;
    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }

    @Override
    public String toString() {
        return " - " + StringUtils.join(content, "\n - ");
    }
}