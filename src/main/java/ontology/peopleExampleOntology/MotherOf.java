package ontology.peopleExampleOntology;

import jade.content.*;

import jade.util.leap.List;

public class MotherOf implements Predicate {
	private List  children = null;
	private Woman mother   = null;

	public void setChildren(List children) {
		this.children = children;
	}

	public void setMother(Woman mother) {
		this.mother = mother;
	}

	public Woman getMother() {
		return mother;
	}

	public List getChildren() {
		return children;
	}
}
