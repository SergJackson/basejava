package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.SqlStorage;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResumeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String uuid = request.getParameter("uuid");
        headerTable(response);
        Storage storage = new SqlStorage();
        //Storage storage = new ListStorage();
        //storage.save(genResumeTest(UUID.randomUUID().toString(), "Full Name 01"));
        //storage.save(genResumeTest(UUID.randomUUID().toString(), "Full Name 02"));
        //storage.save(genResumeTest(UUID.randomUUID().toString(), "Full Name 03"));
        if (uuid != null) {
            bodyTable(response, storage.get(uuid));
        } else {
            for (Resume resume : storage.getAllSorted()) {
                bodyTable(response, resume);
            }
        }
        footerTable(response);
    }

    private void headerTable(HttpServletResponse response) throws IOException {
        response.getWriter().write("" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <link rel=\"stylesheet\" href=\"css/style.css\">\n" +
                "    <title>Курс JavaSE + Web.</title>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <center>\n" +
                "    <table id = \"t01\">\n" +
                "      <tr>\n" +
                "        <th>UUID</th>\n" +
                "        <th>Full Name</th> \n" +
                "      </tr>\n");
    }

    private void bodyTable(HttpServletResponse response, Resume resume) throws IOException {
        response.getWriter().write("" +
                "      <tr>\n" +
                "        <td>" + resume.getUuid() + "</td>\n" +
                "        <td>" + resume.getFullName() + "</td> \n" +
                "      </tr>\n");
    }

    private void footerTable(HttpServletResponse response) throws IOException {
        response.getWriter().write("" +
                "    </table>\n" +
                "  </body>\n" +
                "</html>\n");
    }

}
