package ontology.peopleExampleOntology;

import jade.content.*;
import ontology.peopleExampleOntology.Address;

public class Person implements Concept {
	private String  name    = null;
	private Address address = null;

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public Address getAddress() {
		return address;
	}
}
