/*
 * Class Title:         P2PFactory
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.p2p;

//NASUF API imports
import NASUF.src.uk.ac.livjm.device.IDevice;
import NASUF.src.uk.ac.livjm.p2p.impl.DiSUS;

public class P2PFactory{
    
    //This method creates an IDiSUS interface instance. The return
    //value is an instance consequently any P2P implementation could
    //be used as long as it implements the IDiSUS interface. The method
    //takes one parameter, which is the IDevice. Again this is an
    //interface so any device can be used as long as they implement the
    //IDevice interface.
    public static IDiSUS createJxtaP2P(IDevice device){
        return new DiSUS(device);
    }
}