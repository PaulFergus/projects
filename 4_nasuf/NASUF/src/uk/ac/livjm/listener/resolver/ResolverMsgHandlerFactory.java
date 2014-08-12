/*
 * Class Title:         ResolverMsgHandlerFactory
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

//Jxta API imports
import net.jxta.endpoint.Message;
import net.jxta.protocol.ResolverQueryMsg;

//NASUF API imports
import NASUF.src.uk.ac.livjm.algorithm.sism.abstract_matcher.IAbstractMatcher;
import NASUF.src.uk.ac.livjm.device.impl.controller.mediacenter.JMediaCenter;
import NASUF.src.uk.ac.livjm.listener.resolver.impl.abstract_matcher.AMatcher_DistrES_Handler;
import NASUF.src.uk.ac.livjm.listener.resolver.impl.disus.DiSUS_DECAP_Handler;
import NASUF.src.uk.ac.livjm.listener.resolver.impl.disus.DiSUS_DistrES_Handler;
import NASUF.src.uk.ac.livjm.listener.resolver.impl.disus.DiSUS_Handler;
import NASUF.src.uk.ac.livjm.listener.resolver.impl.disus.DiSUS_SISM_Handler;
import NASUF.src.uk.ac.livjm.listener.resolver.impl.disus.MediaCenter_Handler;
import NASUF.src.uk.ac.livjm.p2p.IDiSUS;

public class ResolverMsgHandlerFactory {
    
    //This method returns a DiSUS_Handler. The return value is 
    //a IHandler inteface, consequently any DiSUS_Handler can 
    //be used as long as the IHandler interface is implemented.
    //This method takes one parameter, which is the P2P 
    //interface.
    public static IResolverHandler createDISUS_Handler(
        IDiSUS disus){
        return new DiSUS_Handler(disus);
    }
    
    //This method returns a DiSUS_DECAP_Handler. The value
    //returned is an IHandler interface, consequently any 
    //DiSUS_DECAP_Handler can be used as long as the IHandler
    //interface is implemented. This method takes four 
    //parameters - the first parameter is the P2P interface
    //and the second parameter is the query message recieved.
    //The third parameter is the service request device 
    //capability model and the fourth is the device capability
    //model.
    public static IResolverHandler createDiSUS_DECAP_Handler(
        IDiSUS disus,
        ResolverQueryMsg msg,
        String srDCM,
        String deviceDCM){
        return new DiSUS_DECAP_Handler(disus, msg, srDCM, deviceDCM);
    }
    
    //This method returns a DiSUS_DistrES_Handler. The value
    //returned is an IHandler interface, consequently any
    //DiSUS_DistrES_Handler can be used as long as the IHandler
    //interface is implemented. This method takes two parameters
    //The first parameter is the P2P interface and the second
    //is the service request peer service capability model.
    public static IResolverHandler createAMatcher_DistrES_Handler(
	IDiSUS disus,
        IAbstractMatcher aMatcher,
        String x_term, String y_term){
        return new AMatcher_DistrES_Handler(disus, aMatcher, x_term, y_term);
    }
    
    public static IResolverHandler createDiSUS_DistrES_Handler(
	IDiSUS disus,
	ResolverQueryMsg msg,
	String term){
	    return 
		new DiSUS_DistrES_Handler(
		    disus,
		    msg,
		    term);
    }
    
    //This method returns a DiSUS_SISM_Handler. The value
    //returned is an IHandler interface, consequently any
    //DiSUS_SISM_Handler can be used as long as the IHandler
    //interface is implemented. This method takes six
    //parameters - the first parameter is the P2P interface
    //the second is the decap message and the third is the 
    //original query. The forth and fifth parameter is the
    //service request peer service capability model and the
    //peer service profile. The last parameter is the module
    //specification for the peer service.
    public static IResolverHandler createDiSUS_SISM_Handler(
        IDiSUS disus,
        Message decapMsg,
        ResolverQueryMsg msg,
        String srPSCM,
        String profile,
        String moduleSpec){
        return new DiSUS_SISM_Handler(disus, decapMsg, msg, srPSCM, profile, moduleSpec);
    }
    
    //This method returns a MediaCenterHandler. The value returned is
    //an IHandler, consequently any MediaCenter object can be used
    //as long as it implements the IHandler interface. This method
    //takes one parameter, which is the UI interface for the media
    //center.
    public static IResolverHandler createMediaCenterHandler(JMediaCenter jmc){
        return new MediaCenter_Handler(jmc);
    }
}