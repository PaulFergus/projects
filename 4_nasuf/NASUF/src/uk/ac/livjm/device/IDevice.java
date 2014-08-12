/*
 * Class Title:         IDevice
 * Class Description:   Need to do
 * Author:              Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Team Members:        Paul Fergus
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               cmppferg@livjm.ac.uk
 * Achnowledgements:    
 */
package NASUF.src.uk.ac.livjm.device;

//Java SDK imports
import java.util.List;

//JXTA API imports
import net.jxta.pipe.PipeMsgListener;
import net.jxta.protocol.ResolverResponseMsg;
import net.jxta.protocol.ResolverQueryMsg;
import net.jxta.util.JxtaBiDiPipe;

//NASUF API imports
import NASUF.src.uk.ac.livjm.service.abstract_service.IAbstractService;

public interface IDevice {
    public void addApplicationPeerServices();
    public void addCoreServices();
    public JxtaBiDiPipe bindToService(String moduleSpecAdv, PipeMsgListener listener);
    public void cleanUp();
    public void discoverApplicationPeerService(ResolverQueryMsg msg);
    public void discoverApplicationPeerService(int id, String dcm, String pscm);
    public void discoverDependentServices();
    public String getDCM();
    public List getDependentServices();
    public void registerService(String serviceName);
    public void setApplicationPeerServiceFound(ResolverResponseMsg value);
    public void sendCommand(String command, JxtaBiDiPipe pipe);
    public void sendServiceRegistrationMsg(IAbstractService service);
    public void sendServiceUnregisteringMsg(IAbstractService service);
    public void stopDevice();
    public void unregisterService(String serviceName);
}
