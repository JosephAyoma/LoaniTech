/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dta.system;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author Dockman
 */
public class dbInsert {

    DBsConnection connectionString = new DBsConnection();
    DBsConnection DBclass = new DBsConnection();
    Connection conn;
    Statement state;
    PreparedStatement pstmt = null;
    ResultSet rs;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    LocalDate dt = LocalDate.now();
    // comboFill fillCr = new comboFill();
    DefaultTableModel model;
    // groupManagement groupManager=new groupManagement();
    // dbInsert save = new dbInsert();

    public byte[] computeHash(String x)
            throws Exception {
        java.security.MessageDigest d = null;
        d = java.security.MessageDigest.getInstance("SHA-1");
        d.reset();
        d.update(x.getBytes());
        return d.digest();
    }

    public void output(JTable table, String query) {

        try {
            connectionString.Connection2();
            String sql = query;
            state = connectionString.conect.createStatement();
            rs = state.executeQuery(sql);
            ResultSetMetaData md = rs.getMetaData();
            DefaultTableModel Table;
            Table = (DefaultTableModel) table.getModel();

            int columns = md.getColumnCount();
            Table.setColumnCount(0);
            for (int i = 1; i <= columns; i++) {

                Table.addColumn(md.getColumnName(i));
            }
            Table.setRowCount(0);
            while (rs.next()) {
                String[] a = new String[columns];
                for (int i = 0; i < columns; i++) {
                    a[i] = rs.getString(i + 1);

                }
                Table.addRow(a);
            }
            rs.close();
            // stmt .close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.print(e);// JOptionPane.showMessageDialog(this,e,e.getMessage(),WIDTH,null);
        }
        //return jtMaintrecord;
    }

    public static String byteArrayToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    public void SaveGroup(JTextField txtGroupName, JTextField txtContactPerson, JTextField txtContactNo,
            String formedDate, JComboBox comboProgGrp, JComboBox comboMeetingDay, JCheckBox checkGrpActive,
            JComboBox comboCrdOff, JTable tblGroups) {
        int status;
        if (checkGrpActive.isSelected()) {
            status = 1;
        } else {
            status = 0;
        }
        try {
            connectionString.Connection2();

            String Insertuser = "INSERT INTO `group_info`(`Group_Name`, `Formed_Date`, `Meeting_Day`, `Contact_Person`,"
                    + "`Contact_Number`, `Program_Linked`, `Is_Active`, `Credit_Officer`)"
                    + "Values"
                    + "('" + txtGroupName.getText().trim() + "',"
                    + "'" + formedDate + "',"
                    + "'" + comboMeetingDay.getSelectedItem() + "',"
                    + "'" + txtContactPerson.getText().trim() + "',"
                    + "'" + txtContactNo.getText().trim() + "',"
                    + "'" + comboProgGrp.getSelectedItem() + "',"
                    + "'" + status + "',"
                    + "'" + comboCrdOff.getSelectedItem() + "');";
            state = connectionString.conect.createStatement();
            state.executeUpdate(Insertuser);

            JOptionPane.showMessageDialog(null, "Group Saved Successfully....");

            txtGroupName.setText("");
            txtContactPerson.setText("");
            txtContactNo.setText("");
            comboProgGrp.setSelectedItem("Select");
            connectionString.conect.close();
            String sql = "SELECT `Serial_No`, `Group_Name`, `Formed_Date`, `Meeting_Day`, `Contact_Person`, "
                    + "`Contact_Number`, `Program_Linked`, `Is_Active`, `Credit_Officer` FROM `group_info`";
            output(tblGroups, sql);
        } catch (SQLIntegrityConstraintViolationException ex) {

            JOptionPane.showMessageDialog(null, "Group Already Exists");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        } catch (HeadlessException ex) {
            Logger.getLogger(dbInsert.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updateGroupInfo(JTextField txtGroupName, JTextField txtContactPerson, JTextField txtContactNo,
            String formedDate, JComboBox comboProgGrp, JComboBox comboMeetingDay, JCheckBox checkGrpActive,
            JComboBox comboCrdOff, JTable tblGroups, JLabel lblContainer) {
        int status;
        if (checkGrpActive.isSelected()) {
            status = 1;
        } else {
            status = 0;
        }
        try {
            connectionString.Connection2();

            String Insertuser = "UPDATE `group_info` SET"
                    + "`Group_Name`='" + txtGroupName.getText().trim() + "',"
                    + "`Formed_Date`='" + formedDate + "',"
                    + "`Meeting_Day`='" + comboMeetingDay.getSelectedItem() + "',"
                    + "`Contact_Person`='" + txtContactPerson.getText().trim() + "',"
                    + "`Contact_Number`='" + txtContactNo.getText().trim() + "',"
                    + "`Program_Linked`='" + comboProgGrp.getSelectedItem() + "',"
                    + "`Is_Active`='" + status + "',"
                    + "`Credit_Officer`='" + comboCrdOff.getSelectedItem() + "' where `Serial_No`=" + lblContainer.getText().trim() + ";";
            state = connectionString.conect.createStatement();
            state.executeUpdate(Insertuser);

            JOptionPane.showMessageDialog(null, "Group Information Updated Successfully....");

            txtGroupName.setText("");
            txtContactPerson.setText("");
            txtContactNo.setText("");
            comboProgGrp.setSelectedItem("Select");
            connectionString.conect.close();
            String sql = "SELECT `Serial_No`, `Group_Name`, `Formed_Date`, `Meeting_Day`, `Contact_Person`, "
                    + "`Contact_Number`, `Program_Linked`, `Is_Active`, `Credit_Officer` FROM `group_info`";
            output(tblGroups, sql);
        } catch (SQLIntegrityConstraintViolationException ex) {

            JOptionPane.showMessageDialog(null, "Group Already Exists");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        } catch (HeadlessException ex) {
            Logger.getLogger(dbInsert.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void SaveCreditOfficer(JCheckBox officerStatus, JTextField txtOfficername,
            JComboBox comboLinkUser, String Effectivedate, JTable tblCreditOfficer) {
        int status;
        if (officerStatus.isSelected()) {
            status = 1;
        } else {
            status = 0;
        }
        if (txtOfficername.getText().isEmpty() | Effectivedate.equals("") | comboLinkUser.getSelectedItem().equals("Select")) {
            JOptionPane.showMessageDialog(null, "Ensure all the fields and selected Approriately!");
        } else {
            try {
                connectionString.Connection2();

                String Insertuser = "INSERT INTO `credit_officer`(`Name`, `Linked_User`, `Start_Date`, `Is_Active`)"
                        + "Values"
                        + "('" + txtOfficername.getText().trim() + "',"
                        + "'" + comboLinkUser.getSelectedItem().toString() + "',"
                        + "'" + Effectivedate + "',"
                        + "" + status + ");";
                state = connectionString.conect.createStatement();
                state.executeUpdate(Insertuser);

                JOptionPane.showMessageDialog(null, "Credit Officer Added Successfully");

                txtOfficername.setText("");

                comboLinkUser.setSelectedItem("Credit Officer");
                connectionString.conect.close();
                String sql = "SELECT `Serial_No`, `Name`, `Linked_User`, `Start_Date`, `Is_Active` FROM `credit_officer`";
                output(tblCreditOfficer, sql);
            } catch (SQLIntegrityConstraintViolationException ex) {

                JOptionPane.showMessageDialog(null, "Officer Already Exists");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
            }
        }

    }

    public void updateCreditOfficer(JCheckBox officerStatus, JTextField txtOfficername,
            JComboBox comboLinkUser, String Effectivedate, JTable tblCreditOfficer, JLabel lblContainer) {
        int status;
        if (officerStatus.isSelected()) {
            status = 1;
        } else {
            status = 0;
        }
        if (txtOfficername.getText().isEmpty() | Effectivedate.equals("") | comboLinkUser.getSelectedItem().equals("Select")) {
            JOptionPane.showMessageDialog(null, "Ensure all the fields and selected Approriately!");
        } else {
            try {
                connectionString.Connection2();

                String Insertuser = "UPDATE `credit_officer`"
                        + " SET `Name`='" + txtOfficername.getText().trim() + "',"
                        + "`Linked_User`='" + comboLinkUser.getSelectedItem().toString() + "',"
                        + "`Start_Date`='" + Effectivedate + "',"
                        + "`Is_Active`=" + status + " WHERE `Serial_No`=" + lblContainer.getText() + ";";

                state = connectionString.conect.createStatement();
                state.executeUpdate(Insertuser);

                JOptionPane.showMessageDialog(null, "Credit Officer information Updated Successfully");

                txtOfficername.setText("");

                comboLinkUser.setSelectedItem("Credit Officer");
                connectionString.conect.close();
                String sql = "SELECT `Serial_No`, `Name`, `Linked_User`, `Start_Date`, `Is_Active` FROM `credit_officer`";
                output(tblCreditOfficer, sql);
            } catch (SQLIntegrityConstraintViolationException ex) {
                JOptionPane.showMessageDialog(null, "Officer Already Exists");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
            }
        }

    }

    public void SaveUsers(JCheckBox UserStatus, JTextField txtNewUsername,
            JPasswordField txtUserpassword, JComboBox comboRole, JTextField txtuserPasswordConfirm) {
        int status;
        if (UserStatus.isSelected()) {
            status = 1;
        } else {
            status = 0;
        }

        if (txtUserpassword.getText().trim().equals(txtuserPasswordConfirm.getText().trim())) {
            try {
                connectionString.Connection2();
                String hash = byteArrayToHexString(this.computeHash(txtUserpassword.getText().trim()));

                String Insertuser = "INSERT INTO `pass_word_pharmacist`(`USER_NAME`, `USER_PASSWORD`, `ROLE`, `STATUS`)"
                        + "Values"
                        + "('" + txtNewUsername.getText().trim() + "',"
                        + "'" + hash + "',"
                        + "'" + comboRole.getSelectedItem() + "',"
                        + "" + status + ");";
                state = connectionString.conect.createStatement();
                state.executeUpdate(Insertuser);

                JOptionPane.showMessageDialog(null, "USER SUCCESSFULLY ADDED.....");

                txtNewUsername.setText("");
                txtUserpassword.setText("");
                txtuserPasswordConfirm.setText("");
                comboRole.setSelectedItem("Credit Officer");
                connectionString.conect.close();
            } catch (SQLIntegrityConstraintViolationException ex) {

                JOptionPane.showMessageDialog(null, "USER EXSITS");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
            } catch (Exception ex) {
                Logger.getLogger(dbInsert.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Password Must be The same");

        }
    }

    public void ChangePassword(String Username, String oldpassword,
            String newpassword, String confirmNepassowrd) {
        //dbCon.Connection2();
        String password = "";
        if ((!newpassword.equals(confirmNepassowrd))) {
            JOptionPane.showMessageDialog(null, "New Password should be the same");
        } else if ((newpassword.equals(oldpassword))) {
            JOptionPane.showMessageDialog(null, "New password can not be the same as the old password");
        } else {
            try {
                String hash = byteArrayToHexString(this.computeHash(newpassword));
                String oldpasswordHash = byteArrayToHexString(this.computeHash(oldpassword));

                DBclass.connecttoDB();

                try {

                    String sql = "SELECT `USER_NAME`, `USER_PASSWORD`, `ROLE`, `STATUS`, `Log_Status` FROM `pass_word_pharmacist`"
                            + " WHERE USER_NAME='" + Username + "';";
                    pstmt = DBclass.conect.prepareStatement(sql);
                    DBclass.resultset = pstmt.executeQuery(sql);

                    while (DBclass.resultset.next()) {
                        password = DBclass.resultset.getString("USER_PASSWORD");
                    }
                    if (password.equals(oldpasswordHash)) {
                        String update = "UPDATE `pass_word_pharmacist`  SET `USER_PASSWORD`='" + hash + "'"
                                + " WHERE `USER_NAME`='" + Username + "';";
                        pstmt = DBclass.conect.prepareStatement(update);
                        pstmt.executeUpdate(update);
                        JOptionPane.showMessageDialog(null, "Password Changed Successfully, Log in again");
                        System.exit(0);

                    } else {
                        JOptionPane.showMessageDialog(null, "Wrong Old Password check again");
                    }

                } catch (SQLException ex) {
                }

            } catch (Exception ex) {

            }

        }

    }

    public void addingGaurantor(JTable tlbGaurantor, JTextField txtGName,
            JTextField txtGRelation, JTextField txtGAddress,
            JTextField txtGContactN, String txtGDOB, JTextField txtGId) {

        model = (DefaultTableModel) tlbGaurantor.getModel();
        model.addRow(new Object[]{
            txtGName.getText().trim(),
            txtGId.getText().trim(),
            txtGRelation.getText().trim(),
            txtGDOB,
            txtGAddress.getText().trim(),
            txtGContactN.getText().trim()
        });
        txtGName.setText(null);
        txtGId.setText(null);
        txtGRelation.setText(null);
        txtGAddress.setText(null);
        txtGContactN.setText(null);
    }

    public void SaveLoanProgram(JTextField txtLoanName, JTextField txtLoanDesc, JTextField txtProcessing, JTextField txtInsuranceFee, JTable tblLoanType) {
        try {
            connectionString.Connection2();

            String Insertuser = "INSERT INTO `loan_type`(`Loan_Type`, `Processing_Fee`, `Insurance~Verification Fee`, `Description`) "
                    + "Values"
                    + "('" + txtLoanName.getText().trim() + "',"
                    + "'" + txtProcessing.getText().trim() + "',"
                    + "'" + txtInsuranceFee.getText().trim() + "',"
                    + "'" + txtLoanDesc.getText().trim() + "');";
            state = connectionString.conect.createStatement();
            state.executeUpdate(Insertuser);
            JOptionPane.showMessageDialog(null, "Loan Program Added Successfully");

            txtLoanName.setText("");
            txtLoanDesc.setText("");
            txtLoanName.setText(null);
            txtLoanDesc.setText(null);
            txtProcessing.setText(null);
            txtInsuranceFee.setText(null);

            connectionString.conect.close();
            String sql = "SELECT `Serial_No`, `Loan_Type`, `Processing_Fee`, `Insurance~Verification Fee`, `Program_Status`, `Description` FROM `loan_type` where `Program_Status`=1 ";
            output(tblLoanType, sql);
        } catch (SQLIntegrityConstraintViolationException ex) {

            JOptionPane.showMessageDialog(null, "Similar Loan Program Already Exists");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        }
    }

    public void SaveGLAccounts(JTextField txtName, JTextField txtCode, JTextField txtDescription,
            JComboBox comboType) {
        try {
            connectionString.Connection2();

            String Insertuser = "INSERT INTO `glaccounts`(`Account_Code`, `Account_Name`, `Account_Type`, `Description`)"
                    + "Values"
                    + "('" + txtCode.getText().trim() + "',"
                    + "'" + txtName.getText().trim() + "',"
                    + "'" + comboType.getSelectedItem() + "',"
                    + "'" + txtDescription.getText().trim() + "');";
            state = connectionString.conect.createStatement();
            state.executeUpdate(Insertuser);
            JOptionPane.showMessageDialog(null, "Account type saved Successfully");

            txtCode.setText("");
            txtName.setText("");
            txtDescription.setText(null);

            connectionString.conect.close();
//            String sql = "SELECT `Serial_No`, `Loan_Type`, `Processing_Fee`, `Insurance~Verification Fee`, `Program_Status`, `Description` FROM `loan_type` where `Program_Status`=1 ";
//            output(tblLoanType, sql);
        } catch (SQLIntegrityConstraintViolationException ex) {

            JOptionPane.showMessageDialog(null, "Similar Account type Already Exists");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        }
    }

    public void SaveDepartments(JTextField txtDepartmentName, JTextField txtDepartDescription, JTable tblDepartments) {
        try {
            connectionString.Connection2();

            String Insertuser = "INSERT INTO `department`(`Department_Name`, `Description`) "
                    + "Values"
                    + "('" + txtDepartmentName.getText().trim() + "',"
                    + "'" + txtDepartDescription.getText().trim() + "');";

            state = connectionString.conect.createStatement();
            state.executeUpdate(Insertuser);
            JOptionPane.showMessageDialog(null, "Saved Successfully");

            txtDepartmentName.setText("");
            txtDepartDescription.setText(null);

            connectionString.conect.close();
            String sql = "SELECT `Serial_No`, `Department_Name`, `Description` FROM `department`";
            output(tblDepartments, sql);
        } catch (SQLIntegrityConstraintViolationException ex) {

            JOptionPane.showMessageDialog(null, "Similar Account type Already Exists");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        }
    }

    public void updateLoanType(JTextField txtLoanName, JTextField txtLoanDesc, JTextField txtProcessing,
            JTextField txtInsuranceFee, JTable tblLoanType, JLabel lblContainer) {
        try {
            connectionString.Connection2();

            String Insertuser = "UPDATE `loan_type` SET"
                    + "`Loan_Type`='" + txtLoanName.getText().trim() + "',"
                    + "`Processing_Fee`='" + txtProcessing.getText().trim() + "',"
                    + "`Insurance~Verification Fee`='" + txtInsuranceFee.getText().trim() + "',"
                    + "`Description`='" + txtLoanDesc.getText().trim() + "' WHERE `Serial_No`='" + lblContainer.getText().trim() + "';";
            state = connectionString.conect.createStatement();
            state.executeUpdate(Insertuser);
            JOptionPane.showMessageDialog(null, "Loan Program Updated Successfully");

            txtLoanName.setText("");
            txtLoanDesc.setText("");
            txtProcessing.setText("");
            txtInsuranceFee.setText("");

            connectionString.conect.close();
            String sql = "SELECT `Serial_No`, `Loan_Type`, `Processing_Fee`, `Insurance~Verification Fee`, `Program_Status`, `Description` FROM `loan_type` where `Program_Status`=1 ";
            output(tblLoanType, sql);
        } catch (SQLIntegrityConstraintViolationException ex) {

            JOptionPane.showMessageDialog(null, "Similar Loan Program Already Exists");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        }
    }

    public void SaveLoanRate(JTextField txtLoanRate, JComboBox loanTypeCombo, JTextField txtRateDesc, JTable tblLoanRates) {
        try {
            connectionString.Connection2();
            String Insertuser = "INSERT INTO `loan_rates`( `Loan_Type`, `Loan_Rate`, `Description`) "
                    + "Values"
                    + "('" + loanTypeCombo.getSelectedItem() + "',"
                    + "'" + txtLoanRate.getText().trim() + "',"
                    + "'" + txtRateDesc.getText().trim() + "');";
            state = connectionString.conect.createStatement();
            state.executeUpdate(Insertuser);
            JOptionPane.showMessageDialog(null, "Loan Rate  Information  for " + loanTypeCombo.getSelectedItem().toString().toUpperCase() + " was  Added Successfully");
            connectionString.conect.close();
            txtRateDesc.setText(null);
            txtLoanRate.setText(null);
            connectionString.conect.close();

            String sql = "SELECT `Serial_No`, `Loan_Type`, `Loan_Rate`, `Description` FROM `loan_rates`";
            output(tblLoanRates, sql);

        } catch (SQLException | HeadlessException ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        }
    }

    public void SaveFeeCharges(JTextField txtFeename, JTextField txtFeeDesc, JTable tblFeeCharges) {
        try {
            connectionString.Connection2();
            String Insertuser = "INSERT INTO `fee_charge`(`Fee_Name`, `Description`)"
                    + "Values"
                    + "('" + txtFeename.getText().trim() + "',"
                    + "'" + txtFeeDesc.getText().trim() + "');";
            state = connectionString.conect.createStatement();
            state.executeUpdate(Insertuser);
            JOptionPane.showMessageDialog(null, "Fee Configuration Added Successfully");

            txtFeeDesc.setText(null);
            txtFeename.setText(null);

            String sql = "SELECT `Serial_No`, `Fee_Name`, `Description` FROM `fee_charge`";

            output(tblFeeCharges, sql);
        } catch (SQLException | HeadlessException ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        }
    }

    public void SavefeePayments(JTextField txtfeeClientId, JComboBox comboFeetype, JTextField txtfeeCollected,
            JTextField txtfeeCode, JTable tblFeeInfor, JXDatePicker feeDateCollected) {
        try {
            connectionString.Connection2();
            String Insertuser = "INSERT INTO `feel_collection`(`Client_ID`, "
                    + "`Fee_Type`, `Fee_Code`,`Date_Collected`, `Amount_Collected`) "
                    + "Values"
                    + "('" + txtfeeClientId.getText().trim() + "',"
                    + "'" + comboFeetype.getSelectedItem() + "',"
                    + "'" + txtfeeCode.getText().trim() + "',"
                    + "'" + format.format(feeDateCollected.getDate()) + "',"
                    + "'" + txtfeeCollected.getText().trim() + "');";
            state = connectionString.conect.createStatement();
            state.executeUpdate(Insertuser);
            JOptionPane.showMessageDialog(null, "Fee Collection Saved  Successfully");

            txtfeeClientId.setText(null);
            txtfeeCode.setText(null);
            txtfeeCollected.setText(null);

            String selectFee = "SELECT `feel_collection`.`Serial_No`,  `members_info`.`Member_Name`, `Client_ID`, `Fee_Type`, `Fee_Code`, `Date_Collected`, `Amount_Collected` "
                    + "FROM `feel_collection`,members_info where members_info.`Member_ID_No`=`feel_collection`.`Client_ID`";

            output(tblFeeInfor, selectFee);
        } catch (SQLException | HeadlessException ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        }
    }

    public void updateFeePayment(JTextField txtfeeClientId, JComboBox comboFeetype, JTextField txtfeeCollected,
            JTextField txtfeeCode, JTable tblFeeInfor, JXDatePicker feeDateCollected, JLabel lblContainer) {
        try {
            connectionString.Connection2();
            String Insertuser = "UPDATE `feel_collection` "
                    + "SET `Client_ID`='" + txtfeeClientId.getText().trim() + "',"
                    + "`Fee_Type`='" + comboFeetype.getSelectedItem() + "',"
                    + "`Fee_Code`='" + txtfeeCode.getText().trim() + "',"
                    + "`Date_Collected`='" + format.format(feeDateCollected.getDate()) + "',"
                    + "`Amount_Collected`='" + txtfeeCollected.getText().trim() + "' "
                    + "WHERE `Serial_No`='" + lblContainer.getText().trim() + "';";

            state = connectionString.conect.createStatement();
            state.executeUpdate(Insertuser);
            JOptionPane.showMessageDialog(null, "Fee Collection Updated Successfully");

            txtfeeClientId.setText(null);
            txtfeeCode.setText(null);
            txtfeeCollected.setText(null);

            String selectFee = "SELECT `feel_collection`.`Serial_No`,  `members_info`.`Member_Name`, `Client_ID`, `Fee_Type`, `Fee_Code`, `Date_Collected`, `Amount_Collected` "
                    + "FROM `feel_collection`,members_info where members_info.`Member_ID_No`=`feel_collection`.`Client_ID`";

            output(tblFeeInfor, selectFee);
        } catch (SQLException | HeadlessException ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        }

    }

    public void saveTransactProces(JTextField txtProcessName, JTextField txtProcessDesc, JTable tblTrasactionProcesses) {
        try {
            connectionString.Connection2();
            String Insertuser = "INSERT INTO `transact_pprocess`(`Fee_Name`, `Description`)"
                    + "Values"
                    + "('" + txtProcessName.getText().trim() + "',"
                    + "'" + txtProcessDesc.getText().trim() + "');";
            state = connectionString.conect.createStatement();
            state.executeUpdate(Insertuser);
            JOptionPane.showMessageDialog(null, "Transaction Configuration Added Successfully");

            txtProcessName.setText(null);
            txtProcessDesc.setText(null);

            String sql = "SELECT `Serial_No`, `Fee_Name`, `Description` FROM `transact_pprocess`";

            output(tblTrasactionProcesses, sql);
        } catch (SQLException | HeadlessException ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        }

    }

    public void updateTransactProcess(JTextField txtProcessName, JTextField txtProcessDesc, JTable tblTrasactionProcesses) {
        model = (DefaultTableModel) tblTrasactionProcesses.getModel();
        int selectedRowIndex = tblTrasactionProcesses.getSelectedRow();
        try {
            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String updone = "UPDATE `fee_charge`,`transact_pprocess` "
                    + "SET fee_charge.Fee_Name='" + txtProcessName.getText().trim() + "',"
                    + " fee_charge.Description='" + txtProcessDesc.getText().trim() + "',"
                    + "feel_collection.Fee_Type='" + txtProcessName.getText().trim() + "'"
                    + "WHERE fee_charge.Serial_No='" + model.getValueAt(selectedRowIndex, 0) + "'"
                    + "OR (feel_collection.Fee_Type='" + model.getValueAt(selectedRowIndex, 1) + "' AND fee_charge.Fee_Name='" + model.getValueAt(selectedRowIndex, 1) + "');";
            state.executeUpdate(updone);

            txtProcessDesc.setText(null);
            txtProcessName.setText(null);

            String sql = "SELECT `Serial_No`, `Fee_Name`, `Description` FROM `transact_pprocess`";
            output(tblTrasactionProcesses, sql);
            JOptionPane.showMessageDialog(null, "The Transaction process Information is Updated Successfully");
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        }
    }

    public void updateFeeCharges(JTextField txtFeename, JTextField txtFeeDesc, JTable tblFeeCharges) {
        model = (DefaultTableModel) tblFeeCharges.getModel();
        int selectedRowIndex = tblFeeCharges.getSelectedRow();
        try {
            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String updone = "UPDATE `fee_charge` SET fee_charge.Fee_Name='" + txtFeename.getText().trim() + "',"
                    + " fee_charge.Description='" + txtFeeDesc.getText().trim() + "'"
                    + " WHERE fee_charge.Serial_No='" + model.getValueAt(selectedRowIndex, 0) + "'";// AND fee_charge.Fee_Name='" + model.getValueAt(selectedRowIndex, 1) + "'";
            state.executeUpdate(updone);

            txtFeeDesc.setText(null);
            txtFeename.setText(null);

            String sql = "SELECT `Serial_No`, `Fee_Name`, `Description` FROM `fee_charge`";
            output(tblFeeCharges, sql);
            JOptionPane.showMessageDialog(null, "The Fee Information is Updated Successfully");
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        }
    }

    public void MemberAdmission(JTextField txtIdNo, JTextField txtname, JTextField txtMobNo, JTextField txtAddress,
            JTextField txtTown, JTextField txtAdminFee, JTextField txtMemberNo, JComboBox comboMegrp,
            JComboBox comboSex, JComboBox comboMarital, String comboDob, JTable tblMember) {
        if (txtname.getText().isEmpty() | comboDob.equals("") | comboMegrp.getSelectedItem().equals("Select") | txtIdNo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ensure all the fields and selected Approriately!");
        } else {
            try {
                LocalDate date = LocalDate.parse(comboDob);

                if ((dt.getYear() - date.getYear()) >= 18) {
                    connectionString.Connection2();
                    state = connectionString.conect.createStatement();
                    String sql1 = " SELECT `Serial_No`, `Member_Name`, `Member_ID_No`, `Member_Gender`,"
                            + " `Member_Phone`, `DOB`, `Marital_Status`, `Current_Address`, `Home_Town`,"
                            + " `Group_Name`, `Is_Active` FROM `members_info` WHERE Member_ID_No='" + txtIdNo.getText().trim() + "';";
                    rs = state.executeQuery(sql1);

                    if (rs.next()) {
                        String Is_Active = rs.getString("Is_Active");

                        switch (Is_Active) {
                            case "1":
                                JOptionPane.showMessageDialog(null, "There Is already An Active member Admitted of That ID Number");
                                break;
                            case "0":
                                JOptionPane.showMessageDialog(null, "There Is  An Inactive member Admitted of That ID Number");
                                break;
                        }
                    } else {
                        try {

                            String Insertuser = "INSERT INTO `members_info`(`Member_Name`, `Member_ID_No`, "
                                    + "`Member_Gender`, `Member_Phone`, `DOB`, `Marital_Status`, `Current_Address`, `Home_Town`, `Group_Name`, `Is_Active`)"
                                    + "Values"
                                    + "('" + txtname.getText().trim() + "',"
                                    + "'" + txtIdNo.getText().trim() + "',"
                                    + "'" + comboSex.getSelectedItem() + "',"
                                    + "'" + txtMobNo.getText().trim() + "',"
                                    + "'" + comboDob + "',"
                                    + "'" + comboMarital.getSelectedItem() + "',"
                                    + "'" + txtAddress.getText().trim() + "',"
                                    + "'" + txtTown.getText().trim() + "',"
                                    + "'" + comboMegrp.getSelectedItem() + "',"
                                    + "1);";
                            state = connectionString.conect.createStatement();
                            state.executeUpdate(Insertuser);
                            connectionString.conect.close();
                            String sql = "SELECT `Serial_No`, `Member_Name`, `Member_ID_No`, `Member_Gender`, `Member_Phone`, `DOB`, `Marital_Status`,"
                                    + " `Current_Address`, `Home_Town`, `Group_Name`, `Is_Active` FROM `members_info`";
                            output(tblMember, sql);
                        } catch (SQLException | HeadlessException ex) {

                            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
                        }
                        try {

                            String Insertuser = "INSERT INTO `member_admission`(`Member_Admin_No`, `Member_ID_No`, "
                                    + "`Group_Name`, `Admin_Fee`, `Admin_Date`, `Is_Active`)"
                                    + "Values"
                                    + "('" + txtMemberNo.getText().trim() + "',"
                                    + "'" + txtIdNo.getText().trim() + "',"
                                    + "'" + comboMegrp.getSelectedItem() + "',"
                                    + "'" + txtAdminFee.getText().trim() + "',"
                                    + "'" + format.format(new Date()) + "',"
                                    + "1);";
                            state = connectionString.conect.createStatement();
                            state.executeUpdate(Insertuser);
                            JOptionPane.showMessageDialog(null, "Member Admitted Successfully");

                            txtname.setText(null);
                            txtIdNo.setText(null);
                            txtMobNo.setText(null);
                            txtAddress.setText(null);
                            txtTown.setText(null);
                            //String sql = "SELECT `Serial_No`, `Fee_Name`, `Description` FROM `fee_charge`";

                            // output(tblFeeCharges, sql);
                        } catch (SQLException | HeadlessException ex) {

                            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Client Must be above 18 years of age");
                }
            } catch (SQLException | HeadlessException s) {

            }
        }
    }

    public void memberUpdate(JTextField txtIdNo, JTextField txtname, JTextField txtMobNo, JTextField txtAddress,
            JTextField txtTown, JTextField txtAdminFee, JTextField txtMemberNo, JComboBox comboMegrp,
            JComboBox comboSex, JComboBox comboMarital, String comboDob, JTable tblMember, JLabel lblContainer,
            JLabel lblContainer2, JCheckBox clientActive,JLabel lblItemCode) {

        model = (DefaultTableModel) tblMember.getModel();
        int isActive = 0;
        //int rowCount = tblMember.getSelectedRow();
        if (clientActive.isSelected()) {
            isActive = 1;
        }
        if (txtname.getText().isEmpty() | comboDob.equals("") | comboMegrp.getSelectedItem().equals("Select") | txtIdNo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ensure all the fields and selected Approriately!");
        } else {
            try {
                LocalDate date = LocalDate.parse(comboDob);

                if ((dt.getYear() - date.getYear()) >= 18) {
                    connectionString.Connection2();
                    state = connectionString.conect.createStatement();
                   /* String sql1 = " SELECT `Serial_No`, `Member_Name`, `Member_ID_No`, `Member_Gender`,"
                            + " `Member_Phone`, `DOB`, `Marital_Status`, `Current_Address`, `Home_Town`,"
                            + " `Group_Name`, `Is_Active` FROM  `members_info` WHERE"
                            + " `Serial_No`='" + lblContainer2.getText().trim() + "';";*/
                    String checkIfLoanActive = "SELECT `Serial_No`, `Loan_Acct_No`, `Client_Name`, `KYC_ID`,"
                            + " `Principal_Disbursed`, `Interest_Disbursed`, `Total_Disbursed`, `Installment_Pay`, "
                            + "`Credit_Officer`, `Loan_Duration`, `Approved_Status`, `Group_Name`, `Program`,"
                            + " `Date_Disbursed`, `Process_Disbursed`, `Monthly_Income`, `Client_Scheme`,"
                            + " `Fund`, `Is_Active` FROM `loan_applications`"
                            + " WHERE `KYC_ID`='" + lblItemCode.getText().trim() + "' AND Is_Active=1;";
                    rs = state.executeQuery(checkIfLoanActive);

                    if (rs.next() & !clientActive.isSelected()) {
                        JOptionPane.showMessageDialog(null, "The Client can not be Deactivate while having active Loan");
                    } else {
                        try {
                            //JOptionPane.showMessageDialog(null, lblContainer2.getText().trim());
                            state = connectionString.conect.createStatement();
                            String Insertuser = "UPDATE `members_info` SET"
                                    + "`Member_Name`='" + txtname.getText().trim() + "',"
                                    + "`Member_ID_No`='" + txtIdNo.getText().trim() + "',"
                                    + "`Member_Gender`='" + comboSex.getSelectedItem() + "',"
                                    + "`Member_Phone`=" + Integer.valueOf(txtMobNo.getText().trim()) + ","
                                    + "`DOB`='" + comboDob + "',"
                                    + "`Marital_Status`='" + comboMarital.getSelectedItem() + "',"
                                    + "`Current_Address`='" + txtAddress.getText().trim() + "',"
                                    + "`Home_Town`='" + txtTown.getText().trim() + "',"
                                    + "`Group_Name`='" + comboMegrp.getSelectedItem() + "',"
                                    + "`Is_Active`="+isActive+" where `Serial_No`='" + lblContainer2.getText().trim() + "'";

                            state.executeUpdate(Insertuser);
                            //connectionString.conect.close();
                            String sql = "SELECT `Serial_No`, `Member_Name`, `Member_ID_No`, `Member_Gender`, `Member_Phone`, `DOB`, `Marital_Status`,"
                                    + " `Current_Address`, `Home_Town`, `Group_Name`, `Is_Active` FROM `members_info`";
                            output(tblMember, sql);
                        } catch (SQLException | HeadlessException ex) {

                            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
                        }
                        try {
                            state = connectionString.conect.createStatement();
                            String Insertuser4 = "UPDATE `member_admission` SET "
                                    + "`Member_Admin_No`='" + txtMemberNo.getText().trim() + "',"
                                    + "`Member_ID_No`='" + txtIdNo.getText().trim() + "',"
                                    + "`Group_Name`='" + comboMegrp.getSelectedItem() + "',"
                                    + "`Admin_Fee`='" + txtAdminFee.getText().trim() + "',"
                                    + "`Admin_Date`='" + format.format(new Date()) + "',"
                                    + "`Is_Active`="+isActive+" WHERE `Member_Admin_No`='" + lblContainer.getText().trim() + "'";

                            state.executeUpdate(Insertuser4);
                            JOptionPane.showMessageDialog(null, "Member information updated Successfully");

                            txtname.setText(null);
                            txtIdNo.setText(null);
                            txtMobNo.setText(null);
                            txtAddress.setText(null);
                            txtTown.setText(null);
                            //String sql = "SELECT `Serial_No`, `Fee_Name`, `Description` FROM `fee_charge`";

                            // output(tblFeeCharges, sql);
                        } catch (SQLException | HeadlessException ex) {

                            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Client Must be above 18 years of age");
                }
            } catch (SQLException | HeadlessException s) {
                JOptionPane.showMessageDialog(null, s);

            }
        }
    }

    public void LoanCalculation(JComboBox comboProgram,
            JTextField txtInsterstDisbursed, JTextField txtDuaration, JTextField txtInstalment, JTextField txtTDisbursed, JTextField txtPrincipal) {
        try {
            txtTDisbursed.setText(null);
            txtInsterstDisbursed.setText(null);

            float LoanRate;
            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql1 = "SELECT `Serial_No`, `Loan_Type`, `Loan_Rate`, `Description` FROM `loan_rates` "
                    + " WHERE Loan_Type='" + comboProgram.getSelectedItem() + "';";
            rs = state.executeQuery(sql1);
            while (rs.next()) {
                LoanRate = rs.getFloat("Loan_Rate");
                float Rate = (LoanRate / 100);

                float interestDis = Integer.parseInt(txtPrincipal.getText().trim()) * Rate;
                txtInsterstDisbursed.setText(String.valueOf(interestDis));

                float TDisbursed = Integer.parseInt(txtPrincipal.getText().trim()) + interestDis;
                txtTDisbursed.setText(String.valueOf(TDisbursed));

                float ny = Float.parseFloat(txtDuaration.getText().trim());
                double nm = Math.ceil(ny * 4);
                double instal = TDisbursed / nm;

                //double instal = TDisbursed / Math.ceil((Integer.parseInt(txtDuaration.getText().trim()) * 4));
                txtInstalment.setText(String.valueOf(instal));
                // JOptionPane.showMessageDialog(null,Rate);
            }
        } catch (SQLException | NumberFormatException ex) {

        }
    }

    public void calculateInProcfee(JTable tlbOtherFee, JTextField txtPrincipal, JComboBox comboProgram) {
        double insuRanceFee = 0;// = 0.017;
        double loanProcessFell = 0;// = 0.04;
        try {

            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql1 = "SELECT `Serial_No`, `Loan_Type`, `Processing_Fee`, `Insurance~Verification Fee`, "
                    + "`Program_Status`, `Description` FROM `loan_type` "
                    + " WHERE Loan_Type='" + comboProgram.getSelectedItem() + "';";
            rs = state.executeQuery(sql1);
            while (rs.next()) {
                insuRanceFee = rs.getDouble("Insurance~Verification Fee");
                loanProcessFell = rs.getDouble("Processing_Fee");

            }
            int insurancFee, LProcessFee;
            insurancFee = (int) (Integer.parseInt(txtPrincipal.getText().trim()) * insuRanceFee);
            LProcessFee = (int) (Integer.parseInt(txtPrincipal.getText().trim()) * loanProcessFell);
            DefaultTableModel model1 = (DefaultTableModel) tlbOtherFee.getModel();
            model1.setValueAt(insurancFee, 0, 1);
            model1.setValueAt(LProcessFee, 1, 1);
        } catch (SQLException | NumberFormatException ex) {

        }

    }

    public void LoanApplicationSave(JTextField txtAcctNumber, JTextField txtInstalment,
            JTextField txtPrincipal, JTextField txtDuaration,
            JTextField txtTDisbursed, JComboBox comboCreOff, JTextField txtCIDNo, JTextField txtInsterstDisbursed,
            JComboBox comboGrp, JComboBox comboMemberFill, JComboBox comboProgram, String disburseDate,
            JTextField txtMonthlyIncome, JTextField txtScheme, JComboBox comboProcess, JComboBox comboFund, JTable tlbOtherFee,
            JTable tlbGaurantor, JTable tblLoansApplied, JTable tblLoansPendingApproval, JButton newButton) {
        model = (DefaultTableModel) tlbGaurantor.getModel();
        int rowCount = model.getRowCount();
        try {
            boolean states;
            String sql1 = "SELECT `KYC_ID`, `Is_Active`, `Approved_Status` FROM `loan_applications` WHERE KYC_ID=" + txtCIDNo.getText().trim() + " and Is_Active=1;";
            rs = state.executeQuery(sql1);
            states = rs.next();

            if (states == false) {
                try {
                    connectionString.Connection2();
                    DefaultTableModel model1 = (DefaultTableModel) tlbOtherFee.getModel();

                    String[] Insertuser = {"INSERT INTO `loanapplicablefee`(`Loan_Account_No`,"
                        + " `KYC_ID_No`, `Loan_Insurance_Fee`, `Loan_Processing_Fee`)"
                        + "Values"
                        + "('" + txtAcctNumber.getText().trim() + "',"
                        + "'" + txtCIDNo.getText().trim() + "',"
                        + "'" + model1.getValueAt(0, 1) + "',"
                        + "'" + model1.getValueAt(1, 1) + "');",
                        "INSERT INTO `loan_applications`(`Loan_Acct_No`, `Client_Name`, `KYC_ID`, `Principal_Disbursed`, `Interest_Disbursed`, `Total_Disbursed`,"
                        + "`Installment_Pay`, `Credit_Officer`, `Loan_Duration`, `Group_Name`, `Program`, `Date_Disbursed`, `Process_Disbursed`, `Monthly_Income`, `Client_Scheme`, `Fund`)"
                        + "Values"
                        + "('" + txtAcctNumber.getText().trim() + "',"
                        + "'" + comboMemberFill.getSelectedItem() + "',"
                        + "'" + txtCIDNo.getText().trim() + "',"
                        + "'" + txtPrincipal.getText().trim() + "',"
                        + "'" + txtInsterstDisbursed.getText().trim() + "',"
                        + "'" + txtTDisbursed.getText().trim() + "',"
                        + "'" + txtInstalment.getText().trim() + "',"
                        + "'" + comboCreOff.getSelectedItem() + "',"
                        + "'" + txtDuaration.getText().trim() + "',"
                        + "'" + comboGrp.getSelectedItem() + "',"
                        + "'" + comboProgram.getSelectedItem() + "',"
                        + "'" + disburseDate + "',"
                        + "'" + comboProcess.getSelectedItem() + "',"
                        + "'" + txtMonthlyIncome.getText().trim() + "',"
                        + "'" + txtScheme.getText().trim() + "',"
                        + "'" + comboFund.getSelectedItem() + "');"};

                    state = connectionString.conect.createStatement();
                    for (String query : Insertuser) {
                        state.addBatch(query);
                    }
                    state.executeBatch();
                    //state.executeUpdate(Insertuser);
                    for (int i = 0; i < rowCount; i++) {
                        try {
                            connectionString.Connection2();
                            String savG = "INSERT INTO `guarantor_data`(`Loan_Account_No`, `KYC_ID_No`, `G_Name`,"
                                    + " `G_Relation`, `G_Address`, `G_KYC_ID`, `G_DOB`, `G_Contact_No`)"
                                    + "Values"
                                    + "('" + txtAcctNumber.getText().trim() + "',"
                                    + "'" + txtCIDNo.getText().trim() + "',"
                                    + "'" + model.getValueAt(0, 0) + "',"
                                    + "'" + model.getValueAt(0, 2) + "',"
                                    + "'" + model.getValueAt(0, 4) + "',"
                                    + "'" + model.getValueAt(0, 1) + "',"
                                    + "'" + model.getValueAt(0, 3) + "',"
                                    + "'" + model.getValueAt(0, 5) + "');";
                            state = connectionString.conect.createStatement();
                            state.executeUpdate(savG);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex);
                        }
                    }

                    JOptionPane.showMessageDialog(null, "Loan Application Submitted Successfully");
                    newButton.doClick();
                    connectionString.conect.close();

                    String sql = "SELECT `Serial_No`, `Loan_Acct_No`, `Client_Name`, `KYC_ID`, `Principal_Disbursed`, `Interest_Disbursed`, "
                            + "`Total_Disbursed`,`Date_Disbursed`,`Installment_Pay`, `Loan_Duration`,`Approved_Status` FROM `loan_applications`";
                    output(tblLoansApplied, sql);
                    output(tblLoansPendingApproval, sql);
                } catch (SQLIntegrityConstraintViolationException ex) {
                    JOptionPane.showMessageDialog(null, "Loan Of that Account Already Exist");
                } catch (SQLException ex) {
                    Logger.getLogger(dbInsert.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "THE CLIENT HAS RECENTLY APPLIED FOR LOAN");

            }

        } catch (SQLException ex) {
            Logger.getLogger(dbInsert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveLoanApproved(JRadioButton radioApproved, JTextField txtKYCIDC, JTextField txtAccountNo, JXDatePicker LoanApprovalDate, JTable tblPayments) {
        //String LoaSt;
        if (radioApproved.isSelected()) {

            int rowCount = tblPayments.getRowCount();
            connectionString.Connection2();
            for (int i = 0; i < rowCount; i++) {
                //otalSale = TotalSale + Integer.parseInt(tblList.getValueAt(i, 2).toString());
                try {
                    String Insertuser = "INSERT INTO `loan_payment_track`(`ID_Number`, `Loan_Account_No`,`Cycle_Period`,"
                            + " `Pay_Date`, `Old_Balance`, `Instalment`, `Interest_Paid`, `Principal_Paid`, `New_Balance`, `Disburse_Date`)"
                            + "Values"
                            + "('" + txtKYCIDC.getText().trim() + "',"
                            + "'" + txtAccountNo.getText().trim() + "',"
                            + "'" + tblPayments.getValueAt(i, 0).toString() + "',"
                            + "'" + tblPayments.getValueAt(i, 1).toString() + "',"
                            + "'" + tblPayments.getValueAt(i, 2).toString() + "',"
                            + "'" + tblPayments.getValueAt(i, 3).toString() + "',"
                            + "'" + tblPayments.getValueAt(i, 4).toString() + "',"
                            + "'" + tblPayments.getValueAt(i, 5).toString() + "',"
                            + "'" + tblPayments.getValueAt(i, 6).toString() + "',"
                            + "'" + format.format(LoanApprovalDate.getDate()) + "');";
                    state = connectionString.conect.createStatement();
                    state.executeUpdate(Insertuser);

                } catch (SQLException | HeadlessException ex) {

                    JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
                }

            }
            try {
                String updateLoaan = "UPDATE `loan_applications` SET`Approved_Status`='Approved'"
                        + " WHERE `Loan_Acct_No`='" + txtAccountNo.getText().trim() + "'";
                state = connectionString.conect.createStatement();
                state.executeUpdate(updateLoaan);
            } catch (SQLException ex) {
                Logger.getLogger(dbInsert.class.getName()).log(Level.SEVERE, null, ex);
            }

            JOptionPane.showMessageDialog(null, "Loan Approved Successfully");
        } else {
            try {
                String updateLoaan = "UPDATE `loan_applications` SET `Is_Active`=0,`Approved_Status`='Rejected' WHERE `Loan_Acct_No`='" + txtAccountNo.getText().trim() + "'";
                state = connectionString.conect.createStatement();
                state.executeUpdate(updateLoaan);
            } catch (SQLException ex) {
                Logger.getLogger(dbInsert.class.getName()).log(Level.SEVERE, null, ex);
            }
            JOptionPane.showMessageDialog(null, "Loan Application was rejected ");
        }
    }

    public void deleteLoanTransaction(JTable tblDailyCollection) {
        try {
            model = (DefaultTableModel) tblDailyCollection.getModel();
            int selectedRowIndex = tblDailyCollection.getSelectedRow();

            if (selectedRowIndex < 1) {
                JOptionPane.showMessageDialog(null, "Select trasaction you need to delete from the table");

            } else {
                Date collectDate = format.parse(model.getValueAt(selectedRowIndex, 8).toString());
                Date currectDate = new Date();

                String collectionDate = format.format(collectDate);
                String CurrentDatee = format.format(currectDate);

                Date collect = format.parse(collectionDate);
                Date current = format.parse(CurrentDatee);

                if (collect.before(current)) {
                    JOptionPane.showMessageDialog(null, "Deletion Is Not Allow, Transaction already committed");
                } else {
                    connectionString.Connection2();
                    String savG = "DELETE FROM `loan_instalment_paytrack` WHERE `Serial_No`='" + model.getValueAt(selectedRowIndex, 0) + "'";
                    state = connectionString.conect.createStatement();
                    state.executeUpdate(savG);

                    String updateLoaan = "UPDATE `loan_payment_track` SET `Pay_Status`='Not Paid'"
                            + " WHERE `Track_Number`='" + model.getValueAt(selectedRowIndex, 1) + "'";
                    state = connectionString.conect.createStatement();

                    state.executeUpdate(updateLoaan);
                    JOptionPane.showMessageDialog(null, "Transaction Deleted and Updated Succesfully");

                }
            }
        } catch (ParseException | SQLException ex) {
            Logger.getLogger(dbInsert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteFeeTransaction(JTable tblFeeInfor) {
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            model = (DefaultTableModel) tblFeeInfor.getModel();
            int selectedRowIndex = tblFeeInfor.getSelectedRow();

            if (selectedRowIndex == -1) {
                JOptionPane.showMessageDialog(null, "Select trasaction you need to delete from the table");

            } else {
                Date collectDate = format.parse(model.getValueAt(selectedRowIndex, 5).toString());
                Date currentDate = new Date();

                // JOptionPane.showMessageDialog(null, format.format(currentDate)+"             "+format.format(collectDate));
                String collectionDate = format.format(collectDate);
                String CurrentDatee = format.format(currentDate);
                Date collect = format.parse(collectionDate);
                Date current = format.parse(CurrentDatee);

                if (collect.before(current)) {
                    JOptionPane.showMessageDialog(null, "Deletion Is Not Allow, Transaction already committed");
                } else if (collect.equals(current)) {
                    connectionString.Connection2();
                    String savG = "DELETE FROM `feel_collection` WHERE `Serial_No`='" + model.getValueAt(selectedRowIndex, 0) + "'";
                    state = connectionString.conect.createStatement();
                    state.executeUpdate(savG);
                    JOptionPane.showMessageDialog(null, "Transaction Deleted Succesfully");
                    String selectFee = "SELECT `feel_collection`.`Serial_No`,  `members_info`.`Member_Name`, `Client_ID`, `Fee_Type`, `Fee_Code`, `Date_Collected`, `Amount_Collected` "
                            + "FROM `feel_collection`,members_info where members_info.`Member_ID_No`=`feel_collection`.`Client_ID`";

                    output(tblFeeInfor, selectFee);
                } else {
                    JOptionPane.showMessageDialog(null, "Deletion Is Not Allow due to date difference");

                }
            }
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(dbInsert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveAccountTypes(JTextField txtAcctname, JTextField txtAccountNo, JTextField txtAccDesc, JTable tblAccountConfig) {
        try {
            connectionString.Connection2();
            String Insertuser = "INSERT INTO `Account_Configuration`(`Account_Name`, `Acount_Code`, `Description`)"
                    + "Values"
                    + "('" + txtAcctname.getText().trim() + "',"
                    + "'" + txtAccountNo.getText().trim() + "',"
                    + "'" + txtAccDesc.getText().trim() + "');";

            state = connectionString.conect.createStatement();
            state.executeUpdate(Insertuser);
            JOptionPane.showMessageDialog(null, "Account Configuration Added Successfully");

            txtAcctname.setText(null);
            txtAccountNo.setText(null);
            txtAccDesc.setText(null);
            String sql = "SELECT `Serial_No`, `Account_Name`, `Acount_Code`, `Description` FROM `account_configuration`";
            output(tblAccountConfig, sql);
        } catch (SQLException | HeadlessException ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        }
    }

    public void updateAccountType(JTextField txtAcctname, JTextField txtAccountNo, JTextField txtAccDesc, JTable tblAccountConfig) {
        model = (DefaultTableModel) tblAccountConfig.getModel();
        int selectedRowIndex = tblAccountConfig.getSelectedRow();
        try {
            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String update = "UPDATE account_configuration, acct_record,acct_tractions "
                    + "SET account_configuration.Account_Name='" + txtAcctname.getText().trim() + "',"
                    + "account_configuration.Description='" + txtAccDesc.getText().trim() + "',"
                    + "account_configuration.Acount_Code='" + txtAccountNo.getText().trim() + "'"
                    + ",acct_record.Acct_Type='" + txtAcctname.getText().trim() + "',acct_tractions.Acct_Type='" + txtAcctname.getText().trim() + "'"
                    + " WHERE account_configuration.Serial_No='" + model.getValueAt(selectedRowIndex, 0) + "'"
                    + "OR account_configuration.Account_Name='" + model.getValueAt(selectedRowIndex, 1) + "' OR "
                    + "account_configuration.Account_Name=acct_record.Acct_Type;";
            state.executeUpdate(update);

            JOptionPane.showMessageDialog(null, "The Account Information Updated Successfully");

            String sql = "SELECT `Serial_No`, `Account_Name`, `Acount_Code`, `Description` FROM `account_configuration`";
            output(tblAccountConfig, sql);
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        }

        txtAcctname.setText(null);
        txtAccountNo.setText(null);
        txtAccDesc.setText(null);
    }

    public void SecurityAcc(JTextField txtSecountAcc, JComboBox combCreditOfficer, JComboBox comboMName,
            JComboBox comboSecGroup, String dateDate, JTextField txtMinDeposit,
            JTextField txtKYCSecID, JTable tblSecAccts, JTable tblSecAccounts) {
        try {
            connectionString.Connection2();
            String savG = "INSERT INTO `security_acct`(`Secuirty_Acct_No`, `M_Name`,"
                    + " `KYC_ID`, `Group_Name`, `Credit_Officer`, `Open_Date`, `Min_Deposit`, `Is_Active`)"
                    + "Values"
                    + "('" + txtSecountAcc.getText().trim() + "',"
                    + "'" + comboMName.getSelectedItem() + "',"
                    + "'" + txtKYCSecID.getText().trim() + "',"
                    + "'" + comboSecGroup.getSelectedItem() + "',"
                    + "'" + combCreditOfficer.getSelectedItem() + "',"
                    + "'" + dateDate + "',"
                    + "'" + txtMinDeposit.getText().trim() + "',"
                    + "'1');";
            state = connectionString.conect.createStatement();
            state.executeUpdate(savG);
            String sql = "SELECT `Serial`, `Secuirty_Acct_No`, `M_Name`, `KYC_ID`,"
                    + " `Group_Name`, `Credit_Officer`, `Open_Date`, `Min_Deposit`, `Is_Active` FROM `security_acct`";
            output(tblSecAccts, sql);
            output(tblSecAccounts, sql);
            JOptionPane.showMessageDialog(null, "Saved Successfully");
        } catch (MySQLIntegrityConstraintViolationException mslcve) {
            JOptionPane.showMessageDialog(null, "Client With the similar KYC ID Already Exist, Active the Security Account");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Client With the similar KYC ID Already Exist, Active the Security Account");
        }
    }

    public void selectCountGuarantor(JTextField txtGuantorCount, JTable tblLoansPendingApproval) {
        int selectedRowIndex = tblLoansPendingApproval.getSelectedRow();
        //DefaultTableModel model = (DefaultTableModel) tblLoansPendingApproval.getModel();
        try {
            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql1 = "SELECT COUNT(Is_Active) FROM `guarantor_data` WHERE Loan_Account_No='" + tblLoansPendingApproval.getValueAt(selectedRowIndex, 1) + "'";
            //+ " WHERE Loan_Account_No='" + tblLoansPendingApproval.getValueAt(selectedRowIndex, 1).toString() + "';";
            rs = state.executeQuery(sql1);
            while (rs.next()) {
                int Count = rs.getInt("Count(Is_Active)");

                txtGuantorCount.setText(String.valueOf(Count));
                //JOptionPane.showMessageDialog(null, Count);
            }

        } catch (SQLException | NumberFormatException ex) {

        }
    }

    public void selectAdminFee(JCheckBox checkAdminFee, JTable tblLoansPendingApproval, JLabel lblAdmin) {
        int selectedRowIndex = tblLoansPendingApproval.getSelectedRow();
        checkAdminFee.setSelected(false);
        lblAdmin.setText(null);
        try {
            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql1 = "SELECT   `Admin_Fee`,`Member_ID_No`,  `Is_Active` "
                    + "FROM `member_admission` WHERE Member_ID_No='" + tblLoansPendingApproval.getValueAt(selectedRowIndex, 3) + "'"
                    + "AND Is_Active=1";
            rs = state.executeQuery(sql1);
            while (rs.next()) {
                int Count = rs.getInt("Admin_Fee");
                checkAdminFee.setSelected(true);
                lblAdmin.setText(String.valueOf(Count));
                //JOptionPane.showMessageDialog(null, Count);
            }

        } catch (SQLException | NumberFormatException ex) {

        }

    }

    public void selectProInsuFee(JCheckBox checkInsurance, JCheckBox checkProcess, JTable tblLoansPendingApproval, JLabel lblInsurance, JLabel lblProcessing) {

        int selectedRowIndex = tblLoansPendingApproval.getSelectedRow();
        //checkInsurance.setSelected(false);
        // checkProcess.setSelected(false);
        lblInsurance.setText("0");
        lblProcessing.setText("0");
        try {
            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql1 = " SELECT `KYC_ID_No`, `Loan_Insurance_Fee`, `Loan_Processing_Fee` FROM `loanapplicablefee` "
                    + "WHERE  `Loan_Account_No`='" + tblLoansPendingApproval.getValueAt(selectedRowIndex, 1) + "'";
            rs = state.executeQuery(sql1);
            while (rs.next()) {
                int Count = rs.getInt("Loan_Insurance_Fee");
                int Count2 = rs.getInt("Loan_Processing_Fee");
                checkInsurance.setSelected(true);
                checkProcess.setSelected(true);
                lblInsurance.setText(String.valueOf(Count));
                lblProcessing.setText(String.valueOf(Count2));
                //JOptionPane.showMessageDialog(null, Count);
            }

        } catch (SQLException | NumberFormatException ex) {

        }

    }

    public void saveSecurityTransaction(JTextField txtKYCNo, JTextField txtSeccAcctNo, JTextField txtSecAmount, JComboBox comboSecType) {
        String transType = comboSecType.getSelectedItem().toString();//"";
        try {
            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql1 = "SELECT `Acct_No`, `KYC_ID`, `Account_Balance`, `Is_Active` FROM `security_transaction`"
                    + "WHERE KYC_ID='" + txtKYCNo.getText().trim() + "' AND  Acct_No='" + txtSeccAcctNo.getText().trim() + "'"
                    + "AND Is_Active=1";
            rs = state.executeQuery(sql1);
            if (rs.next()) {
                //transType=rs.getString("");
                switch (transType) {
                    case "Deposit":
                        JOptionPane.showMessageDialog(null, "Security add....");

                        break;
                    case "Withdraw":
                        JOptionPane.showMessageDialog(null, "Security subtract....");
                        break;

                    case "Return":
                        JOptionPane.showMessageDialog(null, "Security remove....");
                        break;
                }
            } else {
                try {
                    state = connectionString.conect.createStatement();
                    String Insertuser = "INSERT INTO `security_transaction`(`Acct_No`, `KYC_ID`, `Account_Balance`, `Is_Active`)"
                            + "Values"
                            + "('" + txtSeccAcctNo.getText().trim() + "',"
                            + "'" + txtKYCNo.getText().trim() + "',"
                            + "'" + txtSecAmount.getText().trim() + "',"
                            + "1);";
                    state = connectionString.conect.createStatement();
                    state.executeUpdate(Insertuser);
                    JOptionPane.showMessageDialog(null, "Security Saved Successfully");
                } catch (SQLException | HeadlessException ex) {

                    JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
                }

            }

        } catch (SQLException | NumberFormatException ex) {

        }

    }

    /* public void dowhileLoantransaction(JTextField txtSecAmount, JTextField txtInsta,
            JTextField txtTrackNo, JTextField txtAcctNo, JTextField txtOutAmount, JComboBox comboSecProcess,
            JTextField txtpayDate, JXDatePicker paidDate, JTable tblDailyCollection) {
        float Instalment_Balance = 0;
        float outStandBa = Float.parseFloat(txtOutAmount.getText());
        float advanceAmount = 0;
        float IntalmentTopay = Float.parseFloat(txtInsta.getText());
        float amountCollected = Float.parseFloat(txtSecAmount.getText());
        int trackNumber = Integer.parseInt(txtTrackNo.getText().trim());

        advanceAmount = amountCollected - IntalmentTopay;
        do {

        } while (advanceAmount > IntalmentTopay);

    }*/
    public void advanceAmountGreaterLess(JTextField txtSecAmount, JTextField txtInsta,
            JTextField txtTrackNo, JTextField txtAcctNo, JTextField txtOutAmount, JComboBox comboSecProcess,
            JTextField txtpayDate, JXDatePicker paidDate, JTable tblDailyCollection) {
        // float Instalment_Balance = 0;
        float outStandBa = Float.parseFloat(txtOutAmount.getText());
        float advanceAmount = 0;
        float IntalmentTopay = Float.parseFloat(txtInsta.getText());
        float amountCollected = Float.parseFloat(txtSecAmount.getText());
        int trackNumber = Integer.parseInt(txtTrackNo.getText().trim());
        while (amountCollected >= IntalmentTopay) {
            try {
                String getNextInstallment = "SELECT `Track_Number`, `ID_Number`, `Loan_Account_No`, `Cycle_Period`, `Pay_Date`, `Old_Balance`, "
                        + "`Instalment`, `Interest_Paid`, `Principal_Paid`, `New_Balance`, `Disburse_Date`, `Pay_Status`"
                        + " FROM `loan_payment_track` where `Track_Number`=" + trackNumber + " AND  "
                        + "`Loan_Account_No`='" + txtAcctNo.getText().trim() + "'";
                rs = state.executeQuery(getNextInstallment);

                while (rs.next()) {
                    txtOutAmount.setText(String.valueOf(rs.getInt("Old_Balance")));
                    txtTrackNo.setText(String.valueOf(rs.getInt("Track_Number")));
                    txtAcctNo.setText(String.valueOf(rs.getString("Loan_Account_No")));
                    txtInsta.setText(String.valueOf(rs.getFloat("Instalment")));
                    txtpayDate.setText(String.valueOf(rs.getDate("Pay_Date")));
                    txtSecAmount.setText(String.valueOf(advanceAmount));
                    IntalmentTopay = rs.getFloat("Instalment");
                }
                trackNumber = rs.getInt("Track_Number");
                amountCollected = advanceAmount;
                advanceAmount = amountCollected - IntalmentTopay;

                String UpdateNextOldBalanc = "UPDATE `loan_payment_track` SET `Pay_Status`='Paid', `Old_Balance`='" + outStandBa + "' where "
                        + "`Track_Number`=" + trackNumber + " AND  `Loan_Account_No`='" + txtAcctNo.getText().trim() + "'";
                state.addBatch(UpdateNextOldBalanc);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Some error " + ex);

            }
            if (amountCollected < IntalmentTopay) {
                JOptionPane.showMessageDialog(null, "Track number" + trackNumber + "Advance Amount is less than installment" + advanceAmount);

            }
        }
    }

    public void saveLoanTransaction(JTextField txtSecAmount, JTextField txtInsta,
            JTextField txtTrackNo, JTextField txtAcctNo, JTextField txtOutAmount, JComboBox comboSecProcess,
            JTextField txtpayDate, JXDatePicker paidDate, JTable tblDailyCollection) {
        float Instalment_Balance = 0;
        float outStandBa = Float.parseFloat(txtOutAmount.getText());
        float advanceAmount = 0;
        float IntalmentTopay = Float.parseFloat(txtInsta.getText());
        float amountCollected = Float.parseFloat(txtSecAmount.getText());
        int trackNumber = Integer.parseInt(txtTrackNo.getText().trim());

        //next outstanding balance
        if (amountCollected > IntalmentTopay) {

            advanceAmount = amountCollected - IntalmentTopay;

            if (advanceAmount > outStandBa) {
                //do not allow collection of more than the outstanding
                JOptionPane.showMessageDialog(null, "Total Amount Collected can Not be more than Outstanding Balance");

            } else if (advanceAmount <= outStandBa) {

                outStandBa = outStandBa - advanceAmount;

                JOptionPane.showMessageDialog(null, "Amount paid  " + amountCollected);

                try {
                    state = connectionString.conect.createStatement();
                    String Insertuser = "INSERT INTO `loan_instalment_paytrack`(`Loan_Acct_No`, `Track_No`, "
                            + "`OutStanding_Amount`, `Instalment`, `Instalment_Balance`, `Pay_Date`, `Amount_Paid`, "
                            + "`Date_Paid`, `Advance_Amount`, `Pay_Process`)"
                            + "Values"
                            + "('" + txtAcctNo.getText().trim() + "',"
                            + "'" + txtTrackNo.getText().trim() + "',"
                            + "'" + outStandBa + "',"
                            + "'" + txtInsta.getText().trim() + "',"
                            + "'" + Instalment_Balance + "',"
                            + "'" + txtpayDate.getText().trim() + "',"
                            + "" + amountCollected + ","
                            + "'" + format.format(paidDate.getDate()) + "',"
                            + "" + advanceAmount + ","
                            + "'" + comboSecProcess.getSelectedItem().toString() + "');";
                    state.addBatch(Insertuser);

                    String UpdatePayStatus = "UPDATE `loan_payment_track` SET `Pay_Status`='Paid' where `Track_Number`=" + trackNumber + " AND "
                            + "`Loan_Account_No`='" + txtAcctNo.getText().trim() + "'";
                    state.addBatch(UpdatePayStatus);

                    do {

                        try {
                            String getNextInstallment = "SELECT `Track_Number`, `ID_Number`, `Loan_Account_No`, `Cycle_Period`, `Pay_Date`, `Old_Balance`, "
                                    + "`Instalment`, `Interest_Paid`, `Principal_Paid`, `New_Balance`, `Disburse_Date`, `Pay_Status`"
                                    + " FROM `loan_payment_track` where `Track_Number`=" + trackNumber + " AND  "
                                    + "`Loan_Account_No`='" + txtAcctNo.getText().trim() + "'";
                            rs = state.executeQuery(getNextInstallment);

                            while (rs.next()) {
                                txtOutAmount.setText(String.valueOf(rs.getInt("Old_Balance")));
                                txtTrackNo.setText(String.valueOf(rs.getInt("Track_Number")));
                                txtAcctNo.setText(String.valueOf(rs.getString("Loan_Account_No")));
                                txtInsta.setText(String.valueOf(rs.getFloat("Instalment")));
                                txtpayDate.setText(String.valueOf(rs.getDate("Pay_Date")));
                                txtSecAmount.setText(String.valueOf(advanceAmount));

                                IntalmentTopay = rs.getFloat("Instalment");
                                trackNumber = rs.getInt("Track_Number");

                                amountCollected = advanceAmount;
                                advanceAmount = amountCollected - IntalmentTopay;
                                outStandBa = outStandBa - advanceAmount;

                            }

                            String UpdateNextOldBalanc = "UPDATE `loan_payment_track` SET `Pay_Status`='Paid', `Old_Balance`='" + outStandBa + "' where "
                                    + "`Track_Number`=" + (trackNumber + 1) + " AND  `Loan_Account_No`='" + txtAcctNo.getText().trim() + "'";
                            state.addBatch(UpdateNextOldBalanc);

//                            outStandBa = outStandBa - advanceAmount;
//
//                            String UpdateNextOldBalance = "UPDATE `loan_payment_track` SET `Old_Balance`='" + outStandBa + "' where "
//                                    + "`Track_Number`=" + (trackNumber + 1) + " AND  `Loan_Account_No`='" + txtAcctNo.getText().trim() + "'";
//                            state.addBatch(UpdateNextOldBalance);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Some error " + ex);

                        }
                        if (amountCollected < IntalmentTopay) {

                            trackNumber = trackNumber + 1;

                            String nextData = "SELECT `Track_Number`, `ID_Number`, `Loan_Account_No`, `Cycle_Period`, `Pay_Date`, `Old_Balance`, "
                                    + "`Instalment`, `Interest_Paid`, `Principal_Paid`, `New_Balance`, `Disburse_Date`, `Pay_Status`"
                                    + " FROM `loan_payment_track` where `Track_Number`=" + trackNumber + " AND  "
                                    + "`Loan_Account_No`='" + txtAcctNo.getText().trim() + "'";
                            rs = state.executeQuery(nextData);
                            while (rs.next()) {
                                outStandBa = rs.getFloat("Old_Balance");
                                IntalmentTopay = rs.getFloat("Instalment");

                                outStandBa = outStandBa - amountCollected;
                                Instalment_Balance = IntalmentTopay - amountCollected;
                            }

                            String UpdateNextOldBalanc = "UPDATE `loan_payment_track` SET `Old_Balance`='" + outStandBa + "',`Pay_Status`='Not Paid',`Instalment`= '" + Instalment_Balance + "'where "
                                    + "`Track_Number`=" + trackNumber + " AND  `Loan_Account_No`='" + txtAcctNo.getText().trim() + "'";
                            state.addBatch(UpdateNextOldBalanc);

                            JOptionPane.showMessageDialog(null, "Track number" + trackNumber + "Advance Amount is less than installment" + Instalment_Balance);

                        }
//                        else {
//                            JOptionPane.showMessageDialog(null,"Outstanding equals Amount paid");
//                        }
                        trackNumber++;

                    } while (amountCollected >= IntalmentTopay);

                    state.executeBatch();
                    JOptionPane.showMessageDialog(null, "Installment Saved");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex);

                }

                //advance amount may be less than the outstanidng amount but greater than the intallment
                //check if the advance amount is more than the next instalment amount
                //update the next
            }

        } else if (amountCollected < IntalmentTopay) {

            Instalment_Balance = IntalmentTopay - amountCollected;
            outStandBa = outStandBa - amountCollected;

            try {
                state = connectionString.conect.createStatement();
                String Insertuser = "INSERT INTO `loan_instalment_paytrack`(`Loan_Acct_No`, `Track_No`, "
                        + "`OutStanding_Amount`, `Instalment`, `Instalment_Balance`, `Pay_Date`, `Amount_Paid`, "
                        + "`Date_Paid`, `Advance_Amount`, `Pay_Process`)"
                        + "Values"
                        + "('" + txtAcctNo.getText().trim() + "',"
                        + "'" + txtTrackNo.getText().trim() + "',"
                        + "'" + outStandBa + "',"
                        + "'" + txtInsta.getText().trim() + "',"
                        + "'" + Instalment_Balance + "',"
                        + "'" + txtpayDate.getText().trim() + "',"
                        + "" + amountCollected + ","
                        + "'" + format.format(paidDate.getDate()) + "',"
                        + "0,"
                        + "'" + comboSecProcess.getSelectedItem().toString() + "');";
                state.addBatch(Insertuser);

                String UpdatePayStatus = "UPDATE `loan_payment_track` SET `Pay_Status`='Not Paid', `Instalment`='" + Instalment_Balance + "',"
                        + "`Old_Balance`='" + outStandBa + "' where `Track_Number`=" + trackNumber + " AND "
                        + "`Loan_Account_No`='" + txtAcctNo.getText().trim() + "'";
                state.addBatch(UpdatePayStatus);

                state.executeBatch();
                JOptionPane.showMessageDialog(null, "Installment Saved");
                //update the status paid and update the oustanding
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
            txtOutAmount.setText(null);
            txtTrackNo.setText(null);
            txtAcctNo.setText(null);
            txtInsta.setText(null);
            txtpayDate.setText(null);
            txtSecAmount.setText(null);
            //update the oustanding balance for the current track number, no updating the status paid

        } else {
            outStandBa = outStandBa - amountCollected;
            try {
                state = connectionString.conect.createStatement();
                String Insertuser = "INSERT INTO `loan_instalment_paytrack`(`Loan_Acct_No`, `Track_No`, "
                        + "`OutStanding_Amount`, `Instalment`, `Instalment_Balance`, `Pay_Date`, `Amount_Paid`, "
                        + "`Date_Paid`, `Advance_Amount`, `Pay_Process`)"
                        + "Values"
                        + "('" + txtAcctNo.getText().trim() + "',"
                        + "'" + txtTrackNo.getText().trim() + "',"
                        + "'" + outStandBa + "',"
                        + "'" + txtInsta.getText().trim() + "',"
                        + "0,"
                        + "'" + txtpayDate.getText().trim() + "',"
                        + "" + amountCollected + ","
                        + "'" + format.format(paidDate.getDate()) + "',"
                        + "0,"
                        + "'" + comboSecProcess.getSelectedItem().toString() + "');";
                state.addBatch(Insertuser);

                String UpdatePayStatus = "UPDATE `loan_payment_track` SET `Pay_Status`='Paid' where `Track_Number`=" + trackNumber + " AND "
                        + "`Loan_Account_No`='" + txtAcctNo.getText().trim() + "'";
                state.addBatch(UpdatePayStatus);

                String UpdateNextOldBalance = "UPDATE `loan_payment_track` SET `Old_Balance`='" + outStandBa + "' where "
                        + "`Track_Number`=" + (trackNumber + 1) + " AND  `Loan_Account_No`='" + txtAcctNo.getText().trim() + "'";
                state.addBatch(UpdateNextOldBalance);

                state.executeBatch();
                JOptionPane.showMessageDialog(null, "Installment Saved");
                //update the status paid and update the oustanding
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);

            }
            txtOutAmount.setText(null);
            txtTrackNo.setText(null);
            txtAcctNo.setText(null);
            txtInsta.setText(null);
            txtpayDate.setText(null);
            txtSecAmount.setText(null);

        }
    }

    public void loanCollection(JTextField txtSecAmount, JTextField txtInsta,
            JTextField txtTrackNo, JTextField txtAcctNo, JTextField txtOutAmount, JComboBox comboSecProcess,
            JTextField txtpayDate, JXDatePicker paidDate, JTable tblDailyCollection) {
        int Advance_Amount = 0, Instalment_Balance = 0;
        float outStandBa = Float.parseFloat(txtOutAmount.getText()), advanceAmount,
                IntalmentTopay, amountCollected, instalBalance;
        amountCollected = Float.parseFloat(txtSecAmount.getText());
        IntalmentTopay = Float.parseFloat(txtInsta.getText());
        try {
            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String AdD = "SELECT `Loan_Acct_No`,`Instalment_Balance`,`Track_No`,`Advance_Amount`FROM `loan_instalment_paytrack` "
                    + "WHERE Track_No=" + (Integer.valueOf(txtTrackNo.getText()) - 1) + "";
            rs = state.executeQuery(AdD);
            if (rs.next()) {
                Advance_Amount = rs.getInt("Advance_Amount");
                Instalment_Balance = rs.getInt("Instalment_Balance");

                outStandBa = Float.parseFloat(txtOutAmount.getText()) - (amountCollected + Advance_Amount);
                IntalmentTopay = IntalmentTopay + Instalment_Balance;

            } else {
                outStandBa = outStandBa - (amountCollected + Advance_Amount);
            }
            if (amountCollected >= IntalmentTopay || amountCollected >= Float.parseFloat(txtInsta.getText())) {
                try {
                    advanceAmount = amountCollected - (IntalmentTopay - Advance_Amount);

                    state = connectionString.conect.createStatement();

                    String Insertuser = "INSERT INTO `loan_instalment_paytrack`(`Loan_Acct_No`, `Track_No`, "
                            + "`OutStanding_Amount`, `Instalment`, `Instalment_Balance`, `Pay_Date`, `Amount_Paid`, "
                            + "`Date_Paid`, `Advance_Amount`, `Pay_Process`)"
                            + "Values"
                            + "('" + txtAcctNo.getText().trim() + "',"
                            + "'" + txtTrackNo.getText().trim() + "',"
                            + "'" + outStandBa + "',"
                            + "'" + txtInsta.getText().trim() + "',"
                            + "0,"
                            + "'" + txtpayDate.getText().trim() + "',"
                            + "" + amountCollected + ","
                            + "'" + format.format(paidDate.getDate()) + "',"
                            + "" + advanceAmount + ","
                            + "'" + comboSecProcess.getSelectedItem().toString() + "');";
                    state.addBatch(Insertuser);

                    String Update = "UPDATE `loan_payment_track` SET `Pay_Status`='Paid' where `Track_Number`=" + txtTrackNo.getText().trim() + " AND "
                            + "`Loan_Account_No`='" + txtAcctNo.getText().trim() + "'";
                    state.addBatch(Update);
                    state.executeBatch();

                    txtOutAmount.setText(null);
                    txtTrackNo.setText(null);
                    txtAcctNo.setText(null);
                    txtInsta.setText(null);
                    txtpayDate.setText(null);
                    txtSecAmount.setText(null);
                    JOptionPane.showMessageDialog(null, "Transaction saved successfully");

                } catch (SQLException ex) {
                    Logger.getLogger(dbInsert.class.getName()).log(Level.SEVERE, null, ex);
                }
                //}
            } else if (amountCollected < IntalmentTopay) {
                try {

                    instalBalance = IntalmentTopay - (amountCollected + Advance_Amount);
                    state = connectionString.conect.createStatement();

                    String Insertuser = "INSERT INTO `loan_instalment_paytrack`(`Loan_Acct_No`, `Track_No`, "
                            + "`OutStanding_Amount`, `Instalment`, `Instalment_Balance`, `Pay_Date`, `Amount_Paid`, "
                            + "`Date_Paid`, `Advance_Amount`, `Pay_Process`)"
                            + "Values"
                            + "('" + txtAcctNo.getText().trim() + "',"
                            + "'" + txtTrackNo.getText().trim() + "',"
                            + "'" + outStandBa + "',"
                            + "'" + txtInsta.getText().trim() + "',"
                            + "" + instalBalance + ","
                            + "'" + txtpayDate.getText().trim() + "',"
                            + "" + amountCollected + ","
                            + "'" + format.format(paidDate.getDate()) + "',"
                            + "0,"
                            + "'" + comboSecProcess.getSelectedItem().toString() + "');";
                    state.executeUpdate(Insertuser);

                    String Update = "UPDATE `loan_payment_track` SET `Pay_Status`='Not Paid' where `Track_Number`=" + txtTrackNo.getText().trim() + " AND "
                            + "`Loan_Account_No`='" + txtAcctNo.getText().trim() + "'";
                    state.executeUpdate(Update);

                    txtOutAmount.setText(null);
                    txtTrackNo.setText(null);
                    txtAcctNo.setText(null);
                    txtInsta.setText(null);
                    txtpayDate.setText(null);
                    txtSecAmount.setText(null);
                    JOptionPane.showMessageDialog(null, "Transaction saved successfully");

                } catch (SQLException ex) {
                    Logger.getLogger(dbInsert.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            SelectInPay(tblDailyCollection);
        } catch (SQLException ex) {
            Logger.getLogger(dbInsert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void selectSecBalance(JCheckBox checkBoxSecurity, JTable tblLoansPendingApproval, JLabel lblSecurity) {
        int selectedRowIndex = tblLoansPendingApproval.getSelectedRow();
        checkBoxSecurity.setSelected(false);
        lblSecurity.setText(null);
        try {
            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql1 = " SELECT  `KYC_ID`, `Account_Balance`, `Is_Active` FROM `security_transaction`"
                    + "WHERE KYC_ID='" + tblLoansPendingApproval.getValueAt(selectedRowIndex, 3) + "'"
                    + "AND Is_Active=1";
            rs = state.executeQuery(sql1);
            while (rs.next()) {
                int Account_Balance = rs.getInt("Account_Balance");
                checkBoxSecurity.setSelected(true);
                lblSecurity.setText(String.valueOf(Account_Balance));
                //JOptionPane.showMessageDialog(null, Count);
            }

        } catch (SQLException | NumberFormatException ex) {

        }
    }

    public void calAmort(JTextField txtPrincipalAmnt, JLabel lblLoanRate, JTextField txtPeriod, JXDatePicker LoanApprovalDate,
            JTable tblPaymentsSchedule) {

        try {
            double p = Double.parseDouble(txtPrincipalAmnt.getText().trim());
            double iy = Float.parseFloat(lblLoanRate.getText().trim());
            float ny = Float.parseFloat(txtPeriod.getText().trim());
            double newbal;
            double im = (iy / 12 / 4) / 100;
            double nm = Math.ceil(ny * 4);
            double mp, ip, pp, tP;
            int i;

            //mp = p * im * Math.pow(1 + im, (double) nm) / (Math.pow(1 + im, (double) nm) - 1);
            Calendar gcal = Calendar.getInstance();
            Calendar endcal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//yyyy:MM:dd");
            // Date start = sdf.parse("2010.01");
            Date start = LoanApprovalDate.getDate();//new Date();
            //start = sdf.parse("2010.01");
            String nStart = sdf.format(start);

            gcal.setTime(sdf.parse(nStart));
            Date d;// = gcal.getTime();
            String DStart = null;// = sdf.format(d);

            ip = (p * iy) / 100;//interestpayable
            tP = p + ip; //totalpayable
            mp = tP / nm; //weeklypayment
            newbal = tP;//- mp;//new balance to be paid  
            //ip =im * newbal;
            //JOptionPane.showMessageDialog(null,newbal);
            p = tP;
            for (i = 1; i < nm; i++) {
                //ip = p * im;//interest paid 
                ip = im * newbal;
                pp = mp - ip; //princial paid
                // pp = mp - ip;
                //newbal = p - pp; //new balance
                newbal = newbal - mp;
                gcal.add(Calendar.DAY_OF_MONTH, 7);
                d = gcal.getTime();
                DStart = sdf.format(d);
                printSch(i, DStart, p, mp, ip, pp, newbal, tblPaymentsSchedule);
//                printSch(i, DStart, Math.round(newbal / 10.0) * 10, Math.round(mp / 10.0) * 10, Math.round(ip / 10.0) *10,
//                        Math.round(pp / 10.0) * 10, Math.round(newbal / 10.0) * 10);
                p = newbal;  //update old balance
            }
            Date edstart;// = new Date();
            edstart = sdf.parse(DStart);
            endcal.setTime(edstart);
            // endcal.add(Calendar.MONTH, -1 + Integer.parseInt(txtInstNo.getText().trim()));
            endcal.add(Calendar.DAY_OF_MONTH, 7);
            edstart = endcal.getTime();
            String EndStart = sdf.format(edstart);

//last month
            pp = p;
            //ip = p * im;
            ip = im * newbal;
            mp = newbal;//pp + ip;//pp + ip;
            newbal = 0.0;
            printSch(i, EndStart, p, mp, ip, pp, newbal, tblPaymentsSchedule);
//            printSch(i, DStart, Math.round(p / 5.0) * 5, Math.round(mp / 5.0) * 5, Math.round(ip / 5.0) *5,
//                        Math.round(pp / 5.0) * 5, Math.round(newbal / 5.0) * 5);
        } catch (ParseException ex) {

        }

    }

    public void printSch(int i, String DStart, double p, double mp, double ip, double pp, double newbal, JTable tblPaymentsSchedule) {

        DecimalFormat df = new DecimalFormat("#");
        //System.out.format("%-8d%-12.3f%-10.3f%-10.3f%-10.3f%-12.3f\n", i, p, mp, ip, pp, newbal);
        model = (DefaultTableModel) tblPaymentsSchedule.getModel();

        model.addRow(new Object[]{i, DStart, df.format(p), df.format(mp), df.format(ip), df.format(pp), df.format(newbal)});
        //model.addRow(new Object[]{i, DStart, p,mp,ip,pp,newbal});
    }

    public void selectLoanRate(JTable tblLoansPendingApproval, JLabel lblLoanRate) {
        int selectedRowIndex = tblLoansPendingApproval.getSelectedRow();
        lblLoanRate.setText(null);
        try {
            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql1 = "SELECT `Loan_Rate`, `Description` FROM `loan_rates`"
                    + "WHERE Loan_Type='" + tblLoansPendingApproval.getValueAt(selectedRowIndex, 10) + "'";
            rs = state.executeQuery(sql1);
            while (rs.next()) {
                Float Loan_Rate = rs.getFloat("Loan_Rate");
                lblLoanRate.setText(String.valueOf(Loan_Rate));
                //JOptionPane.showMessageDialog(null, Count);
            }

        } catch (SQLException | NumberFormatException ex) {

        }
    }

    public void selectActiveLoan(JTable tblActiveloan, JComboBox comboCrdOfficerM1, JComboBox comboMegrp1) {
        String sql = "SELECT `Loan_Acct_No`, `Client_Name`, `KYC_ID` FROM "
                + "`loan_applications` WHERE  `Group_Name`='" + comboMegrp1.getSelectedItem() + "'"
                + " AND Credit_Officer='" + comboCrdOfficerM1.getSelectedItem() + "'"
                + " AND `Approved_Status`='Approved' AND `Is_active`=1";

        output(tblActiveloan, sql);
    }

    public void selectActiveLoanReport(JTable tblActiveloan, JComboBox comboCrdOfficerM1, JComboBox comboMegrp1) {
        String query = "SELECT `Serial_No`, `Loan_Acct_No`, `Client_Name`, `KYC_ID`, `Principal_Disbursed`, `Interest_Disbursed`, "
                + "`Total_Disbursed`,`Installment_Pay`,`Date_Disbursed`,`Loan_Duration`,`Approved_Status` FROM `loan_applications`"
                + "where `Approved_Status`='Approved' AND `Group_Name`='" + comboMegrp1.getSelectedItem() + "'"
                + " AND Credit_Officer='" + comboCrdOfficerM1.getSelectedItem() + "'";
        output(tblActiveloan, query);
    }

    public void SelectInPay(JTable tblDailyCollection) {
        String sql = "SELECT `Serial_No`, `Loan_Acct_No`,`OutStanding_Amount`, `Instalment`, "
                + "`Instalment_Balance`, `Pay_Date`, `Amount_Paid`, `Date_Paid`, `Advance_Amount`, `Pay_Process` FROM `loan_instalment_paytrack`";
        output(tblDailyCollection, sql);
    }

    public void selectTodaysIntallment(JTable tblActiveloan, JTextField txtOutAmount, JTextField txtAcctNo, JTextField txtTrackNo, JTextField txtInsta,
            JTextField txtpayDate, JTextField txtSecAmount) {
        try {
            int selectedRowIndex = tblActiveloan.getSelectedRow();
            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String checkTodayInst = "SELECT `Serial_No`, `Loan_Acct_No`, `Track_No`, `OutStanding_Amount`, `Instalment`, "
                    + "`Instalment_Balance`, `Pay_Date`, `Amount_Paid`, `Date_Paid`, `Advance_Amount`,"
                    + " `Pay_Process` FROM `loan_instalment_paytrack` where  `Loan_Acct_No`='" + tblActiveloan.getValueAt(selectedRowIndex, 0) + "'"
                    + " AND  `Pay_Date`='" + format.format(new Date()) + "'";
            rs = state.executeQuery(checkTodayInst);
            if (rs.next()) {
                float Old_Balance = rs.getInt("OutStanding_Amount");
                int Track_Number = rs.getInt("Track_No");
                String Loan_Account_No = rs.getString("Loan_Acct_No");
                float Instalment = rs.getInt("Instalment");

                Date Pay_Date = rs.getDate("Pay_Date");
                txtpayDate.setText(String.valueOf(Pay_Date));
                txtOutAmount.setText(String.valueOf(Old_Balance));
                txtTrackNo.setText(String.valueOf(Track_Number));
                txtAcctNo.setText(Loan_Account_No);
                txtInsta.setText(String.valueOf(Instalment));

                JOptionPane.showMessageDialog(null, "Continue Loading and Saving");

            } else {
                String CheckOverdue = "SELECT `Serial_No`, `Loan_Acct_No`, `Track_No`, `OutStanding_Amount`, `Instalment`, "
                        + "`Instalment_Balance`, `Pay_Date`, `Amount_Paid`, `Date_Paid`, `Advance_Amount`,"
                        + " `Pay_Process` FROM `loan_instalment_paytrack` where  "
                        + "`Loan_Acct_No`='" + tblActiveloan.getValueAt(selectedRowIndex, 0) + "'"
                        + " AND  `Pay_Date`='" + format.format(new Date()) + "'";
                rs = state.executeQuery(CheckOverdue);
                if (rs.next()) {
                    float installBalance = rs.getFloat("Instalment_Balance");
                    //Ask to collect overdue of the previous loan installment
                } else {
                    //Ask to collect advance and load the next tracker NID                    
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(dbInsert.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loanTransactionLoad(JTable tblActiveloan, JTextField txtOutAmount, JTextField txtAcctNo, JTextField txtTrackNo, JTextField txtInsta,
            JTextField txtpayDate, JTextField txtSecAmount) {
        float Old_Balance;
        int Track_Number;
        String Loan_Account_No;
        float Instalment;
        Date Pay_Date;

        try {
            txtOutAmount.setText(null);
            txtTrackNo.setText(null);
            txtAcctNo.setText(null);
            txtInsta.setText(null);
            txtpayDate.setText(null);
            txtSecAmount.setText(null);
            int selectedRowIndex = tblActiveloan.getSelectedRow();
            connectionString.Connection2();
            state = connectionString.conect.createStatement();

            String SqlGet = "SELECT `Track_Number`, `ID_Number`, `Loan_Account_No`, `Cycle_Period`, `Pay_Date`, "
                    + "`Old_Balance`, `Instalment`, `Interest_Paid`, `Principal_Paid`, `New_Balance`,"
                    + "`Disburse_Date`, `Pay_Status` FROM `loan_payment_track` where "
                    + "`Loan_Account_No`='" + tblActiveloan.getValueAt(selectedRowIndex, 0) + "' AND Pay_Status='Not Paid';";
            //+ " `Pay_Date` < '" + format.format(new Date()) + "' AND Pay_Status='Not Paid';";
            rs = state.executeQuery(SqlGet);

            if (rs.next()) {
                Old_Balance = rs.getInt("Old_Balance");
                Track_Number = rs.getInt("Track_Number");
                Loan_Account_No = rs.getString("Loan_Account_No");
                Instalment = rs.getInt("Instalment");

                Pay_Date = rs.getDate("Pay_Date");
                txtpayDate.setText(String.valueOf(Pay_Date));
                txtOutAmount.setText(String.valueOf(Old_Balance));
                txtTrackNo.setText(String.valueOf(Track_Number));
                txtAcctNo.setText(Loan_Account_No);
                txtInsta.setText(String.valueOf(Instalment));
                txtSecAmount.setText(String.valueOf(Instalment));

            } else {
                String SqlOverdue = "SELECT `Track_Number`, `ID_Number`, `Loan_Account_No`, `Cycle_Period`, `Pay_Date`, "
                        + "`Old_Balance`, `Instalment`, `Interest_Paid`, `Principal_Paid`, `New_Balance`,"
                        + "`Disburse_Date`, `Pay_Status` FROM `loan_payment_track` where "
                        + "`Loan_Account_No`='" + tblActiveloan.getValueAt(selectedRowIndex, 0) + "'AND "
                        + " `Pay_Date`='" + format.format(new Date()) + "' AND Pay_Status='Not Paid';";
                rs = state.executeQuery(SqlOverdue);
                if (rs.next()) {
                    Old_Balance = rs.getInt("Old_Balance");
                    Track_Number = rs.getInt("Track_Number");
                    Loan_Account_No = rs.getString("Loan_Account_No");
                    Instalment = rs.getInt("Instalment");
                    Pay_Date = rs.getDate("Pay_Date");

                    txtpayDate.setText(String.valueOf(Pay_Date));
                    txtOutAmount.setText(String.valueOf(Old_Balance));
                    txtTrackNo.setText(String.valueOf(Track_Number));
                    txtAcctNo.setText(Loan_Account_No);
                    txtInsta.setText(String.valueOf(Instalment));
                    txtSecAmount.setText(String.valueOf(Instalment));
                } else {
                    String SqlAdvance = "SELECT `Track_Number`, `ID_Number`, `Loan_Account_No`, `Cycle_Period`, `Pay_Date`, "
                            + "`Old_Balance`, `Instalment`, `Interest_Paid`, `Principal_Paid`, `New_Balance`,"
                            + "`Disburse_Date`, `Pay_Status` FROM `loan_payment_track` where "
                            + "`Loan_Account_No`='" + tblActiveloan.getValueAt(selectedRowIndex, 0) + "'AND  Pay_Status='Not Paid';";
                    rs = state.executeQuery(SqlAdvance);
                    while (rs.next()) {
                        Old_Balance = rs.getInt("Old_Balance");
                        Track_Number = rs.getInt("Track_Number");
                        Loan_Account_No = rs.getString("Loan_Account_No");
                        Instalment = rs.getInt("Instalment");
                        Pay_Date = rs.getDate("Pay_Date");

                        txtpayDate.setText(String.valueOf(Pay_Date));
                        txtOutAmount.setText(String.valueOf(Old_Balance));
                        txtTrackNo.setText(String.valueOf(Track_Number));
                        txtAcctNo.setText(Loan_Account_No);
                        txtInsta.setText(String.valueOf(Instalment));
                        txtSecAmount.setText(String.valueOf(Instalment));

                    }

                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(dbInsert.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void selectLoanInfo(JTable tblActiveloan, JTextField txtOutAmount, JTextField txtAcctNo, JTextField txtTrackNo,
            JTextField txtInsta,
            JTextField txtpayDate, JTextField txtSecAmount) {

        try {
            int selectedRowIndex = tblActiveloan.getSelectedRow();
            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sl1 = "SELECT `Serial_No`, `Loan_Acct_No`, `Track_No`, `OutStanding_Amount`, `Instalment`,"
                    + "`Instalment_Balance`, `Pay_Date`, `Amount_Paid`, `Date_Paid`, `Advance_Amount`, `Pay_Process` "
                    + "FROM `loan_instalment_paytrack` where "
                    + "`Loan_Acct_No`='" + tblActiveloan.getValueAt(selectedRowIndex, 0) + "'AND  `Pay_Date`='" + format.format(new Date()) + "' AND Pay_Status='Not Paid';";

            String SqlGet = "SELECT `Track_Number`, `ID_Number`, `Loan_Account_No`, `Cycle_Period`, `Pay_Date`, "
                    + "`Old_Balance`, `Instalment`, `Interest_Paid`, `Principal_Paid`, `New_Balance`,"
                    + "`Disburse_Date`, `Pay_Status` FROM `loan_payment_track` where "
                    + "`Loan_Account_No`='" + tblActiveloan.getValueAt(selectedRowIndex, 0) + "'AND "
                    + " `Pay_Date`='" + format.format(new Date()) + "' AND Pay_Status='Not Paid';";

            rs = state.executeQuery(SqlGet);
            if (rs.next()) {
                float Old_Balance = rs.getInt("Old_Balance");
                int Track_Number = rs.getInt("Track_Number");
                String Loan_Account_No = rs.getString("Loan_Account_No");
                float Instalment = rs.getInt("Instalment");

                String sql1 = "SELECT `Track_Number`, `ID_Number`, `Loan_Account_No`, `Cycle_Period`, `Pay_Date`, `Old_Balance`, `Instalment`,"
                        + " `Interest_Paid`, `Principal_Paid`, `New_Balance`, `Disburse_Date`, `Pay_Status` FROM `loan_payment_track` where"
                        + "`Track_Number`='" + txtTrackNo.getText() + "' AND `Pay_Date`='" + format.format(new Date()) + "'";

                rs = state.executeQuery(sql1);
                Date Pay_Date;//=new Date();

                if (rs.next()) {
                    Pay_Date = rs.getDate("Pay_Date");
                    txtpayDate.setText(String.valueOf(Pay_Date));
                    txtOutAmount.setText(String.valueOf(Old_Balance));
                    txtTrackNo.setText(String.valueOf(Track_Number + 1));
                    txtAcctNo.setText(Loan_Account_No);
                    txtInsta.setText(String.valueOf(Instalment));
                } else {
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog(null, "THE CLIENT HAS NO ACTIVE INSTALMENT TODAY,DO YOU WANT TO COLLECT ADVANCE OR OVERDUE?", "Question", dialogButton);
                    txtTrackNo.setText(String.valueOf(Track_Number + 1));
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        String sqll1 = "SELECT `Track_Number`, `ID_Number`, `Loan_Account_No`, `Cycle_Period`, `Pay_Date`, `Old_Balance`, `Instalment`,"
                                + " `Interest_Paid`, `Principal_Paid`, `New_Balance`, `Disburse_Date`, `Pay_Status` FROM `loan_payment_track` where"
                                + "`Track_Number`='" + txtTrackNo.getText() + "'";// AND `Pay_Date`='" + format.format(new Date()) + "'";
                        rs = state.executeQuery(sqll1);
                        while (rs.next()) {
                            Pay_Date = rs.getDate("Pay_Date");
                            txtpayDate.setText(String.valueOf(Pay_Date));
                        }
                        txtOutAmount.setText(String.valueOf(Old_Balance));

                        txtAcctNo.setText(Loan_Account_No);
                        txtInsta.setText(String.valueOf(Instalment));
                    } else {

                        txtOutAmount.setText(null);
                        txtTrackNo.setText(null);
                        txtAcctNo.setText(null);
                        txtInsta.setText(null);
                        txtpayDate.setText(null);
                        txtSecAmount.setText(null);
                    }

                }
            } else {
                try {
                    connectionString.Connection2();
                    state = connectionString.conect.createStatement();
                    String sql1 = "SELECT `Track_Number`, `ID_Number`, `Loan_Account_No`, `Cycle_Period`, `Pay_Date`, `Old_Balance`, `Instalment`,"
                            + " `Interest_Paid`, `Principal_Paid`, `New_Balance`, `Disburse_Date`, `Pay_Status` FROM `loan_payment_track` where"
                            + "`Loan_Account_No`='" + tblActiveloan.getValueAt(selectedRowIndex, 0) + "'"
                            + "AND  `Pay_Date`='" + format.format(new Date()) + "' AND Pay_Status='Not Paid';";
                    rs = state.executeQuery(sql1);
                    if (rs.next()) {
                        float Old_Balance = rs.getInt("Old_Balance");
                        int Track_Number = rs.getInt("Track_Number");
                        String Loan_Account_No = rs.getString("Loan_Account_No");
                        float Instalment = rs.getInt("Instalment");
                        Date Pay_Date = rs.getDate("Pay_Date");

                        txtOutAmount.setText(String.valueOf(Old_Balance));
                        txtTrackNo.setText(String.valueOf(Track_Number));
                        txtAcctNo.setText(Loan_Account_No);
                        txtInsta.setText(String.valueOf(Instalment));
                        txtpayDate.setText(String.valueOf(Pay_Date));

                    } else {
                        int dialogButton = JOptionPane.YES_NO_OPTION;
                        int dialogResult = JOptionPane.showConfirmDialog(null, "THE CLIENT HAS NO ACTIVE INSTALMENT TODAY,DO YOU WANT TO COLLECT ADVANCE OR OVERDUE?", "Question", dialogButton);
                        if (dialogResult == JOptionPane.YES_OPTION) {
                            String sql2 = "SELECT `Track_Number`, `ID_Number`, `Loan_Account_No`, `Cycle_Period`, `Pay_Date`, `Old_Balance`, `Instalment`,"
                                    + " `Interest_Paid`, `Principal_Paid`, `New_Balance`, `Disburse_Date`, `Pay_Status` FROM `loan_payment_track` where"
                                    + "`Loan_Account_No`='" + tblActiveloan.getValueAt(selectedRowIndex, 0) + "' AND Pay_Status='Not Paid';";
                            rs = state.executeQuery(sql2);
                            if (rs.first()) {
                                float Old_Balance = rs.getInt("Old_Balance");
                                int Track_Number = rs.getInt("Track_Number");
                                String Loan_Account_No = rs.getString("Loan_Account_No");
                                float Instalment = rs.getInt("Instalment");
                                Date Pay_Date = rs.getDate("Pay_Date");

                                txtOutAmount.setText(String.valueOf(Old_Balance));
                                txtTrackNo.setText(String.valueOf(Track_Number));
                                txtAcctNo.setText(Loan_Account_No);
                                txtInsta.setText(String.valueOf(Instalment));
                                txtpayDate.setText(String.valueOf(Pay_Date));
                                JOptionPane.showMessageDialog(null, "You can collect Advance");
                            }

                        } else {

                            txtOutAmount.setText(null);
                            txtTrackNo.setText(null);
                            txtAcctNo.setText(null);
                            txtInsta.setText(null);
                            txtpayDate.setText(null);
                            txtSecAmount.setText(null);
                        }
                    }

                } catch (SQLException | NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(dbInsert.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void SaveAcctTransactions(JRadioButton radioWithraw, JRadioButton radioDeposit, JTextField txtMId, JTextField txtAmount,
            JComboBox comboAccType, JTable aCCTinfo) {
        try {
            connectionString.Connection2();
            String transType = null;
            if (radioWithraw.isSelected()) {
                transType = radioWithraw.getText();
            } else if (radioDeposit.isSelected()) {
                transType = radioDeposit.getText();
            }

            String Insertuser = "INSERT INTO `acct_tractions`(`Member_ID`, `Acct_Type`, `Transaction_Type`, `Transact_Date`, `Transact_Amount`)"
                    + "Values"
                    + "('" + txtMId.getText().trim() + "',"
                    + "'" + comboAccType.getSelectedItem() + "',"
                    + "'" + transType + "',"
                    + "'" + format.format(new Date()) + "',"
                    + "'" + txtAmount.getText().trim() + "');";
            state = connectionString.conect.createStatement();
            state.executeUpdate(Insertuser);
            String sql = "SELECT `Serial_No`, `Member_ID`, `Member_No`, `Acct_Type`, `Current_Balance` FROM `acct_record`";

            try {
                rs = state.executeQuery(sql);
            } catch (SQLException ex) {

            }

            output(aCCTinfo, sql);
        } catch (SQLException | HeadlessException ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        }
    }

    public void choseACCountAndCalculate(JTable AccMemberList, JComboBox comboAccType, JTextField txtCurrentBal, JTextField txtAmount, JTextField txtNewBal) {
        model = (DefaultTableModel) AccMemberList.getModel();
        int Selectindex = AccMemberList.getSelectedRow();
        try {
            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql1 = "SELECT `Serial_No`, `Member_ID`, `Member_No`, `Acct_Type`, `Current_Balance` FROM "
                    + "`acct_record` where  Member_ID='" + model.getValueAt(Selectindex, 0) + "' AND Acct_Type='" + comboAccType.getSelectedItem() + "';";
            rs = state.executeQuery(sql1);

            if (rs.next()) {

                String clietname = rs.getString("Current_Balance");
                txtCurrentBal.setText(clietname);
                txtAmount.setText("0");
                txtNewBal.setText("0");
            } else {
                txtCurrentBal.setText("0");
            }

        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    public void SaveTheAcct(JTable aCCTinfo, JComboBox comboAccType, JTextField txtMId, JTextField txtMnumber, JTextField txtAmount,
            JTextField txtNewBal, JTextField txtMname,
            JRadioButton radioWithraw, JRadioButton radioDeposit) {
        try {
            connectionString.Connection2();

            String Insertuser = "INSERT INTO `Acct_Record`(`Member_ID`, `Member_No`, `Acct_Type`, `Current_Balance`)"
                    + "Values"
                    + "('" + txtMId.getText().trim() + "',"
                    + "'" + txtMnumber.getText().trim() + "',"
                    + "'" + comboAccType.getSelectedItem() + "',"
                    + "'" + txtNewBal.getText().trim() + "');";
            state = connectionString.conect.createStatement();
            state.executeUpdate(Insertuser);

            SaveAcctTransactions(radioWithraw, radioDeposit, txtMId, txtAmount, comboAccType, aCCTinfo);

            JOptionPane.showMessageDialog(null, "Saved.....");

            txtMId.setText(null);
            txtMnumber.setText(null);
            txtAmount.setText(null);
            txtMname.setText(null);
            String sql = "SELECT `Serial_No`, `Member_ID`, `Member_No`, `Acct_Type`, `Current_Balance` FROM `acct_record`";
            try {
                rs = state.executeQuery(sql);
            } catch (SQLException ex) {

            }

            output(aCCTinfo, sql);
        } catch (SQLException | HeadlessException ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        }
    }

    public void CheckTheUpdateSaveAccounts(JTable aCCTinfo, JComboBox comboAccType, JTextField txtMId, JTextField txtMnumber, JTextField txtAmount,
            JTextField txtNewBal, JTextField txtMname, JRadioButton radioWithraw, JRadioButton radioDeposit) {
        try {
            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql1 = "SELECT `Member_ID`, `Member_No`, `Acct_Type`, `Current_Balance` FROM "
                    + "`acct_record` where  Member_ID='" + txtMId.getText().trim() + "' AND Acct_Type='" + comboAccType.getSelectedItem() + "';";
            rs = state.executeQuery(sql1);

            if (rs.next() == true) {
                UpdateAccts(txtNewBal, txtMId, comboAccType, radioWithraw, radioDeposit, txtAmount, aCCTinfo);
            } else if (rs.next() == false) {
                SaveTheAcct(aCCTinfo, comboAccType, txtMId, txtMnumber, txtAmount, txtNewBal, txtMname, radioWithraw, radioDeposit);
            }

        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void saveadjustTransaction(JLabel lblContainer, JComboBox comboAccType1, JComboBox comboSecProcess1, double adjustAmount, JTable tblDailyCollection1) {
        try {
            connectionString.Connection2();

            String Insertuser = "INSERT INTO `acct_tractions`(`Member_ID`, `Acct_Type`, `Transaction_Type`, `Transact_Date`, `Transact_Amount`)"
                    + "Values"
                    + "('" + lblContainer.getText().trim() + "',"
                    + "'" + comboAccType1.getSelectedItem() + "',"
                    + "'" + comboSecProcess1.getSelectedItem() + "',"
                    + "'" + format.format(new Date()) + "',"
                    + "'" + adjustAmount + "');";
            state = connectionString.conect.createStatement();
            state.executeUpdate(Insertuser);
            String sql = "SELECT `Serial_No`, `Member_ID`, `Acct_Type`, `Transaction_Type`, `Transact_Date`, `Transact_Amount` "
                    + "FROM `acct_tractions` where `Transaction_Type`='Adjust'";

            try {
                rs = state.executeQuery(sql);
            } catch (SQLException ex) {

            }

            output(tblDailyCollection1, sql);
        } catch (SQLException | HeadlessException ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        }
    }

    public void LoanAdjustment(JTextField txtCurrentBal1, JTextField txtSecAmount1,
            JTextField txtOutAmount1, JLabel lblContainer,
            JComboBox comboAccType1, JComboBox comboSecProcess1, JTable tblDailyCollection1,
            JTextField txtInsta1, JTextField txtTrackNo1, JTextField txtAcctNo1, JTextField txtpayDate1, JXDatePicker paidDate1) {
        double oustandAmount = Double.parseDouble(txtOutAmount1.getText().trim());
        double currBal = Double.parseDouble(txtCurrentBal1.getText().trim());
        double adjustAmount = Double.parseDouble(txtSecAmount1.getText().trim());
        double accBalance;// = currBal - adjustAmount;

        if (currBal < adjustAmount) {
            JOptionPane.showMessageDialog(null, "You have insufficient fund to make Ajustement");
            txtSecAmount1.setText(txtCurrentBal1.getText().trim());
        } else {
            if (adjustAmount > oustandAmount) {
                JOptionPane.showMessageDialog(null, "Adjust Amount Can not be more than Outstanding Amount");
                txtSecAmount1.setText(txtCurrentBal1.getText().trim());
            } else {
                try {
                    connectionString.Connection2();
                    state = connectionString.conect.createStatement();
                    accBalance = currBal - adjustAmount;

                    String update = "UPDATE `acct_record` SET  `Current_Balance`='" + accBalance + "'"
                            + "where  Member_ID='" + lblContainer.getText().trim() + "' AND Acct_Type='" + comboAccType1.getSelectedItem() + "';";
                    state.executeUpdate(update);

                    saveadjustTransaction(lblContainer, comboAccType1, comboSecProcess1, adjustAmount, tblDailyCollection1);
                    saveLoanTransaction(txtSecAmount1, txtInsta1, txtTrackNo1, txtAcctNo1, txtOutAmount1, comboSecProcess1,
                            txtpayDate1, paidDate1, tblDailyCollection1);

                    JOptionPane.showMessageDialog(null, "Your New  " + comboAccType1.getSelectedItem() + "   Balance is   " + accBalance + " After Ajustement");

                } catch (SQLException | HeadlessException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
                }

                JOptionPane.showMessageDialog(null, "Savings.....");
            }
        }

    }

    public void DoingAcctCalculation(JTextField txtNewBal, JTextField txtCurrentBal, JRadioButton radioWithraw,
            JRadioButton radioDeposit, JTextField txtAmount,
            JTable aCCTinfo) {
        int currBal, newBal;
        int tranAmnt;
        currBal = Integer.valueOf(txtCurrentBal.getText().trim());
        tranAmnt = Integer.valueOf(txtAmount.getText().trim());
        newBal = Integer.valueOf(txtNewBal.getText().trim());
        if (radioWithraw.isSelected()) {
            if (currBal < tranAmnt) {
                JOptionPane.showMessageDialog(null, "You have insufficient fund to make withrwal");
                txtNewBal.setText("0");
                txtAmount.setText(String.valueOf(currBal));
            } else {
                newBal = currBal - tranAmnt;
            }
            txtNewBal.setText(String.valueOf(newBal));
        } else if (radioDeposit.isSelected()) {
            newBal = currBal + tranAmnt;
            txtNewBal.setText(String.valueOf(newBal));
        }
    }

    public void UpdateAccts(JTextField txtNewBal, JTextField txtMId, JComboBox comboAccType, JRadioButton radioWithraw,
            JRadioButton radioDeposit, JTextField txtAmount,
            JTable aCCTinfo) {

        try {
            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String update = "UPDATE `acct_record` SET  `Current_Balance`='" + txtNewBal.getText().trim() + "'"
                    + "where  Member_ID='" + txtMId.getText().trim() + "' AND Acct_Type='" + comboAccType.getSelectedItem() + "';";
            state.executeUpdate(update);

            SaveAcctTransactions(radioWithraw, radioDeposit, txtMId, txtAmount, comboAccType, aCCTinfo);

            JOptionPane.showMessageDialog(null, "Your New  " + comboAccType.getSelectedItem() + "   Balance is   " + txtNewBal.getText().trim() + "");

        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        }
    }

    public boolean checkshop(JLabel lblItemCode) {
        //check if the product or item is already saved in the DB
        boolean itemIsFound = false;

        try {
            connectionString.Connection2();
            String SQL = "SELECT `Serial_No`, `Bs_Name`, `Address`, `Town`, `Email`, `Slogan` FROM `shop_info`"
                    + " WHERE  `Serial_No`= ?";
            pstmt = DBclass.conect.prepareStatement(SQL);
            pstmt.setString(1, lblItemCode.getText().trim());

            DBclass.resultset = pstmt.executeQuery();
            itemIsFound = DBclass.resultset.next();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "there was error...." + e);
        } finally {
            try {
                DBclass.conect.close();
            } catch (SQLException ex) {

            }
        }

        return itemIsFound;
    }

    public void SavingBSInfo(JTextField txtShopName2, JTextField txtShopLocation2, JTextField txtShopAddress2,
            JTextField txtShopEmail2, JTextField txtShopSlogan2, JTable tblShopIn, JLabel lblItemCode) {
        if (checkshop(lblItemCode)) {

            try {
                connectionString.Connection2();
                String updateItems = "UPDATE `shop_info` SET "
                        + "`Bs_Name`=?,"
                        + "`Address`=?,"
                        + "`Town`=?,"
                        + "`Email`=?,"
                        + "`Slogan`=?"
                        + "where Serial_No=?";
                pstmt = DBclass.conect.prepareStatement(updateItems);

                pstmt.setString(1, txtShopName2.getText().trim());
                pstmt.setString(2, txtShopLocation2.getText().trim());
                pstmt.setString(3, txtShopAddress2.getText().trim());
                pstmt.setString(4, txtShopEmail2.getText().trim());
                pstmt.setString(5, txtShopSlogan2.getText().trim());
                pstmt.setString(6, lblItemCode.getText().trim());

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Updated......");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            } finally {
                try {
                    DBclass.conect.close();
                } catch (SQLException ex) {

                }
            }
        } else {

            try {
                connectionString.Connection2();
                String payaccounts = "INSERT INTO `shop_info`(`Bs_Name`, `Address`, `Town`, `Email`, `Slogan`)"
                        + "VALUES('" + txtShopName2.getText().trim() + "','" + txtShopLocation2.getText().trim() + "',"
                        + "'" + txtShopAddress2.getText().trim() + "','" + txtShopEmail2.getText().trim() + "','" + txtShopSlogan2.getText().trim() + "')";
                pstmt = DBclass.conect.prepareStatement(payaccounts);
                pstmt.executeUpdate(payaccounts);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "This error......" + ex);

            }
        }
    }

    public void dataFromtable(JTextField txtShopName2, JTextField txtShopLocation2, JTextField txtShopAddress2,
            JTextField txtShopEmail2, JTextField txtShopSlogan2, JTable tblShopIn, JLabel lblItemCode) {
        // get the model from the jtable
        model = (DefaultTableModel) tblShopIn.getModel();

        // get the selected row index
        int selectedRowIndex = tblShopIn.getSelectedRow();

        lblItemCode.setText(model.getValueAt(selectedRowIndex, 0).toString());
        txtShopName2.setText(model.getValueAt(selectedRowIndex, 1).toString());
        txtShopLocation2.setText(model.getValueAt(selectedRowIndex, 2).toString());
        txtShopAddress2.setText(model.getValueAt(selectedRowIndex, 3).toString());
        txtShopEmail2.setText(model.getValueAt(selectedRowIndex, 4).toString());
        txtShopSlogan2.setText(model.getValueAt(selectedRowIndex, 5).toString());
    }

    public void ResetPASSWORD(JTable tblUsers) {

        model = (DefaultTableModel) tblUsers.getModel();
        int selectedRowIndex = tblUsers.getSelectedRow();
        try {
            connectionString.Connection2();

            String hash = byteArrayToHexString(this.computeHash(model.getValueAt(selectedRowIndex, 0).toString()));
            state = connectionString.conect.createStatement();
            String update = "UPDATE `pass_word_pharmacist` SET `USER_PASSWORD`='" + hash + "', STATUS='1'"
                    + "WHERE `USER_NAME`='" + model.getValueAt(selectedRowIndex, 0).toString() + "';";
            state.executeUpdate(update);
            JOptionPane.showMessageDialog(null, "YOU HAVE SUCCESSFULLY RESET THE PASSWORD");

            conn.close();

        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage().toUpperCase());
        } catch (Exception ex) {

        }
    }

    public void clientGroupTransfer(JComboBox comboGrp, JComboBox comboGrp1, JTextField txtCIDNo,
            JComboBox comboCreOff, JComboBox comboCreOff1, JTable tblTransfers, JLabel user) {

        String fromGroup = comboGrp.getSelectedItem().toString();
        String toGroup = comboGrp1.getSelectedItem().toString();
        if (fromGroup.equals(toGroup)) {
            JOptionPane.showMessageDialog(null, "Client Can Not be transfer to the same group, they belong");
        } else {
            try {
                connectionString.Connection2();
                state = connectionString.conect.createStatement();
                String sql1 = "SELECT `Serial_No`, `Loan_Acct_No`, `Client_Name`, `KYC_ID`, `Principal_Disbursed`, `Interest_Disbursed`,"
                        + " `Total_Disbursed`, `Installment_Pay`, `Credit_Officer`, `Loan_Duration`, `Approved_Status`,"
                        + " `Group_Name`, `Program`, `Date_Disbursed`, `Process_Disbursed`, `Monthly_Income`, `Client_Scheme`, "
                        + "`Fund`, `Is_Active` FROM `loan_applications` WHERE `KYC_ID`=" + txtCIDNo.getText().trim() + " AND Is_Active=1";
                rs = state.executeQuery(sql1);

                if (rs.next() == true) {
                    JOptionPane.showMessageDialog(null, "Client has Active Loan, Group Can Not be changed");

                } else if (rs.next() == false) {
                    try {

                        String Insertuser = "INSERT INTO `client_transfer`(`Client_KYC`, "
                                + "`From_Credit_Officer`, `From_Group`, `To_Credit_Officer`,"
                                + " `To_Group`, `Date_Transfer`, `Transferred_By`)"
                                + "Values"
                                + "('" + txtCIDNo.getText().trim() + "',"
                                + "'" + comboCreOff.getSelectedItem() + "',"
                                + "'" + comboGrp.getSelectedItem() + "',"
                                + "'" + comboCreOff1.getSelectedItem() + "',"
                                + "'" + comboGrp1.getSelectedItem() + "',"
                                + "'" + format.format(new Date()) + "',"
                                + "'" + user.getText().trim() + "');";
                        state = connectionString.conect.createStatement();
                        state.executeUpdate(Insertuser);
                        String update = "UPDATE `members_info` SET `Group_Name`='" + comboGrp1.getSelectedItem() + "'"
                                + "where  Member_ID_No='" + txtCIDNo.getText().trim() + "';";
                        state.executeUpdate(update);
                        String sql = "SELECT `Transfer_ID`, `Client_KYC`, `From_Credit_Officer`, `From_Group`,"
                                + " `To_Credit_Officer`, `To_Group`, `Date_Transfer`, `Transferred_By` "
                                + "FROM `client_transfer`";
                        output(tblTransfers, sql);
                    } catch (SQLException | HeadlessException ex) {
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }

            } catch (SQLException | HeadlessException ex) {
                JOptionPane.showMessageDialog(null, ex);

            }
        }

    }

    public static void main(String args[]) {
        // String k=JOptionPane.showInputDialog(null, "LOG IN", "Enter password?", JOptionPane.QUESTION_MESSAGE);
        // if("DockmanHacker".equals(k))
        //{

//login.show(true);
        java.awt.EventQueue.invokeLater(() -> {
            new DBsConnection().setVisible(true);
        });
        //}
        //else
        //{
        //JOptionPane.showMessageDialog(null, "Wrong Password You can not Continue\n,email josephayoma1@gmail\n or \n Call him on +254729696690 For help");
        //}
    }
}
