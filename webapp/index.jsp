<%@ page import="ru.alepar.ebook.format.EbookType" %>
<%@ page import="ru.alepar.lib.model.Author" %>
<%@ page import="ru.alepar.lib.model.Book" %>
<%@ page import="ru.alepar.lib.model.Folder" %>
<%@ page import="ru.alepar.lib.model.Item" %>
<%@ page import="ru.alepar.servlet.EbookTypeFilter" %>
<%@ page import="ru.alepar.web.AppHolder" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8" %>

<%
    String paramType = request.getParameter("type");
    if (paramType != null && !paramType.isEmpty()) {
        EbookTypeFilter.saveEbookType(EbookType.valueOf(paramType), request);
    }
%>

<html>
<head>
    <title>traum@alepar.ru</title>
    <meta equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

<div style="float: left">
    <form action="" method="get">
        <input type="text" name="query"/><input type="submit" value="go"/>&nbsp;<a href="about.jsp">?</a>
    </form>
</div>
<div style="float: right">
    <form action="" method="get">
        <select name="type" onChange="this.form.submit()">
            <%
                for (EbookType type : EbookType.values()) {
            %>
            <option value="<%=type.name()%>" <%=type.equals(EbookTypeFilter.ebookType(request)) ? "selected" : ""%>><%=type.name()%>
            </option>
            <% } %>
        </select>
        <% if(request.getParameter("query") != null) { %>
            <input type="hidden" name="query" value="<%=request.getParameter("query")%>" />
        <% } %>
        <% if(request.getParameter("path") != null) { %>
            <input type="hidden" name="path" value="<%=request.getParameter("path")%>" />
        <% } %>
    </form>
</div>

<div id="results" style="clear: both;">
    <% String query = request.getParameter("query");
        String path = request.getParameter("path");
        Iterable<Item> items = null;

        if (query != null && !query.isEmpty()) {
            items = AppHolder.query(query);
        }
        if (path != null && !path.isEmpty()) {
            items = AppHolder.list(path);
        }
        if (items != null) {
    %>
    <table width="100%">
        <%
        for (Item item : items) {
            final String link;
            final String label;
            final boolean bold;
            if (item instanceof Author) {
                final Author author = (Author) item;
                link = "?path=" + URLEncoder.encode(author.path, "UTF-8");
                label = author.name;
                bold = true;
            } else if (item instanceof Folder) {
                final Folder folder = (Folder) item;
                link = "?path=" + URLEncoder.encode(folder.path, "UTF-8");
                label = folder.name;
                bold = false;
            } else if (item instanceof Book) {
                final Book book = (Book) item;
                link = "get?" + URLEncoder.encode(book.path, "UTF-8");
                label = book.name;
                bold = false;
            } else {
                label = "unknown item type " + item;
                link = "";
                bold = false;
            }
        %>
        <tr><td><%=bold ? "<b>" : ""%><a href='<%=link%>'>
            <%=label%>
        </a><%=bold ? "</b>" : ""%></td></tr>
    <% } %>
    </table>
<%} %>
</div>
</body>
</html>