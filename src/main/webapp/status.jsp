<%--
  User: craigcook
  Date: May 30, 2010
  Time: 1:21:07 PM

  TODO: - secure this
        - import ative boolean as static
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.cccs.tfs.service.SiteService" %>

<html>
<head><title>Set site active status</title></head>
<body>
<h1>The site is currently <%=(SiteService.IS_ACTIVE) ? "active" : "inactive"%>
</h1>
<%
    if (request.getParameter("active") != null) {
        boolean active = Boolean.parseBoolean(request.getParameter("status"));
        SiteService.IS_ACTIVE = active;
%>
<h1>Setting site as <%=(active) ? "active" : "inactive"%>
</h1>
<%
    }
%>
<h3></h3>
</body>
</html>
