/*
 * Class Title:         DeviceDCM
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

//NASUF API imports
import NASUF.src.uk.ac.livjm.model.dcm.IDCM;
import NASUF.src.uk.ac.livjm.model.dcm.DCMFactory;
import NASUF.src.uk.ac.livjm.model.dcm.impl.Attribute;
import NASUF.src.uk.ac.livjm.model.dcm.impl.DefaultAttribute;

public class DeviceDCM {

    public static String createDCM(){
        IDCM dcm = DCMFactory.createDCMProfile();
        dcm.setProfileName("MyProfile");
        dcm.addComponent("Memory");
        dcm.addComponent("Power");
        dcm.addComponent("CPU");
        dcm.addComponent("Bandwidth");
        
        dcm.addAttribute(
            new Attribute("Power", "power", "HardwarePlatform"));
        dcm.addAttribute(
            new Attribute("CPU", "cpu_load", "HardwarePlatform"));
        dcm.addAttribute(
            new Attribute("Memory", "memory", "HardwarePlatform"));
        dcm.addAttribute(
            new Attribute("Bandwidth", "cpu_load", "HardwarePlatform"));
        
        dcm.addDefaultAttribute(
            new DefaultAttribute("power","importanceRating", "40"));
        dcm.addDefaultAttribute(
            new DefaultAttribute("power", "statusAssessment", "Exceptional"));
        dcm.addDefaultAttribute(
            new DefaultAttribute("power", "statusRating", "100"));
        dcm.addDefaultAttribute(
            new DefaultAttribute("power", "importanceRanking", "4"));
       
        dcm.addDefaultAttribute(
            new DefaultAttribute("cpu_load","importanceRating", "40"));
        dcm.addDefaultAttribute(
            new DefaultAttribute("cpu_load", "statusAssessment", "Exceptional"));
        dcm.addDefaultAttribute(
            new DefaultAttribute("cpu_load", "statusRating", "100"));
        dcm.addDefaultAttribute(
            new DefaultAttribute("cpu_load", "importanceRanking", "4"));
        
        dcm.addDefaultAttribute(
            new DefaultAttribute("memory","importanceRating", "40"));
        dcm.addDefaultAttribute(
            new DefaultAttribute("memory", "statusAssessment", "Exceptional"));
        dcm.addDefaultAttribute(
            new DefaultAttribute("memory", "statusRating", "100"));
        dcm.addDefaultAttribute(
            new DefaultAttribute("memory", "importanceRanking", "4"));
        
        dcm.addDefaultAttribute(
            new DefaultAttribute("bandwidth","importanceRating", "40"));
        dcm.addDefaultAttribute(
            new DefaultAttribute("bandwidth", "statusAssessment", "Exceptional"));
        dcm.addDefaultAttribute(
            new DefaultAttribute("bandwidth", "statusRating", "100"));
        dcm.addDefaultAttribute(
            new DefaultAttribute("bandwidth", "importanceRanking", "40"));
       
        return dcm.toString();
    }
    
//    public static void main(String[] args){
//        DeviceDCM dcm = new DeviceDCM();
//        System.out.println(dcm.createDCM());
//    }
}
