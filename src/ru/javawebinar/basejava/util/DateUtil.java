package ru.javawebinar.basejava.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;

@XmlAccessorType(XmlAccessType.FIELD)
public class DateUtil implements Serializable {
    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }
}