import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JButton;
/*
 * EmonApplet.java
 *
 * Created on May 4, 2005, 2:11 PM
 */

/**
 *
 * @author  VisokayA
 */
public class FemonApplet extends javax.swing.JApplet {
    /** Initialization method that will be called after the applet is loaded
     *  into the browser.
     */
    static jfemonmainPanel panel;
    public static URL codeBase;
    public static URL docBase;
    
    public void init() {
        codeBase = getCodeBase();
        docBase = getDocumentBase();
        jmainFrame.bIsApplet = true;
        panel = new jfemonmainPanel(true);
        getContentPane().add(panel);
    }
    
    public void stop(){
        getContentPane().removeAll();
    }
}
