package com.bridgelabz.addressbook;

import java.util.HashMap;

public interface AddressBookIF {

	String getAddressBookName();

	HashMap<String, Contact> getContactList();

	void addContact(Contact contact);

	void createContact();

	void editContact();

	void viewContact();

	void removeContact(Contact contact);

	void deleteContact();

	boolean isAddressBookEmpty();

	void openAddressBook();

	void sortedByName();

	void sortedByCity();

	void sortedByState();

	void sortedByZip();

}