/*
 * Class Title:         RDFFactory
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.model.rdf;

//Java SDK imports
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Jena API imports
import com.hp.hpl.jena.mem.ModelMem;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdql.Query;
import com.hp.hpl.jena.rdql.QueryEngine;
import com.hp.hpl.jena.rdql.ResultBinding;

//NASUF API imports
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;

//Log4j API imports
import org.apache.log4j.Level;

public class RDFFactory{
    
    //This static method creates a new RDF Model. It takes
    //one input, which is the file that contains the model
    public static Model createRDFModel(File inputFile){
        //Create a new model
	Model dcModel = new ModelMem();
       	try{
	    //Create a new input stream for the input file.
            InputStream in = new 
                BufferedInputStream( new 
                    FileInputStream(inputFile));
	    
	    //If the input stream is null exit.
            if(in == null)
                System.exit(0);
            //Read the file contents into the model
            dcModel.read(new InputStreamReader(in), "");
	    //Close the input stream.
            in.close();
	    //Return the model.
            return dcModel;
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("RDFFactory: createRDFModel: " + 
		    e.toString());
            return null;
        }
    }
    //This static method creates a new RDF Model. It takes
    //one input, which is a string that contains the model
    public static Model createRDFModel(String rdfString){
	//Create a new model
        Model dcModel = new ModelMem();
       	try{
	    //Read the string contents into the model using 
	    //a string reader.
            dcModel.read(new StringReader(rdfString), null);
	    //return the model
            return dcModel;
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("RDFFactory: createRDFModel: " + 
		    e.toString());
            return null;
        }
    }
    
    //This method executes an RDF query and returns a list of results.
    //It takes three parameters - the first is a model, the second is
    //the query string and the last is a list of parameters using in 
    //the query.
    public static List executeQuery(Model dcModel,
                String queryString, 
                List params){
	//Create a new query.
        Query query = new Query(queryString);
	//Set the source to the model passed into this method.
        query.setSource(dcModel);
	//Create a new collection for the results.
        List record = new ArrayList();
        
        ResultBinding res;
	//Exectue the query and iterate through all the results and add each
	//one in turn to the record collection.
        for(Iterator iter = new QueryEngine(query).exec(); iter.hasNext();){
            res = (ResultBinding) iter.next();
            for(Iterator paramsIter = params.iterator(); paramsIter.hasNext();){
                record.add(res.get((String)paramsIter.next()).toString());
            }
        }
	//Return the results back to the caller.
        return record;
    }
    
    //This method allows the RDF model to be displayed. It takes two
    //parameters - the first parameter is the model and the second
    //parameter is the outputstyle.
    public static void displayRDF(Model dcModel, 
                    String outputStyle){
        dcModel.write(System.out, outputStyle);
    }
    
    //This method is the toString for this method. It converts
    //a model into a string and returns it to the caller.
    public static String toString(Model dcModel){
        StringWriter out = new StringWriter();
        dcModel.write(out, "RDF/XML");
        return out.toString();
    }
    
    //This method is the toString for this method. It converts the
    //string into a model and then returns the string representation
    //of that model.
    public static String toString(String model){
        return toString(createRDFModel(model));
    }
}