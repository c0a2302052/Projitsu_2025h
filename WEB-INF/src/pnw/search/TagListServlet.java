package pnw.search;

import javax.servlet.annotation.WebServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.swing.text.html.HTML.Tag;

import pnw.common.PnwDB;

@WebServlet("/GroupH/TagListServlet")
public class TagListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public TagListServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // HTTP応答のエンコード設定
        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM tag_info";
            PnwDB db = new PnwDB("2025h");

            stmt = db.getStmt(sql);
            rs = stmt.executeQuery();

            ArrayList<TagBean> tagArray = new ArrayList<TagBean>();
            while (rs.next()) {
                int id = rs.getInt("tag_id");
                String name = rs.getString("tag_name");
                TagBean tagBean = new TagBean(id, name);
                tagArray.add(tagBean);
            }
            request.setAttribute("tagList", tagArray);
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

        // タグ一覧画面JSPへ転送
        RequestDispatcher dispatcher = request.getRequestDispatcher("/GroupH/kensaku.jsp");
        dispatcher.forward(request, response);
    }
}
