/*
 * Class Title:         ServiceMatchingConstants
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.constant;

public class ServiceMatchingQueryConstants {
    
    //This are the queries used to extract IOPEs from OWL-S Profiles.
    //These are based on the 1.0 OWL-S specification.
    public static final String HAS_EFFECT_QUERY = 
        "SELECT ?effect WHERE(?x profile:hasEffect ?effect)" +
            "(?effect rdf:type process:Effect) " +
        "USING process FOR " +
            "<http://www.daml.org/services/owl-s/1.0/Process.owl#>, " +
            "profile FOR " +
                "<http://www.daml.org/services/owl-s/1.0/Profile.owl#>, " +
            "rdf FOR " +
                "<http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
    public static final String HAS_INPUT_QUERY = 
        "SELECT ?input WHERE(?x profile:hasInput ?input)" +
            "(?input rdf:type process:Input) " +
        "USING process FOR " +
            "<http://www.daml.org/services/owl-s/1.0/Process.owl#>, " +
            "profile FOR " +
                "<http://www.daml.org/services/owl-s/1.0/Profile.owl#>, " +
            "rdf FOR " +
                "<http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
    public static final String HAS_OUTPUT_QUERY =
        "SELECT ?output WHERE(?x profile:hasOutput ?output)" +
            "(?output rdf:type process:Output) " +
        "USING process FOR " +
            "<http://www.daml.org/services/owl-s/1.0/Process.owl#>, " +
            "profile FOR " +
                "<http://www.daml.org/services/owl-s/1.0/Profile.owl#>, " +
            "rdf FOR " +
                "<http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
    public static final String HAS_PRECONDITION_QUERY = 
        "SELECT ?precondition WHERE(?x profile:hasPrecondition ?precondition)" +
            "(?precondition rdf:type process:Precondition) " +
        "USING process FOR " +
            "<http://www.daml.org/services/owl-s/1.0/Process.owl#>, " + 
            "profile FOR " +
                "<http://www.daml.org/services/owl-s/1.0/Profile.owl#>, " +
            "rdf FOR " +
                "<http://www.w3.org/1999/02/22-rdf-syntax-ns#>" ;
}