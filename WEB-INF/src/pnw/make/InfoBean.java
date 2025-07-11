package pnw.make;
import java.sql.Timestamp;

public class InfoBean {
    
    private String tl;
    private int pid;
    private int rnum;
    private Timestamp tm;
    private String nm;
    private String tx;
    private int lnum;
    private int thid;

    public InfoBean(String tl, int rnum, Timestamp tm, String nm, String tx, int lnum, int thid) {
        this.tl = tl;
        this.rnum = rnum;
        this.tm = tm;
        this.nm = nm;
        this.tx = tx;
        this.lnum = lnum;
        this.thid = thid;
    }

    public String getTitle() {
        return tl;
    }

    public int getResponseNum() {
        return rnum;
    }

    public Timestamp getPostTime() {
        return tm;
    }

    public String getName() {
        return nm;
    }

    public String getMainText() {
        return tx;
    }

    public int getLikeNum() {
        return lnum;
    }

    public int getThreadId() {
        return thid;
    }

    public void setID(int pid) {
        this.pid = pid;
    }

    public int getID() {
        return pid;
    } 
}
