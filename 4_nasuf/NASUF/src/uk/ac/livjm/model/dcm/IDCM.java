/*
 * Class Title:         IDCM
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

public interface IDCM {
    public void addAttribute(IAttribute attribute);
    public void addComponent(String componentName);
    public void addDefaultAttribute(IDefaultAttribute attribute);
    public String getProfileName();
    public void setProfileName(String name);
    public String toString();
}