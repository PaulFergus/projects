/*
 * Class Title:         PrefixMapping
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.model.query.rdql.impl;

public class PrefixMapping {
    
    //These variables contain the prefix accociated
    //with some uri.
    private String prefix;
    private String uri;
    
    //Default constructor. The method takes two paraters - the 
    //first parameter is the prefix and the second is the uri.
    public PrefixMapping(
        String prefix,
        String uri) {
	//Set prefix.
        this.setPrefix(prefix)
            .setURI(uri);
    }
    
    //This setter sets the prefix and returns a PrefixMapping object.
    public PrefixMapping setPrefix(String prefix){
        this.prefix = prefix;
        return this;
    }
    
    //Thie getter returns the prefix
    public String getPrefix(){
        return this.prefix;
    }
    
    //This method sets the URI. It takes one parameter
    //which is the uri.
    public PrefixMapping setURI(String uri){
        this.uri = "<" + uri + ">";
        return this;
    }
    
    //This method returns the uri.
    public String getURI(){
        return this.uri;
    }
}