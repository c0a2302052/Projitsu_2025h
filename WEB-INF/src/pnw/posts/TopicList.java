package pnw.posts;

public class TopicList {
    // 投稿内容を管理するリスト
    private int post_id;
    private int response_id;
    private String post_time;
    private String name;
    private String main_text;
    private int likes;
    private int thread_id;

    public TopicList(int post_id, int response_id, String post_time,
            String name, String main_text, int likes, int thread_id) {
        this.post_id = post_id;
        this.response_id = response_id;
        this.post_time = post_time;
        this.name = name;
        this.main_text = main_text;
        this.likes = likes;
        this.thread_id = thread_id;
        }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getResponse_id() {
        return response_id;
    }

    public void setResponse_id(int response_id) {
        this.response_id = response_id;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMain_text() {
        return main_text;
    }

    public void setMain_text(String main_text) {
        this.main_text = main_text;
    }

    public int getLikes() {
        return likes;
    }

    public void setLike_num(int likes) {
        this.likes = likes;
    }

    public int getThread_id() {
        return thread_id;
    }

    public void setThread_id(int thread_id) {
        this.thread_id = thread_id;
    }

}
