/*
 * jstoryPanel.java
 *
 * Created on December 5, 2006, 11:27 AM
 */

/**
 *
 * @author  VisokayA
 */
import java.io.*;
import java.util.*;
import java.lang.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.JComponent;
import java.sql.*;

public class jstoryPanel extends javax.swing.JPanel {
    JFrame myFrame;
    private StringBuffer		selectedProd = new StringBuffer(2);
    private int				flag = 0;
    private String			sText = null;
    private String			sPatteren = null;
    private HeadlineInfo hInfo;
    private int                         irow = 0;
    private javax.swing.JPopupMenu      popupMenu;
    private javax.swing.JMenuItem       menu;
    
    /** Creates new form jstoryPanel */
    public jstoryPanel(JFrame parent) {
        myFrame = parent;
        initComponents();
        myinitComponents();
        initOptionButtons();
        initContentSets();
        /*String command = JOptionPane.showInputDialog("Woot");
            String[] commands = command.split(" ");
            String[] test = {"java","-jar","C:\\test.jar"};
            try{
                Process p = new java.lang.ProcessBuilder(test).start();
            }catch(IOException ex){
                ex.printStackTrace();
            }*/
        if (jmainFrame.bIsApplet==false)
            initPopupMenu();
    }
    
    private void myinitComponents(){
        jstorytextArea.setEditable(false);
        sPatteren = jmainFrame.sSearchData;
        jexitButton.addActionListener(new ExitButtonAction());
        jpreviousButton.addActionListener(new PreviousAction());
        jnextButton.addActionListener(new NextAction());
    }
    
    private void initContentSets(){
        ButtonGroup bpCS = new ButtonGroup();
        bpCS.add(optDN);
        bpCS.add(optSN);
        bpCS.add(optLL);
        bpCS.add(optPB);
        bpCS.add(optTL);
        bpCS.add(optRT);
        optDN.addActionListener(new ContentSetAction());
        optLL.addActionListener(new ContentSetAction());
        optSN.addActionListener(new ContentSetAction());
        optPB.addActionListener(new ContentSetAction());
        optTL.addActionListener(new ContentSetAction());
        optRT.addActionListener(new ContentSetAction());
    }
    
    private void DisplayContentSets(){
        boolean bFlagSet = false;
	optDN.setEnabled(false);
        optLL.setEnabled(false);
        optSN.setEnabled(false);
        optPB.setEnabled(false);
        optTL.setEnabled(false);
        optRT.setEnabled(false);
        jcscOpt.setEnabled(true);
        jfeOpt.setEnabled(true);
        jntmOpt.setEnabled(false);
        
        String prod = jmainFrame.jfemonPanel.serverConn.getContentSets();
        System.out.println("Content Sets "+prod);
        if (prod.contains("DN")==true){
            optDN.setEnabled(true);
            optDN.setSelected(true);
            bFlagSet = true;
        }
        else{
            optDN.setEnabled(false);
        }
        if (prod.contains("SN")==true){
            optSN.setEnabled(true);
            if (bFlagSet == false){                
                optSN.setSelected(true);
                bFlagSet = true;
            }
        }
        else{
            optSN.setEnabled(false);
        }
        if (prod.contains("LL")==true){
            optLL.setEnabled(true);
            if (bFlagSet == false){
                optLL.setSelected(true);
                bFlagSet = true;
            }
        }
        else{
            optLL.setEnabled(false);
        }
        if (prod.contains("PB")==true){
            optPB.setEnabled(true);
            if (bFlagSet == false){
                optPB.setSelected(true);
                bFlagSet =true;
            }
        }
        else{
            optPB.setEnabled(false);
        }
        if (prod.contains("TL")==true){
            optTL.setEnabled(true);
            jtelerateOpt.setEnabled(false);
        }
        else{
            optTL.setEnabled(false);
            jtelerateOpt.setEnabled(false);
        }
        if (prod.contains("RT")==true){
            optRT.setEnabled(true);
            jreutersOpt.setEnabled(false);
        }
        else{
            optRT.setEnabled(false);
            jreutersOpt.setEnabled(false);
        }
    }
    
    private void initOptionButtons(){
        ButtonGroup bpIO = new ButtonGroup();
        bpIO.add(jfeOpt);
        bpIO.add(jcscOpt);
        bpIO.add(jreutersOpt);
        bpIO.add(jtelerateOpt);
        bpIO.add(jndfOpt);
        jfeOpt.addActionListener(new InputOutputAction());
        jcscOpt.addActionListener(new InputOutputAction());
        jtelerateOpt.addActionListener(new InputOutputAction());
        jreutersOpt.addActionListener(new InputOutputAction());
        jndfOpt.addActionListener(new InputOutputAction());
        
        ButtonGroup bpFormat = new ButtonGroup();
        bpFormat.add(jspufOpt);
        bpFormat.add(jasciiOpt);
        bpFormat.add(jntmOpt);
        bpFormat.add(jcff1aOpt);
        jntmOpt.setEnabled(false);
        jcff1aOpt.setEnabled(false);
        jspufOpt.addActionListener(new FormatAction());
        jasciiOpt.addActionListener(new FormatAction());
        jntmOpt.addActionListener(new FormatAction());
        jcff1aOpt.addActionListener(new FormatAction());
    }
    
    private class ContentSetAction implements ActionListener {
         public void actionPerformed(ActionEvent e) {
             if (optRT.isSelected()==true){
                 jcscOpt.setSelected(false);
                 jcscOpt.setEnabled(false);
                 jreutersOpt.setEnabled(true);
                 jreutersOpt.setSelected(true);
                 jtelerateOpt.setEnabled(false);
                 jreutersOpt.doClick();
             }
             else if (optTL.isSelected()==true){
                 jcscOpt.setSelected(false);
                 jcscOpt.setEnabled(false);
                 jtelerateOpt.setEnabled(true);
                 jtelerateOpt.setSelected(true);
                 jreutersOpt.setEnabled(false);
                 jtelerateOpt.doClick();
             }
             else{
                 jcscOpt.setEnabled(true);
                 jtelerateOpt.setEnabled(false);
                 jreutersOpt.setEnabled(false);
                 if (jfeOpt.isSelected()==true)
                     jfeOpt.doClick();
                 else if (jcscOpt.isSelected()==true)
                     jcscOpt.doClick();
                 else if (jndfOpt.isSelected()==true)   
                     jndfOpt.doClick();
                 else{
                     jfeOpt.setSelected(true);
                     jfeOpt.doClick();
                 }
             }
         }
    }
    
    private class InputOutputAction implements ActionListener {
         public void actionPerformed(ActionEvent e) {
             if (jfeOpt.isSelected()==true){
                 try{
                    jntmOpt.setEnabled(false);
                    jproductLabel.setText("");
                    getFrontEndStory(false);
                    jspufOpt.setSelected(true);
                 }
                 catch (Exception ioEx) { }
             }
             else if(jcscOpt.isSelected()==true){
                 try{
                    jntmOpt.setEnabled(false);
                    jcff1aOpt.setEnabled(false);
                    selectedProd.delete(0, 2);
                    if (optDN.isSelected()==true)
                        selectedProd.append("DN");
                    else if(optSN.isSelected()==true)
                        selectedProd.append("SN");
                    else if(optLL.isSelected()==true)
                        selectedProd.append("LL");
                    else if(optPB.isSelected()==true)
                        selectedProd.append("PB");
                    jspufOpt.setSelected(true);
                    getStory(null);
                 }
                 catch (Exception ioEx) { }
             }
             else if (jtelerateOpt.isSelected()==true){
                jntmOpt.setEnabled(false);
                jcff1aOpt.setEnabled(true);
                optTL.setSelected(true);
                selectedProd.delete(0, 2);
                selectedProd.append("TL");
                jspufOpt.setSelected(true);
                getStory(null);
             }
             else if (jreutersOpt.isSelected() ==true){
                jntmOpt.setEnabled(true);
                jcff1aOpt.setEnabled(false);
                optRT.setSelected(true);
                selectedProd.delete(0, 2);
                selectedProd.append("RT");
                jspufOpt.setSelected(true);
                getStory(null);
             }
             else if (jndfOpt.isSelected()==true){
                 formatNDF();
             }
         }
    }
    
    private class FormatAction implements ActionListener {
         public void actionPerformed(ActionEvent e) {
             if (jspufOpt.isSelected()==true){
                 try{
                    if (flag == 1) {
                        jstorytextArea.setText(sText);
                        jstorytextArea.setCaretPosition(0);
                    } else {
                        jstorytextPane.setText(sText);
                        jstorytextPane.setCaretPosition(0);
                    }
                    if(sPatteren.length() > 0)
                        highlight();
                }            
                catch (Exception ioEx) { }
             }
             else if(jasciiOpt.isSelected()==true){
                try{
                    printAscii();
                    if (flag == 1)
                        jstorytextArea.setCaretPosition(0);
                    else
                        jstorytextPane.setCaretPosition(0);
                    if(sPatteren.length() > 0)
                        highlight();
                 }
                 catch (Exception ioEx) { }
             }
             else if (jntmOpt.isSelected()==true){
                 formatRTPayload();
             }
             else if (jcff1aOpt.isSelected() ==true){
                 formatTLPayload();
             }
         }
    }
    
    private void printAscii() {
	int		k = 0;
	int[]		cntlCnt = new int[10000];

	try {
	    String		textArray= null;
	    Document		doc = null;
	    if (flag == 1) {
		doc = jstorytextArea.getDocument();
		textArray = jstorytextArea.getText();
		jstorytextArea.setText("");
	    } else {
		doc = jstorytextPane.getDocument();
		textArray = jstorytextPane.getText();
		jstorytextPane.setText("");
	    }

	    Character		ch = new Character(textArray.charAt(0));
	    Integer		I = new Integer(1);
	    StringBuffer	cntl = new StringBuffer();
	    StringBuffer	str = new StringBuffer(512);
	    int			j = 0;
	    for (int i = 0; i < textArray.length(); i++) {
		if (ch.isISOControl(textArray.charAt(i))) {
		    if (j != 0) {
			str.append(textArray.substring(i-j, i));	
			doc.insertString(doc.getLength(), str.toString(), null);
			str.delete(0, str.toString().length());
			j = 0;
		    }
		    cntl.delete(0, cntl.toString().length());
		    if ((I.toHexString((int)textArray.charAt(i))).length() == 1)
			cntl.append("0");
		    cntl.append(I.toHexString((int)textArray.charAt(i)));

		    cntlCnt[k++] = doc.getLength();
		    doc.insertString(doc.getLength(), cntl.toString(), null);

		    if (textArray.charAt(i) == '\n') {
		        doc.insertString(doc.getLength(),
				ch.toString(textArray.charAt(i)), null);
		    }
		} else {
		    j++;
		}
	    }
	} catch (BadLocationException ble) {
	    System.err.println("Couldn't insert initial text.");
	}
	try {
	    Highlighter.HighlightPainter myPainter = new MyHighlightPainter(Color.CYAN);
	    Highlighter hilite = null;
	    if (flag == 1) {
		hilite = jstorytextArea.getHighlighter();
	    } else {
		hilite = jstorytextPane.getHighlighter();
	    }
	    for (int i = 0; i < k; i++) {
		hilite.addHighlight(cntlCnt[i], cntlCnt[i]+2, myPainter);
	    }
	} catch (BadLocationException ble) {}
    }
    
    private void formatRTPayload() {

	try {
	    int			pos = 0;
	    String		payload = null;
	    Document		doc;
	    Highlighter.HighlightPainter myPainter = new MyHighlightPainter(Color.CYAN);
	    Highlighter hilite = null;

	    if (flag == 1) {
		hilite = jstorytextArea.getHighlighter();
		doc = jstorytextArea.getDocument();
		payload = jstorytextArea.getText();
		jstorytextArea.setText("");
	    } else {
		hilite = jstorytextPane.getHighlighter();
		doc = jstorytextPane.getDocument();
		payload = jstorytextPane.getText();
		jstorytextPane.setText("");
	    }

	    pos = payload.indexOf("AN_Payload");
	    pos = payload.indexOf(")", pos);
	    pos = payload.indexOf("'", pos);
	    String	s1 = payload.substring(pos+1, payload.length()-1);

	    if (flag == 1) {
		jstorytextArea.setText(s1);
		jstorytextArea.setCaretPosition(0);
	    } else {
		jstorytextPane.setText(s1);
		jstorytextPane.setCaretPosition(0);
	    }
	
	} catch (Exception ioEx) {
	    System.out.println("Exception in findString()");
	}
    }
    
    private void formatNDF() {

	try {
	    int			pos = 0;
	    String		payload = null;
	    Document		doc;
	    Highlighter.HighlightPainter myPainter = new MyHighlightPainter(Color.CYAN);
	    Highlighter hilite = null;
	    
            if (flag == 1) {
		hilite = jstorytextArea.getHighlighter();
		doc = jstorytextArea.getDocument();
		payload = jstorytextArea.getText();
		jstorytextArea.setText("");
	    } else {
		hilite = jstorytextPane.getHighlighter();
		doc = jstorytextPane.getDocument();
		payload = jstorytextPane.getText();
		jstorytextPane.setText("");
	    }
	    
	    String	s1 = payload.substring(pos+1, payload.length()-1);
            s1=s1.replaceAll("<","\n<");
            s1=s1.replaceAll(">",">\n");
           
            if (flag == 1) {
		jstorytextArea.setText(s1);
		jstorytextArea.setCaretPosition(0);
	    } else {
		jstorytextPane.setText(s1);
		jstorytextPane.setCaretPosition(0);
	    }
            if(sPatteren.length() > 0)
                highlight();
	    sText = null;
	    sText = s1;
            
	} catch (Exception ioEx) {
	    System.out.println("Exception in findString()");
	}
    }
    
     private void formatTLPayload() {
	try {
	    int			pos = 0;
	    int			index2 = 0;
	    String		payload = null;
	    Document		doc;
	    Highlighter.HighlightPainter myPainter = new MyHighlightPainter(Color.CYAN);
	    Highlighter hilite = null;

	    if (flag == 1) {
		hilite = jstorytextArea.getHighlighter();
		doc = jstorytextArea.getDocument();
		payload = jstorytextArea.getText();
		jstorytextArea.setText("");
	    } else {
		hilite = jstorytextPane.getHighlighter();
		doc = jstorytextPane.getDocument();
		payload = jstorytextPane.getText();
		jstorytextPane.setText("");
	    }

	    pos = payload.indexOf("AN_Payload");
	    pos = payload.indexOf(")", pos);
	    pos = payload.indexOf("'", pos);
	    index2 = payload.indexOf('\u0004', pos);
	    String	s1 = payload.substring(pos+1, index2-2);

	    if (flag == 1) {
		jstorytextArea.setText(s1);
		jstorytextArea.setCaretPosition(0);
	    } else {
		jstorytextPane.setText(s1);
		jstorytextPane.setCaretPosition(0);
	    }
	    sText = null;
	    sText = s1;

	    
	} catch (Exception ioEx) {
	    System.out.println("Exception in findString()");
	}
    }

    private void getProducts(String contSet) {
	String		str = sText;
	StringBuffer	prods = new StringBuffer(2);
	int		index1;
	int		index2;

        prods.delete(0,prods.length());
        jproductLabel.setText("");
	if (contSet.equals("RT") == true) {
	    index1 = str.indexOf("AN_Payload ");
	    index1 = str.indexOf(")", index1);
	    while (true) {
		index1 = str.indexOf('\u001c', index1);
		if (index1 < 0)
		    break;

		index1++;
		String x = str.substring(index1, index1+2);
		if (x.equals("01") == true) {
		    index2 = str.indexOf('\u001c', index1+1);
		    String	xx = str.substring(index1+2, index2);
		    prods.append(xx.trim().replaceAll("\\s", "    "));
                    jproductLabel.setText(prods.toString());
		    break;
		}
	    }
	} else if (contSet.equals("TL") == true) {
	    index1 = str.indexOf("AN_PayloadPerm ");
	    index1 = str.indexOf(")", index1);
	    index1 = str.indexOf("\'", index1);
	    index2 = str.indexOf("\'", index1+1);
	    String	xx = str.substring(index1+1, index2);
	    prods.append(xx.trim().replaceAll("\\s", "    "));
	    jproductLabel.setText(prods.toString());
	} else {
	    index1 = str.indexOf("AN_Product ");
	    String	xx = str.substring(index1, index1+55);

	    while (index1 < str.length()) {
		index1 = str.indexOf("AN_Product  ", index1+1);
		if (index1 < 0) {
		    prods.delete(prods.length()-2, prods.length());
		    break;
		}
		String	x = str.substring(index1, index1+55);
		prods.append(x.substring(51, 53));
		prods.append("    ");
	    }
            jproductLabel.setText(prods.toString());
	}
    } 
     
    private void getStory(String format) {
	int	reqType;

	try {
	    if (jmainFrame.jfemonPanel.serverConn.sock.isConnected() == false) {
		String msg =
		    new String("FEMON Client is not connected to TOOLSSERVER.");
		JOptionPane.showMessageDialog(this, msg,
		"Error Message", JOptionPane.ERROR_MESSAGE);
		//dispose();
	    } else {
		StringBuffer    headline = new StringBuffer(32);
		if (format == null) {
		    headline.append("");
		    reqType = 1;
		} else {
		    String str = jmainFrame.jfemonPanel.serverConn.getServerName();
		    if (str.equalsIgnoreCase("ED2PROD") == true) {
			String msg = new String("ED2PROD Toolsserver deos not support o/p formats.");
			JOptionPane.showMessageDialog(this, msg,
			"Error Message", JOptionPane.ERROR_MESSAGE);
			//sock = new Socket();
			//dispose();
			return;
		    }
		    headline.append(format);
		    reqType = 3;
		}
		headline.append(" ");
		headline.append("C");
		headline.append(" ");
		headline.append(selectedProd.toString());

		headline.append(" ");
                headline.append(hInfo.DocID);
		headline.append(" ");
                headline.append(hInfo.Timestamp);
                System.out.println("jstor1"+hInfo.Timestamp);
	System.out.println("getStory:" +headline.toString());
		sText = jmainFrame.jfemonPanel.serverConn.getStory(jmainFrame.jfemonPanel.is, jmainFrame.jfemonPanel.os, reqType, headline.toString());
		if (sText == null) {
		    String msg = new String("TOOLSSERVER is down.");
		    JOptionPane.showMessageDialog(this, msg,
			"Error Message", JOptionPane.ERROR_MESSAGE);
		    jmainFrame.jfemonPanel.serverConn.sock = new Socket();
		//    dispose();
		}
		if (flag == 1) {
		    jstorytextArea.setText("");
		    jstorytextArea.setText(sText);
		    jstorytextArea.setCaretPosition(0);
		} else {
		    jstorytextPane.setText("");
		    jstorytextPane.setText(sText);
		    jstorytextPane.setCaretPosition(0);
		}
		if (sPatteren.length() > 0)
		    highlight();
		getProducts(selectedProd.toString());
	    }
	} catch (Exception ioEx) { }
    }
    
    private void getFrontEndStory(boolean bContents) {
	try {
	    if (jmainFrame.jfemonPanel.serverConn.sock.isConnected() == false) {
		String msg =
		    new String("FEMON Client is not connected to TOOLSSERVER.");
		JOptionPane.showMessageDialog(this, msg,
		"Error Message", JOptionPane.ERROR_MESSAGE);
		//dispose();
	    } else {
               
                if (irow != -1) {
                    hInfo.FE=jmainFrame.jfemonPanel.table.getValueAt(irow,0).toString();
                    hInfo.DocID=jmainFrame.jfemonPanel.table.getValueAt(irow,1).toString();
                    //hInfo.Timestamp = jmainFrame.jfemonPanel.table.getValueAt(irow, 2).toString();
                    hInfo.Timestamp = Formatter.unTSFormat(jmainFrame.jfemonPanel.table.getValueAt(irow, 2).toString());
                    hInfo.Headline=jmainFrame.jfemonPanel.table.getValueAt(irow,3).toString();
                }                
                
		StringBuffer    headline = new StringBuffer(32);
		//headline.append((String)table.getValueAt(row,0));
		headline.append("");
		headline.append(" ");
		headline.append("F");
		headline.append(" ");
		headline.append(hInfo.FE);
		headline.append(" ");
		headline.append(hInfo.DocID);
		headline.append(" ");
		headline.append(hInfo.Timestamp);
		headline.append(" ");
		headline.append(hInfo.Headline);
                System.out.println("jstor2"+hInfo.Timestamp);
		sText = jmainFrame.jfemonPanel.serverConn.getStory(jmainFrame.jfemonPanel.is, jmainFrame.jfemonPanel.os, 1, headline.toString());
		if (sText == null) {
		    String msg = new String("TOOLSSERVER is down.");
		    JOptionPane.showMessageDialog(this, msg,
			"Error Message", JOptionPane.ERROR_MESSAGE);
		    jmainFrame.jfemonPanel.serverConn.sock = new Socket();
		    //dispose();
		}
		if (flag == 1) {
		    jstorytextArea.setText("");
		    jstorytextArea.setText(sText);
		    jstorytextArea.setCaretPosition(0);
		} else {
		    jstorytextPane.setText("");
		    jstorytextPane.setText(sText);
		    jstorytextPane.setCaretPosition(0);
		}
		if (sPatteren.length() > 0)
		    highlight();
                if (bContents == false)
                    DisplayContentSets();
                else
                    getProducts(selectedProd.toString());
                
                jmainFrame.jstoryframe.setTitle(hInfo.FE+hInfo.Timestamp);
	    }
	} catch (Exception ioEx) { }
    }
    
    public void initStoryInfo(HeadlineInfo hi, int row){ 
        
            try {
                hInfo = hi;
                irow =row;
	    if (jmainFrame.jfemonPanel.serverConn.sock.isConnected() == false) {
		String msg =
		    new String("FEMON Client is not connected to TOOLSSERVER.");
		JOptionPane.showMessageDialog(this, msg,
		"Error Message", JOptionPane.ERROR_MESSAGE);
		//dispose();
	    } else {
		StringBuffer    headline = new StringBuffer(32);
		headline.append("");
		headline.append(" ");
		headline.append("F");
		headline.append(" ");
                headline.append(hi.FE);
		headline.append(" ");
                headline.append(hi.DocID);
		headline.append(" ");
                headline.append(hi.Timestamp);
		headline.append(" ");
                headline.append(hi.Headline);
		sText = jmainFrame.jfemonPanel.serverConn.getStory(jmainFrame.jfemonPanel.is, jmainFrame.jfemonPanel.os, 1, headline.toString());
                if (sText == null) {
		    String msg = new String("TOOLSSERVER is down.");
		    JOptionPane.showMessageDialog(this, msg,
			"Error Message", JOptionPane.ERROR_MESSAGE);
		    jmainFrame.jfemonPanel.serverConn.sock = new Socket();
		   // dispose();
		}
		if (flag == 1) {
		    jstorytextArea.setText("");
		    jstorytextArea.setText(sText);
		    jstorytextArea.setCaretPosition(0);
		} else {
		    jstorytextPane.setText("");
		    jstorytextPane.setText(sText);
		    jstorytextPane.setCaretPosition(0);
		}
		if (sPatteren.length() > 0)
		    highlight();
		
                DisplayContentSets();
                jmainFrame.jstoryframe.setTitle(hInfo.FE+hInfo.Timestamp);
	    }
	} catch (Exception ioEx) { }
    }
    
    
    
  /* private void getStory() {
	int	reqType;

	try {
	    if (jmainFrame.jfemonPanel.serverConn.sock.isConnected() == false) {
		String msg =
		    new String("FEMON Client is not connected to TOOLSSERVER.");
		JOptionPane.showMessageDialog(this, msg,
		"Error Message", JOptionPane.ERROR_MESSAGE);
	    } else {
		StringBuffer    headline = new StringBuffer(32);
	        reqType = 3;
		headline.append(" ");
		headline.append("C");
		headline.append(" ");
		headline.append(selectedProd.toString());

		headline.append(" ");
		headline.append(jmainFrame.jfemonPanel.hi.DocID);
		headline.append(" ");
		headline.append(jmainFrame.jfemonPanel.hi.Timestamp);
		
	System.out.println("getStory:" +headline.toString());
		sText = jmainFrame.jfemonPanel.serverConn.getStory(jmainFrame.jfemonPanel.is, jmainFrame.jfemonPanel.os, reqType, headline.toString());
		if (sText == null) {
		    String msg = new String("TOOLSSERVER is down.");
		    JOptionPane.showMessageDialog(this, msg,
			"Error Message", JOptionPane.ERROR_MESSAGE);
		    jmainFrame.jfemonPanel.serverConn.sock = new Socket();
		}
		if (flag == 1) {
		    jstorytextArea.setText("");
		    jstorytextArea.setText(sText);
		    jstorytextArea.setCaretPosition(0);
		} else {
		    jstorytextPane.setText("");
		    jstorytextPane.setText(sText);
		    jstorytextPane.setCaretPosition(0);
		}

                if (sPatteren.length() > 0)
		    highlight();

		getContentSets(selectedProd.toString());
	    }
	} catch (Exception ioEx) { }
    }
    */
   public void highlight() {
	try {
	    Highlighter    hilite;
	    Document       doc;
	    if (flag == 1) {
		hilite = jstorytextArea.getHighlighter();
		doc = jstorytextArea.getDocument();
	    } else {
		hilite = jstorytextPane.getHighlighter();
		doc = jstorytextPane.getDocument();
	    }
	    int		pos = 0;
	    String	s1 = doc.getText(0, doc.getLength()).toLowerCase();
	    String	s2 = new String(sPatteren).toLowerCase();

	    // Search for pattern
	    while ((pos = s1.indexOf(s2, pos)) >= 0) {
		// Create highlighter using private painter and apply around pattern
		hilite.addHighlight(pos, pos+sPatteren.length(), myHighlightPainter);
		pos += sPatteren.length();
		if (pos >= s1.length())
		    break;
	    }
	} catch (BadLocationException e) { }
    }
   
   // An instance of the private subclass of the default highlight painter
    Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(Color.LIGHT_GRAY);
    
    // A private subclass of the default highlight painter
    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
	public MyHighlightPainter(Color color) {
	    super(color);
	}
    }
    
    private class ExitButtonAction implements ActionListener { 
        public void actionPerformed(java.awt.event.ActionEvent evt){
            myFrame.setVisible(false);
            myFrame.dispose();
        }
    }
    
    private class PreviousAction implements ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent evt){
            jfeOpt.setSelected(true);
            irow--;
            jproductLabel.setText("");
            //selectedProd.append("DN");
            getFrontEndStory(false);
        }
    }
    
    private class NextAction implements ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent evt){
            jfeOpt.setSelected(true);
            irow++;
            jproductLabel.setText("");
            getFrontEndStory(false);
        }
    }
    
    //popup menu code
    private void initPopupMenu(){
       popupMenu = new JPopupMenu();
	menu = new JMenuItem("Save(" +System.getProperty("user.home") +")");
        menu.addActionListener (new java.awt.event.ActionListener () {
            public void actionPerformed (java.awt.event.ActionEvent evt) {
                menuActionHandler (evt);
            }
        });

	popupMenu.add(menu);

        //Add listener to components that can bring up popup menus.
        MouseListener popupListener = new PopupListener();
        
        createTextPane();
        jstorytextArea.addMouseListener(popupListener);
        jstorytextPane.addMouseListener(popupListener);

        StringBuffer  x = new StringBuffer(24);
        x.append((String)jmainFrame.jfemonPanel.table.getValueAt(irow, 0));
                            System.out.println("jstoryPan");
        x.append(Formatter.unTSFormat((String)jmainFrame.jfemonPanel.table.getValueAt(irow,2)));
    }
            
    public void menuActionHandler(ActionEvent e) {
	File		saveFile = null;
	FileWriter	fw = null;
	StringBuffer	savePath = new StringBuffer(128);

	try {
	    savePath.append(System.getProperty("user.home"));
	    savePath.append(System.getProperty("file.separator"));
            savePath.append(jmainFrame.jfemonPanel.table.getValueAt(irow,1));
	    System.out.println("Save Path: " +savePath.toString());
	    saveFile = new File(savePath.toString());
	    fw = new FileWriter(saveFile);
	    fw.write(sText, 0, sText.length());
	    fw.flush();
	    fw.close();
	} catch (Exception ioEx) {
	    System.out.println("Exception");
            System.out.println(ioEx.getMessage());
	    //ioEx.printStackTrace();
	}
    }
   
    class PopupListener extends MouseAdapter {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
	    Object o = evt.getSource();
            if (o.equals(jstorytextArea) == false)
                return;
            if (o.equals(jstorytextPane) == false)
                return;
	}

        public void mousePressed(MouseEvent e) {
            if (e.getButton() != 3)
                return;
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            if (e.getButton() != 3)
                return;
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    private void createTextPane() {
	if (flag == 1) {
	    Font f = new Font("Lucida Console",Font.PLAIN,12);
	
	    //jstorytextArea.setFont(f);
	    jstorytextArea.setEditable(false);
	    jstorytextArea.setLineWrap(true);
	    jstorytextArea.setWrapStyleWord(true);
	    jstorytextArea.setText(sText);
	    jstorytextArea.setCaretPosition(0);
            

	
	} else {
	    Font f = new Font("Lucida Console",Font.PLAIN,12);
	
	    //jstorytextPane.setFont(f);
	    jstorytextPane.setEditable(false);
            initStylesForTextPane();
	    jstorytextPane.setText(sText);
	    jstorytextPane.setCaretPosition(0);
	}
	if (sPatteren.length() > 0)
	    highlight();
	return;
    }
    protected void initStylesForTextPane() {
        //Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().
			getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = jstorytextPane.addStyle("regular", def);
        //StyleConstants.setFontFamily(def, "SansSerif");

        Style s = jstorytextPane.addStyle("bold", regular);
        StyleConstants.setBold(s, true);
    }

    /*************************/
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jinputoutputGroup = new javax.swing.ButtonGroup();
        jstoryPanel = new javax.swing.JPanel();
        jscrollpane = new javax.swing.JScrollPane();
        jstorytextArea = new javax.swing.JTextArea();
        jstorytextPane = new javax.swing.JTextPane();
        jbuttonPanel = new javax.swing.JPanel();
        jexitButton = new javax.swing.JButton();
        jpreviousButton = new javax.swing.JButton();
        jnextButton = new javax.swing.JButton();
        joptionPanel = new javax.swing.JPanel();
        jcontentPanel = new javax.swing.JPanel();
        jfeOpt = new javax.swing.JRadioButton();
        jcscOpt = new javax.swing.JRadioButton();
        jtelerateOpt = new javax.swing.JRadioButton();
        jreutersOpt = new javax.swing.JRadioButton();
        jndfOpt = new javax.swing.JRadioButton();
        jformatPanel = new javax.swing.JPanel();
        jspufOpt = new javax.swing.JRadioButton();
        jasciiOpt = new javax.swing.JRadioButton();
        jntmOpt = new javax.swing.JRadioButton();
        jcff1aOpt = new javax.swing.JRadioButton();
        jninfoPanel = new javax.swing.JPanel();
        jproductLabel = new javax.swing.JLabel();
        jcontentsetPanel = new javax.swing.JPanel();
        optDN = new javax.swing.JRadioButton();
        optLL = new javax.swing.JRadioButton();
        optSN = new javax.swing.JRadioButton();
        optPB = new javax.swing.JRadioButton();
        optRT = new javax.swing.JRadioButton();
        optTL = new javax.swing.JRadioButton();

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        setLayout(new java.awt.GridBagLayout());

        jstoryPanel.setLayout(new java.awt.GridBagLayout());

        jstorytextArea.setColumns(20);
        jstorytextArea.setRows(5);
        jscrollpane.setViewportView(jstorytextArea);
        jscrollpane.setViewportView(jstorytextPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jstoryPanel.add(jscrollpane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.9;
        add(jstoryPanel, gridBagConstraints);

        jbuttonPanel.setLayout(new java.awt.GridBagLayout());

        jexitButton.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jbuttonPanel.add(jexitButton, gridBagConstraints);

        jpreviousButton.setText("Previous Story");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 50);
        jbuttonPanel.add(jpreviousButton, gridBagConstraints);

        jnextButton.setText("Next Story");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 10);
        jbuttonPanel.add(jnextButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        add(jbuttonPanel, gridBagConstraints);

        joptionPanel.setLayout(new java.awt.GridBagLayout());

        jcontentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("EDSYS Input/Output"));
        jcontentPanel.setLayout(new java.awt.GridBagLayout());

        jfeOpt.setSelected(true);
        jfeOpt.setText("FE Input");
        jfeOpt.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jfeOpt.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jcontentPanel.add(jfeOpt, gridBagConstraints);

        jcscOpt.setText("CSCGEN Output");
        jcscOpt.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jcscOpt.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jcscOpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcscOptActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jcontentPanel.add(jcscOpt, gridBagConstraints);

        jtelerateOpt.setText("Telerate Output");
        jtelerateOpt.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jtelerateOpt.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jcontentPanel.add(jtelerateOpt, gridBagConstraints);

        jreutersOpt.setText("Reuters Output");
        jreutersOpt.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jreutersOpt.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 5);
        jcontentPanel.add(jreutersOpt, gridBagConstraints);

        jndfOpt.setSelected(true);
        jndfOpt.setText("NDF Input");
        jndfOpt.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jndfOpt.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jcontentPanel.add(jndfOpt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        joptionPanel.add(jcontentPanel, gridBagConstraints);

        jformatPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Output Formats"));
        jformatPanel.setLayout(new java.awt.GridBagLayout());

        jspufOpt.setSelected(true);
        jspufOpt.setText("Spuf");
        jspufOpt.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jspufOpt.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jformatPanel.add(jspufOpt, gridBagConstraints);

        jasciiOpt.setText("ASCII");
        jasciiOpt.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jasciiOpt.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jformatPanel.add(jasciiOpt, gridBagConstraints);

        jntmOpt.setText("NTM");
        jntmOpt.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jntmOpt.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jformatPanel.add(jntmOpt, gridBagConstraints);

        jcff1aOpt.setText("CFF1A");
        jcff1aOpt.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jcff1aOpt.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 5);
        jformatPanel.add(jcff1aOpt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        joptionPanel.add(jformatPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.05;
        add(joptionPanel, gridBagConstraints);

        jninfoPanel.setLayout(new java.awt.GridBagLayout());
        jninfoPanel.add(jproductLabel, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(jninfoPanel, gridBagConstraints);

        jcontentsetPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Content Sets Story Published On"));
        jcontentsetPanel.setLayout(new java.awt.GridBagLayout());

        optDN.setText("DN");
        optDN.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        optDN.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        jcontentsetPanel.add(optDN, gridBagConstraints);

        optLL.setText("LL");
        optLL.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        optLL.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        jcontentsetPanel.add(optLL, gridBagConstraints);

        optSN.setText("SN");
        optSN.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        optSN.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        jcontentsetPanel.add(optSN, gridBagConstraints);

        optPB.setText("PB");
        optPB.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        optPB.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        jcontentsetPanel.add(optPB, gridBagConstraints);

        optRT.setText("RT");
        optRT.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        optRT.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        jcontentsetPanel.add(optRT, gridBagConstraints);

        optTL.setText("TL");
        optTL.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        optTL.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        jcontentsetPanel.add(optTL, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(jcontentsetPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jcscOptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcscOptActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_jcscOptActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JRadioButton jasciiOpt;
    private javax.swing.JPanel jbuttonPanel;
    private javax.swing.JRadioButton jcff1aOpt;
    private javax.swing.JPanel jcontentPanel;
    private javax.swing.JPanel jcontentsetPanel;
    private javax.swing.JRadioButton jcscOpt;
    private javax.swing.JButton jexitButton;
    private javax.swing.JRadioButton jfeOpt;
    private javax.swing.JPanel jformatPanel;
    private javax.swing.ButtonGroup jinputoutputGroup;
    private javax.swing.JRadioButton jndfOpt;
    private javax.swing.JButton jnextButton;
    private javax.swing.JPanel jninfoPanel;
    private javax.swing.JRadioButton jntmOpt;
    private javax.swing.JPanel joptionPanel;
    private javax.swing.JButton jpreviousButton;
    private javax.swing.JLabel jproductLabel;
    private javax.swing.JRadioButton jreutersOpt;
    private javax.swing.JScrollPane jscrollpane;
    private javax.swing.JRadioButton jspufOpt;
    private javax.swing.JPanel jstoryPanel;
    private javax.swing.JTextArea jstorytextArea;
    private javax.swing.JTextPane jstorytextPane;
    private javax.swing.JRadioButton jtelerateOpt;
    private javax.swing.JRadioButton optDN;
    private javax.swing.JRadioButton optLL;
    private javax.swing.JRadioButton optPB;
    private javax.swing.JRadioButton optRT;
    private javax.swing.JRadioButton optSN;
    private javax.swing.JRadioButton optTL;
    // End of variables declaration//GEN-END:variables
    
}
