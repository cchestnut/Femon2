
import java.awt.Font;
import javax.swing.JOptionPane;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.util.Enumeration;
import javax.swing.plaf.FontUIResource;
import java.awt.Container;
import java.awt.Component;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ChestnutC
 */
public class Formatter {
    private static int fontSize = 12;
    private static String font = "Verdana";
    
    public static String timeStampFormat(String args){
        String result = "";
        try{
        if(args != null){
            result += args.substring(0, 4); //year
            result += "-";
            result += args.substring(4,6); //month
            result += "-";
            result += args.substring(6,8); //day
            result += " ";
            result += args.substring(8,10);//hour
            result += ":";
            result += args.substring(10,12);//mins
            result += ".";
            result += args.substring(12); //rest
        }
        }catch(IndexOutOfBoundsException e){
            System.out.println("String not long enough for timestamp format");
            return args;
        }
        return result;
    }
    
    public static String unTSFormat(String args){
        String result = args;
        if(args != null && args.contains("-")){
            result = args.replaceAll("-", "");
            result = result.replaceAll(" ", "");
            result = result.replaceAll(":", "");
            if(result.contains(".")){
                result = result.replace(".", "");     
            }
        }
        return result;
    }
    
    public static void setFont(Object f){
        if(f instanceof String){
            font = (String) f;
            try{
                setUIFont (new FontUIResource(font, Font.PLAIN, fontSize));
            }catch(NullPointerException e){
                System.out.println("Font not available");
            }
            try{
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            }catch(UnsupportedLookAndFeelException e){
                System.out.println("Font Change Error");
            }
        } 
    }
    
    public static void updateFont(JComponent c){
        if(c != null)
            c.setFont(new FontUIResource(font, Font.PLAIN, fontSize));
        if(c instanceof Container)
            for(Component i : ((Container) c).getComponents())
                if(i instanceof JComponent)
                    updateFont((JComponent)i);
    }
    
    private static void setUIFont (FontUIResource f){
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
          Object key = keys.nextElement();
          Object value = UIManager.get (key);
          if (value != null && value instanceof FontUIResource){
              UIManager.put (key, f);
          }
          }
    } 
}
