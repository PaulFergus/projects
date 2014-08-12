/*
 * Class Title:         ServicePipeMsgListenerFactory
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

//NASUF API imports
import NASUF.src.uk.ac.livjm.algorithm.decap.IDECAPAlgorithm;
import NASUF.src.uk.ac.livjm.algorithm.distres.IDistrESAlgorithm;
import NASUF.src.uk.ac.livjm.algorithm.sism.abstract_matcher.IAbstractMatcher;
import NASUF.src.uk.ac.livjm.algorithm.sism.concrete_matcher.IConcreteMatcher;
import NASUF.src.uk.ac.livjm.listener.pipe.impl.decap.DeCapPipeMsgListener;
import NASUF.src.uk.ac.livjm.listener.pipe.impl.distres.DistrESPipeMsgListener;
import NASUF.src.uk.ac.livjm.listener.pipe.impl.sism.SISMPipeMsgListener;
import NASUF.src.uk.ac.livjm.listener.pipe.impl.audio.AudioPipeMsgListener;
import NASUF.src.uk.ac.livjm.listener.pipe.impl.video.VideoPipeMsgListener;
import NASUF.src.uk.ac.livjm.listener.pipe.impl.player.MediaTransmitterPipeMsgListener;
import NASUF.src.uk.ac.livjm.p2p.IDiSUS;
import NASUF.src.uk.ac.livjm.listener.pipe.impl.mediacenter.UIPipeListener;
import NASUF.src.uk.ac.livjm.device.impl.controller.mediacenter.JMediaCenter;

//Jxta API imports
import net.jxta.util.JxtaBiDiPipe;

public abstract class ServicePipeMsgListenerFactory {
    
    //This static method returns an DeCap pipe message listener
    //This object is returned as an interface, consequently any
    //DeCap listener can be used as long as it implements the 
    //IServicePipeMsgListener interface. This method takes one
    //parameter, which is the DeCap algorithm used again this 
    //parameter is a interface instance so any DeCap algorithm
    //can be used as long as it implements the IDECAPAlgorithm
    //interface.
    public static IServicePipeMsgListener createDeCapPipeMsgListener(
        IDECAPAlgorithm algorithm){
        return new DeCapPipeMsgListener(algorithm);
    }
    
    //This static method returns a DistrES pipe message listener
    //This object is returned as an interface, consequently any
    //DistrES listener can be used as long as it implements the 
    //IServicePipeMsgListener interface. 
    public static IServicePipeMsgListener createDistrESPipeMsgListener(
	IDistrESAlgorithm distres){
        return new DistrESPipeMsgListener(distres);
    }
    
    //This static method returns a SISM pipe message listener
    //This object is returned as an interface, consequently any
    //SISM listener can be used as long as it implements the 
    //IServicePipeMsgListener interface. This method takes two
    //parameters, which are the abstract matcher algorithm  
    //and the concrete matcher algorithm. These parameters are 
    //interface instances so any abstract or concrete matcher 
    //algorithm can be used as long as they implement the 
    //IAbstractMatcher and IConcreteMatcher interfaces.
    public static IServicePipeMsgListener createSISMPipeMsgListener(
        IAbstractMatcher abstractMatcher,
        IConcreteMatcher concreteMatcher){
        return new SISMPipeMsgListener(
            abstractMatcher,
            concreteMatcher);
    }
    
    //This static method returns an audio pipe message listener
    //This object is returned as an interface, consequently any
    //audio listener can be used as long as it implements the 
    //IServicePipeMsgListener interface. This method takes one
    //parameter, which is the port address used to recieve
    //audio data.
    public static IServicePipeMsgListener createAudioPipeMsgListener(
        String port){
        return new AudioPipeMsgListener(port);
    }
    
    public static IServicePipeMsgListener createVideoPipeMsgListener(
        String port){
            return new VideoPipeMsgListener(port);
    }
    
    //This static method returns an player pipe message listener
    //This object is returned as an interface, consequently any
    //player listener can be used as long as it implements the 
    //IServicePipeMsgListener interface. This method takes six
    //parameters - the first and second parameters are the pipe
    //advertisements used to connect to audio and video services.
    //The third is the filename of the multimedia content and the 
    //forth and fifth are the broadcast ip and port address. The 
    //last parameter is the P2P interface. 
    public static IServicePipeMsgListener createPlayerPipeMsgListener(
        String audioPipe,
        String videoPipe,
        String filename,
        String ipAddress,
        String port,
        IDiSUS disus){
        return new MediaTransmitterPipeMsgListener(
            audioPipe, 
            videoPipe,
            filename, 
            ipAddress, 
            port,
            disus);
    }
    
    //This static method returns an media center pipe message 
    //listener. This object is returned as an interface, 
    //consequently any media center UI listener can be used as 
    //long as it implements the IServicePipeMsgListener interface. 
    //This method takes one parameter, which is the JMediaCenter
    //inteface object.
    public static IServicePipeMsgListener 
        createMediaCenterUIPipeMsgListener(JMediaCenter jmc){ 
        return new UIPipeListener(jmc);
    }
}
