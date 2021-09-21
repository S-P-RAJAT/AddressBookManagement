package com.bridgelabz.addressbook;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class AddressBookImpl implements AddressBookIF, Serializable {

    public AddressBook addressBook;
    static final Scanner sc = AddressBookManager.sc;
    static final HashMap<String, List<Contact>> contactNamesByCity = AddressBookManager.contactNamesByCity;
    static final HashMap<String, List<Contact>> contactNamesByState = AddressBookManager.contactNamesByState;

    public AddressBookImpl(String addressBookName) {

        super();
        this.addressBook = new AddressBook(addressBookName, new HashMap<String, Contact>());
    }

    @Override
    public String getAddressBookName() {
        return addressBook.name;
    }

    @Override
    public HashMap<String, Contact> getContactList() {
        return addressBook.contactList;
    }

    @Override
    public void addContact(Contact contact) {
        addressBook.contactList.put(contact.getFirstName() + " " + contact.getLastName(), contact);
        contactNamesByCity.computeIfAbsent(contact.getCity(), k -> new LinkedList<Contact>());
        contactNamesByCity.get(contact.getCity()).add(addressBook.contactList.get(contact.getFirstName()+" "+contact.getLastName()));

        contactNamesByState.computeIfAbsent(contact.getState(), k -> new LinkedList<Contact>());
        contactNamesByState.get(contact.getState()).add(addressBook.contactList.get(contact.getFirstName()+" "+contact.getLastName()));
    }



    public void createContact() {

        HashMap<String, String> map = new HashMap<>();
        Consumer<String> getInput = p -> {
            System.out.print("\nEnter your " + p + ": ");
            map.put(p, sc.nextLine());
        };
        getInput.accept("First Name");
        getInput.accept("Last Name");
        String name = map.get("First Name") + " " + map.get("Last Name");
        if (addressBook.contactList.get(name) != null) {

            System.out.println("Duplicate entry! Contact already exists");
        } else {

            String[] attributes = {"Address", "City", "State", "Zip Code", "Phone Number", "Email Id"};
            Stream.of(attributes).forEach(getInput);
            Contact contact = new Contact(map.get("First Name"), map.get("Last Name"), map.get("Address"), map.get("City"),
                    map.get("State"), Integer.parseInt(map.get("Zip Code")),
                    Long.parseLong(map.get("Phone Number")), map.get("Email Id"));
            addContact(contact);
            System.out
                    .println("Contact Details are added!\n" + addressBook.contactList.get(name));
        }
    }

    @Override
    public void editContact() {

        System.out.println("Enter first name of contact that you want to edit: ");
        String firstName = sc.nextLine();
        System.out.println("Enter last name of contact that you want to edit: ");
        String lastName = sc.nextLine();
        String name = firstName + " " + lastName;
        Contact contact = addressBook.contactList.get(name);
        if (contact != null) {
            System.out.println("Enter your choice: \n1.First Name \n2.Last Name \n3.Address \n4.City \n5.State "
                    + "\n6.Zip Code \n7.Phone Number \n8.EmailId \n9.Skip Editing");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Enter new First Name: ");
                    String newFirstName = sc.nextLine();
                    if (addressBook.contactList.get(newFirstName + " " + lastName) != null) {
                        System.out.println("Duplicate entry! Contact already exists");
                    } else {
                        addressBook.contactList.remove(name);

                        addContact(new Contact(newFirstName, lastName, contact.getAddress(), contact.getCity(),
                                contact.getState(), contact.getZip(), contact.getPhoneNumber(),
                                contact.getEmail()));
                        System.out.println("Contact Details are added!\n"
                                + addressBook.contactList.get(newFirstName + " " + lastName));
                    }

                    break;
                case 2:
                    System.out.println("Enter new Last Name: ");
                    String newLastName = sc.nextLine();
                    if (addressBook.contactList.get(firstName + " " + newLastName) != null) {
                        System.out.println("Duplicate entry! Contact already exists");
                    } else {
                        addressBook.contactList.remove(name);
                        addContact(new Contact(firstName, newLastName, contact.getAddress(), contact.getCity(),
                                contact.getState(), contact.getZip(), contact.getPhoneNumber(),
                                contact.getEmail()));
                        System.out.println("Contact Details are added!\n"
                                + addressBook.contactList.get(firstName + " " + newLastName));
                    }
                    break;
                case 3:
                    System.out.println("Enter new Address: ");
                    String newAddress = sc.nextLine();
                    contact.setAddress(newAddress);
                    break;
                case 4:
                    System.out.println("Enter new City: ");
                    String newCity = sc.nextLine();
                    contactNamesByCity.get(contact.getCity()).remove(contact);
                    contact.setCity(newCity);
                    contactNamesByCity.get(contact.getCity()).add(contact);
                    break;
                case 5:
                    System.out.println("Enter new State: ");
                    String newState = sc.nextLine();
                    contactNamesByState.get(contact.getState()).remove(contact);
                    contact.setState(newState);
                    contactNamesByState.get(contact.getState()).add(contact);

                    break;
                case 6:
                    System.out.println("Enter new ZipCode: ");
                    int newZip = sc.nextInt();
                    contact.setZip(newZip);
                    break;
                case 7:
                    System.out.println("Enter new Phone Number: ");
                    long newPhoneNum = sc.nextLong();
                    contact.setPhoneNumber(newPhoneNum);
                    break;
                case 8:
                    System.out.println("Enter new Email Id: ");
                    String newEmail = sc.nextLine();
                    contact.setEmail(newEmail);
                    break;
                case 9:
                    break;
                default:
                    System.out.println("Choose right choice!!");
            }
        } else {
            System.out.println("No contact with such name exists!");
        }

    }

    @Override
    public void viewContact() {

        System.out.println("\nSort by:\n 1)Name\t2)City\t3)State\t4)Zip");
        switch (sc.nextInt()) {
            case 1:
                sortedByName();
                break;
            case 2:
                sortedByCity();
                break;
            case 3:
                sortedByState();
                break;
            case 4:
                sortedByZip();
                break;
            default:
                System.out.println("Please select correct option!");
        }
    }

    @Override
    public void sortedByName() {

        System.out.println("Sorted By First Name:");
        addressBook.contactList.values().stream()
                .sorted(Comparator.comparing(Contact::getFirstName))
                .forEach(System.out::println);
    }

    @Override
    public void sortedByCity() {

        System.out.println("Sorted By City Name:");
        addressBook.contactList.values().stream()
                .sorted(Comparator.comparing(Contact::getCity))
                .forEach(System.out::println);
    }

    @Override
    public void sortedByState() {

        System.out.println("Sorted By State Name:");
        addressBook.contactList.values().stream()
                .sorted(Comparator.comparing(Contact::getState))
                .forEach(System.out::println);
    }

    @Override
    public void sortedByZip() {

        System.out.println("Sorted By Zip Code:");
        addressBook.contactList.values().stream()
                .sorted(Comparator.comparing(Contact::getZip))
                .forEach(System.out::println);
    }

    @Override
    public void removeContact(Contact contact) {
        addressBook.contactList.remove(contact.getFirstName() + " " + contact.getLastName());
        contactNamesByCity.get(contact.getCity()).remove(contact);
        contactNamesByState.get(contact.getState()).remove(contact);
    }

    @Override
    public void deleteContact() {

        System.out.println("Enter first name of contact that you want to delete: ");
        String firstName = sc.nextLine();
        System.out.println("Enter last name of contact that you want to delete: ");
        String lastName = sc.nextLine();
        Contact contact = addressBook.contactList.get(firstName + " " + lastName);
        if (contact != null) {
            System.out.println(contact + "\nDelete (y/n)");
            if ((sc.nextLine()).equals("y")) {
                removeContact(contact);
                System.out.println("Contact is deleted!");
            }
        } else {
            System.out.println("No contact with such name exists!");
        }
    }

    @Override
    public boolean isAddressBookEmpty() {
        if (addressBook.contactList.isEmpty()) {

            System.out.println("Address Book is empty!");
            return true;
        } else {

            return false;
        }
    }

    public void openAddressBook() {
        int choice;
        do {
            System.out.println(
                    "1.Add Contact \n2.View Contact \n3.Edit Contact \n4.Delete Contact \n5.Exit from the AddressBook");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    this.createContact();
                    break;

                case 2:
                    if (!this.isAddressBookEmpty()) {
                        this.viewContact();
                    }
                    break;

                case 3:
                    if (!this.isAddressBookEmpty()) {
                        this.editContact();
                    }
                    break;

                case 4:
                    if (!this.isAddressBookEmpty()) {
                        this.deleteContact();
                    }
                    break;

                case 5:
                    break;

                default:
                    System.out.println("Choose correct option from above mentioned option only!!");
                    break;
            }
        } while (choice != 5);
    }
}
