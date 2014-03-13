
public class OuterDimension extends Locale {       // OuterDimension IS-A Locale.

    //
    // Public
    //

    // Constructor
    public OuterDimension(int id){
        super(id);
    }

    // Getters and Setters
    public String getDimensionLocation() {
        return DimensionLocation;
    }
    public void setDimensionLocation(String DimensionLocation) {
        this.DimensionLocation = DimensionLocation;
    }


    @Override
    public String toString() {
        return "Outer Dimension..." + super.toString() + "Dimension Location=" + this.DimensionLocation;
    }

    //
    // Private
    //
    private String DimensionLocation;
}
