/*
 * Class Title:         MAUTAlgorithm
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.algorithm.decap.impl;

//Java SDK imports
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Jena API imports
import com.hp.hpl.jena.rdf.model.Model;

//JXTA API imports
import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;

//NASUF API imports.
import NASUF.src.uk.ac.livjm.algorithm.Algorithm;
import NASUF.src.uk.ac.livjm.algorithm.decap.IDECAPAlgorithm;
import NASUF.src.uk.ac.livjm.constant.DECAPConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceDescriptionConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceRequestConstants;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.model.query.QueryFactory;
import NASUF.src.uk.ac.livjm.model.rdf.RDFFactory;
import NASUF.src.uk.ac.livjm.utils.XMLUtility;

//Log4J API imports
import org.apache.log4j.Level;

public class MAUTAlgorithm extends Algorithm implements IDECAPAlgorithm{
   
    //Default constructor. This constructor takes three paramters -
    //the first parameter is the algorithm creators name, the second
    //is the algrithm name and the third is the version number.
    public MAUTAlgorithm(
            String creator,
            String algorithmName,
            String version){
        setCreator(creator);
        setName(algorithmName);
        setVersion(version);
    }
    
    //Capability mathing is based on calculating the combined fittness
    //values of the qos attribute that describe the capabilities the device
    //supports, such as bandwidth, processor, software and hardware
    //capabilities. The fisrt parameters is the devices capability model
    //the second is the capability model described in the service 
    //request, this third is the importance rating value and the 
    //fourth is the status rating value.
    private float calculateAttributes(
            Model deviceModel,
            Model clientModel,
            String importanceRating,
            String statusRating){
        List result = null;
        List params = new ArrayList();
        params.add("x");
        
        //Get Importance Rating. Rember it is the client that decides what
        //attributes are most important, consequently the importance rating
        //in the capability model extracted from the servcie request is used.
        result = RDFFactory.executeQuery(
            clientModel,
            importanceRating,
            params);
        
        Integer impRating = null;
        if(result != null)
            impRating = Integer
		.valueOf((String) 
		    result.iterator().next());
     
        //Get Status Rating
        result = RDFFactory.executeQuery(
             deviceModel,
             statusRating,
             params);
        
        Integer statRating = null;
        if(result != null)
            statRating = Integer
		.valueOf((String) 
		    result.iterator().next());
        
        //Mutiply the importance rating obtained from the service request
        //with the status rating extracted from the devices capability 
        //model.
        if(impRating.intValue() > 0 && statRating.intValue() > 0)
            return impRating.intValue() * statRating.intValue();
        else
            return 0;
    }
    
    //This model calculates the MAUT Score. It accepts three parameters
    //the first parameter is a list of attributes used to describe the 
    //quality of service parameters used to describe the devices 
    //capability model. The second parameter is is the capability model 
    //used by the device and the third parameter is the 
    //device capability model extracted from the service request.
    private float calculateMAUTScore(
        List params,
        Object deviceModel, 
        Object clientModel) {
        //ImportanceRating * StatusRating
        float result = 0;
        Iterator iter = params.iterator();
        //Iterate through all the QoS parameters and calculate the qos
        //score for each and add the returned value to the cumulative
        //result.
        while(iter.hasNext()){
            String param = (String)iter.next();
            result = result + calculateAttributes(
                    (Model)deviceModel,
                    (Model)clientModel,
                    QueryFactory.createDCMQuery(param, "importanceRating"),
                    QueryFactory.createDCMQuery(param, "statusRating"));
        }
        //Return the result to the sender.
        return result;
    }
    
    //This method calls the methods described above and determines whether
    //the device that provides the service is capible of effectively executing
    //the service based on the qos parameters described in the service 
    //request device capability model.
    public Object checkCapability(
            Object params,
            Object deviceDECAP, 
            Object clientDECAP) {
        try{
            //Calculate the devices capabiltiy score.
            float deviceMAUTScore = 
                calculateMAUTScore(
                    (List)params, 
                    //Convert the string representation of the device 
                    //capability description into an RDF model.
                    RDFFactory.createRDFModel(XMLUtility
                            .replaceAllXMLEscapeCharacters((String)deviceDECAP)),
                    RDFFactory.createRDFModel(
                        XMLUtility
                            .replaceAllXMLEscapeCharacters((String)clientDECAP)));

            //Calculate the service request capability score.
            float clientMAUTScore =
                calculateMAUTScore(
                    (List)params, 
                    RDFFactory.createRDFModel(XMLUtility
                        .replaceAllXMLEscapeCharacters((String)clientDECAP)),
                    RDFFactory.createRDFModel(XMLUtility
                        .replaceAllXMLEscapeCharacters((String)clientDECAP)));
        
            //Create the return message.
            Message msg = new Message();
        
            //Add the MAUTResult tag to the message.
            msg.addMessageElement(
                    ServiceDescriptionConstants.NASUF_NAMESPACE,
                        new StringMessageElement(
                            ServiceRequestConstants.REQUEST_TYPE,
                            "MAUTResult", null));
            //Add the Service Request MAUT score.
            msg.addMessageElement(
                ServiceDescriptionConstants.NASUF_NAMESPACE, 
                    new StringMessageElement(
                        DECAPConstants.SERVICE_REQUEST_DECAP_TAG,
                        Float.toString(clientMAUTScore), null));
            //Add the device MAUT score
            msg.addMessageElement(
                ServiceDescriptionConstants.NASUF_NAMESPACE, 
                    new StringMessageElement(
                        DECAPConstants.DEVICE_DECAP_TAG, 
                        Float.toString(deviceMAUTScore), null));
            //Add whether the device has passed or failed the device 
            //capability evaluation.
            msg.addMessageElement(
                ServiceDescriptionConstants.NASUF_NAMESPACE, 
                    new StringMessageElement(
                        DECAPConstants.MAUT_RESULT, 
                        String.valueOf(deviceMAUTScore >= clientMAUTScore), null)); 
            //Return the message to the caller.
            return msg;
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("checkCapability Error: " 
		    + e.toString());
         
            //If an exception occurs, return null.
            return null;
        }
    }
    
    //Each device must implement the cleanUp method which 
    //must ensure that all the objects used by the device
    //are correctly distroyed.
    public void cleanUp(){
        super.cleanUp();
    }
}