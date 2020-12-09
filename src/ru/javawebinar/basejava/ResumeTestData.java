package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.MapResumeStorage;
import ru.javawebinar.basejava.storage.Storage;

import java.io.IOException;
import java.util.Arrays;

/**
 * Test for ru.javawebinar.basejava.storage.ArrayStorage
 */
public class ResumeTestData {
    private final static Storage storage = new MapResumeStorage();

    public static void main(String[] args) throws IOException {
        Resume resume = new Resume("Григорий Кислин");
        // Fill contacts
        resume.setContact(ContactType.PHONE, "+7(921) 855-0482");
        resume.setContact(ContactType.SKYPE, "grigory.kislin");
        resume.setContact(ContactType.EMAIL, "gkislin@yandex.ru");
        resume.setContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        resume.setContact(ContactType.GITHUB, "https://github.com/gkislin");
        resume.setContact(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473");
        resume.setContact(ContactType.WWW, "http://gkislin.ru/");

        // Fill contents
        resume.setSection(
                SectionType.OBJECTIVE,
                new StringSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям")

        );
        resume.setSection(
                SectionType.PERSONAL,
                new StringSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.")
        );
        resume.setSection(
                SectionType.ACHIEVEMENT,
                new ListSection(
                        Arrays.asList(
                                "С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.",
                                "Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.",
                                "Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.",
                                "Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.",
                                "Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).",
                                "Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа."
                        )
                )
        );
        resume.setSection(
                SectionType.QUALIFICATIONS,
                new ListSection(
                        Arrays.asList(
                                "JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2",
                                "Version control: Subversion, Git, Mercury, ClearCase, Perforce",
                                "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, MySQL, SQLite, MS SQL, HSQLDB",
                                "Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy, XML/XSD/XSLT, SQL, C/C++, Unix shell scripts",
                                "Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements).",
                                "Python: Django.",
                                "JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js",
                                "Scala: SBT, Play2, Specs2, Anorm, Spray, Akka",
                                "Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT.",
                                "Инструменты: Maven + plugin development, Gradle, настройка Ngnix, администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, Nagios, iReport, OpenCmis, Bonita, pgBouncer.",
                                "Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, архитектурных шаблонов, UML, функционального программирования",
                                "Родной русский, английский \"upper intermediate\""
                        )
                )
        );

        resume.setSection(
                SectionType.EXPERIENCE,
                new OrganizationSection(Arrays.asList(
                        new Experience(
                                "Java Online Projects",
                                "http://javaops.ru/",
                                "Автор проекта.",
                                "10/2013",
                                "Сейчас",
                                "Создание, организация и проведение Java онлайн проектов и стажировок."
                        ),
                        new Experience(
                                "Wrike",
                                "https://www.wrike.com/",
                                "Старший разработчик (backend)",
                                "10/2014",
                                "01/2016",
                                "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO."
                        ),
                        new Experience(
                                "RIT Center",
                                "",
                                "Java архитектор",
                                "04/2012",
                                "10/2014",
                                "Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из браузера документов MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python."
                        ),
                        new Experience(
                                "Luxoft (Deutsche Bank)",
                                "http://www.luxoft.ru/",
                                "Ведущий программист",
                                "12/2010",
                                "04/2012",
                                "Участие в проекте Deutsche Bank CRM (WebLogic, Hibernate, Spring, Spring MVC, SmartGWT, GWT, Jasper, Oracle). Реализация клиентской и серверной части CRM. Реализация RIA-приложения для администрирования, мониторинга и анализа результатов в области алгоритмического трейдинга. JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Highstock, Commet, HTML5."
                        ),
                        new Experience(
                                "Yota",
                                "https://www.yota.ru/",
                                "Ведущий специалист",
                                "06/2008",
                                "12/2010",
                                "Дизайн и имплементация Java EE фреймворка для отдела \"Платежные Системы\" (GlassFish v2.1, v3, OC4J, EJB3, JAX-WS RI 2.1, Servlet 2.4, JSP, JMX, JMS, Maven2). Реализация администрирования, статистики и мониторинга фреймворка. Разработка online JMX клиента (Python/ Jython, Django, ExtJS)"
                        ),
                        new Experience(
                                "Enkata",
                                "http://enkata.com/",
                                "Разработчик ПО",
                                "03/2007",
                                "06/2008",
                                "Реализация клиентской (Eclipse RCP) и серверной (JBoss 4.2, Hibernate 3.0, Tomcat, JMS) частей кластерного J2EE приложения (OLAP, Data mining)."
                        ),
                        new Experience(
                                "Siemens AG",
                                "https://www.siemens.com/ru/ru/home.html",
                                "Разработчик ПО",
                                "01/2005",
                                "02/2007",
                                "Разработка информационной модели, проектирование интерфейсов, реализация и отладка ПО на мобильной IN платформе Siemens @vantage (Java, Unix)."
                        ),
                        new Experience(
                                "Alcatel",
                                "http://www.alcatel.ru/",
                                "Инженер по аппаратному и программному тестированию",
                                "09/1997",
                                "01/2005",
                                "Тестирование, отладка, внедрение ПО цифровой телефонной станции Alcatel 1000 S12 (CHILL, ASM)."
                        )
                ))
        );
        resume.setSection(
                SectionType.EDUCATION,
                new OrganizationSection(Arrays.asList(
                        new Experience(
                                "Coursera",
                                "https://www.coursera.org/course/progfun",
                                "\"Functional Programming Principles in Scala\" by Martin Odersky",
                                "03/2013",
                                "05/2013",
                                ""
                        ),
                        new Experience(
                                "Luxoft",
                                "http://www.luxoft-training.ru/training/catalog/course.html?ID=22366",
                                "Курс \"Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.\"",
                                "03/2011",
                                "04/2011",
                                ""
                        ),
                        new Experience(
                                "Siemens AG",
                                "http://www.siemens.ru/",
                                "3 месяца обучения мобильным IN сетям (Берлин)",
                                "01/2005",
                                "04/2005",
                                ""
                        ),
                        new Experience(
                                "Alcatel",
                                "http://www.alcatel.ru/",
                                "6 месяцев обучения цифровым телефонным сетям (Москва)",
                                "09/1997",
                                "03/1998",
                                ""
                        ),
                        new Experience(
                                "Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики",
                                "http://www.ifmo.ru/",
                                "Аспирантура (программист С, С++)",
                                "09/1993",
                                "07/1996",
                                ""
                        ),
                        new Experience(
                                "Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики",
                                "http://www.ifmo.ru/",
                                "Инженер (программист Fortran, C)",
                                "09/1987",
                                "07/1993",
                                ""
                        ),
                        new Experience(
                                "Заочная физико-техническая школа при МФТИ",
                                "http://www.school.mipt.ru/",
                                "Закончил с отличием",
                                "09/1984",
                                "06/1987",
                                ""
                        )
                ))
        );

        storage.save(resume);
        printAll();
    }

    private static void printAll() {
        System.out.println("----------------------------");
        for (Resume r : storage.getAllSorted()) {
            System.out.println("Резуме [" + r.getUuid() + "]\n" + r.getFullName());
            System.out.println("----------------------------");
            for (ContactType contactType : ContactType.values()) {
                System.out.println(contactType.getTitle() + ": " + r.getContact(contactType));
            }
            System.out.println("----------------------------");
            for (SectionType sectionType : SectionType.values()) {
                System.out.println(sectionType.getTitle());
                System.out.println(r.getSection(sectionType));
                System.out.println("----------------------------");
            }
        }
    }
}