package com.bridgelabz.addressbook;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;

import static com.bridgelabz.addressbook.AddressBookIOService.WriteContacts;
import static com.bridgelabz.addressbook.AddressBookIOService.readData;
import static com.bridgelabz.addressbook.IOService.DB_IO;

enum IOService {
    CONSOLE_IO, CSV_IO, DB_IO, REST_IO, JSON_IO
}

public class AddressBookManager {
    public static final Scanner sc = new Scanner(System.in);

    public static final int[] bookCount = {0};

    List<AddressBook> addressBook;
    public static HashMap<String, List<Contact>> contactNamesByCity = new HashMap<>();
    public static HashMap<String, List<Contact>> contactNamesByState = new HashMap<>();
    public static AddressBookIF addressBookService = new AddressBookImpl();


    private static final String filePath = "./";

    public AddressBookManager() {
        this.addressBook = new ArrayList<>();

    }


    public void start()
			throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        boolean found;
        System.out.println("Welcome to Address Book Program\n");
        int choice, addressBookNumber = -1;
        addressBook = readData(IOService.DB_IO);


        do {
            System.out.println("\n1.Create new Address Book \n2.Open Address Book \n3.Books List \n"
                    + "4.Search by City Name \n5.Search by State Name  \n6.Exit");
            choice = sc.nextInt();
            switch (choice) {

                case 1:

                    sc.nextLine();
                    System.out.print("Enter the address book name: ");
                    String addressBookName = sc.nextLine();
                    addressBook.add(new AddressBook(addressBookName, new HashMap<>()));

                    break;

                case 2:
                    sc.nextLine();
                    System.out.print("Enter the address book name: ");
                    String bookName = sc.nextLine();
                    Predicate<AddressBook> addressBookNamePredicate = n -> n.getAddressBookName()
							.equals(bookName);
                    AddressBook addressBookObject = addressBook.stream()
                            .filter(addressBookNamePredicate)
                            .findFirst()
                            .orElse(null);

                    if (addressBookObject == null) {
                        System.out.println("No book with such name exists!");
                    } else {
                        addressBookService.openAddressBook(addressBookObject);
                    }
                    break;

                case 3:
                    if (addressBook.size() == 0) {
                        System.out.println("No address books are created yet!");
                    }
                    bookCount[0] = 0;
                    addressBook.stream().forEach(
							p -> System.out.println((++bookCount[0]) + ". " + p.getAddressBookName())
					);
                    break;
                case 4:
                    sc.nextLine();
                    System.out.println("Enter the city name: ");
                    String city = sc.nextLine();

                    if (contactNamesByCity.get(city) == null) {
                        System.out.println("No records founds with city name: " + city);
                    } else {
                        System.out.println("1. List all contacts in "
								+ city + "\n2. Search for a person in " + city);
                        if (sc.nextInt() == 2) {
                            sc.nextLine();
                            System.out.println("Enter First Name: ");
                            String firstName = sc.nextLine();
                            System.out.println("Enter Last Name: ");
                            String lastName = sc.nextLine();
                            found = false;
                            for (Contact person : contactNamesByCity.get(city)) {
                                if (person.getFirstName().equals(firstName)
										&& person.getLastName().equals(lastName)) {
                                    found = true;
                                    System.out.println("Found!\n" + person);
                                }
                            }
                            if (!found) {
                                System.out.println("No users exist with such name in " + city);
                            }
                        } else {
                            int count = 0;
                            StringBuilder output = new StringBuilder();
                            for (Contact person : contactNamesByCity.get(city)) {
                                output.append(person.toString().concat("\n"));
                                count++;
                            }
                            System.out.println(count + " records found!\n" + output);
                        }

                    }
                    break;

                case 5:
                    sc.nextLine();
                    System.out.println("Enter the state name: ");
                    String state = sc.nextLine();
                    if (contactNamesByState.get(state) == null) {
                        System.out.println("No records founds with state name: " + state);
                    } else {
                        System.out.println("1. List all contacts in "
								+ state + "\n2. Search for a person in " + state);
                        if (sc.nextInt() == 2) {
                            sc.nextLine();

                            System.out.println("Enter First Name: ");
                            String firstName = sc.nextLine();
                            System.out.println("Enter Last Name: ");
                            String lastName = sc.nextLine();
                            found = false;
                            for (Contact person : contactNamesByState.get(state)) {
                                if (person.getFirstName().equals(firstName)
										&& person.getLastName().equals(lastName)) {
                                    found = true;
                                    System.out.println("Found!\n" + person);
                                }
                            }
                            if (!found) {
                                System.out.println("No users exist with such name in " + state);
                            }
                        } else {
                            int count = 0;
                            StringBuilder output = new StringBuilder();
                            for (Contact person : contactNamesByState.get(state)) {
                                output.append(person.toString().concat("\n"));
                                count++;
                            }
                            System.out.println(count + " records found!\n" + output);
                        }
                    }
                    break;
                case 6:

                    WriteContacts(addressBook, IOService.JSON_IO);
                    break;

                default:
                    System.out.println("Choose correct option from above mentioned option only!!");
                    break;
            }
        } while (choice != 6);
        sc.close();
    }
}
