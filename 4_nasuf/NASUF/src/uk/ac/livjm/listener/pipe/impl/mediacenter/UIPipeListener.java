/*
 * Class Title:         UIPipeListener
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.listener.pipe.impl.mediacenter;

//Jxta API imports
import net.jxta.pipe.PipeMsgListener;

//NASUF API imports
import NASUF.src.uk.ac.livjm.device.impl.controller.mediacenter.JMediaCenter;
import NASUF.src.uk.ac.livjm.listener.pipe.ServicePipeMsgListener;

public class UIPipeListener extends ServicePipeMsgListener {
    
    //This varaible contains a reference to the user interface.
    //This allows servcies to be automatically added to the UI
    //when the addService is raised.
    private JMediaCenter jmc = null;
    
    //Default constructor. This constructor takes one 
    //parameter, which is a reference to the UI.
    public UIPipeListener(JMediaCenter jmc) {
        this.jmc = jmc;
    }
    
    //This event method is called when a new service is recieved
    public void addService(String service){
        jmc.addService(service);
    } 
}