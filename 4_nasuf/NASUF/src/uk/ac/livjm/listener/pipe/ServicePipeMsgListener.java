/*
 * Class Title:         ServicePipeMsgListener
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.listener.pipe;

//Jxta API imports
import net.jxta.util.JxtaBiDiPipe;

//NASUF API imports
import NASUF.src.uk.ac.livjm.listener.pipe.IServicePipeMsgListener;

public abstract class ServicePipeMsgListener implements IServicePipeMsgListener {
    
    //This class variable uses the bidi pipe used for bidirectional
    //communications between the client and the service.
    public JxtaBiDiPipe pipe = null;
    
    //This setter sets the pipe used by the device.
    public void setPipe(JxtaBiDiPipe pipe) {
        this.pipe = pipe;
    }
    
    //This method must be overloaded in the subclasses
    public void stop(){
    }
    
    //This must be overiden in the subclasses.
    public void pipeMsgEvent(net.jxta.pipe.PipeMsgEvent pipeMsgEvent) {
    }
    
    //Each device must implement the cleanUp method which 
    //must ensure that all the objects used by the device
    //are correctly distroyed.
    public void cleanUp(){
        pipe = null;
    }
}