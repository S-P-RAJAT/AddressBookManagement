package com.bridgelabz.addressbook;


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
}
