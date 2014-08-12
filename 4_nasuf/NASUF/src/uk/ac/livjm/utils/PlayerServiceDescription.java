/*
 * Class Title:         PlayerServiceDescription
 * Class Description:   Need to do
 * Author:              Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Team Members:        Paul Fergus
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               cmppferg@livjm.ac.uk
 * Achnowledgements:    
 */
package NASUF.src.uk.ac.livjm.utils;

import java.util.List;
import java.util.ArrayList;

import NASUF.src.uk.ac.livjm.model.psm.PSMFactory;
import NASUF.src.uk.ac.livjm.model.psm.IPSM;
import NASUF.src.uk.ac.livjm.model.owls.iope.impl.IOPE;
import NASUF.src.uk.ac.livjm.constant.ServiceRequestConstants;

public class PlayerServiceDescription{
    
    private IPSM profile = null;
    /** Creates a new instance of Description */
    public PlayerServiceDescription() {
        profile = PSMFactory.createOWLSPCM();
    }
    
    public IPSM getProfile(){
        return profile;
    }
    public String createPSM(){
        
        List inputs = new ArrayList();
        IOPE input = new IOPE();
        input.setName(ServiceRequestConstants.LIVJM_SERVICE_REQUEST_NAMESPACE 
            + "Play");
        input.setType("Input");
        input.setIOPEDataType(com.hp.hpl.jena.vocabulary.XSD.xbyte.toString());
        inputs.add(input);

        List outputs = new ArrayList();
        IOPE output = new IOPE();
        output.setName(ServiceRequestConstants.LIVJM_SERVICE_REQUEST_NAMESPACE
            +"BinaryStream");
        output.setType("Output");
        output.setIOPEDataType(com.hp.hpl.jena.vocabulary.XSD.xbyte.toString());
        outputs.add(output);

        List preconditions = new ArrayList();
        IOPE precondition = new IOPE();
        precondition.setName(ServiceRequestConstants.LIVJM_SERVICE_REQUEST_NAMESPACE 
            + "AudioVisualPlayer");
        precondition.setType("Precondition");
        precondition.setIOPEDataType(
            ServiceRequestConstants.LIVJM_SERVICE_REQUEST_NAMESPACE + "AudioVisualPlayer");
        preconditions.add(precondition);

        List effects = new ArrayList();
        IOPE effect = new IOPE();
        effect.setName(ServiceRequestConstants.LIVJM_SERVICE_REQUEST_NAMESPACE 
            + "AudioVideo");
        effect.setType("Effect");
        effect.setIOPEDataType(
            ServiceRequestConstants.LIVJM_SERVICE_REQUEST_NAMESPACE + "AudioVideo");
        effects.add(effect);
        profile.createProfile(inputs, outputs, preconditions, effects);
        return profile.toString();
    }
    
    public static void main(String[] args){
        PlayerServiceDescription psd = new PlayerServiceDescription();
        System.out.println(psd.createPSM());
    }
}
