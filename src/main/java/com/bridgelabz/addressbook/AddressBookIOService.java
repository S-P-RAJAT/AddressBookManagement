package com.bridgelabz.addressbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;


public class AddressBookIOService {
    private static final String filePath = "Address_Books/";
    public static final AddressBookIF addressBookService = AddressBookManager.addressBookService;

    public static void WriteContactsToFile(List<AddressBook> addressBookList, IOService ioservice) throws CsvDataTypeMismatchException,
            CsvRequiredFieldEmptyException {
        try {
            if (ioservice == IOService.FILE_IO) {
                for (AddressBook addressBook : addressBookList) {

                    String s = "firstName, lastName, address, city, state, zip, phoneNumber, email";
                    Path playPath = Paths.get(filePath);
                    if (Files.notExists(playPath)) {
                        Files.createDirectory(playPath);
                    }
                    Path tempFile = Paths.get(playPath + "/" + addressBook.getAddressBookName());

                    try (
                            Writer writer = Files.newBufferedWriter(tempFile)
                    ) {
                        StatefulBeanToCsv<Contact> beanToCsv = new StatefulBeanToCsvBuilder<Contact>(writer)
                                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                                .withSeparator('|')
                                .build();

                        addressBookService.setAddressBook(addressBook);
                        beanToCsv.write(new ArrayList<Contact>(addressBookService.getContactList().values()));
                    }
                    System.out.println("Contacts are sucessfully saved");
                }
            } else if (ioservice == IOService.JSON_IO) {
                Gson gson = new Gson();
                String json = gson.toJson(addressBookList);
                FileWriter writer = new FileWriter("Address_Books_Json/" + "AddressBooks.json");
                writer.write(json);
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void readData(List<AddressBook> addressBookList, IOService ioservice) {
        try {
            if (ioservice == IOService.FILE_IO) {
                Path addressBookPath = Paths.get(filePath);
                if (Files.exists(addressBookPath)) {
                    File[] listOfFiles = addressBookPath.toFile().listFiles();
                    for (File file : listOfFiles) {

                        String addressBookName = file.getName().split("[.]")[0];
                        AddressBook newAddressBook = new AddressBook(addressBookName, new HashMap<>());
                        addressBookList.add(newAddressBook);
                        Path newPath = Paths.get(filePath + "/" + addressBookName + ".csv");
                        Reader reader = Files.newBufferedReader(newPath);
                        CsvToBean<Contact> csvToBean = new CsvToBeanBuilder<Contact>(reader)
                                .withType(Contact.class)
                                .withIgnoreLeadingWhiteSpace(true)
                                .withSeparator('|')
                                .build();
                        List<Contact> csvUsers = csvToBean.parse();
                        addressBookService.setAddressBook(newAddressBook);
                        for (Contact contact : csvUsers) {
                            addressBookService.addContact(contact);
                        }
                    }
                }
            } else if (ioservice == IOService.JSON_IO) {
                BufferedReader br = new BufferedReader(new FileReader("Address_Books_Json/" + "AddressBooks.json"));
                Gson gson = new Gson();
                AddressBook[] jsonAddressBookList = gson.fromJson(br, AddressBook[].class);
                for (AddressBook addressBook : jsonAddressBookList) {
                    addressBookService.setAddressBook(addressBook);
                    addressBookList.add(addressBook);
                    for (Contact contact: addressBookService.getContactList().values()) {
                       addressBookService.addContact(contact);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


