package com.bridgelabz.addressbook;

import java.io.Serializable;
import java.util.HashMap;

public class AddressBook implements Serializable {
	String name;
	HashMap<String, Contact> contactList;

	public AddressBook(String addressBookName, HashMap<String, Contact> contactList) {
		super();
		this.name = addressBookName;
		this.contactList = contactList;
		
	}

}
