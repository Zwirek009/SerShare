package ontology.peopleExampleOntology;

import jade.content.onto.*;
import jade.content.schema.*;
//import jade.content.acl.*;

public class PeopleOntology extends Ontology {
	//A symbolic constant, containing the name of this ontology.
	public static final String ONTOLOGY_NAME = "PEOPLE_ONTOLOGY";

	// Concepts
	public static final String PERSON  = "PERSON";
	public static final String MAN     = "MAN";
	public static final String WOMAN   = "WOMAN";
	public static final String ADDRESS = "ADDRESS";

	// Slots
	public static final String NAME   = "NAME";
	public static final String STREET = "STREET";
	public static final String NUMBER = "NUMBER";
	public static final String CITY   = "CITY";
  
	// Predicates
	public static final String FATHER_OF = "FATHER_OF";
	public static final String MOTHER_OF = "MOTHER_OF";

	// Roles in predicates
	public static final String FATHER   = "FATHER";
	public static final String MOTHER   = "MOTHER";
	public static final String CHILDREN = "CHILDREN";

	// Actions
	public static final String MARRY = "MARRY";

	// Arguments in actions
	public static final String HUSBAND = "HUSBAND";
	public static final String WIFE    = "WIFE";

	private static PeopleOntology theInstance = new PeopleOntology(BasicOntology.getInstance());
	
	public static PeopleOntology getInstance() {
		return theInstance;
	}
	
	public PeopleOntology(Ontology base) {
		super(ONTOLOGY_NAME, base, new ReflectiveIntrospector());

		try {
			PrimitiveSchema stringSchema  = (PrimitiveSchema)getSchema(BasicOntology.STRING);
			PrimitiveSchema integerSchema = (PrimitiveSchema)getSchema(BasicOntology.INTEGER);

			ConceptSchema addressSchema = new ConceptSchema(ADDRESS);
			addressSchema.add(STREET, stringSchema,  ObjectSchema.OPTIONAL);
			addressSchema.add(NUMBER, integerSchema, ObjectSchema.OPTIONAL);
			addressSchema.add(CITY,   stringSchema);

			ConceptSchema personSchema = new ConceptSchema(PERSON);
			personSchema.add(NAME,    stringSchema);
			personSchema.add(ADDRESS, addressSchema, ObjectSchema.OPTIONAL);

			ConceptSchema manSchema = new ConceptSchema(MAN);
			manSchema.addSuperSchema(personSchema);

			ConceptSchema womanSchema = new ConceptSchema(WOMAN);
			womanSchema.addSuperSchema(personSchema);

			add(personSchema, Person.class);
			add(manSchema, Man.class);
			add(womanSchema, Woman.class);
			add(addressSchema, Address.class);

			AggregateSchema childrenSchema = new AggregateSchema(BasicOntology.SEQUENCE);

			PredicateSchema fatherOfSchema = new PredicateSchema(FATHER_OF);
			fatherOfSchema.add(FATHER,   manSchema);
			fatherOfSchema.add(CHILDREN, childrenSchema);

			PredicateSchema motherOfSchema = new PredicateSchema(MOTHER_OF);
			motherOfSchema.add(MOTHER,   womanSchema);
			motherOfSchema.add(CHILDREN, childrenSchema);

			add(fatherOfSchema, FatherOf.class);
			add(motherOfSchema, MotherOf.class);

			AgentActionSchema marrySchema = new AgentActionSchema(MARRY);
			marrySchema.add(HUSBAND, manSchema);
			marrySchema.add(WIFE,    womanSchema);

			add(marrySchema);
		} catch(OntologyException oe) { oe.printStackTrace(); }
	}
}
