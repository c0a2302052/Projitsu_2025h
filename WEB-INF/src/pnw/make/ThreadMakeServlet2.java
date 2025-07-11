package pnw.make;

import java.io.IOException;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import pnw.common.PnwDB;

@WebServlet("/make/ThreadMakeServlet2")

public class ThreadMakeServlet2 extends HttpServlet{
    private static final long serialVersionUID = 1L;

    public ThreadMakeServlet2() {
        super();
    }

    /**
     * スレッド作成処理（POST用）
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        ResultSet rs;
        ResultSet rss;
        String forwardURL = null;

        try {
            request.setCharacterEncoding("UTF-8");
            PnwDB db = new PnwDB("2025h");
            String sqlinfo = null;
            String sqltopic = null;
            String sqlmap = null;

            // フォームからのパラメータ取得
            int in_id = 0;
            if (request.getParameter("thread_id") != null) {
                in_id = Integer.parseInt(request.getParameter("thread_id"));
            }

            String title = request.getParameter("thread_title");
            String[] check_tag = request.getParameterValues("tag");
            ArrayList<Integer> tag = new ArrayList<>();
            if (check_tag != null) {
                for (String t : check_tag) {
                    tag.add(Integer.parseInt(t));
                }
            }
            String name = request.getParameter("name");
            String text = request.getParameter("text");

            PreparedStatement stmtinfo;
            PreparedStatement stmttopic;

            // thread_idの最大値取得
            String sqlmax = "SELECT MAX(thread_id) as max_id FROM threads_info";
            PreparedStatement stmtmax = db.getStmt(sqlmax);
            rss = stmtmax.executeQuery();
            int mid = (rss.next()) ? rss.getInt("max_id") + 1 : 1;

            // threads_infoに挿入
            sqlinfo = "INSERT INTO threads_info (thread_id, thread_title) VALUES(?, ?)";
            stmtinfo = db.getStmt(sqlinfo);
            stmtinfo.setInt(1, mid);
            stmtinfo.setString(2, title);
            stmtinfo.executeUpdate();

            // threads_topicに挿入
            sqltopic = "INSERT INTO threads_topic (response_num, post_time, name, main_text, like_num, thread_id) VALUES(1, ?, ?, ?, 0, ?)";
            stmttopic = db.getStmt(sqltopic);
            Timestamp time = new Timestamp(System.currentTimeMillis());
            stmttopic.setTimestamp(1, time);
            stmttopic.setString(2, name);
            stmttopic.setString(3, text);
            stmttopic.setInt(4, mid);
            stmttopic.executeUpdate();

            // mapping_thread_tagに挿入
            sqlmap = "INSERT INTO mapping_thread_tag (thread_id, tag_id) VALUES(?, ?)";
            for (int t : tag) {
                PreparedStatement stmtmap = db.getStmt(sqlmap);
                stmtmap.setInt(1, mid);
                stmtmap.setInt(2, t);
                stmtmap.executeUpdate();
            }

            // データ取得と表示用
            String sql = "SELECT t.response_num, t.post_time, t.name, t.main_text, t.like_num, t.thread_id, i.thread_title " +
                         "FROM threads_topic t JOIN threads_info i ON t.thread_id = i.thread_id " +
                         "WHERE t.thread_id = ? ORDER BY t.post_time ASC";
            PreparedStatement stmt = db.getStmt(sql);
            stmt.setInt(1, mid);
            rs = stmt.executeQuery();

            ArrayList<InfoBean> infoArray = new ArrayList<>();
            while (rs.next()) {
                InfoBean bean = new InfoBean(
                        title,
                        rs.getInt("response_num"),
                        rs.getTimestamp("post_time"),
                        rs.getString("name"),
                        rs.getString("main_text"),
                        rs.getInt("like_num"),
                        rs.getInt("thread_id")
                );
                infoArray.add(bean);
            }

            request.setAttribute("thread_info", infoArray);
            forwardURL = "/make/Thread_make.jsp";

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error_message", e.getMessage());
            forwardURL = "/make/error.jsp";
        }

        // 表示ページへ転送
        RequestDispatcher dispatcher = request.getRequestDispatcher(forwardURL);
        dispatcher.forward(request, response);
    }
}

