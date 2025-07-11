package pnw.thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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

import javax.servlet.RequestDispatcher;

@WebServlet("/thread/ThreadMakeServlet")
public class ThreadMakeServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ThreadMakeServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// HTTP応答のエンコード設定
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
			int in_id = 0;
			int id = 0;
			if(request.getParameter("thread_id") != null) {
				in_id = Integer.valueOf(request.getParameter("thread_id")).intValue();
			}
			//データベースへの入力部

			//スレッドタイトル
			String title = request.getParameter("thread_title");
			//チェックボックス
			String[] check_tag = request.getParameterValues("tag");
			ArrayList<Integer> tag = new ArrayList<Integer>();
			if(check_tag != null) {
				for(int i=0; i<check_tag.length; i++) {
					tag.add(Integer.parseInt(check_tag[i]));
				}
			}
			//名前
			String name = request.getParameter("name");
			//テキストエリア
			String text = request.getParameter("text");
			PreparedStatement stmtinfo = null;
			PreparedStatement stmttopic = null;

			sqlinfo = "INSERT INTO threads_info (thread_id, thread_title) VALUES(?, ?)";
			stmtinfo = db.getStmt(sqlinfo);
			String sqlmax = "SELECT MAX(thread_id) as max_id FROM threads_info";
			PreparedStatement stmtmax = db.getStmt(sqlmax);
			int mid;
			rss = stmtmax.executeQuery();
			if(rss.next()) {
			mid = rss.getInt("max_id")+1;
			}else {
				mid = 1;
			}
			stmtinfo.setInt(1, mid);
			stmtinfo.setString(2, title);

			int retinfo = stmtinfo.executeUpdate();


			sqltopic = "INSERT INTO threads_topic (response_num, post_time, name, main_text, like_num, thread_id) VALUES(1, ?, ?, ?, 0, ?)";
			stmttopic = db.getStmt(sqltopic);
			Timestamp time = new Timestamp(System.currentTimeMillis());
			stmttopic.setTimestamp(1, time);
			stmttopic.setString(2, name);
			stmttopic.setString(3, text);
			stmttopic.setInt(4, mid);
			int rettopic = stmttopic.executeUpdate();


			sqlmap = "INSERT INTO mapping_thread_tag (thread_id, tag_id) VALUES(?, ?)";
			for(int t : tag) {
				PreparedStatement stmtmap = db.getStmt(sqlmap);
				stmtmap.setInt(1, mid);
				stmtmap.setInt(2, t);
				int retmap = stmtmap.executeUpdate();
			}
			

			//実行結果取得
			String sql = "SELECT  t.response_num, t.post_time, t.name, t.main_text, t.like_num, t.thread_id, i.thread_title FROM threads_topic t JOIN threads_info i ON t.thread_id = i.thread_id WHERE t.thread_id = ? ORDER BY t.post_time ASC";
			PreparedStatement  stmt = db.getStmt(sql);
			stmt.setInt(1, mid);
			rs = stmt.executeQuery();
			int cnt = 0;
			ArrayList<InfoBean> infoArray = new ArrayList<InfoBean>();
			while(rs.next()) {
				int rnum = rs.getInt("response_num");
				Timestamp tm = rs.getTimestamp("post_time");
				String nm = rs.getString("name");
				String tx = rs.getString("main_text");
				int lnum = rs.getInt("like_num");
				int thid = rs.getInt("thread_id");

				InfoBean bean = new InfoBean(title, rnum, tm, nm, tx, lnum, thid);
				infoArray.add(bean);
				cnt++;
			}

			request.setAttribute("thread_info", infoArray);
			forwardURL = "/thread/Thread_make.jsp";

		} catch(Exception e) {
				e.printStackTrace();
				request.setAttribute("error_message", e.getMessage());
				forwardURL = "/thread/error.jsp";
		}
		
		



		// 外部ファイルに転送する準備
		// ファイルの場所は，/webapps/pnw/から見た場所
		RequestDispatcher dispatcher = request.getRequestDispatcher(forwardURL);
		// 外部ファイルに表示処理を任せる
		dispatcher.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
