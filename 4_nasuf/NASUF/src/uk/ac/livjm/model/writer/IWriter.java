/*
 * Class Title:         IWriter
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

//Java API imports.
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public interface IWriter {
    public void write(OutputStream out) throws IOException;
    public void write(Writer out);
}