/*
 * Class Title:		OWLServicesFactory
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.model.owls;

//NASUF API imports
import NASUF.src.uk.ac.livjm.model.owls.iope.Effect;
import NASUF.src.uk.ac.livjm.model.owls.iope.impl.EffectImpl;
import NASUF.src.uk.ac.livjm.model.owls.iope.impl.PreconditionImpl;
import NASUF.src.uk.ac.livjm.model.owls.iope.Precondition;

//Jena API imports
import com.hp.hpl.jena.rdf.model.Resource;

//OWL-S API imports
import org.mindswap.owls.OWLSFactory;

public class OWLServicesFactory extends OWLSFactory{
    
    //Default constructory
    public OWLServicesFactory() {
    }
    
    //This method creates a new NASUF Precondition. It takes one 
    //parameter, which is a resouce.
    public static Precondition createNASUFPrecondition(Resource resource) { 
        return new PreconditionImpl(resource); 
    }
    
    //This method creates a new NASUF Effect. It takes one 
    //parameter, which is a recource.
    public static Effect createNASUFEffect(Resource resource) { 
        return new EffectImpl(resource); 
    }
}