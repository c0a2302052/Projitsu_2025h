package pnw.search;

import javax.servlet.annotation.WebServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;
import pnw.common.PnwDB;

@WebServlet("/GroupH/ThreadListServlet")
public class ThreadListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ThreadListServlet() {
        super();
    }
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // HTTP応答のエンコード設定
        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        // 検索キーワードとタグ名を取得
        String keyword = request.getParameter("keyword");
        String[] tagIds = request.getParameterValues("tags"); // チェックボックスのname="tag"で複数取得


        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql;
            PnwDB db = new PnwDB("2025h");
            if (tagIds == null || tagIds.length == 0) {
                // タグ未選択：キーワードのみ
                sql = "SELECT thread_id, thread_title FROM threads_info WHERE thread_title LIKE ?";
                stmt = db.getStmt(sql);
                stmt.setString(1, "%" + (keyword != null ? keyword : "") + "%");
            } else {
                // タグ選択時：キーワード＋タグ全一致
                StringBuilder inClause = new StringBuilder();
                for (int i = 0; i < tagIds.length; i++) {
                    if (i > 0) inClause.append(",");
                    inClause.append("?");
                }
                sql = "SELECT ti.thread_id, ti.thread_title " +
                        "FROM threads_info ti " +
                        "JOIN mapping_thread_tag myt ON ti.thread_id = myt.thread_id " +
                        "WHERE ti.thread_title LIKE ? " +
                        "AND myt.tag_id IN (" + inClause + ") " +
                        "GROUP BY ti.thread_id, ti.thread_title " +
                        "HAVING COUNT(DISTINCT myt.tag_id) = ?";
                stmt = db.getStmt(sql);
                stmt.setString(1, "%" + (keyword != null ? keyword : "") + "%");
                // タグIDをセット
                for (int i = 0; i < tagIds.length; i++) {
                    stmt.setString(i + 2, tagIds[i]);
                }
                // HAVINGのタグ数
                stmt.setInt(tagIds.length + 2, tagIds.length);
            }

            rs = stmt.executeQuery();
            ArrayList<SerchBean> serchArray = new ArrayList<SerchBean>();
            while (rs.next()) {
                int id = rs.getInt("thread_id");
                String title = rs.getString("thread_title");
                SerchBean serchBean = new SerchBean(id, title);
                serchArray.add(serchBean);
            }
            request.setAttribute("thread_list", serchArray);
        } catch (Exception e) {
            e.printStackTrace();
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head><title>バグ発生</title></head>");
            out.println("<body>");
            out.println("<h1>何らかのバグがあります</h1>");
            out.println("<p>" + e + "</p>");
            out.println("</body></html>");
        }


        // 検索結果画面JSPへ転送
        RequestDispatcher dispatcher = request.getRequestDispatcher("searchResult.jsp");
        dispatcher.forward(request, response);
    }
}



