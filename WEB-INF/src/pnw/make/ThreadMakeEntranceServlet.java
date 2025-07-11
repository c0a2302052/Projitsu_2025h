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

@WebServlet("/make/MakeEntrance")
public class ThreadMakeEntranceServlet extends HttpServlet{
    private static final long serialVersionUID = 1L;

    public ThreadMakeEntranceServlet() {
        super();
    }

    /**
     * スレッド作成処理（POST用）
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");
        String forwardURL = null;

        try {
            request.setCharacterEncoding("UTF-8");
            PnwDB db = new PnwDB("2025h");
            String sqltag = null;

            // フォームからのパラメータ取得
            int in_id = 0;
            if (request.getParameter("thread_id") != null) {
                in_id = Integer.parseInt(request.getParameter("thread_id"));
            }

            sqltag = "SELECT * FROM tag_info";
            PreparedStatement stmt = db.getStmt(sqltag);
            ResultSet rs = stmt.executeQuery();
            ArrayList<TagBean> infoArray = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("tag_id");
                String name = rs.getString("tag_name");
                TagBean bean = new TagBean(id, name);
                infoArray.add(bean);
            }

            request.setAttribute("tag_info", infoArray);
            forwardURL = "/make/Thread_make_input3.jsp";

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
