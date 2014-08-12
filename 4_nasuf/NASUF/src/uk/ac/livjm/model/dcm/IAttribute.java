/*
 * Class Title:         IAttribute
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.model.dcm;

public interface IAttribute {
    public String getDefaults();
    public String getResourceName();
    public String getType();
    public void setDefaults(String defaults);
    public void setResourceName(String name);
    public void setType(String type);
}