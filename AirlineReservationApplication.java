import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AirlineReservationApplication {

    // ── CONNECTION ────────────────────────────────────────────────────────────

    private static Connection connect() {
        // Update this path to match your local setup
        String url = "jdbc:sqlite:C:/Users/YourName/Downloads/AirlineProject/AirlineReservation.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // ── INSERT METHODS ────────────────────────────────────────────────────────

    private static void insertPassenger(int passengerID, String name, String passportNum, String email) {
        String sql = "INSERT INTO Passenger (Passenger_ID, Name, Passport_num, Email) VALUES (?, ?, ?, ?);";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, passengerID);
            pstmt.setString(2, name);
            pstmt.setString(3, passportNum);
            pstmt.setString(4, email);
            pstmt.executeUpdate();
            System.out.println("A new passenger has been inserted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void insertFlight(int flightID, int airplaneID, String desAirport, String orAirport,
                                     String departureDate, String arrivalDate) {
        String sql = "INSERT INTO Flight (Flight_ID, Airplane_ID, Des_Airport, Or_Airport, Departure_date, Arrival_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?);";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, flightID);
            pstmt.setInt(2, airplaneID);
            pstmt.setString(3, desAirport);
            pstmt.setString(4, orAirport);
            pstmt.setString(5, departureDate);
            pstmt.setString(6, arrivalDate);
            pstmt.executeUpdate();
            System.out.println("A new flight has been inserted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // ── DELETE METHODS ────────────────────────────────────────────────────────

    private static void deletePassenger(int passengerID) {
        String sql = "DELETE FROM Passenger WHERE Passenger_ID = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, passengerID);
            pstmt.executeUpdate();
            System.out.println("Passenger has been deleted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteFlight(int flightID) {
        String sql = "DELETE FROM Flight WHERE Flight_ID = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, flightID);
            pstmt.executeUpdate();
            System.out.println("Flight has been deleted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // ── UPDATE METHODS ────────────────────────────────────────────────────────

    private static void changeFlightDestination(int flightID, String newDestination) {
        String sql = "UPDATE Flight SET Des_Airport = ? WHERE Flight_ID = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newDestination);
            pstmt.setInt(2, flightID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("The destination of the flight has been updated.");
            } else {
                System.out.println("No flight found with the specified ID.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void changeTicketSeat(int ticketID, String newSeat) {
        String sql = "UPDATE Ticket SET Seat_num = ? WHERE Ticket_ID = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newSeat);
            pstmt.setInt(2, ticketID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("The seat number for the ticket has been updated.");
            } else {
                System.out.println("No ticket found with the specified ID.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // ── MENU ──────────────────────────────────────────────────────────────────

    private static void showMenu() {
        System.out.println("\nHi! What would you like to do with the Airline Reservation database?");
        System.out.println("1. Insert");
        System.out.println("2. Delete");
        System.out.println("3. Change");
        System.out.println("4. Exit");
        System.out.print("Enter your choice (1-4): ");
    }

    // ── MAIN ──────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            showMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleInsert(scanner);
                    break;
                case "2":
                    handleDelete(scanner);
                    break;
                case "3":
                    handleChange(scanner);
                    break;
                case "4":
                    exit = true;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2, 3, or 4.");
            }
        }
        scanner.close();
    }

    // ── HANDLERS ─────────────────────────────────────────────────────────────

    private static void handleInsert(Scanner scanner) {
        System.out.println("Which table would you like to insert into? (Passenger/Flight)");
        String table = scanner.nextLine();

        if ("Passenger".equalsIgnoreCase(table)) {
            System.out.println("Enter Passenger ID, Name, Passport Number, and Email separated by commas:");
            System.out.println("Example: 1, John Smith, P1234567, john@email.com");
            String[] inputs = scanner.nextLine().split(",");
            if (inputs.length == 4) {
                insertPassenger(Integer.parseInt(inputs[0].trim()), inputs[1].trim(),
                        inputs[2].trim(), inputs[3].trim());
            } else {
                System.out.println("Invalid input format. Please provide exactly 4 values.");
            }

        } else if ("Flight".equalsIgnoreCase(table)) {
            System.out.println("Enter Flight ID, Airplane ID, Destination Airport, Origin Airport, Departure Date (YYYY-MM-DD), Arrival Date (YYYY-MM-DD) separated by commas:");
            System.out.println("Example: 101, 5, JFK, YUL, 2026-04-01, 2026-04-01");
            String[] inputs = scanner.nextLine().split(",");
            if (inputs.length == 6) {
                insertFlight(Integer.parseInt(inputs[0].trim()), Integer.parseInt(inputs[1].trim()),
                        inputs[2].trim(), inputs[3].trim(), inputs[4].trim(), inputs[5].trim());
            } else {
                System.out.println("Invalid input format. Please provide exactly 6 values.");
            }

        } else {
            System.out.println("Invalid table name. Please enter 'Passenger' or 'Flight'.");
        }
    }

    private static void handleDelete(Scanner scanner) {
        System.out.println("Which table would you like to delete from? (Passenger/Flight)");
        String table = scanner.nextLine();

        if ("Passenger".equalsIgnoreCase(table)) {
            System.out.println("Enter the Passenger ID to delete:");
            int id = Integer.parseInt(scanner.nextLine());
            deletePassenger(id);

        } else if ("Flight".equalsIgnoreCase(table)) {
            System.out.println("Enter the Flight ID to delete:");
            int id = Integer.parseInt(scanner.nextLine());
            deleteFlight(id);

        } else {
            System.out.println("Invalid table name. Please enter 'Passenger' or 'Flight'.");
        }
    }

    private static void handleChange(Scanner scanner) {
        System.out.println("Which operation would you like to perform? (ChangeDestination/ChangeSeat)");
        String operation = scanner.nextLine();

        if ("ChangeDestination".equalsIgnoreCase(operation)) {
            System.out.println("Enter the Flight ID and new Destination Airport code separated by a comma:");
            System.out.println("Example: 101, LAX");
            String[] inputs = scanner.nextLine().split(",");
            if (inputs.length == 2) {
                changeFlightDestination(Integer.parseInt(inputs[0].trim()), inputs[1].trim());
            } else {
                System.out.println("Invalid input format.");
            }

        } else if ("ChangeSeat".equalsIgnoreCase(operation)) {
            System.out.println("Enter the Ticket ID and new Seat Number separated by a comma:");
            System.out.println("Example: 201, 14A");
            String[] inputs = scanner.nextLine().split(",");
            if (inputs.length == 2) {
                changeTicketSeat(Integer.parseInt(inputs[0].trim()), inputs[1].trim());
            } else {
                System.out.println("Invalid input format.");
            }

        } else {
            System.out.println("Invalid operation. Please enter 'ChangeDestination' or 'ChangeSeat'.");
        }
    }
}
