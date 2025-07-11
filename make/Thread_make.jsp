<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="pnw.make.InfoBean"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>タイトル</title>
</head>
<body>
<h1>入力結果</h1>

<ul>
<%
    ArrayList<InfoBean> infoArray = (ArrayList<InfoBean>) request.getAttribute("thread_info");
    if (infoArray != null && !infoArray.isEmpty()) {
        InfoBean bean = infoArray.get(0);
%>
            <li>スレッドタイトル: <%= bean.getTitle() /* InfoBeanにタイトル取得メソッドがあれば */ %></li>
            <li>レスポンス番号: <%= bean.getResponseNum() %></li>
            <li>投稿時間: <%= bean.getPostTime() %></li>
            <li>名前: <%= bean.getName() %></li>
            <li>本文: <%= bean.getMainText() %></li>
            <li>いいね数: <%= bean.getLikeNum() %></li>
            <li>スレッドID: <%= bean.getThreadId() %></li>
            <hr>
<%
    } else {
%>
    <li>表示するスレッドがありません。</li>
<%
    }
%>
</ul>



</body>
</html>