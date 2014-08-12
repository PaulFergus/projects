/*
 * Class Title:		OWLFactory
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

//NASUF API imports
import NASUF.src.uk.ac.livjm.model.owl.impl.DistrESOntology;

public class OWLFactory{
    
    //This method returns a DistrESOntology. The return value is 
    //an IOntology interface, consequently any DistrESOntology
    //can be used as long as it implements the IOntology 
    //interface.
    public static IOntology createDistrESOntology(){
	return new DistrESOntology();
    }
    
}
