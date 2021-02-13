package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class OrganizationSection extends AbstractSection {
    private static final long serialVersionUID = 1L;
    private List<Organization> content;

    public OrganizationSection() {
    }

    public OrganizationSection(Organization... organizations) {
        this(Arrays.asList(organizations));
    }

    public OrganizationSection(List<Organization> content) {
        Objects.requireNonNull(content, "Organization must not be null");
        this.content = content;
    }

    public List<Organization> getContent() {
        return content;
    }

    @Override
    public String toHtml() {
        String result = "<table class=\"no-border\">\n";
        for (Organization org : content) {
            result += "<tr class=\"no-border\">\n";
            Link link = org.getLink();
            if (link != null) {
                result += "<td class=\"no-border\" colspan=\"2\">\n";
                String url = link.getUrl();
                String name = link.getName();
                result += url != null ? "<a href=\"" + url + "\">" + name + "</a>" : name;
                result += "</td>\n</tr>\n";
                List<Experience> experiences = org.getExperiences();
                if (experiences.size() > 0) {
                    for (Experience experience : experiences) {
                        if (experience != null) {
                            result += "<tr class=\"no-border\">\n<td class=\"no-border\" width=\"10%\">" +
                                    getMonthYearRU(experience.getStartDate()) +
                                    " - " +
                                    (experience.getEndDate() != null ? getMonthYearRU(experience.getEndDate()) : " по настоящее время ") +
                                    "</td>\n<td class=\"no-border\">" +
                                    "<strong>" + experience.getTitle() + "</strong><br />\n" +
                                    "<p>" + experience.getDescription() + "</p>" +
                                    "</td>\n</tr>\n";
                        }
                    }
                }
            }
        }
        return result + "</table>";
    }

    @Override
    public String toHtmlEdit() {
        return toHtml(); // Maybe it will change
    }

    private String getMonthYearRU(LocalDate date) {
        Month month = date.getMonth();
        Locale localeRu = new Locale("ru", "RU");
        return month.getDisplayName(TextStyle.SHORT, localeRu) + ". " + date.getYear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationSection that = (OrganizationSection) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return content.toString();
    }

}
