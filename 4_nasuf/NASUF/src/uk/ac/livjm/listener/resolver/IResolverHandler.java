/*
 * Class Title:         IHandler
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.listener.resolver;

//JXTA API imports
import net.jxta.pipe.PipeMsgListener;
import net.jxta.resolver.QueryHandler;

public interface IResolverHandler extends PipeMsgListener, QueryHandler{
}
