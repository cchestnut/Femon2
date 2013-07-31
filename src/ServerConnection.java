// Version	@(#)ServerConnection.java	1.1	04/03/24
/*
 * ServerConnection.java
 *
 * Created on February 03/2004
 *author  Madhavi Erukulla
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.JDialog.*;
import javax.swing.event.*;

/**
 *
 * @author  rajm
 * @version
 */


public class ServerConnection
{
    private int		headerSize = 58;// Server header size - 12
					// Status	      - 04
					// TimeStamp size     - 16
					// DocID size         - 07
					// Front End size     - 02
					// Products size      - 17
    public Socket		sock = null;
    private StringBuffer	Products = new StringBuffer(32);
    private StringBuffer	ServerName = new StringBuffer(64);

    public String getServerName() {
	return ServerName.toString();
    }

    public ServerConnection () {
	return;
    }

    public String getContentSets() {
	return Products.toString();
    }

    public Socket OpenSocket(JFrame femon, String server, int port)
        throws IOException
    {
        try {
            sock = new Socket(server, port);
	    System.out.println("Connected to " +server +".......");
	    ServerName.append(server);
	    return sock;
        } catch (UnknownHostException e) {
            System.err.println("Don't know about server (UnKnownHostException): "
		+server);
	    StringBuffer msg = new StringBuffer(64);
	    msg.append("Don't know about server (UnKnownHostException): ");
	    msg.append(server);
            JOptionPane.showMessageDialog(femon, msg,
		"Error Message", JOptionPane.ERROR_MESSAGE);

        } catch (IOException e) {
            System.err.println("TOOLSSERVER process is not running on '"
		+server +"' machine.");

	    StringBuffer msg = new StringBuffer(64);
	    msg.append("TOOLSSERVER process is not running on '");
	    msg.append(server);
	    msg.append("' machine.");
            JOptionPane.showMessageDialog(femon, msg,
		"Error Message", JOptionPane.ERROR_MESSAGE);

        }
        return null;
    }

    public void CloseSocket
    (	Socket sock,
	DataInputStream is,
	DataOutputStream os
    ) throws IOException
    {
        sock.close();
        os.close();
        is.close();
	return;
    }

    public String getStory
    (	DataInputStream		is,
	DataOutputStream	os,
	int			reqType,
	String			str
    ) throws IOException
    {
	try {
	    int		size;
	    int		serverStatus = 0;
            String data = null;
	  String[]	sSplit = str.split(" ");
	    String		outputFormat = new String(sSplit[0]);
	    String		frontEnd = new String(sSplit[1]);
	    String		product = new String(sSplit[2]);
	    String		docid = new String(sSplit[3]);
	    String		timeStamp = new String(sSplit[4]);
	    StringBuffer	sendBuffer = new StringBuffer(12+4+4+8+16+1);

	    size = ((4*3)
		+ 8 			// outputFormats (ANA, CFF1A, CF5, NML or NTM)
		+ 4 			// Switch Type (F or Not yet decided)
		+ 4			// Front End Strings (T1, P1, etc)
		+ 8			// docID
		+ 16 + 1		// Timestamp
	       );

	    os.writeInt(size);
	    os.writeInt(serverStatus);
	    os.writeInt(reqType);

	    sendBuffer.append(outputFormat);
	    for (int i = 0; i < (8 - outputFormat.length()); i++)
		sendBuffer.append('\u0000');

	    sendBuffer.append(frontEnd);
	    for (int i = 0; i < (4 - frontEnd.length()); i++)
		sendBuffer.append('\u0000');

	    sendBuffer.append(product);
	    for (int i = 0; i < (4 - product.length()); i++)
		sendBuffer.append('\u0000');

	    sendBuffer.append(docid);
	    for (int i = 0; i < (8 - docid.length()); i++)
		sendBuffer.append('\u0000');

	    sendBuffer.append(timeStamp);
	    for (int i = 0; i < (16 - timeStamp.length()); i++)
		sendBuffer.append('\u0000');
	    sendBuffer.append('\u0000');
            
	    os.write(sendBuffer.toString().getBytes(), 0, +sendBuffer.toString().length());

	    os.flush();
	} catch (Exception ioEx) {
	    ioEx.printStackTrace();
	    System.out.println("IOException in getStory");
	    //System.exit(1);
	    return null;
	}

	int		i = 0;
	int		totalSize;
	int		temp;
	int		total = 0;
	int		status = 0;

	byte[]		timeStamp = new byte[16];
	byte[]		docid = new byte[7];
	byte[]		frontEnd = new byte[2];
	byte[]		products = new byte[17];
	StringBuffer[]	head = new StringBuffer[1];


	totalSize = is.readInt();
	temp = is.readInt();
	temp = is.readInt();

	status = is.readInt();

	try {
	    is.read(timeStamp, 0, 16);
	    //System.out.println("timeStamp:" + new String(timeStamp));

	    is.read(docid, 0, 7);
	    //System.out.println("docid:" +new String(docid));

	    is.read(frontEnd, 0, 2);

	    int n =is.read(products, 0, 17);
            //System.out.println("Read " + n +" bytes");
	    System.out.println("Product:" + new String(products));

	    //is.read(data, 0, (totalSize-headerSize));
	    byte[]		data = new byte[totalSize-headerSize];
	    int k;
	    for (i = 0; i < (totalSize-headerSize); ) {
	        k = is.read(data, i, (totalSize-headerSize-i));
	        i += k;
	    }
	    if (Products.toString().length() > 0)
		Products.delete(0, Products.length());

	    Products.append(new String(products));

	    System.out.println("GetStory ServerConnection-Products:" +Products.toString());
	    

	    return (new String(data));
	} catch (Exception ioEx) {
	    ioEx.printStackTrace();
	    System.out.println("IOException");
	    //System.exit(1);
	    return null;
	}
    }

    public static ArrayList getHeadline
    (	DataInputStream		is,
	DataOutputStream	os,
	String			fends,
	String			date,
	String			stime,
	String			etime,
	String			spatt,
	tableSort		sort,
	HeadlineTableModel      tableModel
    ) throws IOException
    {
        
        ArrayList<HeadlineInfo> headlineList = new ArrayList<HeadlineInfo>();
	int		size;
	int		serverStatus = 0;
	int		reqType = 2;
        StringBuffer	sendBuffer = new StringBuffer(12+4+256+12+8+8+spatt.length()+1);

        size = ((4*3) + 4 + 256 + 12 + 8 + 8 + spatt.length() + (1));

	try {
	    os.writeInt(size);
	    os.writeInt(serverStatus);
	    os.writeInt(reqType);

	    sendBuffer.append("F");
	    for (int i = 0; i < (4 - "F".length()); i++)
		sendBuffer.append('\u0000');

	    sendBuffer.append(fends);

            for (int i = 0; i < (256 - fends.length()); i++)
		sendBuffer.append('\u0000');

	    sendBuffer.append(date);
	    for (int i = 0; i < (12 - date.length()); i++)
		sendBuffer.append('\u0000');

	    sendBuffer.append(stime);
	    for (int i = 0; i < (8 - stime.length()); i++)
		sendBuffer.append('\u0000');

	    sendBuffer.append(etime);
	    for (int i = 0; i < (8 - etime.length()); i++)
		sendBuffer.append('\u0000');

	    sendBuffer.append(spatt);
	    sendBuffer.append('\u0000');
	    os.write(sendBuffer.toString().getBytes(), 0, +sendBuffer.toString().length());

	} catch (Exception ioEx) {
	    ioEx.printStackTrace();
	    System.out.println("IOException in getHeadline");
	}

	int i = 0;
	int status;
	int totalSize = 0;
	int temp;
	int total = 0;
	int nread;
	int nleft;
	int offset=0;
	int first=0;
        
	while (true) {
	    try {
		totalSize = is.readInt();
		temp = is.readInt();
		temp = is.readInt();
		status = is.readInt();
		byte[]		data = new byte[320];
		offset = 0;
		nleft = 296;

		while (nleft > 0) {
		    nread = is.read(data, offset, nleft);
		    if (nread <= 0) {
			System.out.println("nread: " +nread);
			break;
		    }
		    nleft -= nread;
		    offset += nread;
		}
		data[296+1] = '\0';

		/*
		System.out.println("Timestamp:" +(new String(data)).substring(0, 16));
		System.out.println("DocID:" +(new String(data)).substring(16, 23));
		System.out.println("FrontEnd:" +(new String(data)).substring(23, 25));
		System.out.println("Products:" +(new String(data)).substring(25, 42));
		System.out.println("Products:" +(new String(data)).substring(42, 297));
		System.out.println("data:" +new String(data));
		*/

		if ((status == 8) || (status == 12))
			break;

		total++;

                HeadlineInfo hi = new HeadlineInfo();
                hi.FE = new String(data).substring(23, 25);
                hi.DocID = new String(data).substring(16, 23);
                hi.Timestamp = new String(data).substring(0, 16);
                hi.Headline = new String(data).substring(42, 297);
                headlineList.add(hi);
                
                data = null;
                if ((status == 9) || (status == 13))
                break;
               
	    } catch (Exception ioEx) {
		ioEx.printStackTrace();
		System.out.println("IOException");
                return null;
	    }

	} //End of while()

	if (total == 0) {
	    System.out.println("No Story Found...");
	    //return 0;
            return null;
	}
        //sort table data by timestamp
        Collections.sort(headlineList);
        return headlineList;
    } // End of getHeadline()
}

