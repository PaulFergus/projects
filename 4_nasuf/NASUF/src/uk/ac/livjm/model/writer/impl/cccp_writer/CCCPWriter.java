/*
 * Class Title:         CCCPWriter
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.model.writer.impl.cccp_writer;

//Java SDK imports
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;

//Jena API imports
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFSyntax;

//NASUF API imports
import NASUF.src.uk.ac.livjm.constant.DECAPConstants;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.model.dcm.IDCMProperty;
import NASUF.src.uk.ac.livjm.model.dcm.IDCMStatement;
import NASUF.src.uk.ac.livjm.model.writer.AbstractWriter;

//Log4j API imports
import org.apache.log4j.Level;

public class CCCPWriter extends AbstractWriter{
    
    //Default constructor.
    public CCCPWriter() {
	//Create new default model
        model = ModelFactory.createDefaultModel();
	//set version number.
        version = "1.0";
    }

    //This method overrides the write method in the AbstractWriter
    //class. It takes one parameter which is an output stream.
    public void write(OutputStream out) throws IOException {
        write(new OutputStreamWriter(out, "UTF8"));
    }
    
    //This constructor takes one parameter, which is a writer object
    //which will be used to output the model.
    public void write(Writer out){
        try{
	    //Set the model namespace prefixes.
            model.setNsPrefix("ccpp", DECAPConstants.CCPPSCHEMA20021108_NAMESPACE);
            model.setNsPrefix("dcm", DECAPConstants.DECAPNAMESPACE);
	    //Get an RDF Writer.
            RDFWriter writer = model.getWriter();
	    
	    //Set the writer properties.
            writer.setProperty("allowBadURIs", "true");
            writer.setProperty("blockRules", new Resource[] {RDFSyntax.propertyAttr});
            writer.setProperty("showXmlDeclaration", "true");
	    //Write out the model.
            writer.write(model, out, "");
        }catch(Exception e){
	    if(NASUFLogger.isEnabledFor(Level.ERROR))
		NASUFLogger.error("CCCPWriter: write: " + 
		    e.toString());
        }
    }
    
    //This method adds a statement to the model. This method takes 
    //one parameter whichi is a statement object. 
    public void addStatement(IDCMStatement statement){
	//Iterate through each of the properties 
        Iterator iter = statement.getProperties().iterator();
        while(iter.hasNext()){
	    //Get the next property.
            IDCMProperty p = (IDCMProperty)iter.next();
	    //Add the statement to the model.
            addStatement(statement.getResource(), p.getProperty(), p.getValue());
        }
    }
}