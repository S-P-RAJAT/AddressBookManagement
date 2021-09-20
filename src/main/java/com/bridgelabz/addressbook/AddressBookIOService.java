package com.bridgelabz.addressbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;


public class AddressBookIOService {
    private static final String filePath = "Address_Books/";


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


