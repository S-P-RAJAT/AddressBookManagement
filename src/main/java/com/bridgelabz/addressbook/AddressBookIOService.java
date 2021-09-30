package com.bridgelabz.addressbook;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static com.bridgelabz.addressbook.AddressBookFileService.*;


public class AddressBookIOService {
    private static final AddressBookDBService addressBookDBService = AddressBookDBService.getInstance();
    private static final String csvFilePath = "Address_Books/";
    private static final String jsonFileName = "AddressBooks.json";

    public static List<AddressBook> readData(IOService ioservice) {
        List<AddressBook> addressBookList = null;
        try {
            if (ioservice == IOService.CSV_IO) {
                addressBookList = readContactsFromCsv(csvFilePath);
            } else if (ioservice == IOService.JSON_IO) {
                addressBookList = readContactsFromJson(jsonFileName);
            } else if (ioservice == IOService.DB_IO) {
                addressBookList = addressBookDBService.readContactsFromDatabase();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return addressBookList;
    }

    public static void WriteContacts(List<AddressBook> addressBookList, IOService ioservice) throws CsvDataTypeMismatchException,
            CsvRequiredFieldEmptyException {
        try {
            if (ioservice == IOService.CSV_IO) {
                WriteContactsToCsv(addressBookList, csvFilePath);
            } else if (ioservice == IOService.JSON_IO) {
                WriteContactsToJson(addressBookList, jsonFileName);
            } else if (ioservice == IOService.DB_IO) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


