package pnw.posts;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pnw.common.PnwDB;

import java.sql.Timestamp;

import javax.servlet.RequestDispatcher;


@WebServlet("/ThreadServlet")
public class ThreadServlet extends HttpServlet{

    // コンストラクタ
    public ThreadServlet() {
        super();
    }

    // スレッド内容の表示(GETリクエスト)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // HTTP応答のエンコード設定
        response.setContentType("text/html; charset=UTF-8");

        String forwardURL = "./posts/thread.jsp";
        
        // スレッドIDの受取(リクエストパラメータ)
        Integer thread_id = Integer.parseInt(request.getParameter("id"));
        if (thread_id == null) {
            thread_id = 1;
        }

        // 以下でスレッドの内容を取得する
        try {
            // DBの接続
            PnwDB db = new PnwDB("2025h");
            String sql; 
            PreparedStatement stmt;
            String debug_msg = "今のところ大丈夫";

            // スレッドタイトルの取得
            sql = "select * from threads_info where thread_id = ?";
            stmt = db.getStmt(sql);
            stmt.setInt(1, thread_id);
            ResultSet rs1 = stmt.executeQuery();
            String thread_title = "nullです"; // 初期値
            if (rs1.next()) {
                thread_title = rs1.getString("thread_title");
            }
            if (thread_title == null) {
                thread_title = "スレタイがnullだよ";
            }

            // タグの取得
            sql = "select t.tag_name\n" +
                  "from mapping_thread_tag m\n" +
                  "join tag_info t\n" + 
                  "on m.tag_id = t.tag_id\n" +
                  "where m.thread_id = ?";

            stmt = db.getStmt(sql);
            stmt.setInt(1, thread_id);
            ResultSet rs2 = stmt.executeQuery();
            ArrayList<String> tag_list = new ArrayList<String>();
            while (rs2.next()) {
                tag_list.add(rs2.getString(1));
            }
            

            // 投稿内容の取得
            sql = "select * from threads_topic where thread_id = ?";
            stmt = db.getStmt(sql);
            stmt.setInt(1, thread_id);
            ResultSet rs3 = stmt.executeQuery();
            
            // 投稿内容のリスト
            ArrayList<TopicList> comments = new ArrayList<TopicList>();

            // 投稿内容をリストに格納
            while (rs3.next()) {
                TopicList list = new TopicList(
                    rs3.getInt(1), // post_id
                    rs3.getInt(2), // response_id
                    rs3.getString("post_time"),
                    rs3.getString("name"),
                    rs3.getString("main_text"),
                    rs3.getInt(6), // likes
                    rs3.getInt(7) // thread_id
                    );

                comments.add(list);
            }

            if (comments == null) {
                debug_msg = "commentsがnullだよ～";
            }

            // リクエストにセットする
            request.setAttribute("thread_id", thread_id);
            request.setAttribute("thread_title", thread_title);
            request.setAttribute("tag_list", tag_list);
            request.setAttribute("comments", comments);
            request.setAttribute("debug", debug_msg);

        } catch(Exception e) {
            e.printStackTrace();
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head><title>バグ発生</title></head>");
            out.println("<body>");
            out.println("<h1>何らかのバグがあります</h1>");
            out.println("<p>" + e + "</p>");
            out.println("</body></html>");
        }
        
        // スレッド表示ページに内容を転送
        RequestDispatcher dispatcher = request.getRequestDispatcher(forwardURL);
        dispatcher.forward(request, response);
        }


    // スレッドへの投稿
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // まずここで文字コードを安全に指定（失敗しても落ちないように）
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (Exception e) {
            e.printStackTrace(); // ログに残すだけで進める
        }

        // HTTP応答のエンコード設定
        response.setContentType("text/html; charset=UTF-8");

        // スレッドIDの受取(リクエストパラメータ)
        int thread_id = Integer.parseInt(request.getParameter("thread_id"));
        
        // 投稿内容がこの変数に入る
        String name = request.getParameter("name");
        String main_text = request.getParameter("main_text");
        
        System.out.println("2025h:名前:"+name+":テキスト"+main_text);

        // 以下でスレッドに書き込む(DBに書き込み内容を登録する)
        try {
            request.setCharacterEncoding("UTF-8");
            // DBの接続
            PnwDB db = new PnwDB("2025h");
            String sql;
            PreparedStatement stmt;

            // レス番の決定
            sql = "select count(*) from threads_topic where thread_id = ?";
            stmt = db.getStmt(sql);
            stmt.setInt(1, thread_id);
            ResultSet rs = stmt.executeQuery();
            int cnt = 0;
            if (rs.next()) {
                cnt = rs.getInt(1);
            }
            if (cnt == 0) {
                cnt = 1;
            }
            else {
                cnt++;
            }
            
            // 投稿内容を挿入
            sql = "insert into threads_topic (response_num, post_time, name, main_text, like_num, thread_id) values (?, ?, ?, ?, ?, ?)";
            
            // 現在時刻を取得
            Timestamp now = new Timestamp(System.currentTimeMillis());
            stmt = db.getStmt(sql);
            stmt.setInt(1, cnt);
            stmt.setTimestamp(2, now);
            stmt.setString(3, name);
            stmt.setString(4, main_text);
            stmt.setInt(5, 0); // いいね数の初期値は0
            stmt.setInt(6, thread_id);
            int ret = stmt.executeUpdate();
            
            response.sendRedirect(request.getContextPath() + "/ThreadServlet?id=" + thread_id);

        } catch(Exception e) {
            e.printStackTrace();
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head><title>バグ発生</title></head>");
            out.println("<body>");
            out.println("<h1>何らかのバグがあります</h1>");
            out.println("<p>" + e + "</p>");
            out.println("</body></html>");
            
        }
        
    }
    
}