/*
 * Class Title:         UsingModel
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

//Java SDK imports
import java.util.ArrayList;
import java.util.List;

public class UsingModel {
    
    //This collection contains the prefixes used in the 
    //using statement.
    private List prefix = null;
    
    //Default constructor.
    public UsingModel() {
	//Create a new collection.
        prefix = new ArrayList();
    }
    
    //This method allows a prefix to be added to the 
    //prefix collection. This method returns itself
    public UsingModel addPrefixMapping(PrefixMapping prefixMapping){
        this.prefix.add(prefixMapping);
        return this;
    }
    
    //This method returns the collection of prefix mappings.
    public List getPrefixMappings(){
        return prefix;
    }
}