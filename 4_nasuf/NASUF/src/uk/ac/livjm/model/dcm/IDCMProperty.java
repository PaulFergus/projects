/*
 * Class Title:         IDCMProperty
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.model.dcm;

//Jena API imports
import com.hp.hpl.jena.rdf.model.Property;

public interface IDCMProperty {
    public void createProperty(String namespace, String property);
    public Property getProperty();
    public Object getValue();
    public void setValue(Object value);
}