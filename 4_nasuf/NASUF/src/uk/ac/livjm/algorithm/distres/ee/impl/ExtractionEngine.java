/*
 * Class Title:         Merge
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.algorithm.distres.ee.impl;

//Java SDK imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

//Protege OWL API imports
import edu.stanford.smi.protegex.owl.model.OWLModel; 
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass; 
import edu.stanford.smi.protegex.owl.ProtegeOWL; 
import edu.stanford.smi.protegex.owl.inference.protegeowl.ReasonerManager; 
import edu.stanford.smi.protegex.owl.inference.protegeowl.ProtegeOWLReasoner; 
import edu.stanford.smi.protegex.owl.inference.dig.reasoner.DIGReasonerIdentity;

//NASUF API imports
import NASUF.src.uk.ac.livjm.algorithm.Algorithm;
import NASUF.src.uk.ac.livjm.algorithm.distres.ee.IEE;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;

//Log4J API imports
import org.apache.log4j.Level;

public class ExtractionEngine extends Algorithm implements IEE{
    
    //This DistrES ontology model is held in memory using 
    //this OWLModel object.
    public OWLModel distresOntology;
    
    //This is the reasoner used to reason over the distres
    //ontology. 
    private ProtegeOWLReasoner reasoner;
    
    //Default constructor
    public ExtractionEngine(
	Object reasoner,
	Object distresOntology) {
	this.reasoner = (ProtegeOWLReasoner)reasoner;
	this.distresOntology = (OWLModel)distresOntology;
    }
    //This method takes one parameter which is a particular class
    //name and determines all the Ancestor classes associated with 
    //this class. 
    public Collection getAncestorClasses(Object className) {
	try{
	    //Call the Protege OWL API getOWLAncestorClass method
	    //pass the className. In this instance we do not
	    //require a Reasoner Task Listener so leave this null.
	    return reasoner
		.getAncestorClasses(distresOntology
		    .getOWLNamedClass((String)className), null);
	}catch(Exception e){
	    if(NASUFLogger.isEnabledFor(Level.ERROR))
		NASUFLogger.error("getAncestorClasses Error: " + 
		    e.toString());
	    return null;
	}
    }
    
    //This method takes one parameter which is a particular class
    //name and determines all the Decendent classes associated with 
    //this class.
    public Collection getDescendantClasses(Object className){
	try{
	    return reasoner
		.getDescendantClasses(distresOntology
		    .getOWLNamedClass((String)className), null);
	}catch(Exception e){
	    if(NASUFLogger.isEnabledFor(Level.ERROR))
		NASUFLogger.error("getDescendentClasses Error: " + 
		    e.toString());
	    return null;
	}
    }
    
    //This method takes one parameter which is a particular class
    //name and determines all the Equivalent classes associated with 
    //this class.
    public Collection getEquivalentClasses(Object className){
	try{
	    return reasoner
		.getEquivalentClasses(distresOntology
		    .getOWLNamedClass((String)className), null);
	}catch(Exception e){
	    if(NASUFLogger.isEnabledFor(Level.ERROR))
		NASUFLogger.error("getEquivalentClasses Error: " + 
		    e.toString());
	    return null;
	}
    }

    //This method takes one parameter which is a particular class
    //name and determines all the Individuals belonging to this class
    public Collection getIndividualsBelongingToClass(Object className){
	try{
	    return reasoner
		.getIndividualsBelongingToClass(distresOntology
		    .getOWLNamedClass((String)className), null);
	}catch(Exception e){
	    if(NASUFLogger.isEnabledFor(Level.ERROR))
		NASUFLogger.error("getIndividualsBelongingToClass Error: " + 
		    e.toString());
	    return null;
	}
    }
    
    //This method takes one parameter which is a particular class
    //name and determines all the subclasses of this class
    public Collection getSubclasses(Object className) {
	try{
	    return reasoner
		.getSubclasses(distresOntology
		    .getOWLNamedClass((String)className), null);
	}catch(Exception e){
	    if(NASUFLogger.isEnabledFor(Level.ERROR))
		NASUFLogger.error("getSubclasses Error: " + 
		    e.toString());
	    return null;
	}
    }
    
    //This method takes one parameter which is a particular class
    //name and determines all the Individuals belonging to this class
    public Collection getSuperclasses(Object className) {
	try{
	    return reasoner
		.getSuperclasses(distresOntology
		    .getOWLNamedClass((String)className), null);
	}catch(Exception e){
	    if(NASUFLogger.isEnabledFor(Level.ERROR))
		NASUFLogger.error("getSuperclasses Error: " + 
		    e.toString());
	    return null;
	}
    }
    //This method takes one parameter which is a collection of classes
    //and determines all the Superclasses of the intersection of the
    //class collection.
    public Collection getSuperclassesOfIntersection(Object[] classes) {
	try{
	    return reasoner
		.getSuperclassesOfIntersection((OWLClass[]) classes, null);
	}catch(Exception e){
	    if(NASUFLogger.isEnabledFor(Level.ERROR))
		NASUFLogger.error("getSuperclassesOfIntersectin Error: " + 
		    e.toString());
	    return null;
	}
    }   
    //This method determines all the disjoint classes and associates
    //them with the class passed in as a parameter.
    public void addDisjointClasses(OWLNamedClass cls, OWLModel model){
	for(Iterator iter = 
	    distresOntology
		.getOWLNamedClass(
		    cls.getName())
			.getDisjointClasses()
			    .iterator(); iter.hasNext();){
	    OWLNamedClass disjoint = (OWLNamedClass)iter.next();
	    if(!cls.getDisjointClasses().contains(disjoint)){
		//addProperties(disjoint, model);
		//addRestrictions(disjoint, model);
		addIndividuals(disjoint, model);
		cls.addDisjointClass(disjoint);
	    }
	}
    }
    //This method determines all the equivalent classes and associates
    //them with the class passed in as a parameter.
    public void addEquivalentClasses(OWLNamedClass cls, OWLModel model){
	for(Iterator iter = 
	    distresOntology
		.getOWLNamedClass(
		    cls.getName())
			.getEquivalentClasses()
			    .iterator(); iter.hasNext();){
	    OWLNamedClass equivalent = (OWLNamedClass)iter.next();
	    if(cls.getEquivalentClasses().contains(equivalent)){
		//addProperties(equivalent, model);
		//addRestrictions(equivalent, model);
		addIndividuals(equivalent, model);
		cls.addEquivalentClass(equivalent);
	    }
	}
    }
    
    public void addIndividuals(OWLNamedClass cls, OWLModel model){
	for(Iterator iter = 
	    distresOntology
		.getOWLNamedClass(
		    cls.getName())
			.getDirectInstances()
			    .iterator(); iter.hasNext();){
	    OWLIndividual instance = (OWLIndividual)iter.next();
	    if(!cls.getDirectInstances().contains(instance))
		model.createInstance(instance.getName(), cls);
	}
    }
    //Need to do
    public void addProperties(OWLNamedClass cls, OWLModel model){
	
    }
    //This is a recursive method that calls itself, closely moving it closer to its
    //destination. Ordinarily this would be classed as a cyclic method that 
    //calls itself indefinetly. What makes the typical recursive definition different
    //is that there is an endpoint as defined by the for look. For each new method
    //call spawned the number of steps in the new method call is reduced by one, until
    //the addSuperclasses method is called and the conditions are not satisfied in 
    //terms of entering the for loop, i.e. the a cls does not have a superclasses. 
    //There are two types of recursion - tail-recursion and head-recursion. The former
    //is defined when the value of a tail recursive call is immediately returned by 
    //the caller. Head recursion is defined when the returned value is further
    //processed. 
    public OWLNamedClass addSubclasses(OWLNamedClass cls, OWLModel model){
	for(Iterator iter = 
	    distresOntology
		.getOWLNamedClass(
		    cls.getName())
			.getSubclasses(false)
			    .iterator(); iter.hasNext();){
	    OWLNamedClass subclass = (OWLNamedClass)iter.next();
	    
	    OWLNamedClass returnedSubclass =
		addSubclasses(subclass, model);
	    addDisjointClasses(returnedSubclass, model);
	    addEquivalentClasses(returnedSubclass, model);
	    //addProperties(returnedSubclass, model);
	    //addRestrictions(returnedSubclass, model);
	    addIndividuals(returnedSubclass, model);
	    model
		.createOWLNamedSubclass(
		    returnedSubclass.getName(), cls);
	}
	return cls;
    }
    //This is a recursive method that calls itself, closely moving it closer to its
    //destination. Ordinarily this would be classed as a cyclic method that 
    //calls itself indefinetly. What makes the typical recursive definition different
    //is that there is an endpoint as defined by the for look. For each new method
    //call spawned the number of steps in the new method call is reduced by one, until
    //the addSuperclasses method is called and the conditions are not satisfied in 
    //terms of entering the for loop, i.e. the a cls does not have a superclasses. 
    //There are two types of recursion - tail-recursion and head-recursion. The former
    //is defined when the value of a tail recursive call is immediately returned by 
    //the caller. Head recursion is defined when the returned value is further
    //processed. 
    public OWLNamedClass addSuperclasses(OWLNamedClass cls, OWLModel model){	
	for(Iterator iter = 
	    distresOntology
		.getOWLNamedClass(
		    cls.getName())
			.getSuperclasses(false)
			    .iterator(); iter.hasNext();){
	    OWLNamedClass superclass = (OWLNamedClass)iter.next();
	    
	    OWLNamedClass returnedSuperclass =
		addSuperclasses(superclass, model);
	    addDisjointClasses(returnedSuperclass, model);
	    addEquivalentClasses(returnedSuperclass, model);
	    //addProperties(returnedSuperclass, model);
	    //addRestrictions(returnedSuperclass, model);
	    addIndividuals(returnedSuperclass, model);
	    model
		.createOWLNamedSubclass(
		    cls.getName(), returnedSuperclass);   
	}
	return cls;
    }
    
    //Need to do
    public void addRestrictions(OWLNamedClass cls, OWLModel model){
	
    }
    //This is an incomplete method, however at present it allows
    //taxonomical information to be extracted from the devices local
    //ontology. In future work we will look at extacting properties 
    //equivalent classes and disjoint relationships to name a few.
    public Object extractConcept(Object term) {
	//Create a new empty OWLModel 
	OWLModel model = 
	    ProtegeOWL
		.createJenaOWLModel();
	//Try to retrieve the term from the devices ontology.
	OWLNamedClass distresTerm = 
	    distresOntology.getOWLNamedClass((String)term);
	
	//If the term exists then begin to extract the concept 
	//else just return null;
	if(distresTerm != null){
	    //Create a new Class corresponding to the term passed into this
	    //method. 
	    OWLNamedClass classTerm = model.createOWLNamedClass((String)term);
	    addDisjointClasses(classTerm, model);
	    addEquivalentClasses(classTerm, model);
	    addIndividuals(classTerm, model);
	    addSubclasses(classTerm, model);
	    addSuperclasses(classTerm, model);
	    //Return the model to the caller.
	    return model;
	}else{
	    return null;
	}
    }
    
    //This method extracts the concepts surrounding the term passed
    //in as a parameter, however in this instance the depth of the 
    //subsumption tree is controlled using a depth value. This 
    //method has not been implemented in this version of NASUF.
    public Object extractConcept(Object term, int depth) {
	return null;
    }   
    
    public void cleanUp(){
        super.cleanUp();
    }
}
