/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dta.system;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Dockman
 */
public class comboFill {
    
    DBsConnection dbCon = new DBsConnection();
    Connection conn;//=dbConnect.conn;
    Statement state = null;
    Connection conect = null;
    ResultSet rs;
    DefaultTableModel model;
    
    public void selectCreditOfficer(JLabel txtUser, JComboBox comboCrdOff, JComboBox comboCreOff, JComboBox comboCrdOfficerM1, JComboBox comboCrdOfficerM,
            JComboBox comboMegrp, JComboBox comboProgram, JTable tblMember) {
        //int selectedRowIndex = tblLoansPendingApproval.getSelectedRow();
        //lblLoanRate.setText(null);
        try {
            //connectionString.Connection2();
           // state = connectionString.conect.createStatement();

             FillCombobox(comboCrdOff, "credit_officer", "Name", "Is_Active", "1");
            FillCombobox(comboCreOff, "credit_officer", "Name", "Is_Active", "1");
            FillCombobox(comboCrdOfficerM1, "credit_officer", "Name", "Is_Active", "1");
             FillCombobox(comboCrdOfficerM, "credit_officer", "Name", "Is_Active", "1");

             FillcomboMGrp(comboMegrp, comboCrdOfficerM);
            comboProgram(comboProgram, comboMegrp, comboCrdOfficerM);
            dbCon.Connection2();

            String sql1 = "SELECT `Serial_No`, `Name`, `Linked_User`, `Start_Date`, `Is_Active` FROM `credit_officer` where `Linked_User`='" + txtUser.getText().trim() + "'";
            rs = state.executeQuery(sql1);

            if (rs.next()) {
                String Name = rs.getString("Name");

                comboCrdOff.setSelectedItem(Name);
                comboCreOff.setSelectedItem(Name);

                comboCrdOfficerM1.setSelectedItem(Name);
                comboCrdOfficerM.setSelectedItem(Name);

                comboCrdOff.disable();
                comboCrdOfficerM1.disable();
                comboCrdOfficerM.disable();
                comboCreOff.disable();

            }

        } catch (SQLException | NumberFormatException ex) {

        }
    }
    
    public void FillComboboxAdjust(JComboBox combo, String tableName, String columName, String whereColumn, String condition) {
        combo.removeAllItems();
        try {
            dbCon.Connection2();

            combo.insertItemAt("Adjust", 0);
            combo.setSelectedItem("Adjust");
            String drugname;
            //state = dbCon.conect.createStatement();
            state = dbCon.conect.createStatement();
            String sql1 = "SELECT `" + columName + "` FROM `" + tableName + "` where " + whereColumn + "='" + condition + "';";
            rs = state.executeQuery(sql1);

            while (rs.next()) {

                drugname = rs.getString(columName);
                combo.addItem(drugname);
            }
            //dbCon.conect.close();
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    public void FillCombobox(JComboBox combo, String tableName, String columName, String whereColumn, String condition) {
        combo.removeAllItems();
        try {
            dbCon.Connection2();
            combo.insertItemAt("Select", 0);
            combo.setSelectedItem("Select");
            String drugname;
            //state = dbCon.conect.createStatement();
            state = dbCon.conect.createStatement();
            String sql1 = "SELECT `" + columName + "` FROM `" + tableName + "` where " + whereColumn + "='" + condition + "';";
            rs = state.executeQuery(sql1);

            while (rs.next()) {

                drugname = rs.getString(columName);
                combo.addItem(drugname);
            }
            //dbCon.conect.close();
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    public void AcctCombo(JComboBox comboAccType, JTable AccMemberList) {
        dbCon.Connection2();
        comboAccType.removeAllItems();
        comboAccType.insertItemAt("Select".toUpperCase().trim(), 0);
        comboAccType.setSelectedItem("Select".toUpperCase().trim());
        //comboAccounts.removeAllItems();
        // comboAccounts.insertItemAt("Select".toUpperCase().trim(), 0);
        // comboAccounts.setSelectedItem("Select".toUpperCase().trim());
        String clietname;
        try {
            state = dbCon.conect.createStatement();
            String sql1 = "SELECT  `Account_Name`  FROM `account_configuration`";// where `Loan_Type`='" + comboClientLoan.getSelectedItem().toString() + "'";
            rs = state.executeQuery(sql1);

            while (rs.next()) {

                clietname = rs.getString("Account_Name");
                comboAccType.addItem(clietname.toUpperCase().trim());
                // comboAccounts.addItem(clietname.toUpperCase().trim());

            }

        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        /* try {
            model = (DefaultTableModel) AccMemberList.getModel();
            model.setRowCount(0);
            state = conect.createStatement();
            String sql1 = "SELECT   `m_IDNo`, `m_Number` FROM `dbo_members`;";

            rs = state.executeQuery(sql1);

            while (rs.next()) {

                // String username = rs.getString("m_Name");
                String userpassword = rs.getString("m_IDNo");
                String mNumber = rs.getString("m_Number");
                model = (DefaultTableModel) AccMemberList.getModel();
                model.addRow(new Object[]{userpassword, mNumber});

            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }*/
    }

    public void clientAccount(JComboBox comboAccType1, JTable tblActiveLoanBalance) {
        dbCon.Connection2();
        comboAccType1.removeAllItems();
        comboAccType1.insertItemAt("Select".toUpperCase().trim(), 0);
        comboAccType1.setSelectedItem("Select".toUpperCase().trim());
        String clietname;
        model = (DefaultTableModel) tblActiveLoanBalance.getModel();
        int Selectindex = tblActiveLoanBalance.getSelectedRow();
        try {
            state = dbCon.conect.createStatement();
            String sql1 = "SELECT `Serial_No`, `Member_ID`, `Member_No`, `Acct_Type`, `Current_Balance` FROM `acct_record` "
                    + "where `Member_ID`='" + model.getValueAt(Selectindex, 2) + "'";
            rs = state.executeQuery(sql1);

            while (rs.next()) {

                clietname = rs.getString("Acct_Type");
                comboAccType1.addItem(clietname.toUpperCase().trim());

            }

        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    public void FillcomboMGrp(JComboBox comboMegrp, JComboBox comboCrdOfficerM) {
        comboMegrp.removeAllItems();

        try {
            dbCon.Connection2();
            comboMegrp.insertItemAt("Select", 0);
            comboMegrp.setSelectedItem("Select");

            String drugname;

            state = dbCon.conect.createStatement();
            String sql1 = "SELECT  `Group_Name`,`Program_Linked` FROM `group_info` WHERE Is_Active=1 "
                    + "AND credit_officer='" + comboCrdOfficerM.getSelectedItem() + "';";
            rs = state.executeQuery(sql1);

            while (rs.next()) {

                drugname = rs.getString("Group_Name");
                comboMegrp.addItem(drugname);

            }
            //dbCon.conect.close();
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    public void FillcomboMGrpMeetingDay(JComboBox comboMegrp, JComboBox comboBox) {
        comboMegrp.removeAllItems();

        try {
            dbCon.Connection2();
            comboMegrp.insertItemAt("Select", 0);
            comboMegrp.setSelectedItem("Select");

            String drugname;

            state = dbCon.conect.createStatement();
            String sql1 = "SELECT  `Group_Name`,`Program_Linked` FROM `group_info` "
                    + "WHERE Is_Active=1 AND credit_officer='" + comboBox.getSelectedItem() + "';";
            rs = state.executeQuery(sql1);

            while (rs.next()) {

                drugname = rs.getString("Group_Name");
                comboMegrp.addItem(drugname);

            }
            //dbCon.conect.close();
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    public void comboClients(JComboBox comboMemberFill, JComboBox comboGrp) {
        comboMemberFill.removeAllItems();
        try {
            dbCon.Connection2();
            comboMemberFill.insertItemAt("Select", 0);
            comboMemberFill.setSelectedItem("Select");
            String drugname;
            state = dbCon.conect.createStatement();
            String sql1 = "SELECT `members_info`.`Member_Name`,`member_admission`.`Member_ID_No` FROM `members_info`, `member_admission`"
                    + " WHERE `member_admission`.`Member_ID_No`= `members_info`.`Member_ID_No` "
                    + "AND `member_admission`.`Group_Name`='" + comboGrp.getSelectedItem() + "';";

            String sqlMemberName = "SELECT `Serial_No`, `Member_Name`, `Member_ID_No`, `Member_Gender`, `Member_Phone`, `DOB`,"
                    + " `Marital_Status`, `Current_Address`, `Home_Town`, `Group_Name`, `Is_Active`, `Admin_Date` FROM `members_info` "
                    + "where `Group_Name`='" + comboGrp.getSelectedItem() + "' and `Is_Active`=1;";
            rs = state.executeQuery(sqlMemberName);

            while (rs.next()) {

                drugname = rs.getString("Member_Name");
                comboMemberFill.addItem(drugname);
            }
            //dbCon.conect.close();
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    public void comboProgram(JComboBox comboProgram, JComboBox comboGrp, JComboBox comboCreOff) {

        comboProgram.removeAllItems();
        try {
            dbCon.Connection2();

            //comboProgram.insertItemAt("Select", 0);
            //comboProgram.setSelectedItem("Select");
            // String drugname;
            String program;

            state = dbCon.conect.createStatement();
            String sql1 = "SELECT  `Program_Linked` FROM `group_info` WHERE Is_Active=1 AND `Group_Name`='" + comboGrp.getSelectedItem() + "' AND"
                    + " credit_officer='" + comboCreOff.getSelectedItem() + "';";
            rs = state.executeQuery(sql1);

            while (rs.next()) {

                program = rs.getString("Program_Linked");

                comboProgram.addItem(program);
                comboProgram.insertItemAt(program, 0);
            }
            //dbCon.conect.close();
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    public void fillCLientID(JComboBox comboMemberFill, JTextField txtCIDNo, JComboBox comboGrp) {
        txtCIDNo.setText(null);
        try {
            dbCon.Connection2();
            String program;

            state = dbCon.conect.createStatement();
            String sql1 = "SELECT  `Member_ID_No`,`Is_Active` FROM `members_info` "
                    + "where `Member_Name`='" + comboMemberFill.getSelectedItem() + "' AND `Group_Name`='" + comboGrp.getSelectedItem() + "' AND `Is_Active`=1 ;";
            rs = state.executeQuery(sql1);

            while (rs.next()) {

                program = rs.getString("Member_ID_No");
                txtCIDNo.setText(program);
            }
            //dbCon.conect.close();
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void selectFeeCode(JComboBox comboFeetype, JTextField txtfeeCode) {
        txtfeeCode.setText(null);
        try {
            dbCon.Connection2();
            String program;
            
            state = dbCon.conect.createStatement();
            String sql1 = "SELECT `Serial_No`, `Fee_Name`, `Description`, `Is_Active` FROM `fee_charge` WHERE `Fee_Name`='" + comboFeetype.getSelectedItem() + "' AND "
                    + "`Is_Active`=1 ;";
            rs = state.executeQuery(sql1);
            
            while (rs.next()) {
                
                program = rs.getString("Serial_No");                
                txtfeeCode.setText(program);
            }
            //dbCon.conect.close();
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }        
    }
    
}
