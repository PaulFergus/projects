/*
 * Class Title:         IAlgorithm
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:		M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
			cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.algorithm;

public interface IAlgorithm {
    public void cleanUp();
    public String getCreator();
    public String getName();
    public String getVersion();
    public void setCreator(String creator);
    public void setName(String name);
    public void setVersion(String version);
}
