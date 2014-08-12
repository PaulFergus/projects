/*
 * Class Title:         IME
 * Class Description:   Merge Engine Interface
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.algorithm.distres.me;

public interface IME {
    public Object createClass(Object cls, Object model);
    public Object createSubclass(Object cls1, Object cls2, Object model);
    public Object createDisjointClass(Object cls1, Object cls2, Object model);
    public Object createEquivalentClass(Object cls1, Object cls2, Object model);
    public Object createProperty(Object cls, Object property, Object model);
    public Object createRestriction(Object cls, Object restriction, Object model);
    public Object createIndividual(Object cls, Object individual, Object model);
    //This method merges the optimal concept with the devices current ontology
    public boolean mergeConcept(Object concept);
}