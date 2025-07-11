<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, java.util.Map" %>
<%@ page import="pnw.posts.TopicList" %>
<%
    Integer thread_id = (Integer)request.getAttribute("thread_id");
    String thread_title = (String)request.getAttribute("thread_title");
    ArrayList<String> tag_list = (ArrayList<String>)request.getAttribute("tag_list");
    ArrayList<TopicList> comments = (ArrayList<TopicList>)request.getAttribute("comments");
    String debug_msg = (String)request.getAttribute("debug");
%>

<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title><%= thread_title %> | TUT総合掲示板</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/posts/style.css">
</head>
<body>

<header>TUT総合掲示板</header>

<div class="container">
  <div class="thread-title">
    <%= thread_title %>
  </div>
  <div class="tag-container">
  <% if (tag_list != null) {
        for (String tag : tag_list) {
          %>
  <div class="tag"><%= tag %></div>
  <% }} %>
  </div>
  <% if (comments != null) {
        for (TopicList comment : comments) {
          int res_num = comment.getResponse_id();
          String name = comment.getName();
          String time = comment.getPost_time();
          String main_text = comment.getMain_text();
          int likes = comment.getLikes();
  %>
    <div class="comment">
      <div class="comment-number"><%= res_num %>: <%= name %> <span style="color: grey;"> <%= time %></span></div>
      <div><%= main_text.replaceAll("\n", "<br>") %></div>
      <div class="like">♡ <%= likes %></div>
    </div>
  <% }} else { %>
    <div class="comment">
      <div>コメントはまだありません。<%= debug_msg %></div>
    </div>
  <% } %>

  <form action="./ThreadServlet" method="post">
    <input type="hidden" name="thread_id" value="<%= thread_id %>">

    <label for="name">名前</label>
    <input type="text" id="name" name="name" value="名無しさん">

    <label for="main_text">書き込み</label>
    <textarea id="main_text" name="main_text" rows="5"></textarea>

    <button type="submit">投稿する</button>
  </form>
</div>

</body>
</html>
