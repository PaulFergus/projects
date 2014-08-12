/*
 * Class Title:         OWLSWriter
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.model.writer.impl.owls_writer;

//Java SDK imports
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.util.List;

//OWL-S API imports
import org.mindswap.owl.vocabulary.RDF;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.owls.vocabulary.OWLS_1_0;

//Jena API imports
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFSyntax;

//NASUF API imports 
import NASUF.src.uk.ac.livjm.model.owls.iope.Effect;
import NASUF.src.uk.ac.livjm.model.owls.iope.Precondition;
import NASUF.src.uk.ac.livjm.model.writer.AbstractWriter;

public class OWLSWriter extends AbstractWriter {
   
    //This creates a 1.0 compliant instanc of OWLS
    private OWLS_1_0 OWLS = OWLS_1_0.instance;

    //Default constructor
    public OWLSWriter() {
	//Create new default model
	model = ModelFactory.createDefaultModel();
	//set version number.
        version = "1.0";
    }

    //This method overrides the write method in the AbstractWriter
    //class. It takes two parameters - the first parameter is the 
    //owls profile and the second is the outputstream.
    public void write(Profile profile, OutputStream out) throws IOException {
        write(profile, new OutputStreamWriter(out, "UTF8"));
    }
        
    //This method overrides the write method in the AbstractWriter
    //class. It takes two parameters - the first paramete is the profile
    //to be writen and the second is the writer used to write the model.
    public void write(Profile profile, Writer out) {
        //Set the model namespace prefixes.
        model.setNsPrefix("profile", OWLS.Profile.URI);
        model.setNsPrefix("xsd", org.mindswap.owl.vocabulary.XSD.ns);
        model.setNsPrefix("process", OWLS.Process.URI);
        model.setNsPrefix("base", "http://www.livjm.ac.uk/Services#");
	//Get an RDF Writer.	
        RDFWriter writer = model.getWriter("RDF/XML-ABBREV");
	 //Set the writer properties.
        writer.setProperty("allowBadURIs", "true");
        writer.setProperty("blockRules", new Resource[] {RDFSyntax.propertyAttr});
        writer.setProperty("showXmlDeclaration", "true");
        
	//Write the provile and Process IOPEs to the model
        writeProfile(profile);    
        writeProcessIOPEs(profile);
       	//Write out the model.
        writer.write(model, out, "");
    }
    
    //This method writes the profile to the model.
    private void writeProfile(Profile profile) {
	//Get the profile type.
        URI profileType = profile.getType();
	
	//Iterate through all supported versions.
        for(int i = 0; i < OWLSFactory.supportedVersions.length; i++) {
	    //Get the version
            String version = OWLSFactory.supportedVersions[i];
	    //Get the vocabulary.
            OWLS vocabulary = OWLSFactory.getVocabulary(version);
	    //If profile type equals vocabulary.getProfile().Profile
	    //then assign profileType OWLS.Profile.Profile.
            if(profileType.equals(vocabulary.getProfile().Profile)) {
                profileType = OWLS.Profile.Profile;
                break;
            }
        }	
        //Add profile to the model.
        addStatement(profile, RDF.type, profileType);
	//Add all the IOPEs to the profile.
        writeProfileParams(profile, profile.getInputs());
        writeProfileParams(profile, profile.getOutputs());
        writeProfileParams(profile, profile.getPreconditions());
        writeProfileParams(profile, profile.getEffects());
    }

    //This method adds all the IOPEs to the profile model.
    private void writeProfileParams(Profile profile, List params) {
	//Loop through the IOPE collection.
        for(int i = 0; i < params.size(); i++) {
	    //Get the next parameter.
            Parameter param = (Parameter)params.get(i);
      
	    //If param is an instance of Inptu then add the hasInput
	    //statement to the model.
            if(param instanceof Input)
                addStatement(profile, OWLS.Profile.hasInput, param);			
	    //If param is an instance of Output then add the hasOutput
	    //statement to the model.
            else if(param instanceof Output)
                addStatement(profile, OWLS.Profile.hasOutput, param);
	    //If param is an instance of Precondition then add the 
	    //hasPrecondition statement to the model.
            else if(param instanceof Precondition)
                addStatement(profile, OWLS.Profile.hasPrecondition, param);
	    //If param is an instance of Effect then add the hasEffect
	    //statement to the model.
            else if(param instanceof Effect)
                addStatement(profile, OWLS.Profile.hasEffect, param);
        }
    }

    //This method adds the process IOPEs to the profile 
    private void writeProcessIOPEs(Profile profile) {
        writeProcessParams(profile, profile.getInputs());	
        writeProcessParams(profile, profile.getOutputs());
        writeProcessParams(profile, profile.getPreconditions());
        writeProcessParams(profile, profile.getEffects());
    }

    //This method adds the Process IOPE to the profile.
    private void writeProcessParams(Profile profile, List params) {
	//Iterate through the IOPE collection
        for(int i = 0; i < params.size(); i++) {
	    //Get the next IOPE
            Parameter param = (Parameter)params.get(i);
            //If the param is an instance of Input then add the 
	    //OWLS Process Input to the model.
            if(param instanceof Input) {
                addStatement(param, RDF.type, OWLS.Process.Input);
	    //If the param is an instance of Output then add the 
	    //OWLS Process Output to the model
            }else if(param instanceof Output){
                addStatement(param, RDF.type, OWLS.Process.Output);
	    //If the param is an instace of Precondition then add the 
	    //OWLS Process Precondition to the model
            }else if(param instanceof Precondition){
                addStatement(param, RDF.type, OWLS.Process.Precondition);
	    //If the param is an instance of Effect then add the 
	    //OWLS Process Effect to the model
            }else if(param instanceof Effect){
                addStatement(param, RDF.type, OWLS.Process.Effect);
            }           
	    //Add the parameter type to the model.
            addStatement(param, OWLS.Process.parameterType, param.getType());
        }
    }
}