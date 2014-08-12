/*
 * Class Title:         PSMFactory
 * Class Description:   Need to do
 * Author:              Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Team Members:        Paul Fergus
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               cmppferg@livjm.ac.uk
 * Achnowledgements:    
 */
package NASUF.src.uk.ac.livjm.model.psm;

//NASUF API imports
import NASUF.src.uk.ac.livjm.model.psm.impl.PSM;

public class PSMFactory {
    
    //This method returns an instance of an OWLS PCM. The return
    //value is an IPSM interface, conseqnently any OWLS PCM object
    //can be used as long as it implements the IPSM interface.
    public static IPSM createOWLSPCM(){
        return new PSM();
    }
}
