/*
 * Class Title:         SISM
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.service.core_service;

//NASUF API imports
import NASUF.src.uk.ac.livjm.algorithm.AlgorithmFactory;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.p2p.IDiSUS;
import NASUF.src.uk.ac.livjm.service.abstract_service.IAbstractService;
import NASUF.src.uk.ac.livjm.service.core_service.decap.impl.DeCap;
import NASUF.src.uk.ac.livjm.service.core_service.distres.impl.DistrES;
import NASUF.src.uk.ac.livjm.service.core_service.sism.impl.SISM;

//Log4J API imports
import org.apache.log4j.Level;

public class CoreServiceFactory{
   
    //This method creates a new DeCap service. The returned
    //value is an IAbstractService, consequently any decap
    //service can be used as long as it implements the 
    //IAbstractService interface.
    public static IAbstractService createDECAPService(){
        try{
	    //Create new DeCap service
            return new DeCap(
		    //Create a new DeCap Algorithm
                    AlgorithmFactory
                        .createDECAPAlgorithm());
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("CoreServiceFactory: createDECAPService: " + 
		    e.toString());
            return null;
        }
    }
    
    //This method creates a new DistrES service. The returned
    //value is an IAbstractService, consequently any distres
    //service can be used as long as it implements the 
    //IAbstractService interface.
    public static IAbstractService createDistrESService(){
        try{
	    //Create new DistrES service.
            return new DistrES(
		AlgorithmFactory.createDistrESAlgorithm());
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("CoreServiceFactory: createDistrESService: " +
		    e.toString());
            return null;
        }
    }
    
    //This method creates a new SISM service. The returned
    //value is an IAbstractService, consequently any sism
    //service can be used as long as it implements the 
    //IAbstractService interface.
    public static IAbstractService createSISMService(IDiSUS disus){
        try{
	    //Create SISM service.
            return new SISM(
		    //Create abstract matcher algorithm
                    AlgorithmFactory.createAbstractMatcher(disus),
		    //Create concrete matcher algorithm
                    AlgorithmFactory.createConcreteMatcher());
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("SISM: createSISMService: " + 
		    e.toString());
            return null;
        }
    }
}