<%@ page import="ru.alepar.lib.model.Author" %>
<%@ page import="ru.alepar.lib.model.Book" %>
<%@ page import="ru.alepar.lib.model.Folder" %>
<%@ page import="ru.alepar.lib.model.Item" %>
<%@ page import="ru.alepar.web.AppHolder" %>
<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8" %>
<html>
<head>
    <title>traum@alepar.ru</title>
    <meta equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<div align="right"><%=AppHolder.detect(request.getHeader("User-Agent"))%>
</div>
<form action="" method="get">
    <input type="text" name="query"/>
    <input type="submit" value="go"/> <a href="about.jsp">?</a>
</form>

<% String query = request.getParameter("query");
    String path = request.getParameter("path");
    Iterable<Item> items = null;
    if (query != null) {
        items = AppHolder.query(query);
    }
    if (path != null) {
        items = AppHolder.list(path);
    }
    if (items != null) {
%>
<table width="100%">
    <% for (Item item : items) { %>
    <% if (item instanceof Author) {
        Author author = (Author) item;%>
    <tr>
        <td><b><a href='?path=<%=author.path%>'><%=author.name%>
        </a></b></td>
    </tr>
    <% } %>
    <% if (item instanceof Folder) {
        Folder folder = (Folder) item;%>
    <tr>
        <td><a href='?path=<%=folder.path%>'><%=folder.name%>/
        </a></td>
    </tr>
    <% } %>
    <% if (item instanceof Book) {
        Book book = (Book) item;%>
    <tr>
        <td><a href='get?<%=book.path%>'><%=book.name%>
        </a></td>
    </tr>
    <% } %>
    <% } %>
</table>
<%} %>

</body>
</html>