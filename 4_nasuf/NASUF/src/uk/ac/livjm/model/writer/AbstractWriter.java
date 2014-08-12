/*
 * Class Title:         AbstractWriter
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */

package NASUF.src.uk.ac.livjm.model.writer;

//Java SDK imports
import java.io.Writer;
import java.net.URI;

//OWL-S API imports
import org.mindswap.owl.OWLResource;
import org.mindswap.owl.Util;

//Jena API imports
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class AbstractWriter implements IWriter {
    
    //This variable contains the cccp specification verrsion
    //the writer is for.
    protected String version;
    //This varaible contains the model used to create and 
    //store model constructs.
    protected Model model = null;
    //This is the base uri used in the model.
    protected URI base = null;
    
    /** Creates a new instance of AbstractWriter */
    public AbstractWriter() {
    }
    
    //This method must be overriden in the subclass.
    public void write(java.io.Writer out) {
    }
    
    //This method must be overriden in the subclass
    public void write(java.io.OutputStream out) throws java.io.IOException {
    }
    
    //This method converts a URI to an RDF Resouce. The 
    //created resource is then returned to the caller.
    protected Resource toRDF(URI u) {
        if(base != null) u = base.relativize(u);
        return Util.toResource(u);
    }
    
    //This method coverts an OWLResource to a 
    //Jena Resource.
    protected Resource toRDF(OWLResource r) {		
        return r.getJenaResource();
    }

    //This method coverts a string to an RDF Node. The
    //created RDF Node is then returned to the caller.
    protected RDFNode toRDF(String o) {
        return toRDF(o, false);
    }
    
    //This method converts a string to an RDF Node and 
    //ensures that it is wellFormed. The created RDF
    //Node is returned to the caller.
    protected RDFNode toRDF(String o, boolean wellFormed) {
        return model.createLiteral(o, wellFormed);
    }

    //This method adds a statement to the model. This method
    //takes three parameters - the first parameter is the subject
    //the second is the property and the third is the value.
    protected void addStatement(Object s, Object p, Object o) {
        Resource subj = null;
        Property prop = null;
        RDFNode  obj  = null;
	
	//Check to see if s is an instance of Resource. If it is
	//cast it to a resource object.
        if(s instanceof Resource)
            subj = (Resource) s;
	//Check to see if s is an instance of a OWLResource. If it is then
	//cast to a RDFNode.
        else if(s instanceof OWLResource)
            subj = toRDF((OWLResource) s);
	//Check to see if s is an instance of a URI. If it is then
	//cast to a RDFNode.
        else if(s instanceof URI)
            subj = toRDF((URI) s);
        else
            throw new RuntimeException("Invalid subject " + s + " " + s.getClass());
	
	//If p is an instance of a property then cast p into 
	//a property object.
        if(p instanceof Property)
            prop = (Property) p;
	//If p is an instance of a URI then cast p into a 
	//property object.
        else if(p instanceof URI)
            prop = Util.toProperty((URI) p);
        else
            throw new RuntimeException("Invalid property " + p + " " + p.getClass());
	
	//If o is an instance of a RDFNode then cast o into 
	//a RDFNode object.
        if(o instanceof RDFNode)
            obj = (RDFNode) o;
	//If o is an instance of a OWLResource then cast o into
	//a RDFNode object.
        else if(o instanceof OWLResource)
            obj = toRDF((OWLResource) o);
	//If o is an instance of a URI then cast o into a 
	//a RDFNode object.
        else if(o instanceof URI)
            obj = toRDF((URI) o);
	//If o is an instance of a String then cast o into a
	//a RDFNode object.
        else if(o instanceof String)
            obj = toRDF((String) o);
        else
            throw new RuntimeException("Invalid object " + o + " " + o.getClass());
	//Add the statement to the model.
        model.add(subj, prop, obj);
    }
}