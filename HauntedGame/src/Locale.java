public class Locale {

    //
    // Public
    //

    // Constructor
    public Locale(int id) {
        this.id = id;
    }

    // Getters and Setters
    public int getId() {
        return this.id;
    }

    public String getText() {
        return this.name + "\n" + this.desc;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String value) {
        this.name = value;
    }

    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String value) {
        this.desc = value;
    }

    public boolean getHasVisited() {
        return hasVisited;
    }
    public void setHasVisited(boolean hasVisited) {
        this.hasVisited = hasVisited;
    }


    // Other methods
    @Override
    public String toString(){
        return "[Locale id="
                + this.id
                + " name="
                + this.name
                + " desc=" + this.desc
                + " hasVisited=" + this.hasVisited + "]";
    }

    //
    //  Private
    //
    private int     id;
    private String  name;
    private String  desc;
    private boolean hasVisited = false;
}