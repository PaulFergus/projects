/*
 * Class Title:         IEE
 * Class Description:   Extraction Engine Interface
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.algorithm.distres.ee;

//Java SDK imports
import java.util.Collection;

public interface IEE {
    //Get the inferred ancestor classes of the specified class.
    public Collection getAncestorClasses(Object className);
    //Get the inferred descendant classes of the specified class.
    public Collection getDescendantClasses(Object className);
    //Gets the equivalent classes of the specified class
    public Collection getEquivalentClasses(Object className);
    //Gets the individuals that are inferred to be members of the specified class.
    public Collection getIndividualsBelongingToClass(Object className);
    //Gets the (direct) inferred subclasses of the specified class.
    public Collection getSubclasses(Object className);
    //Gets the (direct) inferred superclasses of the specified class.
    public Collection getSuperclasses(Object className);
    //Gets the inferred superclasses of the intersection of the list of specified class.
    public Collection getSuperclassesOfIntersection(Object[] classes);
    //This method allows a concept to be retrived from the 
    //distres ontology based on the term passed in as a parameter.
    //If the term cannot be found then a null object is returned.
    public Object extractConcept(Object term);
    //This method allows a concept to be retrieved from the 
    //distres ontology based on the term passed in as a paramter
    //and the depth value. This value indicates how deep subclass
    //hierarchy should be traversed. If the term cannot be found then
    //a null object is returned. 
    public Object extractConcept(Object term, int depth);
}