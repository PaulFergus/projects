/*
 * Class Title:         IDataObject
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */

package NASUF.src.uk.ac.livjm.protocol.data_objects;

public interface IDataObject {
    public String getDecapValue();
    public String getModuleSpec();
    public String getName();
    public void setDecapValue(String decapValue);
    public void setModuleSpec(String moduleSpec);
    public void setName(String name);
}