/*
 * Class Title:         IAbstractService
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */

package NASUF.src.uk.ac.livjm.service.abstract_service;

//Java SDK imports 
import java.io.InputStream;

//Jxta API imports
import net.jxta.document.Element;
import net.jxta.protocol.ModuleClassAdvertisement;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.protocol.ModuleImplAdvertisement;

public interface IAbstractService {
    public void cleanUp();
    public ModuleClassAdvertisement getModuleClassAdvertisement();
    public ModuleSpecAdvertisement getModuleSpecAdvertisement();
    public ModuleImplAdvertisement getModuleImplAdvertisement();
    public Object getServiceDescription();
    public void publishService(Object service);
    public void removeService(Object service);
    public void startService();
    public void stopService(); 
}