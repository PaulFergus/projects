/*
 * Class Title:         IDistrESAlgorithm
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.algorithm.distres;

public interface IDistrESAlgorithm {
    //Get the distres ontology
    public Object getDistrESOntology();
    //Abstracts the above methods to determine whether a relationship exists between class1 and class2.
    public Object performTermSemInterop(Object xTerm, Object yTerm);
    //This method extracts the concpet surrounding the term passed in as a parameter
    public Object extractConcept(Object term);
    //This method extracts the concept surrounding the term passed in as a parameter
    //however the size of the concept is constrained using the distance value.
    public Object extractConcept(Object term, int distance);
    //This method allows the Unknown Term Table to be returned. 
    public Object getUnknownTermTable();
    //This method allows an Unknown Term to be added to the Unknown Term Table.
    public void addUnknownTerm(Object term);
    //This method allows an Unknown Term to be removed from the Unknown Term Table.
    public Object removeUnknownTerm(Object term);
}