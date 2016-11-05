<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page isELIgnored="false" %>

<html>
<head>
    <title>Login page</title>
</head>
<body>
<h1>Welcome to Homework 5</h1>

<p>Please log in.</p>

<form action="LoginServlet" method="post">
    User Name: <input type="text" name="name"/> <br/>
    Password: <input type="text" name="password"/>
    <input type="submit" value="login"/>
</form>
<a href="create_new.jsp">Create New Account</a>
</body>
</html>