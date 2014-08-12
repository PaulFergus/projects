/*
 * Class Title:         IEPE
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.algorithm.distres.epe;

public interface IEPE {
    //Determines if the specified classes are disjoint from each other.
    public boolean isDisjointTo(Object class1, Object class2, Object model);
    //Determines if one class (class1) is equivalent to another class (class2)
    public boolean isEquivalentTo(Object class1, Object class2, Object model);
    //This method queries the reasoner to determine if class1 is a subclass of class2
    public boolean isSubclassOf(Object class1, Object class2, Object model);
    //Determines if one class (class1) is subsumed by another class (class2)
    public boolean isSubsumedBy(Object class1, Object class2, Object model);
    //This method takes one parameter which is a collectio of concpets to be evolved.
    public void evolveConcepts(Object[] concepts, int topClasses, int topRelationships);
}
