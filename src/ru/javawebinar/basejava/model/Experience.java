package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.Objects;

public class Experience {
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;
    private String description;

    public Experience(LocalDate startDate, LocalDate endDate, String title, String description) {
        Objects.requireNonNull(startDate, "startDate must not be null");
        Objects.requireNonNull(endDate, "endDate must not be null");
        Objects.requireNonNull(title, "title must not be null");
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    @Override
    public String toString() {
        return "\n" + startDate + " - " + endDate + " " + title + "\n" + description + (description.equals("") ? "" : "\n");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Experience experience = (Experience) o;

        if (!startDate.equals(experience.startDate)) return false;
        if (!endDate.equals(experience.endDate)) return false;
        if (!title.equals(experience.title)) return false;
        return description != null ? description.equals(experience.description) : experience.description == null;
    }

    @Override
    public int hashCode() {
        int result = startDate.hashCode();
        result = 31 * result + endDate.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}