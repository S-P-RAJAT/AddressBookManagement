package com.bridgelabz.addressbook;

import com.opencsv.bean.CsvBindByPosition;

import java.io.Serializable;
import java.util.Objects;

public class Contact implements Serializable {

    @CsvBindByPosition(position = 0)
	private String firstName;

	@CsvBindByPosition(position = 1)
	private String lastName;

	@CsvBindByPosition(position = 2)
	private String address;

	@CsvBindByPosition(position = 3)
	private String city;

	@CsvBindByPosition(position = 4)
	private String state;

	@CsvBindByPosition(position = 5)
	private int zip;

	@CsvBindByPosition(position = 6)
	private long phoneNumber;

	@CsvBindByPosition(position = 7)
	private String email;

	public Contact() {}

	public Contact(String firstName, String lastName, String address, String city, String state, int zip,
			long phoneNumber, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}

	@Override
	public String toString() {
		return  firstName + "|" + lastName + "|" + address + "|" + city
				+ "|" + state + "|" + zip + "|" + phoneNumber + "|" + email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Contact contact = (Contact) o;
		return zip == contact.zip && phoneNumber == contact.phoneNumber && Objects.equals(firstName, contact.firstName) && Objects.equals(lastName, contact.lastName) && Objects.equals(address, contact.address) && Objects.equals(city, contact.city) && Objects.equals(state, contact.state) && Objects.equals(email, contact.email);
	}

}
