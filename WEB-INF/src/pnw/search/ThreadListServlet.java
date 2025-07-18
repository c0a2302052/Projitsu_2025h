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
        String searchType = request.getParameter("searchType"); // "AND" or "OR" を想定

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql;
            PnwDB db = new PnwDB("2025h");
            // キーワード分割（全角・半角スペース対応）
            String[] keywords = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim().split("[\\s　]+") : new String[0];
            StringBuilder whereClause = new StringBuilder();
            ArrayList<String> params = new ArrayList<>();
            // キーワード条件生成
            if (keywords.length > 0) {
                if ("AND".equalsIgnoreCase(searchType)) {
                    // AND検索: すべてのキーワードが連続して含まれる（例: "title 1" → "%title%1%"）
                    StringBuilder andKeyword = new StringBuilder();
                    for (int i = 0; i < keywords.length; i++) {
                        andKeyword.append(keywords[i]);
                        if (i < keywords.length - 1) {
                            andKeyword.append("%");
                        }
                    }
                    whereClause.append("ti.thread_title LIKE ?");
                    params.add("%" + andKeyword.toString() + "%");
                } else {
                    // OR検索: いずれかのキーワードが含まれる
                    whereClause.append("(");
                    for (int i = 0; i < keywords.length; i++) {
                        if (i > 0) whereClause.append(" OR ");
                        whereClause.append("ti.thread_title LIKE ?");
                        params.add("%" + keywords[i] + "%");
                    }
                    whereClause.append(")");
                }
            } else {
                // キーワード未指定
                whereClause.append("1=1");
            }
            // タグ条件
            if (tagIds == null || tagIds.length == 0) {
                // タグ未選択
                sql = "SELECT thread_id, thread_title FROM threads_info ti WHERE " + whereClause;
                stmt = db.getStmt(sql);
                for (int i = 0; i < params.size(); i++) {
                    stmt.setString(i + 1, params.get(i));
                }
            } else {
                StringBuilder inClause = new StringBuilder();
                for (int i = 0; i < tagIds.length; i++) {
                    if (i > 0) inClause.append(",");
                    inClause.append("?");
                }
                if ("AND".equalsIgnoreCase(searchType)) {
                    sql = "SELECT ti.thread_id, ti.thread_title " +
                            "FROM threads_info ti " +
                            "JOIN mapping_thread_tag myt ON ti.thread_id = myt.thread_id " +
                            "WHERE ti.thread_title LIKE '%title%1%' ";
                            // " AND myt.tag_id IN (" + inClause + ") " +
                            // "GROUP BY ti.thread_id, ti.thread_title " +  //いらないかも
                            // "HAVING COUNT(DISTINCT myt.tag_id) = ?"   //いらないかも
                    stmt = db.getStmt(sql);
                    int idx = 1;
                    for (String p : params) {
                        stmt.setString(idx++, p);
                    }
                    for (int i = 0; i < tagIds.length; i++) {
                        stmt.setString(idx++, tagIds[i]);
                    }
                    stmt.setInt(idx, tagIds.length);
                } else {
                    sql = "SELECT ti.thread_id, ti.thread_title " +
                            "FROM threads_info ti " +
                            "JOIN mapping_thread_tag myt ON ti.thread_id = myt.thread_id " +
                            "WHERE " + whereClause +
                            " AND myt.tag_id IN (" + inClause + ") " +
                            "GROUP BY ti.thread_id, ti.thread_title";
                    stmt = db.getStmt(sql);
                    int idx = 1;
                    for (String p : params) {
                        stmt.setString(idx++, p);
                    }
                    for (int i = 0; i < tagIds.length; i++) {
                        stmt.setString(idx++, tagIds[i]);
                    }
                }
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
            request.setAttribute("tagList", tagIds);
            request.setAttribute("keyword", keyword);
            request.setAttribute("searchType", searchType);
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

