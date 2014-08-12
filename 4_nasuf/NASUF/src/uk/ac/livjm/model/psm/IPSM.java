/*
 * Class Title:		IPSM
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.model.psm;

//Java SDK imports
import java.io.OutputStream;
import java.util.List;

//OWL-S API imports
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.Parameter;

public interface IPSM {
    
    public void addEffect(Parameter effect);
    public void addInput(Input input);
    public void addOutput(Output output);
    public void addPrecondition(Parameter precondition);
    public void createProfile(List inputs);
    public void createProfile(List inputs, List outputs);
    public void createProfile(List inputs, List outputs, List preconditions);
    public void createProfile(List inputs, List outputs, List preconditions, List effects);
    public List getEffects();
    public List getInputs();
    public List getOutputs();
    public List getPreconditions();
    public String toString();
    public void write(OutputStream out);   
}