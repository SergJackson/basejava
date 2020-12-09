package ru.javawebinar.basejava.model;

public class Experience {
    private String company;
    private String www;
    private String title;
    private String dateStart;
    private String dateFinish;
    private String info;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Experience that = (Experience) o;

        if (!company.equals(that.company)) return false;
        if (www != null ? !www.equals(that.www) : that.www != null) return false;
        if (!title.equals(that.title)) return false;
        if (!dateStart.equals(that.dateStart)) return false;
        if (!dateFinish.equals(that.dateFinish)) return false;
        return info != null ? info.equals(that.info) : that.info == null;
    }

    @Override
    public int hashCode() {
        int result = company.hashCode();
        result = 31 * result + (www != null ? www.hashCode() : 0);
        result = 31 * result + title.hashCode();
        result = 31 * result + dateStart.hashCode();
        result = 31 * result + dateFinish.hashCode();
        result = 31 * result + (info != null ? info.hashCode() : 0);
        return result;
    }

    public Experience(String company, String www, String title, String dateStart, String dateFinish, String info) {
        this.company = company;
        this.www = www;
        this.title = title;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.info = info;
    }

    @Override
    public String toString() {
        return "\n" + company + " : " + www + "\n" +
                dateStart + " - " + dateFinish + " " + title + "\n" +
                info + "\n";
    }
}