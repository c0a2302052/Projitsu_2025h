<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>TUT総合掲示板</title>
  <style>
    body {
      font-family: sans-serif;
      background-color: #f5f5f5;
      margin: 0;
      padding: 0;
    }
    .header {
      background-color: #80eaff;
      padding: 10px;
      font-size: 24px;
      font-weight: bold;
    }
    .container {
      padding: 20px;
      background-color: #fff;
      max-width: 400px;
      margin: 20px auto;
      border: 1px solid #ccc;
      border-radius: 8px;
    }
    .section-title {
      font-size: 18px;
      margin-bottom: 10px;
    }
    label {
      margin-right: 10px;
    }
    .tags {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
      margin: 10px 0;
    }
    .tags label {
      display: flex;
      align-items: center;
    }
    .radio-group {
      margin: 10px 0;
    }
    .submit-btn {
      background-color: #80eaff;
      border: none;
      padding: 10px 20px;
      font-size: 18px;
      cursor: pointer;
      border-radius: 5px;
    }
    .submit-btn:hover {
      background-color: #60d4e6;
    }
  </style>
</head>
<body>
  <div class="header">TUT総合掲示板</div>
  <div class="container">
    <div class="section-title">スレッド検索</div>
    <form action="./ThreadListServlet" method="get">
      <label for="keyword">キーワード</label><br>
      <input type="text" id="keyword" name="keyword" style="width: 100%;"><br><br>

      <div class="radio-group">
        <input type="radio" id="or" name="condition" value="or" checked>
        <label for="or">OR</label>
        <input type="radio" id="and" name="condition" value="and">
        <label for="and">AND</label>
      </div>
      <div class="tags">
        <c:forEach var="tag" items="${tagList}">
          <label>
            <input type="checkbox" name="tags" value="${tag.id}">
            #${tag.name}
          </label>
        </c:forEach>
      </div>
      <div style="text-align: center; margin-top: 20px;">
        <button type="submit" class="submit-btn">検索</button>
      </div>
    </form>
  </div>
</body>
</html>
