/*
 * Class Title:		IOntology
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.model.owl;

//Java API imports
import java.util.List;
import java.util.Map;

public interface IOntology {
    public Map calculateClassFrequency(List classes);
    public Map calculateRelationshipFrequency(List relationships);
    public void cleanUp();
    public void displayModel(Object ontModel, String outputStyle);
    public List getClasses(List ontologies);
    public List getEquivalentClasses(List classes);
    public List getSubclasses(List classes);
    public boolean isEquivalentClass(String x, String y);
    public boolean isSubClassRelationship(String x, String y);
    public void LoadInstance(String uri) throws Exception;
}