package com.bridgelabz.addressbook;

import java.util.HashMap;
import java.util.List;

public interface AddressBookIF {

	HashMap<String, Contact> getContactList();

	void addContact(Contact contact);

	void createContact();

	void editContact();

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