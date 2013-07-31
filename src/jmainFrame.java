/*
 * jmainFrame.java
 *
 * Created on December 1, 2006, 2:06 PM
 */

/**
 *
 * @author  VisokayA
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.JComponent;

/* Change History
 *08-15-08      Andy Visokay        1.2
 *  - made jar run as jar or applet
 *03-05-07      Andy Visokay        1.1
 *  - fixed problems when trying to pull up output formats for TL and RT.  Had to
 *    add back the product option buttons to allow for the specific output format 
 *    selection.  
 *01-05-07      Andy Visokay        1.0
 *  - initial release
 **/

public class jmainFrame extends javax.swing.JFrame {
    final static double VERSION = 2.0;
    final static String PROGNAME = "FEMON";
    final static String formcaption = new String("FEMON Version 2.0");
    public static JFrame jmainframe = null;
    public static JFrame jstoryframe = null;
    public static jfemonmainPanel jfemonPanel;
    public static ArrayList searchResults;
    public static String sSearchData = null;
    public static boolean bIsApplet = false;
    final static String CONFIG_FILE = "femonproperties.txt";
 
    /**
     * Creates new form jmainFrame
     */
    public jmainFrame() {
        initComponents();
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
        jfemonPanel = new jfemonmainPanel(false);
        getContentPane().add(jfemonPanel);
        pack();
    }
     
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                propertySaving(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                propSaving2(evt);
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void propertySaving(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_propertySaving
        
        fontSaveHandler();
    }//GEN-LAST:event_propertySaving

    private void propSaving2(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_propSaving2
        
        fontSaveHandler();
        
    }//GEN-LAST:event_propSaving2
    
    public void fontSaveHandler(){
        if(jfemonPanel.font != null){
            FemonConfig.saveFont(jfemonPanel.font);
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new jmainFrame().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}