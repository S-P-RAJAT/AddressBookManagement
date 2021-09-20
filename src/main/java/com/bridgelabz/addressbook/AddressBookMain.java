package com.bridgelabz.addressbook;


import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.IOException;

public class AddressBookMain {

	
	public static void main(String[] args) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {

		AddressBookManager manager = new AddressBookManager();
		manager.start();
	}
}
