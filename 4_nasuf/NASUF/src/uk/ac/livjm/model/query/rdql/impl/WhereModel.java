/*
 * Class Title:         WhereModel
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
import java.util.Iterator;

public class WhereModel {
    
    private String statementName = "Where";
    private String x, y, z;
    private UsingModel using = null;
    
    //Default constructor.
    public WhereModel() {
    }
    
    //This setter adds the x value.
    public WhereModel setXValue(String variable){
        this.x = variable;
        return this;
    }
    
    //This setter assigns the x value using a namespace.
    public WhereModel setXValue(
        String namespace,
        String value){
        this.x = "<" + namespace + value + ">";
        return this;
    }
    
    //This getter returns the x value.
    public String getXValue(){
        return this.x;
    }
    
    //This setter assigns the y value
    public WhereModel setYValue(String variable){
        this.y = variable;
        return this;
    }
    
    //This setter assigns the y value using a namespace.
    public WhereModel setYValue(
        String namespace,
        String value){
        this.y = "<" + namespace + value + ">";
        return this;
    }
    
    //This getter returns the y value.
    public String getYValue(){
        return this.y;
    }
    
    //This setter sets the z value.
    public WhereModel setZValue(String variable){
        this.z = variable;
        return this;
    }
    
    //This setter sets the z value using a namespace.
    public WhereModel setZValue(
        String namespace,
        String value){
        this.z = "<" + namespace + value + ">";
        return this;
    }
    
    //This getter returns the z value.
    public String getZValue(){
        return this.z;
    }
    
    //This setter assigns the where construct.
    public WhereModel setUsingModel(UsingModel usingModel){
        this.using = usingModel;
        return this;
    }
    
    //This getter returns the where construct.
    public UsingModel getUsingModel(){
        return this.using;
    }
    
    //This method overrides the toString() method and 
    //returns a string representation of the where statement.
    public String toString(){
        String whereModel = "";
        whereModel = "(" + this.x + " " + this.y + " " + this.z + ")";
        Iterator iter = null;
        if(using != null){
            iter = using.getPrefixMappings().iterator();
            if(iter != null){
                whereModel = whereModel + " Using ";

                while(iter.hasNext()){
                    PrefixMapping pm = (PrefixMapping)iter.next();
                    whereModel = whereModel + pm.getPrefix();
                    whereModel = whereModel + " For ";
                    whereModel = whereModel + pm.getURI();
                    if(iter.hasNext()){
                        whereModel = whereModel + ", ";
                    }
                }
            }
        }
	//Return where model.
        return whereModel;
    }
}