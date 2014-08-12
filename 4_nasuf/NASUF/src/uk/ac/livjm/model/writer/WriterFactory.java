/*
 * Class Title:		WriterFactory
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */

package NASUF.src.uk.ac.livjm.model.writer;

//NASUF API imports
import NASUF.src.uk.ac.livjm.model.writer.impl.cccp_writer.CCCPWriter;
import NASUF.src.uk.ac.livjm.model.writer.impl.owls_writer.OWLSWriter;
import NASUF.src.uk.ac.livjm.model.writer.IWriter;

public class WriterFactory {
    
    //This method returns a DCM Writer.
    public static IWriter createDCMWriter(){
	return new CCCPWriter();
    }
    
    //This method returns a OWLSWriter.
    public static IWriter createOWLSWriter(){
	return new OWLSWriter();
    }
}