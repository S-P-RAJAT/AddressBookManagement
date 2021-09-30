package com.bridgelabz.addressbook;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddressBookDBService {
    public static AddressBookIF addressBookService;
    private static AddressBookDBService addressBookDBService;
    private PreparedStatement contactAddedGivenRangeStatement;
    private PreparedStatement contactsInGivenCityOrStateStatement;

    public static AddressBookDBService getInstance() {
        if (addressBookDBService == null)
            addressBookDBService = new AddressBookDBService();
        return addressBookDBService;
    }

    private AddressBookDBService() {
        addressBookService = new AddressBookImpl();
    }

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

    public List<AddressBook> readContactsFromDatabase() throws SQLException {
        String sql = "SELECT book_name from address_book";
        List<AddressBook> addressBookList = new ArrayList<>();
        try {
            Connection connection = this.getConnection();
            Statement statementForBookName = connection.createStatement();
            ResultSet resultForBookName = statementForBookName.executeQuery(sql);
            while (resultForBookName.next()) {
                String bookName = resultForBookName.getString("book_name");
                String sql2 = "SELECT DISTINCT first_name,last_name, address, city, state, zip,"
                        + " phone_number, email FROM contact NATURAL JOIN address NATURAL JOIN"
                        + " address_book NATURAL JOIN contact_type NATURAL JOIN type where book_name = \""
                        + bookName + "\"";
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql2);
                AddressBook newAddressBook = new AddressBook(bookName, new HashMap<>());
                addressBookList.add(newAddressBook);
                addressBookService.setAddressBook(newAddressBook);

                while (result.next()) {
                    Contact contact = this.getContactData(result);
                    addressBookService.addContact(contact);
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookList;
    }

    private Contact getContactData(ResultSet result) throws SQLException {
            String firstName = result.getString("first_name");
            String lastName = result.getString("last_name");
            String address = result.getString("address");
            String city = result.getString("city");
            String state = result.getString("state");
            int zip = result.getInt("zip");
            long phoneNumber = result.getLong("phone_number");
            String email = result.getString("email");

        return new Contact(firstName, lastName, address, city, state, zip, phoneNumber, email);
    }


    public int updateContactInDatabase(String firstName, String lastName, String updateSting) {
        return this.updateContactUsingStatement(firstName, lastName, updateSting);
    }

    private int updateContactUsingStatement(String firstName, String lastName, String updateSting) {
        String sql = String.format(
                "Update contact NATURAL JOIN address set %s where first_name = \"%s\" and last_name  =  \"%s\";",
                updateSting, firstName, lastName);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void preparedStatementToretriveContactsInRange() {
        try {
            Connection connection = this.getConnection();
            String query = "select * from contact NATURAL JOIN address where date_added between ? and ?";
            contactAddedGivenRangeStatement = connection.prepareStatement(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Contact> readContactsAddedInRange(Date startDate, Date endDate) {
        if (contactAddedGivenRangeStatement == null) {
            this.preparedStatementToretriveContactsInRange();
        }
        try {
            contactAddedGivenRangeStatement.setDate(1, startDate);
            contactAddedGivenRangeStatement.setDate(2, endDate);
            ResultSet resultSet = contactAddedGivenRangeStatement.executeQuery();
            List<Contact> contactList= new ArrayList<>();
            while (resultSet.next()) {
                Contact contact = getContactData(resultSet);
                contactList.add(contact);
            }
            return contactList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Contact> readContactsFromGivenCity(String city) {
        return null;
    }

    public List<Contact> readContactsFromGivenState(String state) {
        return null;
    }

    public List<Contact> readContactsFromGivenCityAndState(String city, String state) {
        return null;
    }
}
