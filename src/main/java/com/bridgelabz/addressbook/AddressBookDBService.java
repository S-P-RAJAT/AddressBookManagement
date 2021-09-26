package com.bridgelabz.addressbook;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddressBookDBService {
    public static final AddressBookIF addressBookService = AddressBookManager.addressBookService;

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/address_book_service?useSSL=false";
        String userName = "rajat";
        String password = "Mysql@3q#";
        Connection connection;

        System.out.println("Connecting to database" + jdbcURL);
        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Connection is successful" + connection);

        return connection;
    }
    public List<AddressBook> readData() throws SQLException {
        String sql = "SELECT book_name from address_book";
        List<AddressBook> addressBookList = new ArrayList<>();
        try {
            Connection connection = this.getConnection();
            Statement statementForBookName = connection.createStatement();
            ResultSet resultForBookName = statementForBookName.executeQuery(sql);
            while (resultForBookName.next()) {
                String bookName = resultForBookName.getString("book_name");
                String sql2 = "SELECT DISTINCT first_name,last_name, address, city, state, zip, phone_number, email FROM contact NATURAL JOIN address NATURAL JOIN address_book NATURAL JOIN contact_type NATURAL JOIN type where book_name = \"" + bookName + "\"";
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql2);
                AddressBook newAddressBook = new AddressBook(bookName, new HashMap<>());
                addressBookList.add(newAddressBook);
                addressBookService.setAddressBook(newAddressBook);

                while (result.next()) {
                    String firstName = result.getString("first_name");
                    String lastName = result.getString("last_name");
                    String address = result.getString("address");
                    String city = result.getString("city");
                    String state = result.getString("state");
                    int zip = result.getInt("zip");
                    long phoneNumber = result.getLong("phone_number");
                    String email = result.getString("email");
                    Contact contact = new Contact(firstName, lastName, address, city, state, zip, phoneNumber, email);
                    addressBookService.addContact(contact);
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookList;
    }
}
