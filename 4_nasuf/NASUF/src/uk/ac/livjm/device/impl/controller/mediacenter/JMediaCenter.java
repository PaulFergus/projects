/*
 * Class Title:         JMediaCenter
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.device.impl.controller.mediacenter;

//Java SDK imports
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.DefaultListModel;

//Jxta API imports
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.impl.protocol.ResolverQuery;
import net.jxta.protocol.ResolverQueryMsg;
import net.jxta.util.JxtaBiDiPipe;

//NASUF API imports
import NASUF.src.uk.ac.livjm.constant.DECAPConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceRequestConstants;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.listener.pipe.ServicePipeMsgListenerFactory;
import NASUF.src.uk.ac.livjm.protocol.data_objects.DataObjectFactory;
import NASUF.src.uk.ac.livjm.protocol.data_objects.impl.DependencyServiceDataObject;
import NASUF.src.uk.ac.livjm.protocol.data_objects.impl.PrimaryServiceDataObject;

import NASUF.src.uk.ac.livjm.utils.ReadFileToString;

//Log4J imports
import org.apache.log4j.Level;

public class JMediaCenter 
    extends javax.swing.JInternalFrame{
    
    //DefaultListModels are based on the model view
    //controller pattern. Instead of interacting with
    //Jlist control, items are added and removed from
    //the model. In this way the model notifies any
    //control using the data to update itself.
    private DefaultListModel lstServicesModel;
    private DefaultListModel lstDependencyServicesModel;
    
    //These pipes are used to connect to the required service
    //and send commands. One pipe is used for primary services
    //and the other is used for depedency services.
    private JxtaBiDiPipe pipe;
    private JxtaBiDiPipe dPipe;
    
    //Collections used to store the application
    //specific peer services. These collections
    //also include bin collections so that when
    //a service in a composition is lost or
    //unregistered it can be used again if it 
    //comes back on-line without having to 
    //rediscover it again.
    private Hashtable primaryServices;
    private Hashtable primaryServicesBin;
    private List dependencyServices;
    private List dependencyServicesBin;
    
    //This is the UI helper class
    private MediaCenter mc;
    
    //The handler name describes the type of message
    //either sent or recieved via the Resolver Service.
    private String handlerName;
    
    //These strings contain the device capability model
    //and the peer service capability model.
    private String dcm;
    private String pscm;
   
    public JMediaCenter() {
        lstServicesModel = new DefaultListModel();
        lstDependencyServicesModel = new DefaultListModel();
        initComponents();
        this.setSize(640, 480); 
        
        primaryServices = new Hashtable();
        primaryServicesBin = new Hashtable();
        dependencyServices = new ArrayList();
        dependencyServicesBin = new ArrayList();
        handlerName = "DiSUSMessage";
        mc = new MediaCenter(this);
    }
    
    //Initialises the components that comprise the UI
    private void initComponents() {//GEN-BEGIN:initComponents
        pnlLoadOntologies = new javax.swing.JPanel();
        lblDCM = new javax.swing.JLabel();
        txtDCM = new javax.swing.JTextField();
        btnDCM = new javax.swing.JButton();
        btnQueryButton = new javax.swing.JButton();
        txtPSCM = new javax.swing.JTextField();
        btnPSCM = new javax.swing.JButton();
        lblPSCM = new javax.swing.JLabel();
        pnlFunctions = new javax.swing.JPanel();
        btnPServiceSend = new javax.swing.JButton();
        jscrServices = new javax.swing.JScrollPane();
        lstServices = new javax.swing.JList();
        btnPRemoveService = new javax.swing.JButton();
        jcboService = new javax.swing.JComboBox();
        pnlFunctions1 = new javax.swing.JPanel();
        btnDServiceSend = new javax.swing.JButton();
        jscrDependencyServices = new javax.swing.JScrollPane();
        lstDependencyServices = new javax.swing.JList();
        jcboDependencies = new javax.swing.JComboBox();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lstServices.setModel(lstServicesModel);
        lstDependencyServices.setModel(lstDependencyServicesModel);
        setTitle("NASUF Media Center");
        setPreferredSize(new java.awt.Dimension(398, 319));
        pnlLoadOntologies.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlLoadOntologies.setBorder(new javax.swing.border.TitledBorder("Load Service Request"));
        lblDCM.setText("DCM");
        pnlLoadOntologies.add(lblDCM, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 26, -1, -1));

        txtDCM.setEditable(false);
        pnlLoadOntologies.add(txtDCM, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 44, 344, -1));

        btnDCM.setText("...");
        btnDCM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDCMActionPerformed(evt);
            }
        });

        pnlLoadOntologies.add(btnDCM, new org.netbeans.lib.awtextra.AbsoluteConstraints(378, 42, -1, -1));

        btnQueryButton.setText("Send Query");
        btnQueryButton.setEnabled(false);
        btnQueryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQueryButtonActionPerformed(evt);
            }
        });

        pnlLoadOntologies.add(btnQueryButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(434, 80, 118, -1));

        txtPSCM.setEditable(false);
        pnlLoadOntologies.add(txtPSCM, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 82, 344, -1));

        btnPSCM.setText("...");
        btnPSCM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPSCMActionPerformed(evt);
            }
        });

        pnlLoadOntologies.add(btnPSCM, new org.netbeans.lib.awtextra.AbsoluteConstraints(378, 80, -1, -1));

        lblPSCM.setText("PSCM");
        pnlLoadOntologies.add(lblPSCM, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 66, -1, -1));

        getContentPane().add(pnlLoadOntologies, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 566, 126));

        pnlFunctions.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlFunctions.setBorder(new javax.swing.border.TitledBorder("Services"));
        btnPServiceSend.setText("Send Command");
        btnPServiceSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPServiceSendActionPerformed(evt);
            }
        });

        pnlFunctions.add(btnPServiceSend, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 224, 140, -1));

        lstServices.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstServicesValueChanged(evt);
            }
        });

        jscrServices.setViewportView(lstServices);

        pnlFunctions.add(jscrServices, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 32, 248, 180));

        btnPRemoveService.setActionCommand("Remove Service");
        btnPRemoveService.setLabel("Remove Service");
        btnPRemoveService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPRemoveServiceActionPerformed(evt);
            }
        });

        pnlFunctions.add(btnPRemoveService, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 252, 140, -1));

        jcboService.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Play", "Stop", "Listen" }));
        pnlFunctions.add(jcboService, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 224, 106, -1));

        getContentPane().add(pnlFunctions, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 146, 282, 284));

        pnlFunctions1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlFunctions1.setBorder(new javax.swing.border.TitledBorder("Dependencies"));
        btnDServiceSend.setText("Send Command");
        btnDServiceSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDServiceSendActionPerformed(evt);
            }
        });

        pnlFunctions1.add(btnDServiceSend, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 224, 140, -1));

        jscrDependencyServices.setViewportView(lstDependencyServices);

        pnlFunctions1.add(jscrDependencyServices, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 32, 248, 180));

        jcboDependencies.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Stop", "Listen" }));
        pnlFunctions1.add(jcboDependencies, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 224, 106, -1));

        getContentPane().add(pnlFunctions1, new org.netbeans.lib.awtextra.AbsoluteConstraints(296, 146, 280, 282));

        pack();
    }//GEN-END:initComponents
    
    //This method is used to remove the primary service from 
    //the user interface when an unregsiter service message 
    //is recieved via the UI helper class (mc)
    private void removePrimaryService(String unregServiceName){
        //Before the primary service is removed check that
        //the primary service collection contains it.
        if(primaryServices.contains(unregServiceName))
            primaryServices.remove(unregServiceName);
        //Before the primary service is removed check that
        //the primary service collection bin contains it.
        if(primaryServicesBin.contains(unregServiceName))
            primaryServicesBin.remove(unregServiceName);
        
        //Iterate through all the dependency services and find out
        //if any of them are dependency services belonging to the 
        //primary service. If any are found then remove them and 
        //update the user interface.
        Iterator iter = dependencyServices.iterator();
        List col = new ArrayList();
        while(iter.hasNext()){
            DependencyServiceDataObject ds = 
		(DependencyServiceDataObject)iter.next();
            if(ds.getPrimaryService().equalsIgnoreCase(
                unregServiceName)){
                //Remove the dependency service list option
                lstDependencyServicesModel.removeElement(ds.getName());
                col.add(ds);
            }
        }
        
        //Remove all the dependency services associated with the 
        //primary service
        if(col.size() > 0)
            dependencyServices.removeAll(col);
        
        //Remove the list option
        lstServicesModel.removeElement(unregServiceName);
    }
    
    //This event is triggered when the user requests a service
    //be removed.
    private void btnPRemoveServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPRemoveServiceActionPerformed
        if(lstServices.getSelectedValue() != null)
            removePrimaryService((String)lstServices.getSelectedValue());
    }//GEN-LAST:event_btnPRemoveServiceActionPerformed

    //This event is triggered when the user requests that 
    //a command be sent to a depedency service.
    private void btnDServiceSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDServiceSendActionPerformed
        //Make sure that we have removed the binding to the
        //previous pipe before we try to instanciate another
        //pipe
        dPipe = null;
        //Find the dependency service module spec.
        Iterator iter = dependencyServices.iterator();
        DependencyServiceDataObject ds = null;
        String moduleSpec = "";
        while(iter.hasNext()){
            ds = (DependencyServiceDataObject)iter.next();
            if(ds.getName()
                .equalsIgnoreCase((String)
                    lstDependencyServices
                        .getSelectedValue())){
                moduleSpec = ds.getModuleSpec();
                break;
            }
        }
        //If a module spec is found then bind to it and send 
        //the user defined command.
        if(moduleSpec.length() > 0){
            dPipe = 
                mc.bindToService(moduleSpec,
                    ServicePipeMsgListenerFactory
                        .createMediaCenterUIPipeMsgListener(this));
            //Send command
            mc.sendCommand(jcboDependencies.getSelectedItem().toString(), dPipe);
        }
    }//GEN-LAST:event_btnDServiceSendActionPerformed

    //This event is triggered when the user requests that 
    //a command be sent to a primary service.
    private void btnPServiceSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPServiceSendActionPerformed
        //Make sure that we have removed the binding to the
        //previous pipe before we try to instanciate another
        //pipe
        pipe = null; 
        pipe = 
            mc.bindToService(((PrimaryServiceDataObject)
                primaryServices.get(
                    lstServices
                        .getSelectedValue()))
                            .getModuleSpec(),
                ServicePipeMsgListenerFactory
                    .createMediaCenterUIPipeMsgListener(this));
        //Send the user defined command to the primary service.
        mc.sendCommand(jcboService.getSelectedItem().toString(), pipe);
    }//GEN-LAST:event_btnPServiceSendActionPerformed
    
    //When a list value as changed update the dependency services 
    //associated with primary service in the dependency serice list
    //control
    private void lstServicesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstServicesValueChanged
        // See if this is a listbox selection and the
        // event stream has settled
	if( evt.getSource() == lstServices
            && !evt.getValueIsAdjusting()){
            lstDependencyServicesModel.clear();
            // Get the current selection and place it in the
            // edit field
            String stringValue = (String)lstServices.getSelectedValue();
            //Iterate through the dependency collection and all each
            //dependency service to the lstDependencyServicesModel - this
            //will automatically update the required Jlist.
            if(stringValue != null){
                Iterator iter;
                iter = dependencyServices.iterator();
                DependencyServiceDataObject ds;
                while(iter.hasNext()){
                    ds = (DependencyServiceDataObject)iter.next();
                    if(ds.getPrimaryService()
                        .equalsIgnoreCase(stringValue))
                        if(!lstDependencyServicesModel.contains(ds.getName()))
                            lstDependencyServicesModel
                                .addElement(ds.getName());
                }
            }
        }
    }//GEN-LAST:event_lstServicesValueChanged

    //This event is triggered when the user is loading the 
    //the required peer service capability model. 
    private void btnPSCMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPSCMActionPerformed
        //Open a file chooser control, which allows the user to 
        //navigate the devices file system.
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
        
        //Show the dialog
        int result = fileChooser.showOpenDialog(this);
        if(result == JFileChooser.CANCEL_OPTION)
            return;
        
        //Update the user interface with the file URI.
        txtPSCM.setText(fileChooser.getSelectedFile().getAbsolutePath());
        
        //Call the update Query Button State method to determine
        //whether the send button can be enabled. The send button
        //allows a service request to be created that contains the 
        //DCM and the PSCM.
        updateQueryButtonState();
    }//GEN-LAST:event_btnPSCMActionPerformed
    
    //This event is triggered if a service is discovered based on 
    //the service request created using the DCM and the PSCM
    public void addService(String service){
        String strDECAPValue;
        String strPrimaryService;
        String strDependencyService;
        String strPeerID;
        StructuredTextDocument doc;
        StructuredTextDocument primaryService;
        StructuredTextDocument dependencyService;
        
        //The service description is in plane text so first convert the
        //string into a structured document so that the XML elements
        //can be more easily navigated.
        try{
            doc = (StructuredTextDocument)
                StructuredDocumentFactory.newStructuredDocument(
                    new MimeMediaType( "text/xml" ), 
                        new ByteArrayInputStream(service.getBytes()) );
            
            //Extact the primary service.
            Enumeration en = doc.getChildren("PrimaryService");
            Element el = (Element)en.nextElement();
            
            primaryService = (StructuredTextDocument)
                StructuredDocumentFactory.newStructuredDocument(
                    new MimeMediaType("text/xml"),
                        new ByteArrayInputStream(((String)el.getValue()).getBytes()));
            
            //Extract the device capability score. This will be used when
            //the device selects the required service with the highest
            //DCM score, i.e. always use the best service you have.
            en = doc.getChildren("DeviceDECAPScore");
            el = (Element)en.nextElement();
            strDECAPValue = (String)el.getValue();
            
            //Extract the name of the servic.
            en = primaryService.getChildren("Name");
            el = (Element)en.nextElement();
            strPrimaryService = (String)el.getValue();
            
            //Extract the PeerID
            en = primaryService.getChildren("Parm");
            el = (Element)en.nextElement();
            strPeerID = (String)
                ((Element)el
                    .getChildren("PeerID")
                        .nextElement())
                            .getValue();
            
            //Concatenate the name and the PeerID together to make 
            //it a unique item in the service models and consequently
            //in collectins and JLists.
            strPrimaryService = strPrimaryService + "-" + strPeerID;
            
            //If the service has already been discovered then
            //remove it so that a fresh instance can be added.
            if(lstServicesModel.contains(strPrimaryService))
                removePrimaryService(strPrimaryService);
            
            //If the service is not registered with the interface
            //then add it.
            if(!lstServicesModel.contains(strPrimaryService)){
                lstServicesModel.addElement(strPrimaryService);
                primaryServices.put(strPrimaryService , 
		    DataObjectFactory
			.createPrimaryServiceDataObject(
			    strDECAPValue, 
			    primaryService
				.toString()));
            }
            //Extract the dependent services associated with the 
            //primary service and again convert it into a structured
            //document.
            en = doc.getChildren("DependentService");
            java.util.Enumeration den;
            Element del;
            while(en.hasMoreElements()){
                el = (Element)en.nextElement();
                
                dependencyService = (StructuredTextDocument)
                    StructuredDocumentFactory.newStructuredDocument(
                        new MimeMediaType("text/xml"),
                            new ByteArrayInputStream(((String)el.getValue()).getBytes()));
                
                //Extract the service name.
                den = dependencyService.getChildren("Name");
                del = (Element)den.nextElement();
                strDependencyService = (String)del.getValue();
                
                //Extract the PeerID
                den = dependencyService.getChildren("Parm");
                del = (Element)den.nextElement();
                    strPeerID = (String)
                        ((Element)del
                            .getChildren("PeerID")
                                .nextElement())
                                    .getValue();
            
                //Concatenate the name and the PeerID together to make 
                //it a unique item in the service models and consequently
                //in collectins and JLists.  
                strDependencyService = strDependencyService + "-" + strPeerID;
               
                //If the service is not registered with the interface
                //then add it.
                if(!lstDependencyServicesModel.contains(strDependencyService)){
                    dependencyServices.add(
                       DataObjectFactory
			    .createDepencencyServiceDataObject(
				"",
				strPrimaryService,
				strDependencyService, 
				dependencyService.toString()));
                }
            }
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("JMediaCenter: addService: " + 
                    e.toString());
        }
    }
    
    //This method is overridden by devices that have dependen services 
    //that form part of a service composition. Only devices that use
    //depndency services need to override this method implemented in 
    //the abstract parent class.
    public void unregisterService(
        String serviceName){
        String currentService;
        String unregServiceName;
        String unregServicePeerID;
        String curreServiceName;
        String curreServicePeerID;
        
        String moduleSpec = "";

        StructuredTextDocument unregService;
        StructuredTextDocument curreService;
        
        //The service description is in plane text so first convert the
        //string into a structured document so that the XML elements
        //can be more easily navigated.
        try{
            unregService = (StructuredTextDocument)
                    StructuredDocumentFactory.newStructuredDocument(
                        new MimeMediaType("text/xml"),
                            new ByteArrayInputStream((serviceName.getBytes())));
            Enumeration en;
            Element el;
            
            //Extract the name of the service to be unregistered.
            en = unregService.getChildren("Name");
            el = (Element)en.nextElement();
            unregServiceName = (String) el.getValue();

            //Extract the PeerID for the service to be unregistered.
            en = unregService.getChildren("Parm");
            el = (Element)en.nextElement();
            unregServicePeerID = (String)
                ((Element)el
                    .getChildren("PeerID")
                        .nextElement())
                            .getValue();
            
            //Concatenate the name and the PeerID together to make 
            //it a unique description.  
            unregServiceName = unregServiceName + "-" + unregServicePeerID;
            
            Object currentObjService;
            Iterator iter;
            
            //Make a clone of the primary services collection so that
            //the device can use the clone to do the analysis, whilst 
            //using the original collection to either remove or add 
            //services. This technique avoids concurrent modification
            //errors.
            Hashtable ps = (Hashtable)primaryServices.clone();
            //If the service to be removed is in the primary 
            //services model then remove it. This will also result
            //in all its depedency services being removed.
            if(lstServicesModel.contains(unregServiceName)){
                removePrimaryService(unregServiceName);
            }
            
            //If the dependcy service model contains the service to be
            //unregistered then remove it and add it to the depencecy
            //services bin incase this service becomes available again.
            if(lstDependencyServicesModel.contains(unregServiceName)){
                lstDependencyServicesModel.removeElement(unregServiceName);
             
                List col = new ArrayList();
                
                //Iterator through all the dependency services and add
                //each matching service to the bin directory.
                iter = dependencyServices.iterator();
                DependencyServiceDataObject ds;
                while(iter.hasNext()){
                    ds = (DependencyServiceDataObject)iter.next();
                    if(ds.getName()
                        .equalsIgnoreCase(unregServiceName)){
                        dependencyServicesBin.add(ds);
                        //this collection will be used to remove the 
                        //services from the dependencyServices collection.
                        //This avoids concurrent modification errors.
                        col.add(ds);    
                    }
                }
                
                //If the service to be unregistered was found then 
                //remove is from the dependencyServices collection.
                if(col.size() > 0)
                    dependencyServices.removeAll(col);
            }
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error(e.toString());
        }
    }
    
    //Event messages are recieved from the network when a service 
    //is initially connected. This method is triggered and used to 
    //determine if the controller knows about this service. If so
    //the internal collections and the user interface is updated to
    //reflect that this service is back on line again.
    public void registerService(String serviceName){
        String currentService;
        String regServiceName;
        String regServicePeerID;
        String curreServiceName;
        String curreServicePeerID;
        
        String moduleSpec = "";
        
        StructuredTextDocument regService;
        StructuredTextDocument curreService;
        
        //The service description is in plane text so first convert the
        //string into a structured document so that the XML elements
        //can be more easily navigated.
        try{
            regService = (StructuredTextDocument)
                    StructuredDocumentFactory.newStructuredDocument(
                        new MimeMediaType("text/xml"),
                            new ByteArrayInputStream((serviceName.getBytes())));
            Enumeration en;
            Element el;
            
            //Extract the name of the service being registered. 
            en = regService.getChildren("Name");
            el = (Element)en.nextElement();
            regServiceName = (String) el.getValue();

            //Extract the peerID of the service being registered.
            en = regService.getChildren("Parm");
            el = (Element)en.nextElement();
            regServicePeerID = (String)
                ((Element)el
                    .getChildren("PeerID")
                        .nextElement())
                            .getValue();
            
            //Concatenate the name and the PeerID together to make 
            //it a unique description.  
            regServiceName = regServiceName + "-" + regServicePeerID;
            
            Object currentObjService;
            Iterator iter;
            
            //I think I need to check this because I am sure that when
            //a primary service is unregistered it is completely removed.
            
            //Iterate through the primary services bin and see if the 
            //service has been previously discovered. 
            if(primaryServicesBin.size() > 0){
                Hashtable ps = (Hashtable)primaryServicesBin.clone();

                iter = ps.keySet().iterator();
                
                while(iter.hasNext()){
                    
                    String service = (String)iter.next();
                    //If a servic match is found then remove the service 
                    //from the bin collection and put it in the active 
                    //primary service collection.
                    if(service
                        .equalsIgnoreCase(regServiceName)){
                        
                        //Remove the service from the bin collection
                        //and add it to the primary services collection
                        primaryServices.put(regServiceName, 
                            primaryServicesBin.remove(service));
                        
                        Iterator dIter = dependencyServicesBin.iterator();
                        DependencyServiceDataObject ds = null;
                        
                        List col = new ArrayList();
                        
                        //Check to see if the service has any dependency services
                        //in the depencency services bin. If any services are found
                        //then extract them from the bin and put them in the
                        //active depedency services collection.
                        while(dIter.hasNext()){
                            ds = (DependencyServiceDataObject)dIter.next();
                            if(ds.getPrimaryService().equalsIgnoreCase(regServiceName)){
                                if(!dependencyServices.contains(ds))
                                    dependencyServices.add(ds);
                                if(dependencyServicesBin.contains(ds)){
                                    col.add(ds);
                                    lstDependencyServicesModel
                                        .addElement(ds.getName());
                                }
                            }
                        }
                        
                        //To avoid concurrent modification errors, the services
                        //found are removed from the bin at this point.
                        if(col.size() > 0)
                            dependencyServicesBin.removeAll(col);
                        
                        //If the list model does not contain the service to
                        //registered then add it. The JList controls will be
                        //updated automatically.
                        if(!lstServicesModel.contains(regServiceName))
                            lstServicesModel.addElement(regServiceName);
                        return;
                    }
                }
            }
            
            //If the service to be registered was not a primary service 
            //then check to see if it is a dependency service.
            if(dependencyServicesBin.size() > 0){
                Collection col = new ArrayList();
                
                //Iterate through the dependency services bin
                iter = dependencyServicesBin.iterator();
                DependencyServiceDataObject ds;
                while(iter.hasNext()){
                    ds = (DependencyServiceDataObject)iter.next();
                    //If the servic is found then remove it from the bin 
                    //collection and add it to the active dependency services 
                    //collection.
                    if(ds.getName()
                        .equalsIgnoreCase(regServiceName)){
                        col.add(ds);    
                        if(ds.getPrimaryService()
                            .equalsIgnoreCase((String)lstServices.getSelectedValue()))
                             if(!lstDependencyServicesModel.contains(ds.getName()))
                                lstDependencyServicesModel
                                    .addElement(ds.getName());
                        }
                }
                
                //To avoid concurrent modification errors, the services
                //found are removed from the bin at this point.
                if(col.size() > 0){
                    dependencyServicesBin.removeAll(col);
                    dependencyServices.addAll(col);
                }
            }
            
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("JMediaCenter: registerService: " + e.toString());
        }
    }
    
    //This method determines whether the send button can be enabled or not.
    //The rule is that a DCM and a PSCM must have been loaded.
    private void updateQueryButtonState(){
        if(txtDCM.getText().length() > 0 && txtPSCM.getText().length() > 0){
            btnQueryButton.setEnabled(true);
        }else{
            btnQueryButton.setEnabled(false);
        }
    }
    
    //This event is triggered when the user loads a DCM file
    private void btnDCMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDCMActionPerformed
        //Open a file chooser control, which allows the user to 
        //navigate the devices file system.
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true); 
        
        //Show the dialog
        int result = fileChooser.showOpenDialog(this);
        if(result == JFileChooser.CANCEL_OPTION)
            return;
        
        txtDCM.setText(fileChooser.getSelectedFile().getAbsolutePath());
        
        //Call the update Query Button State method to determine
        //whether the send button can be enabled. The send button
        //allows a service request to be created that contains the 
        //DCM and the PSCM.
        updateQueryButtonState();
    }//GEN-LAST:event_btnDCMActionPerformed
    
    //This method discovers application specific services
    //using the DCM and the PSCM loaded in via the user interface.
    public void discoverApplicationPeerService(){
        //Create service request structured document.
        StructuredTextDocument doc;
        doc = (StructuredTextDocument)
            StructuredDocumentFactory.newStructuredDocument(
                new MimeMediaType("text/xml"),"ServiceRequest");
        //Create the service request type.
        doc.appendChild(
            doc.createElement(
                ServiceRequestConstants.REQUEST_TYPE,
                "findApplicationPeerService"));
        //Add the DCM 
        doc.appendChild(
            doc.createElement(
                DECAPConstants.SERVICE_REQUEST_DECAP_TAG,
                    ReadFileToString.getContents(
                        new File(txtDCM.getText()))));
        //Add the PSCM
        doc.appendChild(
            doc.createElement(
                ServiceRequestConstants.SERVICE_REQUEST_PSCM_TAG,
                    ReadFileToString.getContents(
                        new File(txtPSCM.getText()))));
        System.out.println(ReadFileToString.getContents(
            new File(txtPSCM.getText())));
        //Create a new ResolverQueryMsg.
        ResolverQueryMsg message;
        StringWriter out = new StringWriter();
        try{
            doc.sendToWriter(out);
        }catch(Exception ex){
            if(NASUFLogger.isEnabledFor(Level.ERROR)){
                NASUFLogger.error("JMediaCenter: discoverApplicationPeerservice: " + 
                    ex.toString());
            }
        }

        message = new ResolverQuery();
        //Set the message handler, which specifies what type of 
        //message it is.
        message.setHandlerName(handlerName);
        message.setCredential(null);
        //Add the converted structured document for the service request
        //to the message.
        message.setQuery(out.toString());
       
        if(NASUFLogger.isEnabledFor(Level.INFO)){
            NASUFLogger.info("Sending Query ...");
        }
        // Broadcast to all members of the group
        mc.discoverApplicationPeerService(message);
    }
    
    //This event is triggered when the user selects to discover an 
    //application peer service.
    private void btnQueryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQueryButtonActionPerformed
        //Find the dependent services for this device
        discoverApplicationPeerService();
    }//GEN-LAST:event_btnQueryButtonActionPerformed

    //Each device must implement the cleanUp method which 
    //must ensure that all the objects used by the device
    //are correctly distroyed.
    public void cleanUp(){
        lstServicesModel.clear();
        lstServicesModel = null;
        lstDependencyServicesModel.clear();
        lstDependencyServicesModel = null;
 
        try{
            pipe.close();
            pipe = null;
            dPipe.close();
            dPipe = null;
        }catch(Exception e){
            pipe = null;
            dPipe = null;
        }
 
        primaryServices.clear();
        primaryServices = null;
        primaryServicesBin.clear();
        primaryServicesBin = null;
    
        dependencyServices.clear();
        dependencyServices = null;
        dependencyServicesBin.clear();
        dependencyServicesBin = null;
    
        mc.cleanUp();
        mc = null;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JButton btnDCM;
    javax.swing.JButton btnDServiceSend;
    javax.swing.JButton btnPRemoveService;
    javax.swing.JButton btnPSCM;
    javax.swing.JButton btnPServiceSend;
    javax.swing.JButton btnQueryButton;
    javax.swing.JComboBox jcboDependencies;
    javax.swing.JComboBox jcboService;
    javax.swing.JScrollPane jscrDependencyServices;
    javax.swing.JScrollPane jscrServices;
    javax.swing.JLabel lblDCM;
    javax.swing.JLabel lblPSCM;
    javax.swing.JList lstDependencyServices;
    javax.swing.JList lstServices;
    javax.swing.JPanel pnlFunctions;
    javax.swing.JPanel pnlFunctions1;
    javax.swing.JPanel pnlLoadOntologies;
    javax.swing.JTextField txtDCM;
    javax.swing.JTextField txtPSCM;
    // End of variables declaration//GEN-END:variables
}