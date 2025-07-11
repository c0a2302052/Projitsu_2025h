<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="pnw.mainpage.ThreadInfo" %>
<%
    List<ThreadInfo> threadList = (List<ThreadInfo>) request.getAttribute("threadList");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8" />
  <title>TUT総合掲示板</title>
  <style>
    body {
      font-family: sans-serif;
      background-color: #e0f7fa;
      margin: 0;
      padding: 0;
    }
    .container {
      width: 90%;
      max-width: 600px;
      margin: 30px auto;
      background-color: #b2ebf2;
      border-radius: 10px;
      padding: 20px;
      box-shadow: 2px 2px 10px rgba(0,0,0,0.1);
    }
    .header {
      background-color: #80deea;
      padding: 15px;
      text-align: center;
      font-size: 1.5em;
      font-weight: bold;
      border-radius: 5px;
      margin-bottom: 15px;
    }
    .top-bar {
      display: flex;
      justify-content: space-between;
      margin-bottom: 15px;
    }
    .create-btn {
      background-color: #00acc1;
      color: white;
      border: none;
      padding: 8px 12px;
      border-radius: 5px;
      font-weight: bold;
      cursor: pointer;
    }
    .search-box input {
      padding: 6px;
      border: 1px solid #ccc;
      border-radius: 5px;
      width: 150px;
    }
    .tag-bar {
      display: flex;
      flex-wrap: wrap;
      gap: 6px;
      margin-bottom: 15px;
    }
    .tag-bar button {
      background: #ffffff;
      border: 1px solid #00796b;
      border-radius: 5px;
      padding: 5px 10px;
      cursor: pointer;
      color: #00796b;
      font-weight: bold;
    }
    .thread-list {
      background: #ffffff;
      border-radius: 5px;
      padding: 30px;
      overflow-y: visible;
    }
    .thread-header, .thread {
      display: flex;
      align-items: center;
      padding: 8px 0;
      border-bottom: 1px solid #ccc;
    }
    .thread-header {
      font-weight: bold;
    }
    .thread-header div,
    .thread div {
      flex: 1;
    }
    .thread-title {
      font-weight: bold;
    }
    .tags {
      font-size: 0.8em;
      color: #00796b;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="header">TUT総合掲示板</div>

    <div class="top-bar">
      <button class="create-btn" onclick="location.href='create-thread.html'">スレッド作成</button>
      <form class="search-box" action="#" method="get">
        <input type="text" name="q" placeholder="スレッド検索" />
      </form>
    </div>

    <div class="tag-bar">
      <form action="ThreadListServlet" method="get">
        <button type="submit" name="tag" value="1">#タグ10</button>
        <button type="submit" name="tag" value="2">#タグ2</button>
        <button type="submit" name="tag" value="3">#タグ3</button>
        <button type="submit" name="tag" value="4">#タグ4</button>
        <button type="submit" name="tag" value="5">#タグ5</button>
      </form>
    </div>


    <div class="thread-list">
      <div class="thread-header">
        <div>レス数</div>
        <div>スレッドタイトル</div>
      </div>

      <%
        if (threadList != null && !threadList.isEmpty()) {
          for (ThreadInfo t : threadList) {
      %>
        <div class="thread">
          <div><%= t.getResponseCount() %></div>
          <div>
            <span class="thread-title"><%= t.getTitle() %></span><br/>
            <span class="tags">
              <%
                for (String tag : t.getTags()) {
                  if (tag != null && !tag.isEmpty()) {
              %>#<%= tag %> <% } } %>
            </span>
          </div>
        </div>
      <%
          }
        } else {
      %>
        <div class="thread"><div colspan="2">スレッドがありません。</div></div>
      <%
        }
      %>
    </div>
  </div>
</body>
</html>
