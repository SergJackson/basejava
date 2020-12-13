package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Experience {
    private Link homePage;
    private List<Period> periods = new ArrayList<>();

    public Experience(String name, String url, LocalDate startDate, LocalDate endDate, String title, String description) {
        Objects.requireNonNull(name, "Name must not be null");
        this.homePage = new Link(name, url);
        addPeriodExperience(startDate, endDate, title, description);
    }

    public void addPeriodExperience(LocalDate startDate, LocalDate endDate, String title, String description) {
        this.periods.add(new Period(startDate, endDate, title, description));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Experience that = (Experience) o;

        if (!homePage.equals(that.homePage)) return false;
        return periods.equals(that.periods);
    }

    @Override
    public int hashCode() {
        int result = homePage.hashCode();
        result = 31 * result + periods.hashCode();
        return result;
    }

    @Override
    public String toString() {
        String result = "\n" + homePage + "\n";
        for (Period period : periods) {
            result = result.concat(period.toString());
        }
        return result;
    }
}
