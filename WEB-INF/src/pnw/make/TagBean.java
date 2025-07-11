package pnw.make;

public class TagBean {
    private int id;
    private String name;

    public TagBean(int id, String name) {
        this.id = id;
        this.name  = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
