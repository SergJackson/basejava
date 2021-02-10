package ru.javawebinar.basejava.model;

import java.util.List;
import java.util.Objects;

public class ListSection extends AbstractSection {
    private static final long serialVersionUID = 1L;

    private List<String> content;

    public ListSection() {
    }

    public ListSection(List<String> content) {
        Objects.requireNonNull(content, "list content must not be null");
        this.content = content;
    }

    public List<String> getContent() {
        return content;
    }

    @Override
    public String toHtml() {
        return content.size() > 0 ? "<ul>\n<li>" + String.join("</li>\n<li>", content) + " </li>\n</ul>" : "";
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
        return " - " + String.join("\n - ", content);
    }
}
