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
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        String line = "-".repeat(45);

        // Menu
        while (option != -1) {
            System.out.println();
            System.out.println(line);
            System.out.println("            PATIENT AND DRUG MENU");
            System.out.println(line);
            System.out.println("1. Save new drug information to text file.");
            System.out.println("2. Display drug information from text file.");
            System.out.println("3. Save patient information to database.");
            System.out.println("4. Display patient information from database.");
            System.out.println("-1 to exit.");
            System.out.println();

            Integer menuSelection = null;

            while (menuSelection == null) {
                System.out.print("Enter your selection: ");
                String input = scanner.nextLine();

                try {
                    menuSelection = Integer.parseInt(input);
                } catch (NumberFormatException error) {
                    System.out.println("Invalid selection. Please select an option 1-4.");
                }
            }

            option = menuSelection;

            switch (option) {
                case 1:
                    saveDrug(scanner);
                    break;
                case 2:
                    System.out.println();
                    displayDrugs();
                    break;
                case 3:
                    savePatient(scanner);
                    break;
                case 4:
                    System.out.println();
                    displayPatients();
                    break;
                case -1:
                    System.out.println("Exiting program ...");
                    break;
                default:
                    System.out.println("Invalid selection. Please choose an option 1-4.");
                    break;
            }
        }

        scanner.close();
    }

    // Write Patient object(s) to the database
    public static void savePatient(Scanner scanner) {
        int option = 0;

        while (option != -1) {
            Integer id = null;

            while (id == null) {
                System.out.print("Enter patient ID: ");
                String input = scanner.nextLine();

                try {
                    int parsedID = Integer.parseInt(input);

                    if (patientIDInUse(parsedID)) {
                        System.out.println("Patient ID already exists. Please enter a different ID.");
                    } else {
                        id = parsedID;
                    }
                } catch (NumberFormatException error) {
                    System.out.println("Invalid ID. Please enter an integer.");
                }
            }

            System.out.print("Enter patient first name: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter patient last name: ");
            String lastName = scanner.nextLine();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate patientDOB = null;

            // While patientDOB is null, keep prompting the user for input until we get a
            // properly formatted date
            while (patientDOB == null) {
                System.out.print("Enter patient date of birth (YYYY-MM-DD): ");
                String dob = scanner.nextLine();

                try {
                    patientDOB = LocalDate.parse(dob, formatter);
                } catch (DateTimeParseException error) {
                    System.out.println("Invalid date. Please use YYYY-MM-DD format.");
                }
            }

            // Create new patient object
            Patient patient = new Patient(id, firstName, lastName, patientDOB);

            // Create query to insert the patient data provided
            String query = "INSERT INTO patients (id, first_name, last_name, dob)" + " VALUES (?, ?, ?, ?)";

            try {
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);

                statement.setInt(1, patient.getPatientID());
                statement.setString(2, patient.getPatientFirstName());
                statement.setString(3, patient.getPatientLastName());
                statement.setDate(4, java.sql.Date.valueOf(patient.getPatientDOB()));

                statement.executeUpdate();

                System.out.println();
                System.out.println("Successfully added patient to the database!");

                connection.close();

            } catch (SQLException error) {
                System.out.println("The patient could not be saved. Please try again.");
            }

            // Prompt user with option to exit or continue
            System.out.println();
            Integer continueOption = null;

            while (continueOption == null) {
                System.out.print("Enter -1 for main menu or any other number to continue: ");
                String input = scanner.nextLine();

                try {
                    continueOption = Integer.parseInt(input);
                } catch (NumberFormatException error) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            option = continueOption;
        }
    }

    public static void displayPatients() {
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

    public static void saveDrug(Scanner scanner) {
        int option = 0;

        while (option != -1) {
            Integer id = null;

            while (id == null) {
                System.out.print("Enter drug ID: ");
                String input = scanner.nextLine();

                try {
                    id = Integer.parseInt(input);
                } catch (NumberFormatException error) {
                    System.out.println("Invalid ID. Please enter an integer.");
                }
            }

            System.out.print("Enter drug name: ");
            String drugName = scanner.nextLine();

            Double drugCost = null;

            while (drugCost == null) {
                System.out.print("Enter drug cost: ");
                String input = scanner.nextLine();

                try {
                    double parsedCost = Double.parseDouble(input);

                    if (parsedCost < 0) {
                        System.out.println("Cost can't be negative. Please try again.");
                    } else {
                        drugCost = parsedCost;
                    }
                } catch (NumberFormatException error) {
                    System.out.println("Invalid cost. Please enter a valid number.");
                }
            }

            System.out.print("Enter drug dosage: ");
            String dosage = scanner.nextLine();

            try {
                File file = new File("Drugs.txt");
                FileWriter fileWriter = new FileWriter(file, true);
                Drug drug = new Drug(id, drugName, drugCost, dosage);

                fileWriter.write(drug.toString());
                fileWriter.write("\n");

                fileWriter.close();
                System.out.println();
                System.out.println("Successfully added new drug to the file!");
            } catch (IOException error) {
                error.printStackTrace();
            }

            // Prompt user with option to exit or continue
            Integer continueOption = null;

            while (continueOption == null) {
                System.out.print("Enter -1 for main menu or any other number to continue: ");
                String input = scanner.nextLine();

                try {
                    continueOption = Integer.parseInt(input);
                } catch (NumberFormatException error) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            option = continueOption;
        }
    }

    public static void displayDrugs() {
        try {
            FileInputStream fileInputStream = new FileInputStream("Drugs.txt");
            int i;

            while ((i = fileInputStream.read()) != -1) {
                System.out.print((char) i);
            }

            System.out.println();
            fileInputStream.close();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    // This is a helper method to check if the ID already exists in the database
    public static boolean patientIDInUse(int id) {
        String query = "SELECT id FROM patients WHERE id = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            boolean inUse = resultSet.next();

            connection.close();
            return inUse;
        } catch (SQLException error) {
            System.out.println("Unable to retrieve the patient ID.");

            return false;
        }
    }
}
