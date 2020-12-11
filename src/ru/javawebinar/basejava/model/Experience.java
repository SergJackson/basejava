package ru.javawebinar.basejava.model;

public class Experience {
    private String companyName;
    private String url;
    private String title;
    private String dateStart;
    private String dateFinish;
    private String info;

    public Experience(String companyName, String url, String title, String dateStart, String dateFinish, String info) {
        this.companyName = companyName;
        this.url = url;
        this.title = title;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Experience that = (Experience) o;

        if (!companyName.equals(that.companyName)) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (!title.equals(that.title)) return false;
        if (!dateStart.equals(that.dateStart)) return false;
        if (!dateFinish.equals(that.dateFinish)) return false;
        return info != null ? info.equals(that.info) : that.info == null;
    }

    @Override
    public int hashCode() {
        int result = companyName.hashCode();
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + title.hashCode();
        result = 31 * result + dateStart.hashCode();
        result = 31 * result + dateFinish.hashCode();
        result = 31 * result + (info != null ? info.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n" + companyName + " : " + url + "\n" +
                dateStart + " - " + dateFinish + " " + title + "\n" +
                info + "\n";
    }
}
