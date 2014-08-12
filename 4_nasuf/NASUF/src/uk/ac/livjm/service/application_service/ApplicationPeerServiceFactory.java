/*
 * Class Title:         ApplicatinPeerServiceFactory
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.service.application_service;

//NASUF API imports
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.p2p.IDiSUS;
import NASUF.src.uk.ac.livjm.service.application_service.media_reciever.impl.audio_reciever.AudioRecieverService;
import NASUF.src.uk.ac.livjm.service.application_service.media_reciever.impl.video_reciever.VideoRecieverService;
import NASUF.src.uk.ac.livjm.service.application_service.media_transmitter.impl.MediaTransmitterService;
import NASUF.src.uk.ac.livjm.service.abstract_service.IAbstractService;

//Log4J API imports
import org.apache.log4j.Level;

public class ApplicationPeerServiceFactory {
    
    //This method creates a new media transmitter service and returns
    //an IApplicationPeerService interface. Because the return value 
    //is an interface any transmitter service can be used as long as
    //it implements the IApplicationPeerService interface. This method
    //takes seven parameters - the first and second parameters are the
    //audio and video pipes used to recieved data. The third is the
    //media file to be transmitted and the forth and fifth are the ip
    //and port address, which comprise the endpoint. The last parameter
    //is the P2P interface.
    public static IAbstractService createMediaTransmitterService(
        String audioPipe,
        String videoPipe,
        String filename,
        String ipAddress,
        String port,
        String sd,
        IDiSUS disus){
        try{
	    //Return a new media transmitter service.
            return new
                MediaTransmitterService(
                    audioPipe, 
                    videoPipe, 
                    filename,
                    ipAddress,
                    port,
                    sd,
                    disus);
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("ApplicationPeerServiceFactory: createMediaTransmitterService: " + 
		    e.toString());
            return null;
        }
    }
    
    //This method creates a new audio reciever service. Because the return 
    //value is an interface any transmitter service can be used as long as
    //it implements the IApplicationPeerService interface. This method
    //takes three parameters - the first parameter is port address the 
    //service uses to recieve audio data and the second is the service
    //type, in this instance an audio service. The last parameter is the 
    //service description.
    public static IAbstractService createAudioRecieverService(
        String port,
        String type,
        String sd){
        try{
            return new AudioRecieverService(
                    port, type, sd);
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("ApplicationPeerServiceFactory: createAudioRecieverService: " + 
		    e.toString());
            return null;
        }
    }
    
    //This method creates a new video reciever service. Because the return 
    //value is an interface any transmitter service can be used as long as
    //it implements the IApplicationPeerService interface. This method
    //takes three parameters - the first parameter is port address the 
    //service uses to recieve video data and the second is the service
    //type, in this instance an video service. The last parameter is the 
    //service description.
    public static IAbstractService createVideoRecieverService(
        String port,
        String type,
        String sd){
        try{
            return new VideoRecieverService(
                    port, type, sd);
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("ApplicationPeerServiceFactory: createVideoRecieverService: " + 
		    e.toString());
            return null;
        }
    }
}