import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class Demo {

    public static void main(String[] args) {

    }

    // Write Patient object(s) to the database
    public static void SavePatient() {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Patient> patientList = new ArrayList<>();
        int option = 0;

        while (option != -1) {
            System.out.println("Enter patient ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Enter patient first name: ");
            String firstName = scanner.nextLine();

            System.out.println("Enter patient last name: ");
            String lastName = scanner.nextLine();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate patientDOB = null;

            // While patientDOB is null, keep prompting the user for input until we get a
            // properly formatted date
            while (patientDOB == null) {
                System.out.println("Enter patient date of birth (YYYY-MM-DD): ");
                String dob = scanner.nextLine();

                // THINK ABOUT ADDING CUSTOM EXCEPTION
                try {
                    patientDOB = LocalDate.parse(dob, formatter);
                } catch (DateTimeParseException error) {
                    System.out.println("Invalid date. Please use YYYY-MM-DD format.");
                }
            }

            // Add new patient to the patient array
            patientList.add(new Patient(id, firstName, lastName, patientDOB));

            // Prompt user with option to exit or continue
            System.out.println("Enter -1 to exit or any other number key to continue: ");
            option = scanner.nextInt();
            scanner.nextLine();
        }

        // Create query to insert the patient data provided
        String query = "INSERT INTO patients (id, first_name, last_name, dob)" + " VALUES (?, ?, ?, ?)";

        for (int i = 0; i < patientList.size(); i++) {
            try {
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);

                statement.setInt(1, patientList.get(i).getPatientID());
                statement.setString(2, patientList.get(i).getPatientFirstName());
                statement.setString(3, patientList.get(i).getPatientLastName());
                statement.setDate(4, java.sql.Date.valueOf(patientList.get(i).getPatientDOB()));

                int updateRow = statement.executeUpdate();
                System.out.println(updateRow + " rows updated.");

                connection.close();

            } catch (SQLException error) {
                error.printStackTrace();
            }
        }

        scanner.close();
    }

    public static void DisplayPatients() {
        ArrayList<Patient> patientList = new ArrayList<>();
        String query = "SELECT * FROM patients";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Patient temp = new Patient();

                temp.setPatientID(resultSet.getInt("id"));
                temp.setPatientFirstName(resultSet.getString("first_name"));
                temp.setPatientLastName(resultSet.getString("last_name"));
                temp.setPatientDOB(resultSet.getDate("dob").toLocalDate());

                patientList.add(temp);
            }

            connection.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }

        for (Patient patient : patientList) {
            System.out.println(patient);
        }
    }

    public static void SaveDrug() {
        Scanner scanner = new Scanner(System.in);
        int option = 0;

        while (option != -1) {
            System.out.println("Enter drug ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Enter drug name: ");
            String drugName = scanner.nextLine();

            System.out.println("Enter drug cost: ");
            double drugCost = scanner.nextDouble();
            scanner.nextLine();

            System.out.println("Enter drug dosage: ");
            String dosage = scanner.nextLine();

            try {
                File file = new File("Drugs.txt");
                FileWriter fileWriter = new FileWriter(file, true);
                Drug drug = new Drug(id, drugName, drugCost, dosage);

                fileWriter.write(drug.toString());
                fileWriter.write("\n");

                fileWriter.close();
            } catch (IOException error) {
                error.printStackTrace();
            }

            // Prompt user with option to exit or continue
            System.out.println("Enter -1 to exit or any other number key to continue: ");
            option = scanner.nextInt();
            scanner.nextLine();
        }

        scanner.close();
    }

    public static void DisplayDrugs() {
        try {
            FileInputStream fileInputStream = new FileInputStream("Drugs.txt");
            int i;

            while ((i = fileInputStream.read()) != -1) {
                System.out.print((char) i);
            }

            fileInputStream.close();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
