package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;
import ru.javawebinar.basejava.util.DateUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        if (uuid.equals("")) {
            uuid = UUID.randomUUID().toString();
            storage.save(new Resume(uuid, fullName));
        }
        Resume r = storage.get(uuid);
        r.setFullName(fullName);
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.setContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            AbstractSection section = null;
            String value = null;
            switch (type) {
                case PERSONAL:
                case OBJECTIVE:
                    value = request.getParameter(type.name().trim());
                    section = value.length() > 0 ? new SingleLineSection(value) : null;
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    value = request.getParameter(type.name().trim());
                    List<String> content = Arrays
                            .stream(value.split("\r\n|\n"))
                            .filter(s -> s.trim().length() > 0)
                            .collect(Collectors.toList());
                    section = content.size() > 0 ? new ListSection(content) : null;
                    break;
                case EDUCATION:
                case EXPERIENCE:
                    List<Organization> organizations = new ArrayList<>();
                    String[] names = request.getParameterValues(type.name() + "_name");
                    String[] urls = request.getParameterValues(type.name() + "_url");
                    for (int i = 0; i < names.length; i++) {
                        String nameParam = type.name() + "_" + i;
                        if (names[i].trim().length() > 0) {
                            String[] startDates = request.getParameterValues(nameParam + "_startDate");
                            String[] endDates = request.getParameterValues(nameParam + "_endDate");
                            String[] titles = request.getParameterValues(nameParam + "_title");
                            String[] descriptions = request.getParameterValues(nameParam + "_description");
                            List<Experience> experiences = new ArrayList<>();
                            for (int x = 0; x < startDates.length; x++) {
                                if (startDates[x].trim().length() > 0 && titles[x].trim().length() > 0) {
                                    experiences.add(new Experience(
                                            DateUtil.parse(startDates[x]),
                                            DateUtil.parse(endDates[x]),
                                            titles[x],
                                            descriptions[x]));
                                }
                            }
                            organizations.add(new Organization(new Link(names[i], urls[i]), experiences));
                        }
                    }
                    section = organizations.size() > 0 ? new OrganizationSection(organizations) : null;
                    break;
            }

            if (section != null) {
                r.setSection(type, section);
            } else {
                r.getSections().remove(type);
            }
        }
        storage.update(r);
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
                r = storage.get(uuid);
                break;
            case "edit":
                r = storage.get(uuid);
                for (SectionType sectionType : Arrays.asList(SectionType.EDUCATION, SectionType.EXPERIENCE)) {
                    List<Organization> listEducations = new ArrayList<>();
                    listEducations.add(0, Organization.EMPTY);
                    OrganizationSection orgSection = (OrganizationSection) r.getSection(sectionType);
                    if (orgSection != null) {
                        for (Organization org : orgSection.getContent()) {
                            if (org != null) {
                                List<Experience> listExperiences = new ArrayList<>();
                                listExperiences.add(0, Experience.EMPTY);
                                for (Experience exp : org.getExperiences()) {
                                    if (exp != null) {
                                        listExperiences.add(exp);
                                    }
                                }
                                org.setExperiences(listExperiences);
                                listEducations.add(org);
                            }
                        }
                    }
                    r.setSection(sectionType, new OrganizationSection(listEducations));
                }
                break;
            case "add":
                r = Resume.EMPTY;
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }

}
