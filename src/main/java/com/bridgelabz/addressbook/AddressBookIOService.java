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

    public static void WriteContactsToFile(HashMap<String, Contact> contactList, String addressBookName,IOService ioservice) {
        try {
            if(ioservice == IOService.FILE_IO) {
                String s = "firstName, lastName, address, city, state, zip, phoneNumber, email";
                Path playPath = Paths.get(filePath);
                if (Files.notExists(playPath)) {
                    Files.createDirectory(playPath);
                }
                Path tempFile = Paths.get(playPath + "/" + addressBookName);

                StringBuffer empBuffer = new StringBuffer();
                contactList.forEach((name, contact) -> {
                    String employeeDataString = contact.getFirstName()
                            + "|" + contact.getLastName()
                            + "|" + contact.getAddress()
                            + "|" + contact.getCity()
                            + "|" + contact.getState()
                            + "|" + contact.getZip()
                            + "|" + contact.getPhoneNumber()
                            + "|" + contact.getEmail()
                            + "\n";

                    empBuffer.append(employeeDataString);
                });

                Files.write(Paths.get(String.valueOf(tempFile)), empBuffer.toString().getBytes());
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


