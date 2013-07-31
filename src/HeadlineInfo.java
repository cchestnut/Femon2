/*
 * HeadlineInfo.java
 *
 * Created on December 4, 2006, 11:21 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author VisokayA
 */
import java.util.*;

public class HeadlineInfo implements Comparable {
    String FE;
    String DocID;
    String Timestamp;
    String Headline;
    
    public int compareTo(Object o) {
        HeadlineInfo hi = (HeadlineInfo)o;
        return Formatter.unTSFormat(this.Timestamp).compareTo(Formatter.unTSFormat(hi.Timestamp));
    }
}
