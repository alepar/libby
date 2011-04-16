<%@ page import="ru.alepar.lib.index.Author" %>
<%@ page import="ru.alepar.lib.index.Book" %>
<%@ page import="ru.alepar.web.IndexHolder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>traum@alepar.ru</title></head>
<body>

<form action="" method="get">
    <input type="text" name="query"/>
    <input type="submit" value="go"/>
</form>

<%
    String query = request.getParameter("query");
    if (query != null) {
        IndexHolder.Result result = IndexHolder.query(query);
        Author[] authors = result.authors;
        Book[] books = result.books;
        int max = Math.max(authors.length, books.length);
%>
<table width="100%">
    <th width="50%">Authors</th>
    <th width="50%">Books</th>
    <%
        for (int i = 0; i < max; i++) {
    %>
    <tr>
        <td><% if (i < authors.length) {
            Author author = authors[i]; %> <%=author.name%> <%} %></td>
        <td><% if (i < books.length) {
            Book book = books[i]; %> <%=book.seriesName%>: <%=book.name%> <%} %></td>
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