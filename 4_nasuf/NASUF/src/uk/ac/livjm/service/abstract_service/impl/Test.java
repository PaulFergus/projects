package NASUF.src.uk.ac.livjm.service.abstract_service.impl;

//JXTA API imports
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;

public class Test {
    
    /** Creates a new instance of Test */
    public Test() {
	//Create a structured document
	StructuredDocument md 
	    = StructuredDocumentFactory
		.newStructuredDocument( 
		    new MimeMediaType( "text/xml" ), "metaData" );

	//Add the peer id to the document.
	TextElement scheme 
	    = (TextElement)md
		.createElement(
		    "scheme");
		
	md.appendChild(scheme);
	
	TextElement name
	    = (TextElement)md
		.createElement(
		    "name");
	scheme.appendChild(name);
	
	TextElement fName
	    = (TextElement)md
		.createElement(
		    "firstName");
	
	name.appendChild(fName);
	
	TextElement location
	    = (TextElement)md
		.createElement(
		    "location");
	
	scheme.appendChild(location);
	
	System.out.println(md.toString());
    }
    
    public static void main(String args[]){
	Test test = new Test();
    }
}
