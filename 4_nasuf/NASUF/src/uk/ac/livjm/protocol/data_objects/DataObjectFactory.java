/*
 * Class Title:         DataObjectFactory
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */

package NASUF.src.uk.ac.livjm.protocol.data_objects;

//NASUF API imports
import NASUF.src.uk.ac.livjm.protocol.data_objects.impl.DependencyServiceDataObject;
import NASUF.src.uk.ac.livjm.protocol.data_objects.impl.PrimaryServiceDataObject;
import NASUF.src.uk.ac.livjm.protocol.data_objects.peer_service.impl.PeerServiceDataObject;

public class DataObjectFactory {
    
    //This method returns a dependency service data object 
    //of type IDataObject. Because this is an interface any
    //data object can be returned as long as it implements the 
    //IDataObject interface.
    public static IDataObject createDepencencyServiceDataObject(
	String decapValue, String primaryService, 
	String name, String moduleSpec){
	return new DependencyServiceDataObject(
		    decapValue,
		    primaryService,
		    name,
		    moduleSpec);
    }
    
    //This method returns a primary service data object 
    //of type IDataObject. Because this is an interface any
    //data object can be returned as long as it implements the 
    //IDataObject interface.
    public static IDataObject createPrimaryServiceDataObject(
	String decapValue, String moduleSpec){
	return new PrimaryServiceDataObject(
		    decapValue,
		    moduleSpec);
    }
    
    //This method returns a peer service data object
    //of type IDataObject. Because this is an interface any
    //data obejct can be returned as long as it implements
    //the IDataObject interface.
    public static IDataObject createPeerServiceDataObject(){
	return new PeerServiceDataObject();
    }
}