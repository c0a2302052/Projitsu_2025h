<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="pnw.common.GroupH.SerchBean" %>
<html>
<head>
    <title>検索結果</title>
    <style>
        body {
            font-family: "メイリオ", sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
        }

        header {
            background-color: #87E6FA;
            padding: 1em;
            text-align: center;
            font-size: 28px;
            font-weight: bold;
            color: #000;
        }

        .container {
            background-color: #ffffff;
            max-width: 700px;
            margin: 30px auto;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }

        h2 {
            color: #333;
            border-bottom: 2px solid #87E6FA;
            padding-bottom: 0.3em;
        }

        .info-box {
            background-color: #e6faff;
            padding: 10px 15px;
            border: 1px solid #bdeaff;
            border-radius: 6px;
            margin-bottom: 15px;
            font-size: 16px;
        }

        ul {
            list-style: none;
            padding-left: 0;
        }

        li {
            background-color: #f0fbff;
            margin-bottom: 10px;
            padding: 10px 15px;
            border-left: 5px solid #87E6FA;
            border-radius: 6px;
        }

        a {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 18px;
            background-color: #87E6FA;
            color: black;
            text-decoration: none;
            font-weight: bold;
            border-radius: 8px;
        }

        a:hover {
            background-color: #70d5ea;
        }
    </style>
</head>
<body>
    <header>TUT総合掲示板 - 検索結果</header>

    <div class="container">
        <h2>検索結果</h2>

        <div class="info-box">
            検索キーワード: <strong><%= request.getAttribute("keyword") != null ? request.getAttribute("keyword") : "" %></strong>
        </div>

        <div class="info-box">
            選択したタグ: <strong><%= request.getAttribute("tagList") != null ? String.join("、", (String[]) request.getAttribute("tagList")) : "" %></strong>
        </div>

<%
    ArrayList<SerchBean> threadList = (ArrayList<SerchBean>) request.getAttribute("thread_list");
    if (threadList == null || threadList.size() == 0) {
%>
        <p>該当するスレッドはありません。</p>
<%
    } else {
%>
        <ul>
<%
        for(SerchBean thread : threadList) {
%>    
            <li>
                <strong>スレッドID:</strong> <%= thread.getID() %><br>
                <strong>タイトル:</strong> <%= thread.getTitle() %>
            </li>
<%
        }
%>
        </ul>
<%
    }
%>

        <a href="./TagListServlet">検索画面に戻る</a>
    </div>
</body>
</html>