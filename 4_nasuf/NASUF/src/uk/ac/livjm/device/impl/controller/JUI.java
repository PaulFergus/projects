/*
 * Class Title:         JUI
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.device.impl.controller;

//NASUF API imports
import NASUF.src.uk.ac.livjm.device.impl.controller.mediacenter.JMediaCenter;

public class JUI extends javax.swing.JFrame {
    
    //This is the media center user interface helper class
    private JMediaCenter mc;
    
    //Default constructor
    public JUI() {
        initComponents();
        this.setSize(800, 600);
    }
    private void initComponents() {//GEN-BEGIN:initComponents
        desktopPane = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();
        NASUF = new javax.swing.JMenu();
        mediaCenterMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        exitMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setTitle("Networked Appliance Service Utilisation Framework");
        setName("PCF");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        desktopPane.setAutoscrolls(true);
        getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);

        NASUF.setText("NASUF");
        NASUF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NASUFActionPerformed(evt);
            }
        });

        mediaCenterMenuItem.setLabel("Media Center");
        mediaCenterMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mediaCenterMenuItemActionPerformed(evt);
            }
        });

        NASUF.add(mediaCenterMenuItem);

        NASUF.add(jSeparator1);

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });

        NASUF.add(exitMenuItem);

        menuBar.add(NASUF);

        helpMenu.setText("Help");
        contentMenuItem.setText("Contents");
        helpMenu.add(contentMenuItem);

        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
    }//GEN-END:initComponents

    private void mediaCenterMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mediaCenterMenuItemActionPerformed
        //Create a new media center interface.
	mc = new JMediaCenter();
	//Add the pane to the MDI
        desktopPane.add(mc);
	//Make the media center UI visible.
        mc.setVisible(true);
    }//GEN-LAST:event_mediaCenterMenuItemActionPerformed

    private void NASUFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NASUFActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_NASUFActionPerformed
    
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        this.cleanUp();
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed
    
    //This event is called when the application is exited.
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        this.cleanUp();
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    //Each device must implement the cleanUp method which 
    //must ensure that all the objects used by the device
    //are correctly distroyed.
    public void cleanUp(){
        if(mc != null)
            mc.cleanUp();
        mc = null;
    }
    
    //This is the main entery point into the executable class.
    public static void main(String args[]) {
        new JUI().show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JMenu NASUF;
    javax.swing.JMenuItem aboutMenuItem;
    javax.swing.JMenuItem contentMenuItem;
    javax.swing.JDesktopPane desktopPane;
    javax.swing.JMenuItem exitMenuItem;
    javax.swing.JMenu helpMenu;
    javax.swing.JSeparator jSeparator1;
    javax.swing.JMenuItem mediaCenterMenuItem;
    javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables
}