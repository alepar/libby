<%@ page import="ru.alepar.ebook.format.EbookType" %>
<%@ page import="ru.alepar.lib.model.Folder" %>
<%@ page import="ru.alepar.lib.model.Item" %>
<%@ page import="ru.alepar.servlet.EbookTypeFilter" %>
<%@ page import="ru.alepar.web.ItemLinkFormatter" %>
<%@ page import="ru.alepar.web.LibbyApp" %>
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
            <% for (EbookType type : EbookType.values()) { %>
            <option value="<%=type.name()%>" <%=type == EbookTypeFilter.ebookType(request) ? "selected" : ""%>><%=type.name()%> </option>
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
            items = LibbyApp.Instance.get().query(query);
        }
        if (path != null && !path.isEmpty()) {
            items = LibbyApp.Instance.get().list(path);
        }
        if (items != null) {
    %>
    <table width="100%">
        <%
        final ItemLinkFormatter linkFormatter = new ItemLinkFormatter();
        for (Item item : items) {
            final Folder parent = item.parentFolder();%>
        <tr><td>
            <% if (query != null && !query.isEmpty() && parent != null && (!(item instanceof Folder) || !"..".equals(((Folder)item).name))) { %> <%=linkFormatter.getLink(parent)%> / <% } %>
            <%=linkFormatter.getLink(item)%>
        </td></tr>
    <% } %>
    </table>
<%} %>
</div>
</body>
</html>