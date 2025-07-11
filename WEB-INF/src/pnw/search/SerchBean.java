package pnw.search;

public class SerchBean {
    
    private String tl;
    private int id;

    public SerchBean(int id, String tl) {
        this.id = id;
        this.tl = tl;
    }

    public String getTitle() {
        return tl;
    }

    public int getID() {
        return id;
    }

}