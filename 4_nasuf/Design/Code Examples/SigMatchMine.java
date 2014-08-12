/*
 * Class Title:         Signature Matching using RDF and Inference Rules
 * Class Description:   This simple example illustrates how candiate 
 *                      signatures can be matched against required
 *                      signatures using RDF and Inference Rules
 * Author:              Paul Fergus
 * Project:             PhD Research
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Date:                23-08-04
 * Email:               cmppferg@livjm.ac.uk
 * Achnowledgements:    N/A
 */

package Documentation.SignatureMatching;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.vocabulary.RDF;

public class SigMatchMine {
    
    public SigMatchMine() {
    }
    
    //This method requires on parameter, which is the candiate signature. This signature is
    //concatenated with the required signature paramter names and associated data types 
    //including the rule that checks if a candidate signature mathes a required signature.
    public boolean match(String signature){
        //This is a metadata description of the parameter names and their associated data types.
        //Candiate signatures must match on the parameter name and the parameter type.
        String param_dataType = 
        "-> (http://www.livjm.ac.uk/Signame:Name rdf:type http://www.livjm.ac.uk/Sigtype:String). " +
        "-> (http://www.livjm.ac.uk/Signame:Age rdf:type http://www.livjm.ac.uk/Sigtype:Integer). " +
        "-> (http://www.livjm.ac.uk/Signame:Message rdf:type http://www.livjm.ac.uk/Sigtype:String). ";
       
        //This is a simple rule that is used to determine if the candidate signature passed into
        //this method as a parameter matches. If a match is found then a triple is added to the 
        //ontlogy model, which indicates a match has been found, i.e.
        //http://www.livjm.ac.uk/SigParam:Match rdf:type http://www.livjm.ac.uk/Sigtype:True.
        
        //This rule is a first attempt and it is more than likely that it can be further simplified
        String rule = "[rule1: " +
        "   (?name rdf:type ?Name) " +
        "   (?name rdf:type http://www.livjm.ac.uk/Signame:Name)," +
        "   (?name rdf:type http://www.livjm.ac.uk/Sigtype:String), " +
        "   (?age rdf:type ?Age), " +
        "   (?age rdf:type http://www.livjm.ac.uk/Signame:Age)," +
        "   (?age rdf:type http://www.livjm.ac.uk/Sigtype:Integer), " +
        "   (?message rdf:type ?Message), " +
        "   (?message rdf:type http://www.livjm.ac.uk/Signame:Message)," +
        "   (?message rdf:type http://www.livjm.ac.uk/Sigtype:String)" +
        "   -> (http://www.livjm.ac.uk/SigParam:Match rdf:type http://www.livjm.ac.uk/Sigtype:True)] "; 
        
        return executeMatchEngine(param_dataType + signature + rule);
    }
    
    //This method creates the reasoner and executes expressions to see if candidate signatures
    //match required signatures. 
    private boolean executeMatchEngine(String expression){
        Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(expression));
        InfModel inf = ModelFactory.createInfModel(reasoner, 
            ModelFactory.createDefaultModel());
        if(inf.contains(inf.getResource("http://www.livjm.ac.uk/SigParam:Match"),
            RDF.type, 
            inf.getResource("http://www.livjm.ac.uk/Sigtype:True"))){
            return true;
        }else{
            return false;
        }
    }
    
    public static void main(String[] args) {
        SigMatchMine smm = new SigMatchMine();
        
        //This example indicates a correct match. All the parameter names and their associated 
        //data types match.
        String correctSignature = "" +
        "-> (http://www.livjm.ac.uk/sigParam:nameValue rdf:type http://www.livjm.ac.uk/Signame:Name). " +
        "-> (http://www.livjm.ac.uk/sigParam:nameValue rdf:type http://www.livjm.ac.uk/Sigtype:String). " +
        "-> (http://www.livjm.ac.uk/sigParam:ageValue rdf:type http://www.livjm.ac.uk/Signame:Age). " +
        "-> (http://www.livjm.ac.uk/sigParam:ageValue rdf:type http://www.livjm.ac.uk/Sigtype:Integer). " +
        "-> (http://www.livjm.ac.uk/sigParam:messageValue rdf:type http://www.livjm.ac.uk/Signame:Message). " +
        "-> (http://www.livjm.ac.uk/sigParam:messageValue rdf:type http://www.livjm.ac.uk/Sigtype:String). ";
        System.out.println("Match result: " + smm.match(correctSignature));
        
        //This example illustrates an incorrect signature. In this instance I have change the required
        //Name data type from String to Integer. When this is run the matching algorithm returns false.
        //You can play around with any of these parameters and you will see that various combinations will
        //fail
        String incorrectSignature = "" +
        "-> (http://www.livjm.ac.uk/sigParam:nameValue rdf:type http://www.livjm.ac.uk/Signame:Name). " +
        "-> (http://www.livjm.ac.uk/sigParam:nameValue rdf:type http://www.livjm.ac.uk/Sigtype:Integer). " +
        "-> (http://www.livjm.ac.uk/sigParam:ageValue rdf:type http://www.livjm.ac.uk/Signame:Age). " +
        "-> (http://www.livjm.ac.uk/sigParam:ageValue rdf:type http://www.livjm.ac.uk/Sigtype:Integer). " +
        "-> (http://www.livjm.ac.uk/sigParam:messageValue rdf:type http://www.livjm.ac.uk/Signame:Message). " +
        "-> (http://www.livjm.ac.uk/sigParam:messageValue rdf:type http://www.livjm.ac.uk/Sigtype:String). ";
        System.out.println("Match result: " + smm.match(incorrectSignature));
    }
}
