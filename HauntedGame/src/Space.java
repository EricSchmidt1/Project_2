public class Space extends Locale {       // Space IS-A Locale.

    //
    // Public
    //

    // Constructor
    public Space(int id){
        super(id);
    }

    // Getters and Setters
    public String getNearestPlanet() {
        return nearestPlanet;
    }
    public void setNearestPlanet(String nearestPlanet) {
        this.nearestPlanet = nearestPlanet;
    }


    @Override
    public String toString() {
        return "Space..." + super.toString() + " nearestPlanet=" + this.nearestPlanet;
    }

    //
    // Private
    //
    private String nearestPlanet;
}