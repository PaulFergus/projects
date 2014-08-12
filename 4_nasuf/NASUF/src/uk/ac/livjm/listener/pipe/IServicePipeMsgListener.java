/*
 * Class Title:         IServicePipeMsgListener
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

//JXTA API imports
import net.jxta.util.JxtaBiDiPipe;
import net.jxta.pipe.PipeMsgListener;

public interface IServicePipeMsgListener extends PipeMsgListener{
    public void setPipe(JxtaBiDiPipe pipe);
    public void stop();
    public void cleanUp();
}
