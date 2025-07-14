/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dta.system;

import java.awt.HeadlessException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 *
 * @author Dockman
 */
public class systemActivation {
      File file3 = new File(".\\config.xml");
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      //JFrame myFrame;//=new JFrame();
     // homeWindow frameLoad=new homeWindow();
    
    public void createXmlFile2(Document doc, String user, String database, String password) throws Exception {
        Element root = doc.createElement("Student");
        doc.appendChild(root);
        Element element1 = doc.createElement("user");
        root.appendChild(element1);
        Text text1 = doc.createTextNode(user);
        element1.appendChild(text1);

        Element element2 = doc.createElement("database");
        root.appendChild(element2);
        Text text2 = doc.createTextNode(database);
        element2.appendChild(text2);

        Element element3 = doc.createElement("password");
        root.appendChild(element3);
        Text text3 = doc.createTextNode(password);
        element3.appendChild(text3);

//        Element element4 = doc.createElement("url");
//        root.appendChild(element4);
//        Text text4 = doc.createTextNode(url);
//        element4.appendChild(text4);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
        String xmlString = sw.toString();

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file3, true)))) {
            bw.write(xmlString);
            bw.flush();
        }
    }
    public void THEADMINO() {

        // connecttoDB();
        //int Rnum = 1;
        Date untildate = new Date(); //"2011-10-08";//can take any date in current format   
        Date newDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal.setTime(untildate);
        cal2.setTime(newDate);
        cal.add(Calendar.DATE, 30);
        cal2.add(Calendar.DATE, 30);

        // String ExpireDate = dateFormat.format(cal.getTime());
        String installDate = dateFormat.format(untildate);
        String newDateEx = dateFormat.format(cal2.getTime());
        int code = 24 * 369 * untildate.getDate();
        String str, Newstr = " ";
        str = String.valueOf(code);
        for (int i = 0; i < str.length(); i++) {
            char ch = Character.toLowerCase(str.charAt(i));
            switch (ch) {
                case '1':
                    Newstr = Newstr + "!";
                    break;
                case '2':
                    Newstr = Newstr + "?";
                    break;
                case '3':
                    Newstr = Newstr + ">";
                    break;
                case '4':
                    Newstr = Newstr + "<";
                    break;
                case '5':
                    Newstr = Newstr + "+";
                    break;
                case '6':
                    Newstr = Newstr + "%";
                    break;
                case '7':
                    Newstr = Newstr + "#";
                    break;
                case '8':
                    Newstr = Newstr + "@";
                    break;
                case '9':
                    Newstr = Newstr + "&";
                    break;
                case '0':
                    Newstr = Newstr + "*";
                    break;
                default:
                    break;
            }
        }
        try {
            if (file3.exists()) {
                file3.delete();
                System.out.println("File deleted....");
            }
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            createXmlFile2(doc, installDate, newDateEx, Newstr);
            JOptionPane.showMessageDialog(homeWindow.loginFrame, "SYSTEM ACTIVATED");
        } catch (Exception E) {
            JOptionPane.showMessageDialog(homeWindow.loginFrame, E);
        }

    }

    public void THEADMINOnow() {

        Date untildate = new Date(); //"2011-10-08";//can take any date in current format   
        Date newDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal.setTime(untildate);
        cal2.setTime(newDate);
        cal.add(Calendar.DATE, 30);
        cal2.add(Calendar.DATE, 30);

        // String ExpireDate = dateFormat.format(cal.getTime());
        String installDate = dateFormat.format(untildate);
        String newDateEx = dateFormat.format(cal2.getTime());
        int code = 24 * 369 * untildate.getDate();
        String str, Newstr = " ";
        str = String.valueOf(code);
        for (int i = 0; i < str.length(); i++) {
            char ch = Character.toLowerCase(str.charAt(i));
            switch (ch) {
                case '1':
                    Newstr = Newstr + "!";
                    break;
                case '2':
                    Newstr = Newstr + "?";
                    break;
                case '3':
                    Newstr = Newstr + ">";
                    break;
                case '4':
                    Newstr = Newstr + "<";
                    break;
                case '5':
                    Newstr = Newstr + "+";
                    break;
                case '6':
                    Newstr = Newstr + "%";
                    break;
                case '7':
                    Newstr = Newstr + "#";
                    break;
                case '8':
                    Newstr = Newstr + "@";
                    break;
                case '9':
                    Newstr = Newstr + "&";
                    break;
                case '0':
                    Newstr = Newstr + "*";
                    break;
                default:
                    break;
            }
        }
        try {
            if (file3.exists()) {
                file3.delete();
                System.out.println("File deleted....");
            }
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            createXmlFile2(doc, installDate, newDateEx, Newstr);
            //JOptionPane.showMessageDialog(null, "File Created Successfully");
        } catch (Exception E) {
            JOptionPane.showMessageDialog(homeWindow.loginFrame, E);
        }

        JOptionPane.showMessageDialog(homeWindow.loginFrame, "SYSTEM ACTIVATED");

    }

    public void checkEXPIREDATE() {

        Date nowdate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nwDate = dateFormat.format(nowdate);
        // frame=new JFrame();

        Date untildate = new Date(); //"2011-10-08";//can take any date in current format   
        Date newDate = new Date();

        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal.setTime(untildate);
        cal2.setTime(newDate);

        cal.add(Calendar.DATE, 30);
        cal2.add(Calendar.DATE, 30);

        int code = 24 * 369 * untildate.getDate();
        String str, Newstr = " ";
        str = String.valueOf(code);
        for (int i = 0; i < str.length(); i++) {
            char ch = Character.toLowerCase(str.charAt(i));
            switch (ch) {
                case '1':
                    Newstr = Newstr + "!";
                    break;
                case '2':
                    Newstr = Newstr + "?";
                    break;
                case '3':
                    Newstr = Newstr + ">";
                    break;
                case '4':
                    Newstr = Newstr + "<";
                    break;
                case '5':
                    Newstr = Newstr + "+";
                    break;
                case '6':
                    Newstr = Newstr + "%";
                    break;
                case '7':
                    Newstr = Newstr + "#";
                    break;
                case '8':
                    Newstr = Newstr + "@";
                    break;
                case '9':
                    Newstr = Newstr + "&";
                    break;
                case '0':
                    Newstr = Newstr + "*";
                    break;
                default:
                    break;
            }
        }
        try {
            if (file3.exists()) {

                try {
                    //File inputFile = new File(".\\config.xml");
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(file3);
                    doc.getDocumentElement().normalize();
                    System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
                    NodeList nList = doc.getElementsByTagName("Student");
                    System.out.println("----------------------------");

                    for (int temp = 0; temp < nList.getLength(); temp++) {
                        Node nNode = nList.item(temp);
                        System.out.println("\nCurrent Element :" + nNode.getNodeName());

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            System.out.println("user : "
                                    + eElement.getElementsByTagName("user").item(0).getTextContent());
                            //txtusername.setText(eElement.getElementsByTagName("user").item(0).getTextContent());
                            System.out.println("Database : "
                                    + eElement.getElementsByTagName("database").item(0).getTextContent());

                            String trialDate = eElement.getElementsByTagName("database").item(0).getTextContent();

                            System.out.println("Password : "
                                    + eElement.getElementsByTagName("password").item(0).getTextContent());

                            String strt = eElement.getElementsByTagName("password").item(0).getTextContent();
                            if (dateFormat.parse(nwDate).after(dateFormat.parse(trialDate))) {
                                //JOptionPane.showMessageDialog(null, trialDate);
                                CHECKACTIVATION();
                            } else {
                                //THEADMINO(); //JOptionPane.showMessageDialog(null, "System Already Active,Reactivate after:  " + ExPDate);
                            }
                        }
                    }
                } catch (ParserConfigurationException | SAXException | IOException | DOMException | ParseException e) {

                    //Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, e);
                }
            } else {
                String serialNumber = JOptionPane.showInputDialog(homeWindow.loginFrame, "Enter the serial number for activation?");
                if ("DIGI1234POSTEST".equals(serialNumber)) {
                    THEADMINOnow();
                } else {
                    JOptionPane.showMessageDialog(homeWindow.loginFrame, "Wrong Activation Serial, Email jclarelimited@gmail.com "
                            + "or Call/Text +254729696690 to get activation serial number");
                    System.exit(0);
                }

            }

        } catch (HeadlessException E) {
            JOptionPane.showMessageDialog(null, E);
        }

    }

    public void CHECKACTIVATION() {

        String Newstr = "";
        String str = "";
        Date trialDate = new Date();
        String intalDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date toText;// = format.parse(intalDate);
        String trialDateD = String.valueOf(trialDate);
        try {
            //file3 = new File(".\\config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file3);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("Student");
            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                // int temp=1;
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    intalDate = eElement.getElementsByTagName("user").item(0).getTextContent();
                    trialDateD = eElement.getElementsByTagName("database").item(0).getTextContent();
                    str = eElement.getElementsByTagName("password").item(0).getTextContent();

                    System.out.println("Instaled Date : "
                            + eElement.getElementsByTagName("user").item(0).getTextContent());

                    System.out.println("Trial End Date : "
                            + eElement.getElementsByTagName("database").item(0).getTextContent());

                    System.out.println("Activation code : "
                            + eElement.getElementsByTagName("password").item(0).getTextContent());

                    for (int i = 0; i < str.length(); i++) {
                        char ch = Character.toLowerCase(str.charAt(i));
                        switch (ch) {
                            case '!':
                                Newstr = Newstr + "1";
                                break;
                            case '?':
                                Newstr = Newstr + "2";
                                break;
                            case '>':
                                Newstr = Newstr + "3";
                                break;
                            case '<':
                                Newstr = Newstr + "4";
                                break;
                            case '+':
                                Newstr = Newstr + "5";
                                break;
                            case '%':
                                Newstr = Newstr + "6";
                                break;
                            case '#':
                                Newstr = Newstr + "7";
                                break;
                            case '@':
                                Newstr = Newstr + "8";
                                break;
                            case '&':
                                Newstr = Newstr + "9";
                                break;
                            case '*':
                                Newstr = Newstr + "0";
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            //JOptionPane.showMessageDialog(null, Newstr);
            String name = JOptionPane.showInputDialog(homeWindow.loginFrame, "ENTER THE ACTIVATION CODE?");
            if (name == null ? Newstr == null : name.equals(Newstr)) {

                THEADMINO();
                JOptionPane.showMessageDialog(homeWindow.loginFrame, "THANK YOU FOR USING LOANITECH SYSTEM SOL.FOR "
                        + "ANY SUPPORT CONTACT +254729696690 OR EMAIL JCLARELIMITED@GMAIL.COM AT ANY GIVEN TIME");

            } else {
                try {
                    toText = format.parse(intalDate);

                    JOptionPane.showMessageDialog(homeWindow.loginFrame, "WRONG SERIAL NUMBER,TO GET SERIAL CODE TEXT  " + dateFormat.format(toText) + "  "
                            + "TO +254729696690 OR EMAIL JCLARELIMITED@GMAIL.COM, ENSURE YOU PAID FULLY");
                    System.exit(0);
                } catch (ParseException ex) {
                    // Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {

        }
    }
    
}
