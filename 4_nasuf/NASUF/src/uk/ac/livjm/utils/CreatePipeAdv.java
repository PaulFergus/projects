/*
 * Class Title:         CreatePipeAdv
 * Class Description:   Need to do
 * Author:              Sun Microsystems
 * Project:             Wireless PnP Home Network
 * Team Members:        Paul Fergus
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Date:                20-08-03
 * Email:               cmppferg@livjm.ac.uk
 * Achnowledgements:    
 */
package NASUF.src.uk.ac.livjm.utils;

//Java SDK imports
import java.io.InputStream;

//JXTA API imports
import net.jxta.document.MimeMediaType;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.id.IDFactory;
import net.jxta.pipe.PipeService;
import net.jxta.util.JxtaServerPipe;
import net.jxta.peer.PeerID;

//NASUF API imports
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;

//Log4J API imports
import org.apache.log4j.Level;

public class CreatePipeAdv {
   
    // a simple helper function that will create a pipe advertisement
    // for the input pipe that we are going to open up
    public static PipeAdvertisement createPipeAdvertisement(
            PeerGroup peerGroup,
            String name) {
        try {
            // create the pipe advertisement object
            PipeAdvertisement pipeAdvertisement 
                = (PipeAdvertisement)AdvertisementFactory
                    .newAdvertisement(
                        PipeAdvertisement
                            .getAdvertisementType() );
            pipeAdvertisement
                .setPipeID(
                    IDFactory
                        .newPipeID(
                            peerGroup
                                .getPeerGroupID()));
            pipeAdvertisement
                .setType(PipeService.UnicastType);

            // create a simple name for the pipe that we can do
            // an easy lookup on when we are searching later on
            pipeAdvertisement.setName(name + ": " + peerGroup.getPeerID());
            
            return pipeAdvertisement;

        }catch(Exception e) {
            if(NASUFLogger.isEnabledFor(Level.ERROR)){
                NASUFLogger.error( "problem creating the pipe advertisement" );
            }
            return null;
        }
    }

    public static PipeAdvertisement readPipeAdv(
            InputStream is){
        PipeAdvertisement pipeadv = null;
        try{
            pipeadv = (PipeAdvertisement)
                AdvertisementFactory.newAdvertisement(
                    MimeMediaType.XMLUTF8, is);
            is.close();
            return pipeadv;
        }catch (Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR)){
                NASUFLogger.error(e.toString());
            }
            return null;
        }
    }

    public static void main(String[] args) {
        try{
            PeerGroup group = 
                net.jxta.peergroup.PeerGroupFactory.newNetPeerGroup();
            
            PipeAdvertisement pipe 
                = createPipeAdvertisement(
                    group,
                    "VideoRecieverService");
            System.out.println(pipe.toString());
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
    }
}
