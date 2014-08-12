/*
 * Class Title:         Handler
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.listener.resolver;

//JXTA API imports
import net.jxta.resolver.ResolverService;
import net.jxta.util.JxtaBiDiPipe;

//NASUF API imports
import NASUF.src.uk.ac.livjm.listener.resolver.impl.disus.DiSUS_Handler;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.p2p.IDiSUS;

//Log4J API imports
import org.apache.log4j.Level;

public abstract class ResolverHandler implements IResolverHandler{
    
    //This class variable contains a reference to the 
    //DiSUS_Handler so that the call-back methods can
    //be called.
    public static DiSUS_Handler drh = null;
    //This class varaible contains a reference 
    //to the P2P interface.
    protected IDiSUS disus;
    
    //This variable contains the pipe used to connect
    //to specific core services. 
    protected JxtaBiDiPipe pipe = null;
    
    //This method must be overriden in the subclass.
    public void pipeMsgEvent(net.jxta.pipe.PipeMsgEvent pipeMsgEvent) {
    }
    
    //This method must be overriden in the subclass.
    public int processQuery(net.jxta.protocol.ResolverQueryMsg resolverQueryMsg) {
	return ResolverService.OK;
    }
    
    //This method must be overriden in the subclass
    public void processResponse(net.jxta.protocol.ResolverResponseMsg resolverResponseMsg) {
    }
    
    //This method is called when the object is destroyed.
    //it ensures that the pipe is closed and destroyed.
    //It also ensures that the DiSUS_Handler is de-
    //referenced.
    public void finalize(){
	try{
	    //If an error occurs ensure that the pipe
	    //is closed.
	    pipe = null;
	}catch(Exception ioe){
	    if(NASUFLogger.isEnabledFor(Level.ERROR))
		NASUFLogger.error("ResolverHandler: finalize: " + 
		    ioe.toString());
	}
    }
}