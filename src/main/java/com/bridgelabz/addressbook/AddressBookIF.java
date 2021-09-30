package com.bridgelabz.addressbook;

import java.util.HashMap;
import java.util.List;

public interface AddressBookIF {

	HashMap<String, Contact> getContactList();

	Contact getContact(String firstName, String lastName);

	void addContact(Contact contact);

	void createContact();

	void editContact();

	void updateEmail(String firstName, String lastName, Contact contact, String newEmail);

	void updatePhoneNumber(String firstName, String lastName, Contact contact, long newPhoneNumber);

	void updateZipCode(String firstName, String lastName, Contact contact, int newZip);

	void updateState(String firstName, String lastName, Contact contact, String newState);

	void updateCity(String firstName, String lastName, Contact contact, String newCity);

	void updateAddress(String firstName, String lastName, Contact contact, String newAddress);

	void updateLastName(String firstName, String lastName, Contact contact, String newLastName);

	void updateFirstName(String firstName, String lastName, Contact contact, String newFirstName);

	void viewContact();

	void removeContact(Contact contact);

	void deleteContact();

	boolean isAddressBookEmpty();

	void setAddressBook(AddressBook addressBook);

	void openAddressBook(AddressBook addressBook);

	void sortedByName();

	void sortedByCity();

	void sortedByState();

	void sortedByZip();

}