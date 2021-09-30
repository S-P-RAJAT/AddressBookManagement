package com.bridgelabz.addressbook;


import java.util.List;
import java.sql.SQLException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import static com.bridgelabz.addressbook.AddressBookIOService.readData;


public class AddressBookDBTest {

    @Test
    public void givenAddressBookDB_WhenRetrieved_ShouldMatchContactCount() {
        List<AddressBook> addressBookList = readData(IOService.DB_IO);
        assertEquals(2, addressBookList.size());
    }
}
