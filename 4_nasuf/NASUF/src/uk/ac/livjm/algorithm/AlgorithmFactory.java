/*
 * Class Title:         AlgorithmFactory
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.algorithm;

//NASUF API imports
import NASUF.src.uk.ac.livjm.algorithm.decap.IDECAPAlgorithm;
import NASUF.src.uk.ac.livjm.algorithm.decap.impl.MAUTAlgorithm;
import NASUF.src.uk.ac.livjm.algorithm.distres.ee.IEE;
import NASUF.src.uk.ac.livjm.algorithm.distres.me.IME;
import NASUF.src.uk.ac.livjm.algorithm.distres.ee.impl.ExtractionEngine;
import NASUF.src.uk.ac.livjm.algorithm.distres.me.impl.ME;
import NASUF.src.uk.ac.livjm.algorithm.distres.epe.IEPE;
import NASUF.src.uk.ac.livjm.algorithm.distres.epe.impl.EPE;
import NASUF.src.uk.ac.livjm.algorithm.distres.IDistrESAlgorithm;
import NASUF.src.uk.ac.livjm.algorithm.distres.impl.DistrESAlgorithm;
import NASUF.src.uk.ac.livjm.algorithm.sism.abstract_matcher.IAbstractMatcher;
import NASUF.src.uk.ac.livjm.algorithm.sism.abstract_matcher.impl.AbstractMatcher;
import NASUF.src.uk.ac.livjm.algorithm.sism.concrete_matcher.IConcreteMatcher;
import NASUF.src.uk.ac.livjm.algorithm.sism.concrete_matcher.impl.ConcreteMatcher;
import NASUF.src.uk.ac.livjm.p2p.IDiSUS;

//This is the Algorithm Factory which used the Fatory and Factory Method 
//Pattern.
public abstract class AlgorithmFactory {
    
    //This method returns a new instance of the IDECAPAlgorithm.
    //This is an inteface, consequently any algorithm can 
    //be used as long as the IDECAPAlgorithm interface is 
    //implemented.
    public static IDECAPAlgorithm createDECAPAlgorithm(){
        return new MAUTAlgorithm(
                "Paul Fergus",
                "MAUTAlgorithm",
                "1.0");
    }
    
    public static IDistrESAlgorithm createDistrESAlgorithm(){
	return new DistrESAlgorithm();
    }
    
    public static IEE createExtractionEngine(Object reasoner, Object model){
	return new ExtractionEngine(reasoner, model);
    }
    
    public static IEPE createEvolutionaryPatternExtractor(Object model){
	return new EPE(model);
    }
    
    public static IME createMergeEngine(){
	return new ME();
    }
    
    //This method returns a new instance of the IAbstractMatcher.
    //Again this is an inteface, consequently any matcher 
    //algorithm can be used as long as the IAbstractMatcher 
    //interface is implemented. 
    public static IAbstractMatcher createAbstractMatcher(IDiSUS disus){
        return new AbstractMatcher(disus);
    }
    
    //This method returns a new instance of the IConcreteMatcher
    //Again this is an interface, consequently any concrete matcher
    //algorithm can be used as long as the IConcreteMatcher 
    //inteface is implemented.
    public static IConcreteMatcher createConcreteMatcher(){
        return new ConcreteMatcher();
    }
}