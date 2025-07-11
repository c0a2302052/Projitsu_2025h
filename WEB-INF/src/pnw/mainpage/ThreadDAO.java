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

    public List<ThreadInfo> getAllThreads() {
        List<ThreadInfo> list = new ArrayList<>();

        try {
            String sql = "SELECT thread_id, thread_title FROM threads_info";
            PreparedStatement stmt = getStmt(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ThreadInfo t = new ThreadInfo();
                t.setThreadId(rs.getInt("thread_id"));
                t.setTitle(rs.getString("thread_title"));

                // タグやレス数は仮データ（あとで追加可能）
                t.setTags(new ArrayList<>());
                t.setResponseCount(0);

                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
public List<ThreadInfo> getThreadsByTagId(int tagId) {
    List<ThreadInfo> list = new ArrayList<>();
    try {
        String sql = "SELECT I.thread_id, I.thread_title " +
                     "FROM threads_info I " +
                     "JOIN mapping_thread_tag M ON I.thread_id = M.thread_id " +
                     "WHERE M.tag_id = ?";
        PreparedStatement stmt = getStmt(sql);
        stmt.setInt(1, tagId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            ThreadInfo thread = new ThreadInfo();
            thread.setThreadId(rs.getInt("thread_id"));
            thread.setTitle(rs.getString("thread_title"));
            thread.setTags(new ArrayList<>());  // 必要ならあとで追加
            thread.setResponseCount(0);         // 仮値
            list.add(thread);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}


}
