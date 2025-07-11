<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>
            スレッド作成
        </title>
        <link rel="stylesheet" type="text/css" href="css/thread_make.css">
    </head>
    <body>
        <div class="container">
            <div class="header">TUT総合掲示板</div>
            <h1>スレッド作成</h1>
            <form action="./ThreadMakeServlet2" method="POST">
                <h3>スレッドタイトル</h3>
                <input type="text" size="50" maxlength="50" name="thread_title" required><br>
                <br>
                <h3>タグ</h3><br>
                <div class="tags">
                    <c:forEach var="tag" items="${tag_info}">
                        <input type="checkbox" name="tag" value="${tag.id}">#${tag.name}
                    </c:forEach>
                </div>
                <br>
                <h4>名前:
                    <input type="text" size="15" maxlength="15" name="name" value="名無しさん" required>
                </h4>
                <br>
                <h3>テキスト入力</h3>
                <textarea name="text" rows="10" cols="80" required></textarea>
                <div class="button-container">
                    <input type="submit" value="書き込む" class="submit-button">
                </div>>
            </form>
        </div>
    </body>
</html>