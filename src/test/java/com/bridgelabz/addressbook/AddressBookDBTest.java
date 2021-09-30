package com.bridgelabz.addressbook;


import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import static com.bridgelabz.addressbook.AddressBookIOService.readData;


public class AddressBookDBTest {

    @Test
    public void givenAddressBookDB_WhenRetrieved_ShouldMatchContactCount() {
        List<AddressBook> addressBookList = readData(IOService.DB_IO);
        assertEquals(2, addressBookList.size());
    }

    @Test
    public void givenUpdateQuery_WhenSuccessful_ShouldSyncWithDB() throws SQLException {
        AddressBookImpl addressBookService = new AddressBookImpl();
        AddressBookDBService addressBookDBService = AddressBookDBService.getInstance();
        List<AddressBook> addressBookList = addressBookDBService.readContactsFromDatabase();
        addressBookService.setAddressBook(addressBookList.get(0));
        String firstName = "Rajat";
        String lastName = "S P";
        Contact contact = addressBookService.getContact(firstName, lastName);
        addressBookService.updateCity(firstName, lastName, contact, "Bangalore");
        addressBookList = addressBookDBService.readContactsFromDatabase();
        Assert.assertEquals(contact, addressBookList.get(0).contactList.get("Rajat" + " " + "S P"));


    }
    @Test
    public void givenDateRange_WhenCorrect_RetrieveAllContactsAdded() {
        AddressBookDBService addressBookDBService =AddressBookDBService.getInstance();
        LocalDate startDate = LocalDate.of(2021, 9, 19);
        LocalDate endDate = LocalDate.of(2021, 9, 26);
        List<Contact> contacts = addressBookDBService.readContactsAddedInRange(Date.valueOf(startDate),
                Date.valueOf(endDate));
        Assert.assertEquals(2, contacts.size());
    }
    @Test
    public void givenCity_WhenCorrect_RetrieveAllContactsInCity() {
        AddressBookDBService addressBookDBService =AddressBookDBService.getInstance();
        String city = "Mumbai";
        List<Contact> contacts = addressBookDBService.readContactsFromGivenCity(city);
        Assert.assertEquals(1, contacts.size());
    }
    @Test
    public void givenState_WhenCorrect_RetrieveAllContactsInState() {
        AddressBookDBService addressBookDBService =AddressBookDBService.getInstance();
        String state = "Karnataka";
        List<Contact> contacts = addressBookDBService.readContactsFromGivenState(state);
        Assert.assertEquals(2, contacts.size());
    }
    @Test
    public void givenCityAndState_WhenCorrect_RetrieveAllContactsInCityAndState() {
        AddressBookDBService addressBookDBService =AddressBookDBService.getInstance();
        String city = "Bangalore";
        String state = "Karnataka";
        List<Contact> contacts = addressBookDBService.readContactsFromGivenCityAndState(city, state);
        Assert.assertEquals(2, contacts.size());
    }
}
