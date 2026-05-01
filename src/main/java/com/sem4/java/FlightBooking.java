package com.sem4.java;
import java.sql.*;
import java.util.Scanner;

public class FlightBooking {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String url = "jdbc:mysql://localhost:3306/airlinedb";
        String user = "root";
        String password = "2305";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);

            conn.setAutoCommit(false); // Start transaction

            System.out.print("Enter Flight ID: ");
            int flightId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Passenger Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Seats Required: ");
            int seats = sc.nextInt();

            // Step 1: Check availability
            PreparedStatement ps1 = conn.prepareStatement(
                "SELECT available_seats, price_per_seat FROM flights WHERE flight_id = ?"
            );
            ps1.setInt(1, flightId);

            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                int available = rs.getInt("available_seats");
                double price = rs.getDouble("price_per_seat");

                if (available >= seats) {

                    // Step 2: Update seats
                    PreparedStatement ps2 = conn.prepareStatement(
                        "UPDATE flights SET available_seats = available_seats - ? WHERE flight_id = ?"
                    );
                    ps2.setInt(1, seats);
                    ps2.setInt(2, flightId);
                    ps2.executeUpdate();

                    // Step 3: Insert booking
                    double total = seats * price;

                    PreparedStatement ps3 = conn.prepareStatement(
                        "INSERT INTO bookings (passenger_name, flight_id, seats_booked, total_amount) VALUES (?, ?, ?, ?)"
                    );
                    ps3.setString(1, name);
                    ps3.setInt(2, flightId);
                    ps3.setInt(3, seats);
                    ps3.setDouble(4, total);
                    ps3.executeUpdate();

                    conn.commit();
                    System.out.println("Booking Successful!");

                } else {
                    conn.rollback();
                    System.out.println("Booking Failed: Not enough seats available");
                }

            } else {
                System.out.println("Flight not found");
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}