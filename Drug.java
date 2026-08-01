public class Drug {

    // Declare variables
    private int drugID;
    private String drugName;
    private double drugCost;
    private String dosage;

    // Constructors
    public Drug() {

    }

    public Drug(int drugID, String drugName, double drugCost, String dosage) {
        this.drugID = drugID;
        this.drugName = drugName;
        this.drugCost = drugCost;
        this.dosage = dosage;
    }

    // Getter and setter methods
    public int getDrugID() {
        return this.drugID;
    }

    public String getDrugName() {
        return this.drugName;
    }

    public double getDrugCost() {
        return this.drugCost;
    }

    public String getDosage() {
        return this.dosage;
    }

    public void setDrugID(int drugID) {
        this.drugID = drugID;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public void setDrugCost(double drugCost) {
        this.drugCost = drugCost;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    // Override toString method
    @Override
    public String toString() {
        return String.format("Drug ID: %d, Drug Name: %s, Drug Cost: $%.2f, Dosage: %s", this.drugID, this.drugName,
                this.drugCost, this.dosage);
    }
}
