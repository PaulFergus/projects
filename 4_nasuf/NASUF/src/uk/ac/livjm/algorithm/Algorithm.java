/*
 * Class Title:         Algorithm
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.algorithm;

public abstract class Algorithm implements IAlgorithm {
    
    //This method contain the information regarding 
    //the name of the algorithm, the version and the 
    //creator.
    private String creator = null;
    private String name = null;
    private String version = null;
    
    //Default constructor.
    public Algorithm() {
    }
    
    //This getter method returns the creator details.
    public String getCreator() {
        return creator;
    }
    
    //This getter method returns the name of the 
    //algorithm
    public String getName() {
        return name;
    }
    
    //This getter method returns the version number
    //for the algorithm.
    public String getVersion() {
        return version;
    }
    
    //This setter method assigns the creator details
    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    //This setter method assigns the algoirthm name
    public void setName(String name) {
        this.name = name;
    }
    
    //This setter method sets the algorithm variable
    public void setVersion(String version) {
        this.version = version; 
    }
    
    //Each device must implement the cleanUp method which 
    //must ensure that all the objects used by the device
    //are correctly distroyed.
    public void cleanUp(){
        name = null;
        version = null;
        creator = null;
    }
}
