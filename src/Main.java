package src;

import java.sql.*;

public class Main {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/demo";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "root";

    public static void main(String[] args) {
        // Try-with-resources ensures proper resource management
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            System.out.println("Connection established");

            // Printing single record
            printSingleRecord(con, 1);

            // Printing all records
            printData(con);

            // Inserting record
            insertData(con, "vishal", 50);
            printData(con);

            // Updating record
            updateData(con, "vishal", 40);
            printData(con);

            // Deleting record
            deleteData(con, "vishal");
            printData(con);

        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
        }
    }

    public static void printSingleRecord(Connection con, int studentId) throws SQLException {
        String sql = "SELECT sname FROM student WHERE sid = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Name of the student is " + rs.getString("sname"));
                } else {
                    System.out.println("No student found with ID: " + studentId);
                }
            }
        }
    }

    public static void printData(Connection con) throws SQLException {
        String sql = "SELECT * FROM student";
        try (Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("%d | %s | %d%n", rs.getInt(1), rs.getString(2), rs.getInt(3));
            }
            System.out.println();
        }
    }

    public static void insertData(Connection con, String name, int marks) throws SQLException {
        String sql = "INSERT INTO student (sname, smarks) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, marks);
            ps.executeUpdate();
            System.out.println("Record inserted: " + name);
        }
    }

    public static void updateData(Connection con, String name, int marks) throws SQLException {
        String sql = "UPDATE student SET smarks = ? WHERE sname = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, marks);
            ps.setString(2, name);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Record updated: " + name);
            } else {
                System.out.println("No record found to update for: " + name);
            }
        }
    }

    public static void deleteData(Connection con, String name) throws SQLException {
        String sql = "DELETE FROM student WHERE sname = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Record deleted: " + name);
            } else {
                System.out.println("No record found to delete for: " + name);
            }
        }
    }
}
