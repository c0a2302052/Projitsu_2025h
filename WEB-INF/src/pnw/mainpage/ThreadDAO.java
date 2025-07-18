package pnw.mainpage;

import pnw.common.PnwDB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ThreadDAO extends PnwDB {

    public ThreadDAO() {
        super("2025h"); // ← あなたのDB名（確認済み）
    }

    // ① 全スレッドの取得（レス数付き）
    public List<ThreadInfo> getAllThreads() {
        List<ThreadInfo> list = new ArrayList<>();

        try {
            String sql = 
                "SELECT i.thread_id, i.thread_title, COUNT(t.thread_id) AS response_count " +
                "FROM threads_info i " +
                "LEFT JOIN threads_topic t ON i.thread_id = t.thread_id " +
                "GROUP BY i.thread_id, i.thread_title";

            PreparedStatement stmt = getStmt(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ThreadInfo t = new ThreadInfo();
                t.setThreadId(rs.getInt("thread_id"));
                t.setTitle(rs.getString("thread_title"));
                t.setResponseCount(rs.getInt("response_count"));
                t.setTags(new ArrayList<>()); // あとで追加できる

                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ② 指定タグ付きスレッドの取得（レス数付き）
    public List<ThreadInfo> getThreadsByTagId(int tagId) {
        List<ThreadInfo> list = new ArrayList<>();
        try {
            String sql =
                "SELECT i.thread_id, i.thread_title, COUNT(t.thread_id) AS response_count " +
                "FROM threads_info i " +
                "JOIN mapping_thread_tag m ON i.thread_id = m.thread_id " +
                "LEFT JOIN threads_topic t ON i.thread_id = t.thread_id " +
                "WHERE m.tag_id = ? " +
                "GROUP BY i.thread_id, i.thread_title";

            PreparedStatement stmt = getStmt(sql);
            stmt.setInt(1, tagId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ThreadInfo thread = new ThreadInfo();
                thread.setThreadId(rs.getInt("thread_id"));
                thread.setTitle(rs.getString("thread_title"));
                thread.setResponseCount(rs.getInt("response_count"));
                thread.setTags(new ArrayList<>());  // タグ取得処理は別途でも可

                list.add(thread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}