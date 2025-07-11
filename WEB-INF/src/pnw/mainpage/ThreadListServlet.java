package pnw.mainpage;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/model/ThreadListServlet")
public class ThreadListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    try {
        String tagIdStr = request.getParameter("tag");
        ThreadDAO dao = new ThreadDAO();
        List<ThreadInfo> threadList;

        if (tagIdStr != null && !tagIdStr.isEmpty()) {
            int tagId = Integer.parseInt(tagIdStr);
            threadList = dao.getThreadsByTagId(tagId);
        } else {
            threadList = dao.getAllThreads();
        }

        request.setAttribute("threadList", threadList);
        RequestDispatcher rd = request.getRequestDispatcher("/front/index.jsp");
        rd.forward(request, response);

    } catch (Exception e) {
        e.printStackTrace();
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().println("<p>エラー: " + e.getMessage() + "</p>");
    }
}

}
