/*
 * Class Title:         IDCMStatement
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

//Java API imports
import java.util.List;

//Jena API imports.
import com.hp.hpl.jena.rdf.model.Resource;

public interface IDCMStatement {
    public IDCMStatement addProperty(IDCMProperty property);
    public void createResource(String namespace, String value);
    public List getProperties();
    public Resource getResource();
}