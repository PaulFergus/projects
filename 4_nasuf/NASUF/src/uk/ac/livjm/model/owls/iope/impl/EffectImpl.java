/*
 * Class Title:		EffectImpl
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
import NASUF.src.uk.ac.livjm.model.owls.iope.Effect;

//Jena API imports
import com.hp.hpl.jena.rdf.model.Resource;

//OWL-S API imports
import org.mindswap.owls.process.impl.ParameterImpl;

public class EffectImpl extends ParameterImpl implements Effect {

    //Default constructor. This method takes one parameter,
    //which is a resource.
    public EffectImpl(Resource resource) {
	super(resource);
    }
}