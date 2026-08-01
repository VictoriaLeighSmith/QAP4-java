import java.time.LocalDate;

public class Patient {

    // Declare variables
    private int patientID;
    private String patientFirstName;
    private String patientLastName;
    private LocalDate patientDOB;

    // Constructors
    public Patient() {

    }

    public Patient(int patientID, String patientFirstName, String patientLastName, LocalDate patientDOB) {
        this.patientID = patientID;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
        this.patientDOB = patientDOB;
    }

    // Getter and setter methods
    public int getPatientID() {
        return this.patientID;
    }

    public String getPatientFirstName() {
        return this.patientFirstName;
    }

    public String getPatientLastName() {
        return this.patientLastName;
    }

    public LocalDate getPatientDOB() {
        return this.patientDOB;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public void setPatientDOB(LocalDate patientDOB) {
        this.patientDOB = patientDOB;
    }

    // Override toString method
    @Override
    public String toString() {
        return String.format("Patient ID: %d, Patient First Name: %s, Patient Last Name: %s, Patient DOB: %s",
                this.patientID,
                this.patientFirstName, this.patientLastName, this.patientDOB);
    }
}
