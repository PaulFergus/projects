/*
 * Class Title:         IDiSUS
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

//Java SDK imports
import java.util.List;

//JXTA API imports
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.protocol.ResolverQueryMsg;
import net.jxta.protocol.ResolverResponseMsg;
import net.jxta.resolver.QueryHandler;
import net.jxta.util.JxtaBiDiPipe;

//NASUF API imports
import NASUF.src.uk.ac.livjm.service.abstract_service.IAbstractService;

public interface IDiSUS {
    public void addApplicationPeerService(IAbstractService service);
    public void addCoreService(IAbstractService service);
    public JxtaBiDiPipe bindToService(String moduleSpec, PipeMsgListener listener);
    public void changePeerGroup(PeerGroup peerGroup);
    public void cleanUp();
    public void discoverApplicationPeerService(int id, String dcm, String pscm);
    public void discoveryApplicationPeerService(ResolverQueryMsg msg);
    public ModuleSpecAdvertisement discoverCoreService(String serviceName);
    public List getApplicationPeerServices();
    public List getCoreServices();
    public List getDependentServices();
    public PeerGroup getPeerGroup();
    public String getDCM();
    public void registerService(String serviceName);
    public void removeAllApplicationPeerServices();
    public void removeAllCoreServices();
    public void removeApplicationPeerService(IAbstractService service);
    public void removeCoreService(IAbstractService service);
    public void sendCommand(String command, JxtaBiDiPipe pipe);
    public void sendResolverQuery(String id, ResolverQueryMsg msg);
    public void sendResolverResponse(String source, ResolverResponseMsg msg);
    public void setDECAPResult(ResolverResponseMsg result);
    public void startCoreServices();
    public void startJxta();
    public void startJxta(PeerGroup peerGroup);
    public void startJxta(QueryHandler handler);
    public void startServices();
    public void stopManager();
    public void unregisterService(String serviceName);
}