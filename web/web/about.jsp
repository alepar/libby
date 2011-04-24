<%@ page import="ru.alepar.web.AppHolder" %>
<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8" %>
<html>
<head>
    <title>traum@alepar.ru - About</title>
    <meta equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

<div style="font-weight: bold">Query cheat sheet</div>
<table cellspacing="10">
    <thead style="font-style: italic;">
    <td>Example</td>
    <td>Description</td>
    </thead>
    <tr>
        <td>test*</td>
        <td>multiple character wildcard search</td>
    </tr>
    <tr>
        <td>te?t</td>
        <td>single character wildcard search</td>
    </tr>
    <tr>
        <td>roam~</td>
        <td>fuzzy search, will find terms like foam and roams</td>
    </tr>
    <tr>
        <td>roam~0.8</td>
        <td>fuzzy search, number specifies the required similarity. The value is between 0 and 1, with a value closer to
            1 only terms with a higher similarity will be matched.
        </td>
    </tr>
    <tr>
        <td>jakarta^4 apache</td>
        <td>boosting a term. Will make term "jakarta" more relevant by a boost factor of 4.</td>
    </tr>
    <tr>
        <td></td>
        <td><a href="http://lucene.apache.org/java/3_1_0/queryparsersyntax.html">more</a></td>
    </tr>
</table>

<table width="100%">
    <tr align="center">
        <td><%=AppHolder.status()%>
        </td>
    </tr>
    <tr align="center">
        <td>libby ver.0.5</td>
    </tr>
    <tr align="center">
        <td><a href="http://lucene.apache.org"><img src="/static/lucene.gif"
                                                    alt="Lucene" style="border-style: none"/></a></td>
    </tr>
    <tr align="center">
        <td><a href="http://tomcat.apache.org"><img src="/static/tomcat.gif" alt="Tomcat"
                                                    style="border-style: none"/></a>
        </td>
    </tr>
</table>

</body>
</html>