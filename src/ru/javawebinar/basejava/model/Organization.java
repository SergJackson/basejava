package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Organization {
    private Link homePage;
    private List<Experience> experiences = new ArrayList<>();

    public Organization(String name, String url, LocalDate startDate, LocalDate endDate, String title, String description) {
        Objects.requireNonNull(name, "Name must not be null");
        this.homePage = new Link(name, url);
        addPeriodExperience(startDate, endDate, title, description);
    }

    public void addPeriodExperience(LocalDate startDate, LocalDate endDate, String title, String description) {
        this.experiences.add(new Experience(startDate, endDate, title, description));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (!homePage.equals(that.homePage)) return false;
        return experiences.equals(that.experiences);
    }

    @Override
    public int hashCode() {
        int result = homePage.hashCode();
        result = 31 * result + experiences.hashCode();
        return result;
    }

    @Override
    public String toString() {
        String result = "\n" + homePage + "\n";
        for (Experience experience : experiences) {
            result = result.concat(experience.toString());
        }
        return result;
    }
}
