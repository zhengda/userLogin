<%@ page language="java" pageEncoding="UTF-8"%><%
session.setAttribute("user", null);
session.invalidate();
response.sendRedirect("login.jsp");
%>