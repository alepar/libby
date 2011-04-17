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

<form action="" method="get">
    <input type="text" name="query"/>
    <input type="submit" value="go"/>
</form>

<%
    String query = request.getParameter("query");
    if (query != null) {
        AppHolder.Result result = AppHolder.query(query);
        Author[] authors = result.authors;
        Book[] books = result.books;
        int max = Math.max(authors.length, books.length);
%>
<table width="100%">
    <th>Authors</th>
    <th>Books</th>
    <%
        for (int i = 0; i < max; i++) {
    %>
    <tr>
        <td><% if (i < authors.length) {
            Author author = authors[i]; %> <a href='view?<%=author.path%>'><%=author.name%>
        </a> <%} %></td>
        <td><% if (i < books.length) {
            Book book = books[i]; %> <a
                href='get?<%=book.path%>'><%=book.seriesName != null ? book.seriesName + ": " : ""%><%=book.name%>
        </a> <%} %></td>
    </tr>
    <%
        }
    %>
</table>
<%
    }
%>

</body>
</html>