package pnw.make;
import java.sql.Timestamp;

public class InfoBean {
    
    private int thid;

    public InfoBean(int thid) {
        this.thid = thid;
    }


    public int getThreadId() {
        return thid;
    }

}
