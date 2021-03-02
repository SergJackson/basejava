<%@ page import="ru.javawebinar.basejava.model.*" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
<%@ page import="java.time.LocalDate" %>
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
                        <dd><textarea rows="5" name="${typeSection.name()}" style="text-wrap: none"
                                      cols="68">${resume.getSection(typeSection).toHtmlEdit()}</textarea>
                        </dd>
                    </dl>
                </c:when>
                <c:when test="${typeSection == SectionType.EXPERIENCE || typeSection == SectionType.EDUCATION}">
                    <dl>
                        <dt>${typeSection.title}</dt>
                    </dl>
                    <div style="margin-left: 30px;">
                        <c:set var="organisations" value="${resume.sections.get(typeSection)}"/>
                        <c:if test="${empty organisations}">
                            <c:set var="organisations"
                                   value="<%=new OrganizationSection(new Organization(\"\",null, new Experience(LocalDate.now(), LocalDate.now(), \"\", null)))%>"/>
                        </c:if>
                        <jsp:useBean id="organisations" type="ru.javawebinar.basejava.model.AbstractSection"/>
                        <c:forEach var="organisation"
                                   items="<%=((OrganizationSection)organisations).getContent()%>"
                                   varStatus="number_org">
                            <dl>
                                <dt>Организация</dt>
                                <dd>
                                    <input type="text" name="${typeSection.name()}_name"
                                           size=50%
                                           value="${organisation.link.name}">
                                </dd>
                            </dl>
                            <dl>
                                <dt>Сайт</dt>
                                <dd>
                                    <input type="text" name="${typeSection.name()}_url"
                                           size=50%
                                           value="${organisation.link.url}">
                                </dd>
                            </dl>
                            <br>
                            <div style="margin-left: 30px">
                                <c:forEach var="experience"
                                           items="${organisation.experiences}">
                                    <jsp:useBean id="experience" type="ru.javawebinar.basejava.model.Experience"/>
                                    <dl>
                                        <dt>Начальная дата:</dt>
                                        <dd><input type="text"
                                                   name="${typeSection.name()}_${number_org.index}_startDate"
                                                   size=10
                                                   value="<%=DateUtil.format(experience.getStartDate())%>" placeholder="MM/yyyy">
                                    </dl>
                                    <dl>
                                        <dt>Конечная дата:</dt>
                                        <dd><input type="text"
                                                   name="${typeSection.name()}_${number_org.index}_endDate"
                                                   size=10
                                                   value="<%=DateUtil.format(experience.getEndDate())%>" placeholder="MM/yyyy">
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>Должность:</dt>
                                        <dd><input type="text"
                                                   name="${typeSection.name()}_${number_org.index}_title"
                                                   size=40%
                                                   value="${experience.title}">
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>Описание:</dt>
                                        <dd><input type="text"
                                                   name="${typeSection.name()}_${number_org.index}_description"
                                                   size=40%
                                                   value="${experience.description}">
                                        </dd>
                                    </dl>
                                    <br>
                                </c:forEach>
                            </div>
                        </c:forEach>
                    </div>
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
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>