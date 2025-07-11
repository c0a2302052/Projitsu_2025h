<!-- filepath: c:\mika\大学講義\3年\前期\先進情報プロジェクト実習Ⅰ\グループ開発\2025h\WEB-INF\src\pnw\common\GroupH\searchResult.jsp -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="pnw.search.SerchBean" %>
<html>
<head>
    <title>検索結果</title>
</head>
<body>
    <h2>検索結果</h2>
    <p>検索キーワード: <strong><%= request.getParameter("keyword") != null ? request.getParameter("keyword") : "" %></strong></p>
    <hr>
<%
    // ThreadListServletから渡された検索結果リストを取得
    ArrayList<SerchBean> threadList = (ArrayList<SerchBean>) request.getAttribute("thread_list");
    if (threadList == null || threadList.size() == 0) {
%>
    <p>該当するスレッドはありません。</p>

    <%
    } else {
%>
    <ul>
    <%
        // 検索結果をリスト表示
        for(SerchBean thread : threadList) {
    %>    
        <li>
            <strong>スレッドID:</strong> <%= thread.getID() %>
            <strong>タイトル:</strong> <%= thread.getTitle() %>
        </li>
    <%
        }
    %>
    </ul>
<%
    }
%>
<br>
<a href="./TagListServlet">検索画面に戻る</a>
</body>
</html>