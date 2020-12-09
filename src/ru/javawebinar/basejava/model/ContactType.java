package ru.javawebinar.basejava.model;

public enum ContactType {
    PHONE("Телефон"),
    SKYPE("Скайп"),
    EMAIL("Email"),
    GITHUB("GitHub"),
    LINKEDIN("LinkedIn"),
    STACKOVERFLOW("Stackoverflow"),
    WWW("Домашняя страница");

    private String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
