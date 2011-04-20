<%@ page import="ru.alepar.lib.index.Author" %>
<%@ page import="ru.alepar.lib.index.Book" %>
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
    <input type="submit" value="go"/>
</form>

<% String query = request.getParameter("query");
    if (query != null) {
        AppHolder.Result result = AppHolder.query(query); %>
<table width="100%">
    <% for (Author author : result.authors) { %>
    <tr>
        <td><b><a href='?path=<%=author.path%>'><%=author.name%>
        </a></b></td>
    </tr>
    <% } %>
    <% for (Book book : result.books) { %>
    <tr>
        <td><a href='get?<%=book.path%>'><%=book.seriesName != null ? book.seriesName + ": " : ""%><%=book.name%>
        </a></td>
    </tr>
    <% } %>
</table>
<% } %>

</body>
</html>