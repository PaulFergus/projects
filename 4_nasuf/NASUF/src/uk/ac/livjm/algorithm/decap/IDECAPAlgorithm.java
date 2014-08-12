/*
 * Class Title:         IDECAPAlgorithm
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.algorithm.decap;

//Java SDK imports
import java.util.List;

//NASUF API imports
import NASUF.src.uk.ac.livjm.algorithm.IAlgorithm;

public interface IDECAPAlgorithm extends IAlgorithm {
    public Object checkCapability(
	    Object params,
            Object deviceDECAP, 
            Object clientDECAP);
    public void cleanUp();
}
