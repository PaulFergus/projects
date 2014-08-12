/*
 * Class Title:         PeerServiceDataObject
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */

package NASUF.src.uk.ac.livjm.protocol.data_objects.peer_service.impl;

//Java API imports
import java.io.InputStream;

//Jxta API imports
import net.jxta.document.Element;

//NASUF API imports
import NASUF.src.uk.ac.livjm.protocol.data_objects.AbstractDataObject;

public class PeerServiceDataObject extends AbstractDataObject{
    
    //This varaibles contain the data required to 
    //publish a service within NASUF.
    private String moduleClass;
    private String moduleSpec;
    private String version;
    private String creator;
    private InputStream pipeAdvIS;
    private String implCode;
    private Element implCompat;
    private String implDescription;
    private String implProvider;
    
    //Default constructor.
    public PeerServiceDataObject(){
    }
    
    public void setModuleClass(String mod){
	this.moduleClass = mod;
    }
    
    public String getModuleClass(){
	return this.moduleClass;
    }
    
    public void setModuleSpec(String mod){
	this.moduleSpec = mod;
    }
    
    public String getModuleSpec(){
	return this.moduleSpec;
    }
    
    public void setVersion(String version){
	this.version = version;
    }
    
    public String getVersion(){
	return this.version;
    }
    
    public void setCreator(String creator){
	this.creator = creator;
    }
    
    public String getCreator(){
	return this.creator;
    }
    
    public void setPipeAdvInputStream(InputStream is){
	this.pipeAdvIS = is;
    }
    
    public InputStream getPipeAdvInputStream(){
	return this.pipeAdvIS;
    }
    
    public void setImplCode(String implCode){
	this.implCode = implCode;
    }
    
    public String getImplCode(){
	return this.implCode;
    }
    
    public void setCompat(Element compat){
	this.implCompat = compat;
    }
    
    public Element getImplCompat(){
	return this.implCompat;
    }
    
    public void setImplDescription(String description){
	this.implDescription = description;
    }
    
    public String getImplDescription(){
	return this.implDescription;
    }
    
    public void setImplProvider(String provider){
	this.implProvider = provider;
    }
    
    public String getImplProvider(){
	return this.implProvider;
    }
}
