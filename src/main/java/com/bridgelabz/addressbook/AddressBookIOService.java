package com.bridgelabz.addressbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;


public class AddressBookIOService {
    private static final String filePath = "Address_Books/";

    public static void WriteContactsToFile(HashMap<String, Contact> contactList, String addressBookName,IOService ioservice) throws IOException, CsvDataTypeMismatchException,
            CsvRequiredFieldEmptyException {
        try {
            if(ioservice == IOService.FILE_IO) {
                String s = "firstName, lastName, address, city, state, zip, phoneNumber, email";
                Path playPath = Paths.get(filePath);
                if (Files.notExists(playPath)) {
                    Files.createDirectory(playPath);
                }
                Path tempFile = Paths.get(playPath + "/" + addressBookName);

                try (
                        Writer writer = Files.newBufferedWriter(tempFile)
                ) {
                    StatefulBeanToCsv<Contact> beanToCsv = new StatefulBeanToCsvBuilder<Contact>(writer)
                            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                            .withSeparator('|')
                            .build();

                    beanToCsv.write(new ArrayList<Contact>(contactList.values()));
                }
                System.out.println("Contacts are sucessfully saved");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void readData(List<AddressBookIF> addressBook, IOService ioservice) {
        try {
            if(ioservice == IOService.FILE_IO) {
                Path addressBookPath = Paths.get(filePath);
                if (Files.exists(addressBookPath)) {
                    File[] listOfFiles = addressBookPath.toFile().listFiles();
                    for (File file : listOfFiles) {

                        String addressBookName = file.getName();
                        AddressBookIF newAddressBook = new AddressBookImpl(addressBookName);
                        addressBook.add(newAddressBook);
                        Path newPath = Paths.get(filePath+"/"+addressBookName);
                        Reader reader = Files.newBufferedReader(newPath);
                        CsvToBean<Contact> csvToBean =  new CsvToBeanBuilder<Contact>(reader)
                                .withType(Contact.class)
                                .withIgnoreLeadingWhiteSpace(true)
                                .withSeparator('|')
                                .build();
                        List<Contact> csvUsers = csvToBean.parse();

                        for (Contact contact : csvUsers){
                            newAddressBook.addContact(contact);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


