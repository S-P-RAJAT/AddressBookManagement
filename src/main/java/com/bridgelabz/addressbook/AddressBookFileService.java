package com.bridgelabz.addressbook;

import com.google.gson.Gson;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddressBookFileService {

    public static final AddressBookIF addressBookService = AddressBookManager.addressBookService;

    public static List<AddressBook> readContactsFromCsv(String filePath) throws IOException {
        List<AddressBook> addressBookList = new ArrayList<>();
        Path addressBookPath = Paths.get(filePath);
        if (Files.exists(addressBookPath)) {
            File[] listOfFiles = addressBookPath.toFile().listFiles();
            assert listOfFiles != null;
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
        return addressBookList;
    }

    public static List<AddressBook> readContactsFromJson(String fileName)
            throws FileNotFoundException {
        List<AddressBook> addressBookList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        Gson gson = new Gson();
        AddressBook[] jsonAddressBookList = gson.fromJson(br, AddressBook[].class);
        for (AddressBook addressBook : jsonAddressBookList) {
            addressBookService.setAddressBook(addressBook);
            addressBookList.add(addressBook);
            for (Contact contact : addressBookService.getContactList().values()) {
                addressBookService.addContact(contact);
            }
        }
        return addressBookList;
    }

    public static void WriteContactsToCsv(List<AddressBook> addressBookList, String filePath)
            throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        for (AddressBook addressBook : addressBookList) {
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
                beanToCsv.write(new ArrayList<>(addressBookService.getContactList().values()));
            }
            System.out.println("Contacts are successfully saved");
        }
    }

    public static void WriteContactsToJson(List<AddressBook> addressBookList, String fileName)
            throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(addressBookList);
        FileWriter writer = new FileWriter(fileName);
        writer.write(json);
        writer.close();
    }
}
