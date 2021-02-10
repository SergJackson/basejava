<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.OrganizationSection" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <h3>Секции:</h3>
        <c:forEach var="typeSection" items="<%=SectionType.values()%>">
            <c:choose>
                <c:when test="${typeSection == SectionType.OBJECTIVE || typeSection == SectionType.PERSONAL}">
                    <dl>
                        <dt>${typeSection.title}</dt>
                        <dd><input type="text" name="${typeSection.name()}" size=90%
                                   value="${resume.getSection(typeSection)}"></dd>
                    </dl>
                </c:when>
                <c:when test="${typeSection == SectionType.ACHIEVEMENT || typeSection == SectionType.QUALIFICATIONS}">
                    <dl>
                        <dt>${typeSection.title}</dt>
                        <dd><textarea rows="5" name="${typeSection.name()}"
                                      cols="68">${resume.getSection(typeSection)}</textarea>
                        </dd>
                    </dl>
                </c:when>
                <c:when test="${typeSection == SectionType.EXPERIENCE || typeSection == SectionType.EDUCATION}">
                    <dl>
                        <dt>${typeSection.title}</dt>
                        <dd>
                            <div>
                                <c:set var="organisations" value="${resume.sections.get(typeSection)}"/>
                                <jsp:useBean id="organisations" type="ru.javawebinar.basejava.model.AbstractSection"/>
                                <c:forEach var="organisation"
                                           items="<%=((OrganizationSection)organisations).getContent()%>"
                                           varStatus="number_org">
                                    <dl>
                                        <dd>
                                        <dd>
                                        <dt>Организация</dt>
                                        <dd>
                                            <input type="text" name="${typeSection.name()}_org_${number_org.index}_name"
                                                   size=20%
                                                   value="${organisation.link.name}">
                                        </dd>
                                        <dt>Сайт</dt>
                                        <dd>
                                            <input type="text" name="${typeSection.name()}_org_${number_org.index}_url"
                                                   size=20%
                                                   value="${organisation.link.url}">
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dd>
                                            <div>
                                                <c:forEach var="experience"
                                                           items="${organisation.experiences}"
                                                           varStatus="number_exp">
                                                    <dl>
                                                        <dd>
                                                        <dd>
                                                        <dt>C</dt>
                                                        <dd>
                                                            <input type="text"
                                                                   name="${typeSection.name()}_org_${number_org.index}_exp_${number_exp.index}_startDate"
                                                                   size=20%
                                                                   value="${experience.startDate}">
                                                        </dd>
                                                        <dt>По</dt>
                                                        <dd>
                                                            <input type="text"
                                                                   name="${typeSection.name()}_org_${number_org.index}_exp_${number_exp.index}_stopDate"
                                                                   size=20%
                                                                   value="${experience.endDate}">
                                                        </dd>
                                                        <dt>Должность/Специальность</dt>
                                                        <dd>
                                                            <input type="text"
                                                                   name="${typeSection.name()}_org_${number_org.index}_exp_${number_exp.index}_title"
                                                                   size=20%
                                                                   value="${experience.title}">
                                                        </dd>
                                                        <dt>Описание</dt>
                                                        <dd>
                                                            <input type="text"
                                                                   name="${typeSection.name()}_org_${number_org.index}_exp_${number_exp.index}_description"
                                                                   size=20%
                                                                   value="${experience.description}">
                                                        </dd>
                                                    </dl>
                                                </c:forEach>
                                            </div>
                                        </dd>
                                    </dl>
                                </c:forEach>
                            </div>
                        </dd>
                    </dl>
                </c:when>
            </c:choose>
        </c:forEach>
        <!--
                <input type="text" name="section" size=30 value="1"><br/>
                <input type="text" name="section" size=30 value="2"><br/>
                <input type="text" name="section" size=30 value="3"><br/>
        -->
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>