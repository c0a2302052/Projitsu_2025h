<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>エラー</title>
</head>
    <body>
        <h1>エラー</h1>
        <p>エラー内容: <%= request.getAttribute("error_message") %></p>
</body>
</html>