/*
 * Class Title:         VideoProcessorUI
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.device.impl.video;

//Java SDK imports
import java.awt.Color;

//NASUF API Imports
import NASUF.src.uk.ac.livjm.constant.DeviceConstants;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;

//Log4j API imports
import org.apache.log4j.Level;

public class VideoProcessorUI extends javax.swing.JFrame {
   
    //This is the audio processor that allows the audio
    //service to be delopyed, stopped and destroyed.
    private VideoProcessor ap;
   
    //Default Constructor
    public VideoProcessorUI() {
        initComponents();
        this.setSize(270, 130);
        //For simplicity automatically announce the device 
        //once it is initially started.
        announceDevice();
    }
    
    //Initialises the UI components
    private void initComponents() {//GEN-BEGIN:initComponents
        lblStatus = new javax.swing.JLabel();
        lblStopped = new javax.swing.JLabel();
        btnAnnounceButton = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setTitle("Video Device");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        lblStatus.setFont(new java.awt.Font("Dialog", 1, 18));
        lblStatus.setText("Status:");
        getContentPane().add(lblStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 17, -1, -1));

        lblStopped.setFont(new java.awt.Font("Dialog", 0, 18));
        lblStopped.setForeground(java.awt.Color.red);
        lblStopped.setText("Stopped");
        getContentPane().add(lblStopped, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 18, 107, -1));

        btnAnnounceButton.setText("Announce device");
        btnAnnounceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnnounceButtonActionPerformed(evt);
            }
        });

        getContentPane().add(btnAnnounceButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 49, 234, -1));

        pack();
    }//GEN-END:initComponents

    //This method connects the device to the NASUF network. The 
    //device adds adds all the core and application specific 
    //services it provides before each one is started. Once all
    //the services have been started the device sends a service
    //registration message within the network. This message is
    //used to update any controllers and more importantly update
    //service compositions within the network that use the
    //services provided by this device.
    private void announceDevice(){
        ap = new VideoProcessor("49148");
        ap.addCoreServices();
        ap.discoverDependentServices();
        ap.addApplicationPeerServices();
        ap.startServices();
        //This while loop is important because it is difficult
        //to determine when the required framework services have
        //started. This is dependent on the device, bandwidth
        //and network congestion. Consequently the time required
        //to register and start services will differ. This posses
        //a problem because the device may try to send a registration
        //message before the p2p interface has been started. To 
        //overcome this problem the code that sends the message 
        //is contained within a try catch, which itself is contained
        //in an infinite while loop. It an exception is encountered
        //the thread is put to slee for one second before the device
        //attempts to re-submit the message. Once the message has been
        //successfully sent the while is broken using the break 
        //keyword and the interface is updated appropiatly.
        while(true){
            try{
                Thread.sleep(1000);
                ap.sendServiceRegistrationMsg();
                break;
            }catch(Exception e){
                //The services might not have had time 
                //to start up properly so just keep
                //trying.
                continue;
            }
        }
        btnAnnounceButton.setText(DeviceConstants.LABEL_REMOVE);
        lblStopped.setText("Started");
        lblStopped.setForeground(Color.BLUE);
    }
    
    //This method removes the device from the NASUF network
    //It stops all the services it provides before updating
    //the user interface.
    private void removeDevice(){
        try{
            if(ap != null){
                if(NASUFLogger.isEnabledFor(Level.DEBUG))
                    NASUFLogger.debug("Destroying the ap instance");
                
                //Stop the devie and all its associated core and 
                //application specific peer services.
                ap.stopDevice();
                ap.cleanUp();
                ap = null;
            }
        }catch(Throwable e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("VideoProcessorUI: removeDevice: " + 
                    e.toString());
        }
        btnAnnounceButton.setText(DeviceConstants.LABEL_ANNOUNCE);
        lblStopped.setText("Stopped");
        lblStopped.setForeground(Color.RED);
    }
    
    //This event is triggered when the Announce/Remove button
    //is pressed on the UI.
    private void btnAnnounceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnnounceButtonActionPerformed
        if (evt.getSource() == btnAnnounceButton) {
            String currentLabel = btnAnnounceButton.getText();
            if (currentLabel.equals(DeviceConstants.LABEL_ANNOUNCE)){
                if(NASUFLogger.isEnabledFor(Level.INFO))
                    NASUFLogger.info("Registering Device");
                
                //Announce the device in the NASUF network
                announceDevice();
            }else{
                if(NASUFLogger.isEnabledFor(Level.INFO))
                    NASUFLogger.info("Unregistering Device");
                
                //Remove the device from the network
                removeDevice();
            }
        }
    }//GEN-LAST:event_btnAnnounceButtonActionPerformed
    
    //When the form is exited ensure that the device
    //cleans itself up before the exit method is called.
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        ap.cleanUp();
        ap = null;
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JButton btnAnnounceButton;
    javax.swing.JLabel lblStatus;
    javax.swing.JLabel lblStopped;
    // End of variables declaration//GEN-END:variables
   
    //Application entry point.
    public static void main(String args[]) {
        new VideoProcessorUI().show();
    }
}
