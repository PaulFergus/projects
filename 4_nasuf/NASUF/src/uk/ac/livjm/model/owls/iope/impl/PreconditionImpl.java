/*
 * Class Title:		PreconditionImpl
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.model.owls.iope.impl;

//NASUF API imports
import NASUF.src.uk.ac.livjm.model.owls.iope.Precondition;

//Jena API imports
import com.hp.hpl.jena.rdf.model.Resource;

//OWL-S API imports
import org.mindswap.owls.process.impl.ParameterImpl;

public class PreconditionImpl extends ParameterImpl implements Precondition {
    
    //Default constructor
    public PreconditionImpl(Resource resource) {
	super(resource);
    }
}