/*
 * Class Title:         IDefaultAttribute
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

public interface IDefaultAttribute {
    public String getAttributeName();
    public String getAttributeValue();
    public String getResourceName();
    public void setAttributeName(String name);
    public void setAttributeValue(String value);
    public void setResourceName(String resourceName);
}