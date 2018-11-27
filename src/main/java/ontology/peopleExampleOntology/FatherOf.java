package ontology.peopleExampleOntology;

import jade.content.*;

import jade.util.leap.List;

public class FatherOf implements Predicate {
	private List children = null;
	private Man  father   = null;

	public void setChildren(List children) {
		this.children = children;
	}

	public void setFather(Man father) {
		this.father = father;
	}

	public Man getFather() {
		return father;
	}

	public List getChildren() {
		return children;
	}
}
