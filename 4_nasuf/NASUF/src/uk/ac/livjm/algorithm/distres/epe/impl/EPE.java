/*
 * Class Title:         EPE
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.algorithm.distres.epe.impl;

//Java SDK imports
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map;

//Protege OWL API imports
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ReasonerManager; 
import edu.stanford.smi.protegex.owl.inference.protegeowl.ProtegeOWLReasoner; 
import edu.stanford.smi.protegex.owl.inference.dig.reasoner.DIGReasonerIdentity;
import edu.stanford.smi.protegex.owl.model.OWLModel; 
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.ProtegeOWL;

//NASUF API imports
import NASUF.src.uk.ac.livjm.algorithm.Algorithm;
import NASUF.src.uk.ac.livjm.algorithm.AlgorithmFactory;
import NASUF.src.uk.ac.livjm.algorithm.distres.epe.IEPE;
//import NASUF.src.uk.ac.livjm.algorithm.distres.ee.IEE;
import NASUF.src.uk.ac.livjm.algorithm.distres.me.IME;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;

//Log4J API imports
import org.apache.log4j.Level;

public class EPE extends Algorithm implements IEPE {
    
    private Collection models = new ArrayList();
    
    //This model contains the devices local ontology.
    private OWLModel distresOntology;
    
    private ReasonerManager reasonerManager;
    
    //This constant defines the http url used to connect to 
    //the racer dig reasoner. 
    private final String REASONER_URL = "http://localhost:8080";
    
    //This collection contains collections of classes that
    //individual models contain, i.e.
    //[[a, b, c], [b, c], [a, d, f], [a]]
    private Collection models_owlNamedClasses = new ArrayList();
    
    //This collection contains all the unique classes that 
    //exist across all models used in the evolutionary 
    //process.
    private Collection classDictionary = new ArrayList();
    
    //This collection contains a count of the occurance of each 
    //class that appears in all models, i.e. [[a, 3], [b, 2]]
    private Map classF = new TreeMap();
    
    //This collection contains all possible relationships that may
    //exist between the classes in the classDictionary collection.
    private Collection classCombinationCollection = new ArrayList();
    
    //This collection contains a collection of different relationships
    //found for each model, i.e. [[a subclassOf b], [b disjointWith d]]
    private Collection relationshipsFound = new ArrayList();
    
    //This collection contains the disjoint frequency beteen terms
    private Map relationshipF = new TreeMap();
    
    //This object allows concepts to be extracted from the devices
    //ontology.
//    private IEE ee;
    
    /** Creates a new instance of EPE */
    public EPE(Object distresOntology) {
	
	//Reference the external ontology internally within this 
	//algorithm.
	this.distresOntology = (OWLModel)distresOntology;
	
//	//Create a new instance of the extraction engine so that concepts
//	//can be retrieved from the devices ontology.
//	ee = AlgorithmFactory.createExtractionEngine();
	
	//This is a workaround because there is a bug in protege
	//3.1. Mathew Horridge says that this will be fixed in 
	//the next release of Protege.
	Thread.currentThread()
	    .setContextClassLoader(
		EPE
		    .class
			.getClassLoader());

	// Get the reasoner manager and obtain a reasoner for 
        // the OWL model. 
        reasonerManager = ReasonerManager.getInstance(); 
    }
    //This method allows n models to be added to the models
    //collection for analysis. These models are the concpets
    //that are to be evolved. 
    public void addConcept(Object concept){
	models.add(readOWL(concept));
    }
    //This method converts a string representation of the 
    //concpet into some application specific ontology
    //model 
    public Object readOWL(Object f){
        try{
	    OWLModel model = 
		ProtegeOWL
		    .createJenaOWLModelFromReader(
			new StringReader((String)f));
	    return model;
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
		NASUFLogger.error("readOWL Error: " + e.toString());
	    return null;
        }
    }
    
    public void runAnalysis(){
        models_owlNamedClasses.addAll(listOWLNamedClasses());
        classDictionary.addAll(createDictionary());
        calculateNodeTF();
        createClassCombinations();
        findRelationships();
	calculateRelationshipF();
	createOptimalConcept(5,5);
    }
    //This method returns a true of false value depending on whether
    //class1 is disjoint to class 2.
    public boolean isDisjointTo(Object class1, Object class2, Object model) {
	OWLModel owlModel = (OWLModel)model;
	try{
	    return ((ProtegeOWLReasoner)getReasoner(owlModel))
		.isDisjointTo(owlModel
		    .getOWLNamedClass((String)class1),
		    owlModel.getOWLNamedClass((String)class2), null);
	}catch(Exception e){
	    return false;
	}
    }
    //This method returns a true or false value depending on whether
    //class1 is equivalent to class2
    public boolean isEquivalentTo(Object class1, Object class2, Object model){
	OWLModel owlModel = (OWLModel)model;
	try{
	    Collection col = 
		((ProtegeOWLReasoner)getReasoner(owlModel))
		    .getEquivalentClasses(
			owlModel.getOWLNamedClass((String)class1), null);
	
	    Iterator iter = col.iterator();
	    while(iter.hasNext()){
		OWLClass cls = (OWLClass)iter.next();
		if(cls.getName().equalsIgnoreCase((String)class2))
		    return true;
	    }
	}catch(Exception e){
	    return false;
	}
	return false;
    }
    //This method returns a true or false value depending on whether 
    //class1 is a subclass of class2.
    public boolean isSubclassOf(Object class1, Object class2, Object model){
	
	try{
	    OWLModel owlModel = (OWLModel)model;

	    Collection col = owlModel.getSubclasses(
		owlModel.getOWLNamedClass((String)class2));
	    if(col.contains(
		owlModel
		    .getOWLNamedClass(
			(String)class1))){
		return true;
	    }else{
		return false;
	    }
	}catch(Exception e){
	    return false;
	}
    }
    //Determines if one class (class1) is subsumed by another class (class2). 
    //In other words, determines if cls2 is a superclass of cls1.
    //Example isSubsumedBy("AudioSpeaker", "Subwoofer")
    //AudioSpeaker is subsumed by Subwoofer if Subwoofer is a superclass of
    //AudioSpeaker.
    //This is a slight problem with the interpretation of this method. For
    //now this method should be interpreted as class2 is subsumed by class1
    //i.e. class1 is the superclass rather than class2. In the above example
    //isSubsumedBy("AudioSpeaker", "Subwoofer") comes back true and according
    //to the API documentation should be interpret as AudioSpeaker is 
    //subsumed by Subwoofer, i.e. Subwoofer is a superclass of AudioSpeaker.
    //This is clearly not the case - I need to investigate this further. For
    //now I make the interpretation in the opposite way.
    public boolean isSubsumedBy(Object class1, Object class2, Object model) {
	OWLModel owlModel = (OWLModel)model;
	try{
	    return ((ProtegeOWLReasoner)getReasoner(owlModel))
		.isSubsumedBy(
		    owlModel
			.getOWLNamedClass((String)class1),
		    owlModel
			.getOWLNamedClass((String)class2), null);
	}catch(Exception e){
	    return false;
	}
    }
    //This method returns a reasoner for the model passed into the 
    //this method.
    public Object getReasoner(Object model){
	ProtegeOWLReasoner reasoner = 
	    reasonerManager
		.getReasoner((OWLModel)model);
	
        // Set the reasoner URL and test the connection 
        reasoner.setURL(REASONER_URL); 
        if(reasoner.isConnected()) { 
	    // Get the reasoner identity - this contains information 
            // about the reasoner, such as its name and version, 
            // and the tell and ask operations that it supports. 
            DIGReasonerIdentity reasonerIdentity = reasoner.getIdentity(); 
            if(NASUFLogger.isEnabledFor(Level.INFO))
		NASUFLogger.info("Connected to " + reasonerIdentity.getName());
	    try{
		//The classifyTaxonomy method bulk queries the reasoner for the consistency, 
		//infered superclasses, and equivalent classes of very class in the ontology
		//(using only 3 HTTP request/responses) and then inserts this information
		//into the Protege-OWL model. The model can then be queried for the stored
		//inferred information.
		reasoner.classifyTaxonomy(null);
		return reasoner;
	    }catch(Exception e){
		if(NASUFLogger.isEnabledFor(Level.ERROR))
		    NASUFLogger.error("getReasoner Error: " + 
			e.toString());
	    }
	}
	return null;
    }
    //This method process all the models returned as a result of 
    //a ResolverService query. Note that there may be many models
    //returned that satisfy the query, consequently the models 
    //collection could potentially contain zero or more models. 
    //This method is designed to process each model in turn and 
    //extract each of the classes conained in the particular model
    //Each collection of classes is then added to another collection
    //so for example it may look as follows:
    //[[a, b, c], [b, c], [a, d, f], [a]]. This allows us to determine
    //all the classes used across all models. Later this collection will
    //be used to determine what the genral consensus is in terms of 
    //the classes used to describe a concept, i.e. the nodes that are 
    //most common across all models will be extracted. For example the 
    //class 'a' will be used because it is a common node cross all models,
    //whist 'f' will not be used because it is only used once and is 
    //interpreted as a not that does not follow the general consensus. 
    private Collection listOWLNamedClasses(){
	Collection owlClasses = new ArrayList();
        Iterator modelsIter = models.iterator();
        int count = 0;
        while(modelsIter.hasNext()){
            Collection sV = new ArrayList();
            OWLModel model = (OWLModel)modelsIter.next();
            Iterator classIter = model.listOWLNamedClasses();
            while(classIter.hasNext()){
                OWLClass owlClass = (OWLClass)classIter.next();
                sV.add(owlClass.getName());
            }
            //Add the current array to the structVector array
            owlClasses.add(sV);
        }
	return owlClasses;
    }
    //This methods iterates through each model and extracts each
    //class in turn. The classDictionary checks to see if the
    //extracted class exists in the dictionary. If it does not 
    //then the class is added to the dictionary. This results in
    //a collection of unique classes that exist across all models
    //being used for the evolutionary process. 
    private Collection createDictionary(){
	Collection classes = new ArrayList();
        Iterator iter = models_owlNamedClasses.iterator();
        while(iter.hasNext()){
            Collection sv = (Collection)iter.next();
            Iterator classIter = sv.iterator();
            while(classIter.hasNext()){
                Object value = classIter.next();
                if(!classes.contains(value)){
                    classes.add(value);
                }
            }
        }
	return classes;
    }
    //This method process all the classes used in the evolutionary
    //process and extracts each in turn. Each model is iterated over
    //and each class in turn is extracted and compared to the classes
    //contained in the classDictionary. For a particular class its 
    //occurance value is incremented for each time the class is
    //encountered in the models. 
    private void calculateNodeTF(){
        Iterator dicIter = classDictionary.iterator();
        while(dicIter.hasNext()){
            Object dictionaryClass = dicIter.next();
            Iterator svIter = models_owlNamedClasses.iterator();
            int count = 0;
            while(svIter.hasNext()){
                Collection value = (Collection)svIter.next();
                Iterator valueIter = value.iterator();
                while(valueIter.hasNext()){
                    OWLClass cls = (OWLClass)valueIter.next();
                    if(cls.getName().equals(dictionaryClass.toString())){
                        count++;
                    }
                }
            }
            classF.put(dictionaryClass, new Integer(count));
        }
    }
    //This method creates all the possible class combinations that may exist between
    //all the classes contained in the classDictionary. 
    private void createClassCombinations(){
        Collection dicClone = new ArrayList();
	dicClone.addAll(classDictionary);
	
        Iterator iter = classDictionary.iterator();
        while(iter.hasNext()){
            OWLClass value = (OWLClass)iter.next();
            Iterator cloneIter = dicClone.iterator();
            while(cloneIter.hasNext()){
                OWLClass cloneValue = (OWLClass)cloneIter.next();
                if(!cloneValue.equals(value)){
                    if(!classCombinationCollection.contains(value.getName() + " " + cloneValue.getName()) &&
                        !classCombinationCollection.contains(cloneValue.getName() + " " + value.getName())){
                        classCombinationCollection.add(value.getName() + " " + cloneValue.getName());
                    }
                }
            }
        }
    }
    //This method finds the relationships that exist between all classes in 
    //the classCombinationCollection;
    private void findRelationships(){
        Iterator modelIter = models.iterator();
        while(modelIter.hasNext()){
            OWLModel model = (OWLModel)modelIter.next();
            Collection docRelationships = new ArrayList();
            Iterator relIter = classCombinationCollection.iterator();
            while(relIter.hasNext()){
                String relation = (String)relIter.next();
                String[] relations = relation.split(" ");
                try{
		    if(isDisjointTo(
			relations[0].toString(), relations[1].toString(), model)){
			docRelationships.add(relations[0] + " " + "disjointWith " + relations[1]);
		    }else if(isDisjointTo(
			relations[1].toString(), relations[0].toString(), model)){
			docRelationships.add(relations[1] + " " + "disjointWith " + relations[0]);
		    }else if(isEquivalentTo(
			relations[0].toString(), relations[1].toString(), model)){
			docRelationships.add(relations[0] + " " + "equivalentTo " + relations[1]);    
		    }else if(isEquivalentTo(
			relations[1].toString(), relations[0].toString(), model)){
			docRelationships.add(relations[1] + " " + "equivalentTo " +  relations[0]);    
		    }else if(isSubclassOf(
			relations[0].toString(),relations[1].toString(), model)){
			docRelationships.add(relations[0] + " " + "subclassOf " + relations[1]); 
		    }else if(isSubclassOf(
			relations[1].toString(),relations[0].toString(), model)){
			docRelationships.add(relations[1] + " " + "subclassOf " + relations[0]); 
		    }else if(isSubsumedBy(
			relations[0].toString(), relations[1].toString(), model)){
			docRelationships.add(relations[0] + " " + "subsumes " + relations[1]);
		    }else if(isSubsumedBy(
			relations[1].toString(), relations[0].toString(), model)){
			docRelationships.add(relations[1] + " " + "subsumes " + relations[0]);
		    }
                }catch(Exception e){
		    if(NASUFLogger.isEnabledFor(Level.ERROR))
			NASUFLogger.error("createDocumentRelationshipVector Error: " + 
			    e.toString());
		    continue;
                }
            }
            relationshipsFound.add(docRelationships);
        }
    }
    
    private void calculateRelationshipF(){
        Collection cloneRelationshipsFound = new ArrayList();
	Collection ignore = new ArrayList();
	cloneRelationshipsFound.addAll(relationshipsFound);

	//This iteratores through model relationships
	Iterator rfIter = relationshipsFound.iterator();
	while(rfIter.hasNext()){
	    Collection model = (Collection)rfIter.next();
	    //This iterates through the relationship pairs in a model.
	    Iterator modelIter = model.iterator();
	    while(modelIter.hasNext()){
		Object relationshipPair = modelIter.next();
		int count = 0;
		//This iterates through the clone model relationships
		Iterator cloneRFIter = cloneRelationshipsFound.iterator();
		while(cloneRFIter.hasNext()){
		    Collection cloneModel = (Collection)cloneRFIter.next();
		    //This iterates through the relationship pairs in a model
		    Iterator cloneModelIter = cloneModel.iterator();
		    while(cloneModelIter.hasNext()){
			if(!ignore.contains(relationshipPair)){
			    if(cloneModelIter.next().equals(relationshipPair)){
				ignore.add(relationshipPair);
				count++;
			    }
			}
		    }
		}
		relationshipF.put(relationshipPair, new Integer(count));
	    }
	}
    }
    
    //This method searches through the topClassesCollection and
    //extracts the top n classes from the collection that occur
    //within all ontology models being used in the evolutionary
    //process.
    private Object getTopClasses(int topClasses){
	Object tempKey = null;
	Object tempValue = null;
	Map topClassesCollection = new TreeMap();
	if(topClasses < classF.size()){
	    for(int i = 0; i < topClasses; i++){
		int count = 0;
		Iterator iter = classF.keySet().iterator();
		while(iter.hasNext()){
		    Object cls = iter.next();
		    int value = ((Integer)classF.get(cls)).intValue();
		    if(value > count){
		       tempKey = cls;
		       tempValue = classF.get(cls);
		    }
		}
		if(tempKey != null && tempValue != null){
		    classF.remove(tempKey);
		    topClassesCollection.put(tempKey, tempValue);	    
		}
	    }
	    return topClassesCollection;
	}else{
	    return classF;
	}
    }
    //This method searches through the topRelationshipsCollection and
    //extracts the top n classes from the collection that occur
    //within all ontology models being used in the evolutionary
    //process.
    private Object getTopRelationships(int topRelationships){
	Object tempKey = null;
	Object tempValue = null;
	Map topRelationshipsCollection = new TreeMap();
	if(topRelationships < topRelationshipsCollection.size()){
	    for(int i = 0; i < topRelationships; i++){
		int count = 0;
		Iterator iter = relationshipF.keySet().iterator();
		while(iter.hasNext()){
		    Object cls = iter.next();
		    int value = ((Integer)relationshipF.get(cls)).intValue();
		    if(value > count){
			tempKey = cls;
			tempValue = relationshipF.get(cls);
		    }
		}
		if(tempKey != null && tempValue != null){
		    relationshipF.remove(tempKey);
		    topRelationshipsCollection.put(tempKey, tempValue);
		}
	    }
	    return topRelationshipsCollection;
	}else{
	    return relationshipF;
	}
    }
    private void createClasses(Object topClassesCollection,
	Object mergeEngine){
	Iterator iter = ((Map)topClassesCollection).keySet().iterator();
	while(iter.hasNext()){
	    Object cls = iter.next();
	    OWLClass nCls = 
		distresOntology
		    .getOWLNamedClass(((OWLClass)cls)
			.getName());
	    if(nCls.equals(null)){
		((IME)mergeEngine).createClass(nCls, distresOntology);
	    }
	}
    }
    private void createRelationships(Object topRelationshipsCollection,
	Object mergeEngine){
	Iterator iter = ((Map)topRelationshipsCollection).keySet().iterator();
	while(iter.hasNext()){
	    String rel = (String)iter.next();
	    String[] parts = rel.split(" ");
	    if(parts[1].equalsIgnoreCase("disjointWith")){
		((IME)mergeEngine)
		    .createDisjointClass(parts[0], parts[2], distresOntology);
	    }else if(parts[1].equalsIgnoreCase("equivalentTo")){
		((IME)mergeEngine)
		    .createEquivalentClass(parts[0], parts[2], distresOntology);
	    }else if(parts[1].equalsIgnoreCase("subclassOf")){
		((IME)mergeEngine)
		    .createSubclass(parts[0], parts[2], distresOntology);
	    }
	    
	}
    }
    private void createIndividuals(Object topClassesCollection,
	Object mergeEngine){
	Iterator classes = ((Map)topClassesCollection).keySet().iterator();
	while(classes.hasNext()){
	    OWLClass cls = (OWLClass)classes.next();
	    Iterator modelsIter = models.iterator();
	    while(modelsIter.hasNext()){
		OWLModel model = (OWLModel)modelsIter.next();
		Collection instances = model.getInstances(cls);
		Iterator instanceIter = instances.iterator();
		while(instanceIter.hasNext()){
		    ((IME)mergeEngine).createIndividual(cls, instanceIter.next(), distresOntology);
		}
	    }
	}
    }
    //This method creates an optimal concept based on the top n classes
    //and top n relationships
    private void createOptimalConcept(int topClasses, int topRelationships){
	IME me = AlgorithmFactory.createMergeEngine();
	Map topClassesCollection = 
	    (Map)getTopClasses(topClasses);
	Map topRelationshipsCollection = 
	    (Map)getTopRelationships(topRelationships);
	createClasses(topClassesCollection, me);
	createRelationships(topRelationshipsCollection, me);
	createIndividuals(topClassesCollection, me);
    }
    //This method evolves the concepts contained in the object collection
    //and produces an optimal ontology structure that is said to capture
    //the general concensus.
    public void evolveConcepts(Object[] concepts, 
	int topClasses, 
	int topRelationships) {
	//Add the concepts to the OWLModel collection. This
	//call takes each string representation of a concept
	//and converts it into a OWLModel.
	for(int i = 0; i < concepts.length -1; i++)
	    addConcept((String)concepts[i]);
	runAnalysis();
	createOptimalConcept(topClasses, topRelationships);
    }
    public void cleanUp(){
        super.cleanUp();
    }
}