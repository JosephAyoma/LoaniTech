/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dta.system;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.*;
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
 * @author DOCKMAN
 */
public class DBsConnection extends javax.swing.JFrame {
    
    String url;
    String user, con;//="root";
    String pass;// ="LIMO";
    Statement state = null;
    Connection conect = null;
    ResultSet resultset = null;
    java.net.URL title = getClass().getResource("title.PNG");
    Image img;
    homeWindow homie;// = new homeWindow();
    File file = new File("./Connection.xml");

    public DBsConnection() {
        initComponents();
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.white);
        Toolkit kit = Toolkit.getDefaultToolkit();
       img = kit.createImage(title);
       setIconImage(img);
//        java.net.URL url = getClass().getResource("title.PNG");
//        Toolkit kit = Toolkit.getDefaultToolkit();
//        Image img = kit.createImage(url);
//        this.setIconImage(img);
        
    }
    
    public void Connection2() {
        select();
        pass = txtPassword.getText();
        url = url = "jdbc:mysql://" + combourl.getText() + ":3306/" + txtDbName.getText();
        //combourl.getSelectedItem().toString() + txtDbName.getText();
        user = txtUserName.getText();
        pass = txtPassword.getText();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conect = DriverManager.getConnection(url, user, pass);
            state = conect.createStatement();
            lebelStatus.setText("CONNECTED TO THE DATABASE");
            lebelStatus.setForeground(Color.BLACK);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.print(e);
            lebelStatus.setText("COULD NOT CONNECT TO THE DATABASE, AN ERROR OCCURED");
            lebelStatus.setForeground(Color.red);
        } /*finally {
            if (conect != null) {
                try {
                    conect.close();
                } catch (SQLException ignore) {
                }
            }
        }*/

    }
    
    public void creatTables() {
        Connection2();
        String sql;
        try {
              
            String k = "CREATE TABLE `loan_payment_track` (\n"
                    + " `Track_Number` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + " `ID_Number` int(11) NOT NULL,\n"
                    + " `Cycle_Period` int(11) NOT NULL,\n"
                    + " `Pay_Date` date NOT NULL,\n"
                    + " `Old_Balance` float NOT NULL,\n"
                    + " `Instalment` float NOT NULL,\n"
                    + " `Interest_Paid` float NOT NULL,\n"
                    + " `Principal_Paid` float NOT NULL,\n"
                    + " `New_Balance` float NOT NULL,\n"
                    + " `Disburse_Date` date NOT NULL,\n"
                    + " `Pay_Status` varchar(12) NOT NULL DEFAULT 'Not Paid',\n"
                    + " PRIMARY KEY (`Track_Number`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=489 DEFAULT CHARSET=latin1";
            state.addBatch(k);
            sql = " CREATE TABLE `loan-paid_info` (\n"
                    + " `Serial_No` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + " `Client_ID_No` int(11) NOT NULL,\n"
                    + " `Loan_No` int(11) NOT NULL,\n"
                    + " `Track_No` int(11) NOT NULL,\n"
                    + " `Intal_To_Pay` float NOT NULL,\n"
                    + " `Amount_Paid` float NOT NULL,\n"
                    + " `Date_Paid` date DEFAULT NULL,\n"
                    + " PRIMARY KEY (`Serial_No`),\n"
                    + " UNIQUE KEY `Track_No` (`Track_No`),\n"
                    + " CONSTRAINT `loan-paid_info_ibfk_1` FOREIGN KEY (`Track_No`) REFERENCES `loan_payment_track` (`Track_Number`) ON UPDATE CASCADE\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=latin1";
            state.addBatch(sql);
            String a = " CREATE TABLE `account_configuration` (\n"
                    + " `Serial_No` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + " `Account_Name` varchar(60) NOT NULL,\n"
                    + " `Acount_Code` int(11) NOT NULL,\n"
                    + " `Description` varchar(254) NOT NULL,\n"
                    + " PRIMARY KEY (`Serial_No`,`Account_Name`,`Acount_Code`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1";
            state.addBatch(a);
            String b = " CREATE TABLE `acct_record` (\n"
                    + " `Serial_No` int(10) unsigned NOT NULL AUTO_INCREMENT,\n"
                    + " `Member_ID` int(11) NOT NULL,\n"
                    + " `Member_No` varchar(50) NOT NULL,\n"
                    + " `Acct_Type` varchar(50) NOT NULL,\n"
                    + " `Current_Balance` float NOT NULL DEFAULT '0',\n"
                    + " PRIMARY KEY (`Serial_No`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1";
            state.addBatch(b);
            String c = " CREATE TABLE `acct_tractions` (\n"
                    + " `Serial_No` int(10) unsigned NOT NULL AUTO_INCREMENT,\n"
                    + " `Member_ID` int(11) NOT NULL,\n"
                    + " `Acct_Type` varchar(50) NOT NULL,\n"
                    + " `Transaction_Type` varchar(50) NOT NULL,\n"
                    + " `Transact_Date` datetime NOT NULL,\n"
                    + " `Transact_Amount` float NOT NULL,\n"
                    + " PRIMARY KEY (`Serial_No`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1";
            state.addBatch(c);
            
            String d = " CREATE TABLE `approvedloanpaytrack` (\n"
                    + " `Serial_No` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + " `Loan_Type` varchar(254) NOT NULL,\n"
                    + " `Pri_Amount` float NOT NULL,\n"
                    + " `Total_Amount` float NOT NULL,\n"
                    + " `Amount_Paid` float NOT NULL,\n"
                    + " `OutStanding` float NOT NULL,\n"
                    + " `Dibursed_Date` date NOT NULL,\n"
                    + " `Loan_Status` int(11) NOT NULL DEFAULT '1',\n"
                    + " `Client_ID_No` varchar(254) NOT NULL,\n"
                    + " PRIMARY KEY (`Serial_No`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1";
            state.addBatch(d);
            String j = "CREATE TABLE `loan_application` (\n"
                    + " `Serial_No` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + " `ID_No` int(11) NOT NULL,\n"
                    + " `Loan_Type` varchar(254) NOT NULL,\n"
                    + " `Loan_Purpose` varchar(254) NOT NULL,\n"
                    + " `Loan_Amount` float NOT NULL,\n"
                    + " `Loan_Period` int(11) NOT NULL,\n"
                    + " `Intalment_Pay` float NOT NULL,\n"
                    + " `Date_Applied` date DEFAULT NULL,\n"
                    + " `Date_Required` date DEFAULT NULL,\n"
                    + " `Approval_Status` varchar(30) NOT NULL,\n"
                    + " `Approval_Date` date DEFAULT NULL,\n"
                    + " PRIMARY KEY (`Serial_No`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1";
            state.addBatch(j);
            String e = "CREATE TABLE `approved_active_loans` (\n"
                    + " `Loan_No` int(11) NOT NULL,\n"
                    + " `Pri_Amount` float NOT NULL,\n"
                    + " `Interest_Amount` float NOT NULL,\n"
                    + " `Total_Amount` float NOT NULL,\n"
                    + " `Loan_Period` int(11) NOT NULL,\n"
                    + " `Number_Instalment` int(11) NOT NULL,\n"
                    + " `Loan_Status` int(11) NOT NULL,\n"
                    + " `Client_ID_No` int(11) NOT NULL,\n"
                    + " PRIMARY KEY (`Loan_No`),\n"
                    + " CONSTRAINT `approved_active_loans_ibfk_1` FOREIGN KEY (`Loan_No`) REFERENCES `loan_application` (`Serial_No`) ON UPDATE CASCADE\n"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=latin1";
            state.addBatch(e);
           
            String g = " CREATE TABLE `dbo_members` (\n"
                    + " `Serial_No` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + " `m_Name` varchar(254) NOT NULL,\n"
                    + " `m_Phone` bigint(20) NOT NULL,\n"
                    + " `m_DOB` date NOT NULL,\n"
                    + " `m_Address` varchar(254) NOT NULL,\n"
                    + " `m_Email` varchar(60) NOT NULL,\n"
                    + " `m_IDNo` int(11) NOT NULL,\n"
                    + " `m_Gender` varchar(20) NOT NULL,\n"
                    + " `m_Address_Code` varchar(254) NOT NULL,\n"
                    + " `m_Town` varchar(54) NOT NULL,\n"
                    + " `m_Number` varchar(60) NOT NULL,\n"
                    + " `m_Marrital_Status` varchar(60) NOT NULL,\n"
                    + " `k_Name` varchar(254) NOT NULL,\n"
                    + " `k_ID_No` int(11) NOT NULL,\n"
                    + " `k_Marital_Status` varchar(60) NOT NULL,\n"
                    + " `k_Relationship` varchar(60) NOT NULL,\n"
                    + " `k_phone` bigint(20) NOT NULL,\n"
                    + " `k_Email` varchar(254) NOT NULL,\n"
                    + " `k_DOB` date NOT NULL,\n"
                    + " `k_Gender` varchar(60) NOT NULL,\n"
                    + " `k_Address` varchar(254) NOT NULL,\n"
                    + " `k_Town` varchar(60) NOT NULL,\n"
                    + " PRIMARY KEY (`m_IDNo`),\n"
                    + " UNIQUE KEY `m_phone` (`m_Phone`),\n"
                    + " UNIQUE KEY `k_phone` (`k_phone`),\n"
                    + " UNIQUE KEY `k id no` (`k_ID_No`),\n"
                    + " UNIQUE KEY `auto` (`Serial_No`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1";
            state.addBatch(g);
            String h = "CREATE TABLE `feel_collection` (\n"
                    + " `Serial_No` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + " `Client_ID` int(11) NOT NULL,\n"
                    + " `Fee_Type` varchar(254) NOT NULL,\n"
                    + " `Fee_Code` int(11) NOT NULL,\n"
                    + " `Date_Collected` date NOT NULL,\n"
                    + " `Amount_Collected` float NOT NULL,\n"
                    + " PRIMARY KEY (`Serial_No`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1";
            state.addBatch(h);
            String i = "CREATE TABLE `fee_charge` (\n"
                    + " `Serial_No` int(10) unsigned NOT NULL AUTO_INCREMENT,\n"
                    + " `Fee_Name` varchar(125) NOT NULL,\n"
                    + " `Description` varchar(255) NOT NULL,\n"
                    + " PRIMARY KEY (`Serial_No`),\n"
                    + " UNIQUE KEY `fee_Name` (`Fee_Name`),\n"
                    + " KEY `Serial_No` (`Serial_No`) USING BTREE\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1";
            state.addBatch(i);
          
            String m = "CREATE TABLE `loan_type` (\n"
                    + " `Serial_No` int(11) unsigned NOT NULL AUTO_INCREMENT,\n"
                    + " `Loan_Type` varchar(254) NOT NULL,\n"
                    + " `Description` varchar(255) NOT NULL,\n"
                    + " PRIMARY KEY (`Serial_No`),\n"
                    + " UNIQUE KEY `Loan_Type` (`Loan_Type`) USING BTREE\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1";
            state.addBatch(m);
            String l = "CREATE TABLE `loan_rates` (\n"
                    + " `Serial_No` int(10) unsigned NOT NULL AUTO_INCREMENT,\n"
                    + " `Loan_Type` varchar(254) NOT NULL,\n"
                    + " `Loan_Rate` float NOT NULL,\n"
                    + " `Description` varchar(255) NOT NULL,\n"
                    + " PRIMARY KEY (`Loan_Type`),\n"
                    + " UNIQUE KEY `Serial_No` (`Serial_No`) USING BTREE,\n"
                    + " KEY `Foreign key` (`Loan_Type`) USING BTREE,\n"
                    + " CONSTRAINT `loan_rates_ibfk_1` FOREIGN KEY (`Loan_Type`) REFERENCES `loan_type` (`Loan_Type`) ON UPDATE CASCADE\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1";
            state.addBatch(l);
            
            String p = "CREATE TABLE `pass_word_pharmacist` (\n"
                    + " `USER_NAME` varchar(25) NOT NULL,\n"
                    + " `USER_PASSWORD` varchar(50) DEFAULT NULL,\n"
                    + " `ROLE` varchar(20) NOT NULL,\n"
                    + " `STATUS` int(11) NOT NULL DEFAULT '1',\n"
                    + " PRIMARY KEY (`USER_NAME`)\n"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=latin1";
            state.addBatch(p);
//            String q = "CREATE TABLE `feel_collection` (\n"
//                    + " `Serial_No` int(11) NOT NULL AUTO_INCREMENT,\n"
//                    + " `Client_ID` int(11) NOT NULL,\n"
//                    + " `Fee_Type` varchar(254) NOT NULL,\n"
//                    + " `Fee_Code` int(11) NOT NULL,\n"
//                    + " `Date_Collected` date NOT NULL,\n"
//                    + " `Amount_Collected` float NOT NULL,\n"
//                    + " PRIMARY KEY (`Serial_No`)\n"
//                    + ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1";
//            state.addBatch(q);
            
            state.executeBatch();
//            state.executeUpdate(sql);
//            state.executeUpdate(a);
//            state.executeUpdate(b);
//            state.executeUpdate(c);
//            state.executeUpdate(d);
//            state.executeUpdate(e);
            /*state.executeUpdate(f);state.executeUpdate(g);
    state.executeUpdate(k);
    state.executeUpdate(h);*/
            state.close();
            conect.close();
            JOptionPane.showMessageDialog(null, "Tables Created successfully!");
        } catch (SQLException ex) {
            System.out.print(ex);
            JOptionPane.showMessageDialog(null, ex);
        }
        try {
            Connection2();
            
            String sql1 = "Insert into pass_word_pharmacist values('Admin','systemadmin','Admin',1) ";
            //String sql2="Insert into feetable values('Two',0,0,0) ";
            //String sql3="Insert into feetable values('Three',0,0,0) ";
            //String sql4="Insert into feetable values('Four',0,0,0) ";

            state.executeUpdate(sql1); //state.executeUpdate(sql2);
            // state.executeUpdate(sql3); //state.executeUpdate(sql4);

            JOptionPane.showMessageDialog(null, "Admin Added,Click Ok to Continue");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    public void select() {
        try {
            File inputFile = new File(".\\Connection.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
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
                    txtUserName.setText(eElement.getElementsByTagName("user").item(0).getTextContent());
                    System.out.println("Database : "
                            + eElement.getElementsByTagName("database").item(0).getTextContent());
                    txtDbName.setText(eElement.getElementsByTagName("database").item(0).getTextContent());
                    System.out.println("Password : "
                            + eElement.getElementsByTagName("password").item(0).getTextContent());
                    String inputHash = "";
                    
                    txtPassword.setText(eElement.getElementsByTagName("password").item(0).getTextContent());
                    System.out.println("Url : "
                            + eElement.getElementsByTagName("url").item(0).getTextContent());
                    combourl.setText(eElement.getElementsByTagName("url").item(0).getTextContent());
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
      public Connection connecttoDB() {
        try {
             
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
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
                    txtUserName.setText(eElement.getElementsByTagName("user").item(0).getTextContent());

                    System.out.println("Database : "
                            + eElement.getElementsByTagName("database").item(0).getTextContent());
                    txtDbName.setText(eElement.getElementsByTagName("database").item(0).getTextContent());
                    System.out.println("Password : "
                            + eElement.getElementsByTagName("password").item(0).getTextContent());

                    txtPassword.setText(eElement.getElementsByTagName("password").item(0).getTextContent());
                    System.out.println("Url : " + eElement.getElementsByTagName("url").item(0).getTextContent());
                    combourl.setText(eElement.getElementsByTagName("url").item(0).getTextContent());

                }
            }
            
        } catch (ParserConfigurationException | SAXException | IOException ex) {
           Logger.getLogger(DBsConnection.class.getName()).log(Level.SEVERE, null, ex);
       }
       /* try {
                Class.forName("com.mysql.jdbc.Driver");
                conect= DriverManager.getConnection("jdbc:mysql://" + combourl.getText() + ":3306/" + txtDbName.getText(),
                        txtUserName.getText(),
                        txtPassword.getText());
                // state = conect.prepareStatement(url);
                 lebelStatus.setText("CONNECTED TO DATABASE....");

            } catch (ClassNotFoundException|SQLException  e) {
                System.out.print(e);
                lebelStatus.setText("COULD NOT CONNECT TO THE DATABASE, AN ERROR OCCURED");
                lebelStatus.setForeground(Color.red);            
        } */
       // select();
        pass = txtPassword.getText();
        url = url = "jdbc:mysql://" + combourl.getText() + ":3306/" + txtDbName.getText();
        //combourl.getSelectedItem().toString() + txtDbName.getText();
        user = txtUserName.getText();
        pass = txtPassword.getText();
        
         boolean st;// = true;
         
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conect = DriverManager.getConnection(url, user, pass);
            state = conect.prepareStatement(url);

            resultset = state.executeQuery("SHOW TABLES;");
            resultset = state.getResultSet();
            st = resultset.next();
            if (st) {
                lebelStatus.setText("Connected to the Database");
                lebelStatus.setForeground(Color.BLACK);
            } else {
                lebelStatus.setText("Run script for complete DB setup");
                lebelStatus.setForeground(Color.red);
            } 
           
        } catch (ClassNotFoundException | SQLException  e) {
            System.out.print(e);
            lebelStatus.setText("COULD NOT CONNECT TO THE DATABASE, AN ERROR OCCURED");
            lebelStatus.setForeground(Color.red);
        } 
//        finally{
//            try {
//                if (conect != null) {
//                    conect.close();
//                }
//                if (state != null) {
//                    state.close();
//                }
//            } catch (SQLException ex) {
//
//            }
//        }
        return conect;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lebelStatus = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txtDbName = new javax.swing.JTextField();
        txtUserName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        combourl = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(401, 215));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        lebelStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lebelStatus.setText(" ");

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton1.setText("Test");
        jButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton2.setText("Save");
        jButton2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton3.setText("Get");
        jButton3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Database Configurations", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        txtDbName.setText(" ");

        txtUserName.setText(" ");

        jLabel3.setText("Database name");

        jLabel4.setText("User Name");

        jLabel5.setText("Password");

        jLabel6.setText("Server");

        combourl.setText(" ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                    .addComponent(txtDbName)
                    .addComponent(txtPassword)
                    .addComponent(combourl))
                .addGap(24, 24, 24))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDbName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(combourl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jButton4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton4.setText("Run Script");
        jButton4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton5.setText("BackUp");
        jButton5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lebelStatus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lebelStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Connection2();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed
    public static void createXmlFile(Document doc, String user, String database, String password, String url) throws Exception {
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
        
        Element element4 = doc.createElement("url");
        root.appendChild(element4);
        Text text4 = doc.createTextNode(url);
        element4.appendChild(text4);
        
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
        String xmlString = sw.toString();
        
        File file = new File(".\\Connection.xml");
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)))) {
            bw.write(xmlString);
            bw.flush();
        }
    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        File file = new File(".\\Connection.xml");
        if (file.exists()) {
            file.delete();
            System.out.println("File deleted....");
            
        }
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            
            createXmlFile(doc, txtUserName.getText().trim(), txtDbName.getText().trim(), txtPassword.getText().trim(), combourl.getText().trim());
            System.out.println("Xml File Created Successfully");
            JOptionPane.showMessageDialog(null, "File Created Successfully");
            Connection2();
        } catch (Exception E) {
            System.out.println(E);
        }
        //homie.enable();
// TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        select();

// TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        //homie.enable();        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        creatTables();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
       
        //txtpath.setText(backUpPath);
        
        try {
            String backUpPath = "";
            JFileChooser fc = null;
            if (fc == null) {
                fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.setAcceptAllFileFilterUsed(false);
            }
            int returnVal = fc.showDialog(null, "Open");
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                backUpPath = file.getAbsolutePath();
            }
            try {
                File inputFile = new File(".\\Connection.xml");
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(inputFile);
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
                        txtUserName.setText(eElement.getElementsByTagName("user").item(0).getTextContent());
                        System.out.println("Database : "
                                + eElement.getElementsByTagName("database").item(0).getTextContent());
                        txtDbName.setText(eElement.getElementsByTagName("database").item(0).getTextContent());
                        System.out.println("Password : "
                                + eElement.getElementsByTagName("password").item(0).getTextContent());
                        String inputHash = "";

                        txtPassword.setText(eElement.getElementsByTagName("password").item(0).getTextContent());
                        System.out.println("Url : "
                                + eElement.getElementsByTagName("url").item(0).getTextContent());
                        combourl.setText(eElement.getElementsByTagName("url").item(0).getTextContent());
                    }
                }
            } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
                JOptionPane.showMessageDialog(null, e);
            } 
            Backup b = new Backup();
            byte[] data = b.getData(combourl.getText().trim(), "3306", txtUserName.getText().trim(), txtPassword.getText().trim(), txtDbName.getText().trim()).getBytes();
            File filedst = new File(backUpPath + "\\" + txtDbName.getText().trim() + ".zip");
            try (FileOutputStream dest = new FileOutputStream(filedst); ZipOutputStream zip = new ZipOutputStream(
                    new BufferedOutputStream(dest))) {
                zip.setMethod(ZipOutputStream.DEFLATED);
                zip.setLevel(Deflater.BEST_COMPRESSION);
                zip.putNextEntry(new ZipEntry("data.sql"));
                zip.write(data);
            }
            JOptionPane.showMessageDialog(this, "Back Up Successfully." + "\n" + "For Database: " + txtDbName.getText().trim() + "\n        " + "On Dated: ", "Database BackUp Wizard", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | HeadlessException ex) {
            JOptionPane.showMessageDialog(this, "Back Up Failed." + "\n" + "For Database: " + txtDbName.getText().trim() + "\n " + "On     Dated: ", "Database BackUp Wizard", JOptionPane.INFORMATION_MESSAGE);
        }
        
       // homie.backUpDB.show(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // String k=JOptionPane.showInputDialog(null, "LOG IN", "Enter password?", JOptionPane.QUESTION_MESSAGE);
        // if("DockmanHacker".equals(k))
        //{

//login.show(true);
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DBsConnection().setVisible(true);
            }
        });
        //}
        //else
        //{
        //JOptionPane.showMessageDialog(null, "Wrong Password You can not Continue\n,email josephayoma1@gmail\n or \n Call him on +254729696690 For help");
        //}
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField combourl;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lebelStatus;
    private javax.swing.JTextField txtDbName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
}
