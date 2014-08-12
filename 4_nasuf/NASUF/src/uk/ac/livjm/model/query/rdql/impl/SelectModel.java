/*
 * Class Title:         SelectModel
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
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

//NASUF API imports
import NASUF.src.uk.ac.livjm.constant.DECAPConstants;

public class SelectModel {
    
    //This collections contain the fields and where parts 
    //of the query.
    private List fields;
    private List whereModels;
    
    public SelectModel() {
	//Create new collections
        fields = new ArrayList();
        whereModels = new ArrayList();
    }
    
    //This method adds a field, which are used in the 
    //select part of the query, i.e. Select name where
    //name is a field. This method also returns itself
    //to the caller.
    public SelectModel addSelectField(String field){
        fields.add(field);
        return this;
    }
    
    //This getter returns the fields collection.
    public List getFields(){
        return this.fields;
    }
    
    //This method adds a where statement to the whereModels
    //collection. This method also returns itself to the 
    //caller.
    public SelectModel addWhereStatement(WhereModel whereModel){
        whereModels.add(whereModel);
        return this;
    }
    
    //This method returns the whereModels collection
    public List getWhereModels(){
        return this.whereModels;
    }
    
    //This is overriden toString() for this class. It returns
    //the query as a string.
    public String toString(){
        String query = "";
        query = "Select ";
	//Add the fields to the select part of the query.
        Iterator iter = fields.iterator();
        while(iter.hasNext()){
            query = query + (String) iter.next();
            if(iter.hasNext()){
                query = query + ", ";
            }
        }
	//Add the where parts of the query.
        iter = whereModels.iterator();
        if(iter != null){
            query = query + " Where ";
        
            while(iter.hasNext()){
                query = query + ((WhereModel)iter.next()).toString();
                if(iter.hasNext()){
                    query = query + ", ";
                }
            }
        }
	//Return the query.
        return query;
    }
}