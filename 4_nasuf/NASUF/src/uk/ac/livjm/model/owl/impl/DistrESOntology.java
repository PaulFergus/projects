/*
 * Class Title:		DistrESOntology
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */

package NASUF.src.uk.ac.livjm.model.owl.impl;

//Java API imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//NASUF API imports
import NASUF.src.uk.ac.livjm.model.owl.IOntology;

public class DistrESOntology implements IOntology{
    
    /** Creates a new instance of DistrESOntology */
    public DistrESOntology() {
    }
    
    public void LoadInstance(String uri) throws Exception {
    }
    
    public Map calculateClassFrequency(List classes) {
	//I need to change this.
	return new HashMap();
    }
    
    public Map calculateRelationshipFrequency(List relationships) {
	//I need to change this.
	return new HashMap();
    }
    
    public void displayModel(Object ontModel, String outputStyle) {
    }
    
    public List getClasses(List ontologies) {
	//I need to change this.
	return new ArrayList();
    }
    
    public List getEquivalentClasses(List classes) {
	//I need to change this.
	return new ArrayList();
    }
    
    public List getSubclasses(List classes) {
	//I need to change this.
	return new ArrayList();
    }
    
    public boolean isEquivalentClass(String x, String y) {
	//This needs to change
	return true;
    }
    
    public boolean isSubClassRelationship(String x, String y) {
	//This needs to change.
	return true;
    }
    
    public void cleanUp() {
    }
}
