/*
 * jfemonmainPanel.java
 *
 * Created on December 1, 2006, 2:08 PM
 */

/**
 *
 * @author  VisokayA
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;


public class jfemonmainPanel extends javax.swing.JPanel {
    public String gSelectedRdrs = null;
    public StringBuffer gsbSelectedRdrs = new StringBuffer();
    public Socket                               sock;
    static public ServerConnection              serverConn;
    private String                              machineName = null;
    private int                                 port = 14001;
    static public DataInputStream               is = null;
    static public DataOutputStream              os = null;
    private tableSort                           sortTable;
    private String                              devServer = "edsysdev.newswires.dowjones.net.";
    private String                              qaServer  = "edsysqa.newswires.dowjones.net.";
    private String                              prodServer = "edsys.newswires.dowjones.net.";
    private JCheckBox                           cbReader;
    public boolean                              bIsApplet = false;
    public HeadlineTableModel tablemodel;
    public JTable table = null;
    private int irow = 0;
    public String font;
    
    HeadlineInfo hi = new HeadlineInfo();
    HashMap detailsMap = new HashMap();
        
    /**
     * Creates new form jfemonmainPanel
     */
    public jfemonmainPanel(boolean isApplet) {
        bIsApplet = isApplet;
        initComponents();
        try{
            FemonConfig.ReadConfigData();
        }
        catch(ConfigException cfge){
            StringBuffer msg = new StringBuffer(64);
            msg.append(cfge.getMessage());
            JOptionPane.showMessageDialog(null,msg,"Error Message",JOptionPane.ERROR_MESSAGE); 
            System.exit(0);
        }
        initServerOptionButtons();
        initCommandButtons();
        initFontCombos();
        jcountLabel.setText("0");
    }
    
    private void initFontCombos(){
        jfontComboBox.setModel(new javax.swing.DefaultComboBoxModel (new String[] { 
            "Arial Black", 
            "Times New Roman",
            "Verdana", 
            "Wingdings" }));
        
        if(FemonConfig.sFontPref != null){
            jfontComboBox.setSelectedItem(FemonConfig.sFontPref);
        }
        
        Formatter.setFont(jfontComboBox.getSelectedItem());
    }
    private void initCommandButtons(){
        jsearchButton.addActionListener(new SearchButtonAction());
        jexitButton.addActionListener(new ExitButtonAction());
        jsearchText.addActionListener(new SearchButtonAction());
    }
    
    public JButton getSearchButton () {
	return jsearchButton;
    }
    
    public JButton getExitButton () {
	return jexitButton;
    }
    
    private class SearchButtonAction implements ActionListener {
         public void actionPerformed(ActionEvent e) {
             SwingWorker worker = new SwingWorker(){
                public Object construct(){
                    Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
                    setCursor(hourglassCursor);
                    if (table != null){
                        tablemodel.clearTableModel();
                        table.removeAll();
                        table.repaint();
                        jcountLabel.setText("0");
                    }
                    String sDate = (String)jdateText.getText();
                    String sStartTime = (String)jstartText.getText();
                    String sEndTime = (String)jendText.getText();
                    jmainFrame.sSearchData = (String)jsearchText.getText();
                    
                    try {
                        if (sock.isConnected() == false) {
                            StringBuffer msg = new StringBuffer(64);
                                    msg.append("FEMON Client is not connected to TOOLSSERVER.");
                                    JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);  
                        }
                        //if (gSelectedRdrs.indexOf("SY") >= 0) {
                       //     return;
                        //} 
                       // else {  
                        
                        
                        System.out.println("Readers: "+gsbSelectedRdrs.toString());
                            jmainFrame.searchResults =serverConn.getHeadline(is, os, gsbSelectedRdrs.toString(), sDate, sStartTime, sEndTime, jmainFrame.sSearchData, sortTable, tablemodel);
                       // }
                        if (jmainFrame.searchResults == null){
                            StringBuffer msg = new StringBuffer(64);
                                    msg.append("No results were found for this query.");
                                    JOptionPane.showMessageDialog(null,msg,"Informational",JOptionPane.INFORMATION_MESSAGE);  
                        }
                        else 
                        {
                                if (table ==null)
                                   initStoryTable();
                                else{
                                    tablemodel.LoadHeadlineTable();
                                     //display story count
                                    int icount = table.getRowCount();
                                    String count = Integer.toString(icount);
                                    jcountLabel.setText(count);
                                }
                        }
                    } 
                    catch (Exception ioEx) {
                        System.out.println("956: ");
                    } 
                Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                setCursor(normalCursor);
                return null;  //return null for threadworker class
             }//end public construct
        };//end swingworker
        worker.start();//new thread stuff
     }
    }
    
    private class ExitButtonAction implements ActionListener {
         public void actionPerformed(ActionEvent e) {
             try{  
                JButton src = (JButton)e.getSource();
                JPanel pan1 = (JPanel)src.getParent();
                JPanel fpan = (JPanel)pan1.getParent();
                JPanel npan = (JPanel)fpan.getParent();
                JLayeredPane lpan = (JLayeredPane)npan.getParent();
                JRootPane rpan = lpan.getRootPane();
                jmainFrame mfpan = (jmainFrame)rpan.getParent();
                mfpan.fontSaveHandler();
             }catch(NullPointerException npe){
                 System.out.println("Error Saving Font via Exit Button");
             }catch(ClassCastException cce){
                 System.out.println("Error Saving Font :(");
             }    
             System.exit(0);
         }
    }
    
     private void initServerOptionButtons(){
        //put all radio buttons into button group
        ButtonGroup bp = new ButtonGroup();
        bp.add(jdevOpt);
        bp.add(jqaOpt);
        bp.add(jprodOpt);
         
        //initialize command for radio buttons
        jqaOpt.setActionCommand(qaServer);
        jdevOpt.setActionCommand(devServer);
        jprodOpt.setActionCommand(prodServer);
        
        jdevOpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
               optionButtonAction(evt);
            }
        });

        jqaOpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
             optionButtonAction(evt);
            }
        });
        
        jprodOpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                optionButtonAction(evt);
            }
        });
    }
    
    private void optionButtonAction(final java.awt.event.ActionEvent evt){
        if (table != null){
            tablemodel.clearTableModel();
            table.removeAll();
            table.repaint();
            jcountLabel.setText("0");
        }
        
        machineName = evt.getActionCommand();
        initReaderCheckBoxes(); 
        jrdrPanel.validate();
        SwingWorker worker = new SwingWorker(){
            public Object construct() {
             
            sock = new Socket();
            try {
                serverConn = new ServerConnection();
                sortTable = new tableSort();
            } catch (Exception ioEx) { }

            try {
                 sock = serverConn.OpenSocket(jmainFrame.jmainframe, machineName, port);
                 if (sock != null) {
                     is = new DataInputStream(sock.getInputStream());
                     os = new DataOutputStream(sock.getOutputStream());
                 } else {
                     return null;
                 }
             } catch (Exception ioEx) {
                 sock = new Socket();
             }
                return null;
            }
        };
        worker.start();       
   }
    
    private void initStoryTable(){
        try{
            tablemodel = new HeadlineTableModel(this);
            tablemodel.LoadHeadlineTable();
            //create table like table model and add a scrollpane
            table = new JTable(tablemodel);    

            JScrollPane sp = new JScrollPane(table);    
            jheadlinePanel.add(sp,BorderLayout.CENTER);
            //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setColumnSelectionAllowed(false);
            table.setRowSelectionAllowed(true);
            
            //set column sizes
            TableColumn col = table.getColumnModel().getColumn(0);
            col.setPreferredWidth(25);
            col = table.getColumnModel().getColumn(1);
            col.setPreferredWidth(65);
            col = table.getColumnModel().getColumn(2);
            col.setPreferredWidth(140);
            col = table.getColumnModel().getColumn(3);
            col.setPreferredWidth(445);
            
            //display story count
            int icount = table.getRowCount();
            String count = Integer.toString(icount);
            jcountLabel.setText(count);
            
            table.addMouseListener(new MouseAdapter(){
             public void mouseClicked(MouseEvent e){
                 int i=-1;
                 if (e.getClickCount() == 2){
                        i = table.getSelectedRow();
                        if (i != -1) {
                            hi.FE=table.getValueAt(i,0).toString();
                            hi.DocID=table.getValueAt(i,1).toString();
                            System.out.println("jfemonDoubleClick");
                            hi.Timestamp= Formatter.unTSFormat(table.getValueAt(i,2).toString());
                            hi.Headline=table.getValueAt(i,3).toString();
                            irow = table.getSelectedRow();
                            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
                            setCursor(hourglassCursor);
                            HandleStoryFrame();
                            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                            setCursor(normalCursor);
                     }
              }
                 else if (e.getClickCount() == 1){
                        i = table.getSelectedRow();
                        if (i != -1) {
                            hi.FE=table.getValueAt(i,0).toString();
                            hi.DocID=table.getValueAt(i,1).toString();
                            System.out.println("jfemonSingleClick");
                            hi.Timestamp= Formatter.unTSFormat(table.getValueAt(i,2).toString());
                            hi.Headline=table.getValueAt(i,3).toString();
                        }
              }
             }} );
         }
        catch(IOException ioexp){
            StringBuffer msg = new StringBuffer(64);
            msg.append("Error loading headline table.  Exception: " + ioexp.getMessage());
            JOptionPane.showMessageDialog(null,msg,"Error Message",JOptionPane.ERROR_MESSAGE);
        }
     }
    
    public JTable getTable(){
        return table;
    }  
   
    private void HandleStoryFrame(){
    jmainFrame.jstoryframe = (JFrame)detailsMap.get(hi.Headline);
        if (jmainFrame.jstoryframe == null){
            jmainFrame.jstoryframe = new jstoryFrame(hi,irow);
            jmainFrame.jstoryframe.addWindowListener(new java.awt.event.WindowAdapter(){
                public void windowClosed(java.awt.event.WindowEvent e){
                    JFrame source = (JFrame)e.getSource();
                    if (!detailsMap.containsValue(source))
                        return;
                    java.util.Iterator i = detailsMap.keySet().iterator();
                    String key = null;
                    while (i.hasNext()){
                        String k = (String)i.next();
                        JFrame f = (JFrame) detailsMap.get(k);
                        if (f == source){
                            key = k;
                            break;
                        }
                    }
                    if (key !=null)
                        detailsMap.remove(key);
                }
            });
            jmainFrame.jstoryframe.setVisible(true);
            detailsMap.put(hi.Headline,jmainFrame.jstoryframe);
        }
        else{
            jmainFrame.jstoryframe.toFront();
        }
    }
        
    private void initReaderCheckBoxes(){
        String rdrs=null;
        jrdrPanel.removeAll();
        if (machineName.equals(prodServer))
           rdrs = FemonConfig.sProdReaders;
        else if (machineName.equals(qaServer))
           rdrs = FemonConfig.sQAReaders;
        else if (machineName.equals(devServer))         
            rdrs = FemonConfig.sDevReaders;
            
        String[] readers = FemonRoutines.StringtoArray(rdrs,",");
        String read = null;        

        //loop through and put each reader into an array
        for (int i=0; i < readers.length; i++){
            read = readers[i];
            cbReader = new JCheckBox(read);
            jrdrPanel.add(cbReader);

            cbReader.setActionCommand(read);
            cbReader.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                        checkBoxReaderAction(evt);
                    }
                });
        }
    }
    
    private void checkBoxReaderAction(final java.awt.event.ActionEvent evt)
    {
        int a=0;
        String tempRdr=null;
        tempRdr = evt.getActionCommand();
        Component component[] = new Component[20];
        component = jrdrPanel.getComponents();
        
        if (tempRdr.equals("ALL")){
             
             if (gsbSelectedRdrs.length()>0)
                gsbSelectedRdrs.delete(0,gsbSelectedRdrs.length());
             for (int i=0; i<component.length; i++) {
                if (component[i] instanceof JCheckBox) {
                    if( ((JCheckBox)component[0]).isSelected()==false)
                    {
                        ((JCheckBox)component[i]).setSelected(false);
                    }
                    else
                    {
                        ((JCheckBox)component[i]).setSelected(true);
                        gsbSelectedRdrs.append(((JCheckBox)component[i]).getText());
                        gsbSelectedRdrs.append(",");
                    }
                }
             }
             if (gsbSelectedRdrs.length()>0)
             {
                 gsbSelectedRdrs.delete(0,4);
                 gsbSelectedRdrs.delete(gsbSelectedRdrs.length()-1,gsbSelectedRdrs.length());
             }
        }
        else
        {
            ((JCheckBox)component[0]).setSelected(false);
            a = gsbSelectedRdrs.indexOf(tempRdr);
            if (a == -1){
                if (gsbSelectedRdrs.length()>0)
                    gsbSelectedRdrs.append(",");
                gsbSelectedRdrs.append(tempRdr);
            }
            else if (a < 3) 
            {
                a = 0; 
                gsbSelectedRdrs.delete(a, a+3);
            }
            else 
            {
                    a--;
                    gsbSelectedRdrs.delete(a, a+3);
            }
        }
      //  System.out.println(gsbSelectedRdrs);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jmachinePanel = new javax.swing.JPanel();
        jprodOpt = new javax.swing.JRadioButton();
        jqaOpt = new javax.swing.JRadioButton();
        jdevOpt = new javax.swing.JRadioButton();
        jrdrPanel = new javax.swing.JPanel();
        jsearchPanel = new javax.swing.JPanel();
        jtimePanel = new javax.swing.JPanel();
        jsearchButton = new javax.swing.JButton();
        jendText = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jstartText = new javax.swing.JTextField();
        jdatetextPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jdateText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jsearchText = new javax.swing.JTextField();
        regExCheckBix = new javax.swing.JCheckBox();
        jheadlinePanel = new javax.swing.JPanel();
        jbuttonPanel = new javax.swing.JPanel();
        jcountLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jexitButton = new javax.swing.JButton();
        jfontComboBox = new javax.swing.JComboBox();

        setPreferredSize(new java.awt.Dimension(700, 700));
        setLayout(new java.awt.GridBagLayout());

        jmachinePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Server:"));
        jmachinePanel.setLayout(new java.awt.GridBagLayout());

        jprodOpt.setText("Production Server");
        jprodOpt.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jprodOpt.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 20);
        jmachinePanel.add(jprodOpt, gridBagConstraints);

        jqaOpt.setText("QA Server");
        jqaOpt.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jqaOpt.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        jmachinePanel.add(jqaOpt, gridBagConstraints);

        jdevOpt.setText("Development Server");
        jdevOpt.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jdevOpt.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 25);
        jmachinePanel.add(jdevOpt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.03;
        add(jmachinePanel, gridBagConstraints);

        jrdrPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Readers:"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.1;
        add(jrdrPanel, gridBagConstraints);

        jsearchPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Search Criteria:"));
        jsearchPanel.setLayout(new java.awt.GridBagLayout());

        jtimePanel.setLayout(new java.awt.GridBagLayout());

        jsearchButton.setText("Search");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        jtimePanel.add(jsearchButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        jtimePanel.add(jendText, gridBagConstraints);

        jLabel3.setText("End Time (hhmm):");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jtimePanel.add(jLabel3, gridBagConstraints);

        jLabel2.setText("Start Time (hhmm):");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jtimePanel.add(jLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jtimePanel.add(jstartText, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jsearchPanel.add(jtimePanel, gridBagConstraints);

        jdatetextPanel.setLayout(new java.awt.GridBagLayout());

        jLabel4.setText("Search Text:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jdatetextPanel.add(jLabel4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jdatetextPanel.add(jdateText, gridBagConstraints);

        jLabel1.setText("Date (yyyymmdd):");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jdatetextPanel.add(jLabel1, gridBagConstraints);

        jsearchText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jsearchTextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 2);
        jdatetextPanel.add(jsearchText, gridBagConstraints);

        regExCheckBix.setText("RegEx Search");
        regExCheckBix.setToolTipText("Search with Regular Expression");
        regExCheckBix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regExCheckBixActionPerformed(evt);
            }
        });
        jdatetextPanel.add(regExCheckBix, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jsearchPanel.add(jdatetextPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.05;
        add(jsearchPanel, gridBagConstraints);

        jheadlinePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        add(jheadlinePanel, gridBagConstraints);

        jbuttonPanel.setLayout(new java.awt.GridBagLayout());

        jcountLabel.setLabelFor(jLabel5);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jbuttonPanel.add(jcountLabel, gridBagConstraints);

        jLabel5.setText("Grid Count:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 200, 0, 0);
        jbuttonPanel.add(jLabel5, gridBagConstraints);

        jexitButton.setText("Exit");
        jexitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jexitButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 225, 0, 15);
        jbuttonPanel.add(jexitButton, gridBagConstraints);

        jfontComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jfontComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jfontComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jbuttonPanel.add(jfontComboBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.05;
        add(jbuttonPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jexitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jexitButtonActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_jexitButtonActionPerformed

    private void jsearchTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jsearchTextActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jsearchTextActionPerformed

    private void regExCheckBixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regExCheckBixActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_regExCheckBixActionPerformed

    private void jfontComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jfontComboBoxActionPerformed
        
        if (jfontComboBox.getSelectedItem() != null){
            font = (String)jfontComboBox.getSelectedItem();
            Formatter.setFont(font);
            Formatter.updateFont(table);
            Formatter.updateFont(jrdrPanel);
            this.revalidate();
            
        }
    }//GEN-LAST:event_jfontComboBoxActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jbuttonPanel;
    private javax.swing.JLabel jcountLabel;
    private javax.swing.JTextField jdateText;
    private javax.swing.JPanel jdatetextPanel;
    private javax.swing.JRadioButton jdevOpt;
    private javax.swing.JTextField jendText;
    private javax.swing.JButton jexitButton;
    private javax.swing.JComboBox jfontComboBox;
    private javax.swing.JPanel jheadlinePanel;
    private javax.swing.JPanel jmachinePanel;
    private javax.swing.JRadioButton jprodOpt;
    private javax.swing.JRadioButton jqaOpt;
    private javax.swing.JPanel jrdrPanel;
    private javax.swing.JButton jsearchButton;
    private javax.swing.JPanel jsearchPanel;
    private javax.swing.JTextField jsearchText;
    private javax.swing.JTextField jstartText;
    private javax.swing.JPanel jtimePanel;
    private javax.swing.JCheckBox regExCheckBix;
    // End of variables declaration//GEN-END:variables
    
}
