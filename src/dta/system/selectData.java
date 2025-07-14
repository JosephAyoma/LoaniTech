/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dta.system;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author Dockman
 */
public class selectData {

    Connection conn;
    Statement state;
    ResultSet rs;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    //dbInsert outJus = new dbInsert();
    DBsConnection connectionString = new DBsConnection();

    public void selectClientLoan(JTable tblLoansApplied, JTable tlbGauranton, JTable tlbOtherFee,
            JTextField txtPrincipal, JTextField txtCIDNo, JComboBox comboMemberFill,
            JComboBox comboCreOff, JComboBox comboGrp, JComboBox comboProgram, JTextField txtDuaration,
            JComboBox comboProcess, JTextField txtAcctNumber, JTextField txtMonthlyIncome, JTextField txtScheme,
            JXDatePicker disburseDate, JComboBox comboFund, JTextField txtInstalment) throws SQLException {

        DefaultTableModel model = (DefaultTableModel) tblLoansApplied.getModel();
        DefaultTableModel model2 = (DefaultTableModel) tlbGauranton.getModel();
        int rowCount = tblLoansApplied.getSelectedRow();
        Date disburseD;
        model2.setRowCount(0);
        try {
            connectionString.Connection2();
            state = connectionString.conect.createStatement();

            String sql = "SELECT loanapplicablefee.`Loan_Insurance_Fee`,loanapplicablefee.`Loan_Processing_Fee`,"
                    + "loan_applications.`Loan_Acct_No`,\n"
                    + "loan_applications.`Client_Name`,\n"
                    + "loan_applications.`KYC_ID`, \n"
                    + "loan_applications.`Principal_Disbursed`, \n"
                    + "loan_applications.`Total_Disbursed`,\n"
                    + "loan_applications.`Installment_Pay`,\n"
                    + "loan_applications.`Group_Name`, \n"
                    + "loan_applications.`Program`,\n"
                    + "loan_applications.`Date_Disbursed`,\n"
                    + "loan_applications.`Credit_Officer`,\n"
                    + "loan_applications.`Loan_Duration`,\n"
                    + "loan_applications.`Process_Disbursed`, \n"
                    + "loan_applications.`Monthly_Income`,\n"
                    + "loan_applications.`Client_Scheme`,\n"
                    + "loan_applications.`Fund`,"
                    + "`guarantor_data`.`G_Name`, `guarantor_data`.`G_Relation`, `guarantor_data`.`G_Address`, "
                    + "`guarantor_data`.`G_KYC_ID`, `guarantor_data`.`G_DOB`, `guarantor_data`.`G_Contact_No`,"
                    + " guarantor_data.`Is_Active` FROM loanapplicablefee,loan_applications,`guarantor_data`"
                    + " WHERE loan_applications.`Loan_Acct_No`= loanapplicablefee.Loan_Account_No AND "
                    + "loanapplicablefee.Loan_Account_No=guarantor_data.Loan_Account_No "
                    + "AND guarantor_data.Loan_Account_No='" + model.getValueAt(rowCount, 1).toString() + "' "
                    + "AND guarantor_data.Is_Active=1 AND guarantor_data.Is_Active=loan_applications.Is_Active";
            rs = state.executeQuery(sql);

            if (rs.next()) {

                String creditOfficer = rs.getString("Credit_Officer");
                String Group_Name = rs.getString("Group_Name");
                String Program = rs.getString("Program");
                String Client_Name = rs.getString("Client_Name");
                String Process_Disbursed = rs.getString("Process_Disbursed");
                String Monthly_Income = rs.getString("Monthly_Income");
                String Fund = rs.getString("Fund");
                String Client_Scheme = rs.getString("Client_Scheme");
                Date Date_Disbursed = rs.getDate("Date_Disbursed");
                String KYC_ID = rs.getString("KYC_ID");
                String Loan_Duration = rs.getString("Loan_Duration");
                String Principal_Disbursed = rs.getString("Principal_Disbursed");
                String Installment_Pay = rs.getString("Installment_Pay");
                String Loan_Insurance_Fee = rs.getString("Loan_Insurance_Fee");
                String Loan_Processing_Fee = rs.getString("Loan_Processing_Fee");
                String G_Name = rs.getString("G_Name");
                String G_KYC_ID = rs.getString("G_KYC_ID");
                String G_Relation = rs.getString("G_Relation");
                String G_DOB = rs.getString("G_DOB");
                String G_Address = rs.getString("G_Address");
                String G_Contact_No = rs.getString("G_Contact_No");

                tlbOtherFee.setValueAt(Loan_Insurance_Fee, 0, 1);
                tlbOtherFee.setValueAt(Loan_Processing_Fee, 1, 1);

                comboCreOff.setSelectedItem(creditOfficer);
                comboGrp.setSelectedItem(Group_Name);
                comboProgram.setSelectedItem(Program);
                comboMemberFill.setSelectedItem(Client_Name);
                comboProcess.setSelectedItem(Process_Disbursed);
                txtMonthlyIncome.setText(Monthly_Income);
                comboFund.setSelectedItem(Fund);
                txtScheme.setText(Client_Scheme);
                disburseDate.setDate(Date_Disbursed);
                txtCIDNo.setText(KYC_ID);
                txtDuaration.setText(Loan_Duration);
                txtPrincipal.setText(Principal_Disbursed);
                txtInstalment.setText(Installment_Pay);
                txtAcctNumber.setText(model.getValueAt(rowCount, 1).toString());
                model2.addRow(new Object[]{G_Name, G_KYC_ID, G_Relation, G_DOB, G_Address, G_Contact_No});
                //comboMemberFill.setSelectedItem( model.getValueAt(rowCount, 2).toString());//added because one click was not working
            } else {
                JOptionPane.showMessageDialog(null, "The Loan Is Inactive or was Rejected");
            }

        } catch (SQLException ex) {
            Logger.getLogger(selectData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connectionString.conect != null) {
                try {
                    connectionString.conect.close();
                } catch (SQLException ignore) {
                }
            }
        }

    }

    public void loadAcctType(JTextField txtCode, JComboBox comboType, JLabel lblCode) {
        try {
            connectionString.Connection2();
            state = connectionString.conect.createStatement();

            String sql = "SELECT `Serial`, `Account_Code`, `Account_Name`, `Account_Type`, `Description` FROM `glaccounts` WHERE "
                    + "`Account_Type`='" + comboType.getSelectedItem().toString() + "'";
            rs = state.executeQuery(sql);

            if (rs.last()) {
                int code = rs.getInt("Account_Code");
                int maincode = Integer.parseInt(lblCode.getText().trim());
                int currentCode = (code - maincode) + 1;
                txtCode.setText(String.valueOf(currentCode));
            } else {
                txtCode.setText("1");
            }
        } catch (SQLException sql) {

        }
    }

    public void loadFeeCollected(JTextField txtfeeClientId, JComboBox comboFeetype, JTextField txtfeeCollected,
            JTextField txtfeeCode, JTable tblFeeInfor, JXDatePicker feeDateCollected, JComboBox comboMemberFill1, JLabel lblContainer) {
        int selectedRowIndex = tblFeeInfor.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) tblFeeInfor.getModel();

        try {

            txtfeeClientId.setText(model.getValueAt(selectedRowIndex, 2).toString());
            lblContainer.setText(model.getValueAt(selectedRowIndex, 0).toString());
            txtfeeCollected.setText(model.getValueAt(selectedRowIndex, 6).toString());
            txtfeeCode.setText(model.getValueAt(selectedRowIndex, 4).toString());
            comboFeetype.setSelectedItem(model.getValueAt(selectedRowIndex, 3).toString());
            comboMemberFill1.setSelectedItem(model.getValueAt(selectedRowIndex, 1).toString());
            Date dateCollected = format.parse(model.getValueAt(selectedRowIndex, 5).toString());

            feeDateCollected.setFormats(format);
            feeDateCollected.setDate(dateCollected);

        } catch (ParseException ex) {
            Logger.getLogger(selectData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void dashBoard(JLabel lbltodaysCollection, JLabel lblTodaysOverdue, JLabel lblAdmission,
            JLabel lblToverdue, JLabel lblBorrower, JLabel lblToutstanding, JLabel lblPending,
            JLabel lblTDisbursed, JLabel lblApproved) {
        int sum = 0;
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);

        try {

            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql = "SELECT sum( `Amount_Paid`) FROM `loan_instalment_paytrack` WHERE `Date_Paid`=CURDATE();";
            rs = state.executeQuery(sql);
            while (rs.next()) {
                sum = rs.getInt("sum( `Amount_Paid`)");
                lbltodaysCollection.setText(String.valueOf(formatter.format(sum)));
            }

        } catch (SQLException ex) {
            Logger.getLogger(selectData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connectionString.conect != null) {
                try {
                    connectionString.conect.close();
                } catch (SQLException ignore) {
                }
            }
        }

        try {

            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql = "SELECT sum( `Instalment`) FROM `loan_payment_track` WHERE `Pay_Date`= CURRENT_DATE() AND `Pay_Status`='Not Paid';";
            rs = state.executeQuery(sql);
            while (rs.next()) {
                sum = rs.getInt("sum( `Instalment`)");
                lblTodaysOverdue.setText(String.valueOf(formatter.format(sum)));
            }

        } catch (SQLException ex) {
            Logger.getLogger(selectData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connectionString.conect != null) {
                try {
                    connectionString.conect.close();
                } catch (SQLException ignore) {
                }
            }
        }

        try {

            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql = "SELECT count(`Member_Admin_No`) FROM `member_admission` WHERE `Admin_Date`=CURRENT_DATE() and Is_Active=1;";
            rs = state.executeQuery(sql);
            while (rs.next()) {
                sum = rs.getInt("count(`Member_Admin_No`)");
                lblAdmission.setText(String.valueOf(formatter.format(sum)));
            }

        } catch (SQLException ex) {
            Logger.getLogger(selectData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connectionString.conect != null) {
                try {
                    connectionString.conect.close();
                } catch (SQLException ignore) {
                }
            }
        }
        try {

            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql = "SELECT sum( `Instalment`) FROM `loan_payment_track` WHERE `Pay_Date`<=CURRENT_DATE() AND `Pay_Status`='Not Paid';";
            rs = state.executeQuery(sql);
            while (rs.next()) {
                sum = rs.getInt("sum( `Instalment`)");
                lblToverdue.setText(String.valueOf(formatter.format(sum)));
            }

        } catch (SQLException ex) {
            Logger.getLogger(selectData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connectionString.conect != null) {
                try {
                    connectionString.conect.close();
                } catch (SQLException ignore) {
                }
            }
        }
        try {

            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql = "SELECT count(`Loan_Acct_No`) FROM `loan_applications` WHERE `Approved_Status`='Approved' and `Is_Active`=1;";
            rs = state.executeQuery(sql);
            while (rs.next()) {
                sum = rs.getInt("count(`Loan_Acct_No`)");
                lblBorrower.setText(String.valueOf(formatter.format(sum)));
            }

        } catch (SQLException ex) {
            Logger.getLogger(selectData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connectionString.conect != null) {
                try {
                    connectionString.conect.close();
                } catch (SQLException ignore) {
                }
            }
        }
        try {

            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql = "SELECT sum(`Instalment`) FROM `loan_payment_track` WHERE `Pay_Status`='Not Paid';";
            rs = state.executeQuery(sql);
            while (rs.next()) {
                sum = rs.getInt("sum(`Instalment`)");
                lblToutstanding.setText(String.valueOf(formatter.format(sum)));
            }

        } catch (SQLException ex) {
            Logger.getLogger(selectData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connectionString.conect != null) {
                try {
                    connectionString.conect.close();
                } catch (SQLException ignore) {
                }
            }
        }

        try {

            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql = "SELECT count(`Loan_Acct_No`) FROM `loan_applications` WHERE `Approved_Status`='pending' and `Is_Active`=1;";
            rs = state.executeQuery(sql);
            while (rs.next()) {
                sum = rs.getInt("count(`Loan_Acct_No`)");
                lblPending.setText(String.valueOf(formatter.format(sum)));
            }

        } catch (SQLException ex) {
            Logger.getLogger(selectData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connectionString.conect != null) {
                try {
                    connectionString.conect.close();
                } catch (SQLException ignore) {
                }
            }
        }
        try {

            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql = "SELECT count(`Loan_Acct_No`) FROM `loan_applications` WHERE"
                    + " `Approved_Status`='Approved'";// and `Is_Active`=1;"; //and `Date_Disbursed`=CURRENT_DATE()
            rs = state.executeQuery(sql);
            while (rs.next()) {
                sum = rs.getInt("count(`Loan_Acct_No`)");
                lblApproved.setText(String.valueOf(formatter.format(sum)));
            }

        } catch (SQLException ex) {
            Logger.getLogger(selectData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connectionString.conect != null) {
                try {
                    connectionString.conect.close();
                } catch (SQLException ignore) {
                }
            }
        }

        try {

            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql = "SELECT sum( `Total_Disbursed`) FROM `loan_applications` WHERE `Date_Disbursed`=CURRENT_DATE() and `Approved_Status`='Approved' and `Is_Active`=1;";
            rs = state.executeQuery(sql);
            while (rs.next()) {
                sum = rs.getInt("sum( `Total_Disbursed`)");
                lblTDisbursed.setText(String.valueOf(formatter.format(sum)));
            }

        } catch (SQLException ex) {
            Logger.getLogger(selectData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connectionString.conect != null) {
                try {
                    connectionString.conect.close();
                } catch (SQLException ignore) {
                }
            }
        }

    }

    public void selectMember(JTable tblMember, JComboBox comboCrdOfficerM, JComboBox comboMegrp,
            JTextField txtMobNo, JTextField txtTown, JTextField txtAddress, JTextField txtIdNo, JTextField txtname,
            JComboBox comboSex, JXDatePicker comboDob, JComboBox comboMarital, JTextField txtMemberNo, JTextField txtAdminFee,
            JLabel lblContainer, JLabel lblContainer2,JLabel lblItemCode) {
        lblContainer.setText("");
        try {
            DefaultTableModel model = (DefaultTableModel) tblMember.getModel();
            //DefaultTableModel model2 = (DefaultTableModel) tblMember.getModel();
            int rowCount = tblMember.getSelectedRow();
            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql = "SELECT `Serial_No`, `Group_Name`, `Formed_Date`, `Meeting_Day`, "
                    + "`Contact_Person`, `Contact_Number`, `Program_Linked`, `Is_Active`, `Credit_Officer` FROM `group_info` "
                    + " WHERE  Is_Active=1 AND Group_Name='" + model.getValueAt(rowCount, 9).toString() + "'";
            rs = state.executeQuery(sql);
            while (rs.next()) {
                comboCrdOfficerM.setSelectedItem(rs.getString("Credit_Officer"));
                comboMegrp.setSelectedItem(model.getValueAt(rowCount, 9).toString());
            }
            String sql2 = "SELECT `Serial_No`, `Member_Admin_No`, `Member_ID_No`, `Group_Name`,"
                    + " `Admin_Fee`, `Admin_Date`, `Is_Active` FROM `member_admission` WHERE "
                    + " Member_ID_No='" + model.getValueAt(rowCount, 2).toString() + "' AND "
                    + "Group_Name='" + model.getValueAt(rowCount, 9).toString() + "' AND `Is_Active`=1 ";
            rs = state.executeQuery(sql2);
            while (rs.next()) {

                txtMemberNo.setText(rs.getString("Member_Admin_No"));
                lblContainer.setText(rs.getString("Member_Admin_No"));
                txtAdminFee.setText(rs.getString("Admin_Fee"));
                lblItemCode.setText(rs.getString("Member_ID_No"));

                //  JOptionPane.showMessageDialog(null,lblContainer2.getText().trim());
            }
            Date date;// = new Date();
            try {
                date = format.parse(model.getValueAt(rowCount, 5).toString());
                txtname.setText(model.getValueAt(rowCount, 1).toString());
                txtIdNo.setText(model.getValueAt(rowCount, 2).toString());
                comboSex.setSelectedItem(model.getValueAt(rowCount, 3).toString());
                txtMobNo.setText(model.getValueAt(rowCount, 4).toString());
                comboDob.setDate(date);
                comboMarital.setSelectedItem(model.getValueAt(rowCount, 6).toString());
                txtAddress.setText(model.getValueAt(rowCount, 7).toString());
                txtTown.setText(model.getValueAt(rowCount, 8).toString());

                lblContainer2.setText(model.getValueAt(rowCount, 0).toString());

            } catch (ParseException ex) {
                Logger.getLogger(selectData.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (SQLException ex) {
            Logger.getLogger(selectData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connectionString.conect != null) {
                try {
                    connectionString.conect.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    public void selectClientForFee(JTextField txtfeeClientId, JComboBox comboGrp7, JComboBox comboMemberFill1) {
        try {

            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql = "SELECT `Serial_No`, `Member_Name`, `Member_ID_No`, `Member_Gender`, `Member_Phone`, `DOB`,"
                    + " `Marital_Status`, `Current_Address`, `Home_Town`, `Group_Name`, `Is_Active`, `Admin_Date`"
                    + " FROM `members_info` WHERE  Is_Active=1 AND `Member_Name`='" + comboMemberFill1.getSelectedItem() + "'"
                    + "and `Group_Name` ='" + comboGrp7.getSelectedItem() + "'";
            rs = state.executeQuery(sql);
            while (rs.next()) {
                txtfeeClientId.setText(rs.getString("Member_ID_No"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(selectData.class.getName()).log(Level.SEVERE, null, ex);
        } 
        try {
            connectionString.conect.close(); /*finally {
            
            }*/
            
        } catch (SQLException ex) {
            Logger.getLogger(selectData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void selectClientAdmin(JTextField txtMnumber, JComboBox comboGrp7, JComboBox comboMemberFill1) {
        try {

            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String sql = "SELECT `Serial_No`, `Member_Name`, `Member_ID_No`, `Member_Gender`, `Member_Phone`, `DOB`,"
                    + " `Marital_Status`, `Current_Address`, `Home_Town`, `Group_Name`, `Is_Active`, `Admin_Date`"
                    + " FROM `members_info` WHERE  Is_Active=1 AND `Member_Name`='" + comboMemberFill1.getSelectedItem() + "'"
                    + "and `Group_Name` ='" + comboGrp7.getSelectedItem() + "'";
            rs = state.executeQuery(sql);
            while (rs.next()) {
                txtMnumber.setText(rs.getString("Serial_No"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(selectData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connectionString.conect != null) {
                try {
                    connectionString.conect.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    public void selectcreditOfficer(JTable tblCreditOfficer, JTextField txtOfficername, JComboBox comboLinkUser,
            JXDatePicker Effectivedate,
            JCheckBox officerStatus, JLabel lblContainer) {
        try {
            DefaultTableModel model = (DefaultTableModel) tblCreditOfficer.getModel();
            int rowCount = tblCreditOfficer.getSelectedRow();

            lblContainer.setText(model.getValueAt(rowCount, 0).toString());
            Date effectiveDate = format.parse(model.getValueAt(rowCount, 3).toString());

            Effectivedate.setDate(effectiveDate);
            txtOfficername.setText(model.getValueAt(rowCount, 1).toString());
            comboLinkUser.setSelectedItem(model.getValueAt(rowCount, 2).toString());

            if ("1".equals(model.getValueAt(rowCount, 4).toString())) {
                officerStatus.isSelected();
            }
        } catch (ParseException ex) {
            Logger.getLogger(selectData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void selectgroup(JTable tblGroups, JComboBox comboCrdOff, JComboBox comboProgGrp, JComboBox comboMeetingDay,
            JTextField txtGroupName, JTextField txtContactPerson, JTextField txtContactNo, JXDatePicker formedDate,
            JCheckBox checkGrpActive, JLabel lblContainer) {
        DefaultTableModel model = (DefaultTableModel) tblGroups.getModel();
        int rowCount = tblGroups.getSelectedRow();
        txtGroupName.setText(model.getValueAt(rowCount, 1).toString());
        try {
            formedDate.setDate(format.parse(model.getValueAt(rowCount, 2).toString()));
        } catch (ParseException ex) {
            Logger.getLogger(selectData.class.getName()).log(Level.SEVERE, null, ex);
        }
        comboMeetingDay.setSelectedItem(model.getValueAt(rowCount, 3).toString());
        txtContactPerson.setText(model.getValueAt(rowCount, 4).toString());
        txtContactNo.setText(model.getValueAt(rowCount, 5).toString());
        comboProgGrp.setSelectedItem(model.getValueAt(rowCount, 6).toString());
        lblContainer.setText(model.getValueAt(rowCount, 0).toString());
        if ("1".equals(model.getValueAt(rowCount, 7).toString())) {
            checkGrpActive.isSelected();
        }

        comboCrdOff.setSelectedItem(model.getValueAt(rowCount, 8).toString());

    }

    public void selectLoanStatus(JTable tblLoansPendingApproval) {
        DefaultTableModel model = (DefaultTableModel) tblLoansPendingApproval.getModel();
        int selectedRowIndex = tblLoansPendingApproval.getSelectedRow();
        try {
            String loanStatus;

            connectionString.Connection2();
            state = connectionString.conect.createStatement();
            String selectLoan = "SELECT `Serial_No`, `Loan_Acct_No`, `Client_Name`, `KYC_ID`, `Principal_Disbursed`, `Interest_Disbursed`, "
                    + "`Total_Disbursed`, `Installment_Pay`, `Credit_Officer`, `Loan_Duration`, `Approved_Status`,"
                    + " `Group_Name`, `Program`, `Date_Disbursed`, `Process_Disbursed`, `Monthly_Income`,"
                    + " `Client_Scheme`, `Fund`, `Is_Active` FROM `loan_applications` "
                    + "where `Loan_Acct_No`='" + model.getValueAt(selectedRowIndex, 1).toString() + "'";
            rs = state.executeQuery(selectLoan);
            if (rs.next()) {
                loanStatus = rs.getString("Approved_Status");
                switch (loanStatus) {
                    case "Pending":
                        JOptionPane.showMessageDialog(null, "The Loan is Pending Approval, changes allowed before approval. ");
                        break;
                    case "Approved":
                        state = connectionString.conect.createStatement();
                        String checkPayment = "SELECT `Track_Number`, `ID_Number`, `Loan_Account_No`, `Cycle_Period`, `Pay_Date`, "
                                + "`Old_Balance`, `Instalment`, `Interest_Paid`, "
                                + "`Principal_Paid`, `New_Balance`, `Disburse_Date`, `Pay_Status` FROM `loan_payment_track`"
                                + " where `Loan_Account_No`='" + model.getValueAt(selectedRowIndex, 1).toString() + "' AND `Pay_Status`='Paid'";
                        rs = state.executeQuery(checkPayment);
                        if (rs.next()) {
                            JOptionPane.showMessageDialog(null, "The Loan have been approved and collection made, no update allowed");

                        } else {
                            JOptionPane.showMessageDialog(null, "The Loan have been approved, No collection Made, You can Update");
                        }
                        break;

                    case "Rejected":
                        JOptionPane.showMessageDialog(null, "The Loan was rejected make a new Application");
                        break;
                }

            }
            state.close();
        } catch (SQLException ex) {
            Logger.getLogger(selectData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connectionString.conect != null) {
                try {
                    connectionString.conect.close();
                } catch (SQLException ignore) {
                }
            }
        }

    }

      public void updateLoanAcctountInactive(String nid) {
        int balance=0;
        try {
            connectionString.Connection2();
            state = connectionString.conect.createStatement();
           /* String query = "SELECT  `loan_applications`.`Loan_Acct_No`, `loan_applications`.`Client_Name`, "
                    + "`loan_applications`.`KYC_ID`, `loan_applications`.`Principal_Disbursed`,"
                    + " `loan_applications`.`Interest_Disbursed`, `loan_applications`.`Total_Disbursed`, "
                    + "`loan_applications`.`Credit_Officer`, `loan_applications`.`Loan_Duration`, "
                    + " `loan_applications`.`Group_Name`, `loan_applications`.`Date_Disbursed`, "
                    + "`loan_applications`.`Is_Active`,sum( `loan_instalment_paytrack`.`Amount_Paid`) "
                    + "as 'AmountPaid',(`loan_applications`.`Total_Disbursed`)- "
                    + "sum( `loan_instalment_paytrack`.`Amount_Paid`) as 'Balance'  \n"
                    + "FROM `loan_applications`,`loan_instalment_paytrack` WHERE  "
                    + "`loan_instalment_paytrack`.`Loan_Acct_No`=`loan_applications`.`Loan_Acct_No`"
                    + " AND `loan_applications`.`KYC_ID`='" + nid + "' AND `loan_applications`.`Is_Active`=1 GROUP BY  `loan_applications`.`Loan_Acct_No` "
                    + " HAVING Balance <=0;";*/
            String findLoan = "SELECT   `loan_applications`.`Client_Name`,\n"
                    + "`loan_applications`. `KYC_ID`,\n"
                    + "`loan_applications`.`Group_Name`,\n"
                    + "`loan_applications`.`Program`,\n"
                    + "`loan_applications`.`Total_Disbursed`,\n"
                    + "`loan_applications`.`Credit_Officer`,\n"
                    + "`loan_applications`.`Loan_Duration`,\n"
                    + "`loan_instalment_paytrack`.`Loan_Acct_No`,\n"
                    + "SUM( `loan_instalment_paytrack`.`Amount_Paid`)AS 'Total_Paid',"
                    + "( `loan_applications`.`Total_Disbursed`-SUM( `loan_instalment_paytrack`.`Amount_Paid`)) "
                    + "AS 'Outstanding Amount'\n"
                    + "FROM `loan_applications`, `loan_instalment_paytrack` \n"
                    + "WHERE `loan_instalment_paytrack`.`Loan_Acct_No`=`loan_applications`.`Loan_Acct_No` "
                    + "AND `loan_applications`.`KYC_ID`='" + nid + "' GROUP BY `Loan_Acct_No`  \n"
                    + "ORDER BY `loan_applications`.`Group_Name` ASC;";
            rs = state.executeQuery(findLoan);
            while (rs.next()) {
                balance = rs.getInt("Outstanding Amount");
                String loanAcct = rs.getString("Loan_Acct_No"); 
                if (balance <= 0) {
                    String Update = "UPDATE `loan_applications` SET `Is_Active`=0 where `Loan_Acct_No`="
                            + "'" + loanAcct + "' AND `loan_applications`.`KYC_ID`='" + nid + "'";
                    state.executeUpdate(Update); 
                } else {
                    JOptionPane.showMessageDialog(null, "Client has a Loan with Balance More than 0");
                }
            }

        } catch (SQLException ex) {
           //JOptionPane.showMessageDialog(null, ex);
        } 

    }

//    public void updateLoanAcctountInactiveOnloand() {
//        int balance;
//        try {
//            connectionString.Connection2();
//            state = connectionString.conect.createStatement();
//            String query = "SELECT  `loan_applications`.`Loan_Acct_No`, `loan_applications`.`Client_Name`, "
//                    + "`loan_applications`.`KYC_ID`, `loan_applications`.`Principal_Disbursed`, "
//                    + "`loan_applications`.`Interest_Disbursed`, `loan_applications`.`Total_Disbursed`,"
//                    + " `loan_applications`.`Credit_Officer`, `loan_applications`.`Loan_Duration`, "
//                    + " `loan_applications`.`Group_Name`, `loan_applications`.`Date_Disbursed`,"
//                    + " `loan_applications`.`Is_Active`,sum( `loan_instalment_paytrack`.`Amount_Paid`)"
//                    + " as 'AmountPaid',(`loan_applications`.`Total_Disbursed`)- sum( `loan_instalment_paytrack`.`Amount_Paid`)"
//                    + " as 'Balance'  \n"
//                    + "FROM `loan_applications`,`loan_instalment_paytrack` WHERE  "
//                    + "`loan_instalment_paytrack`.`Loan_Acct_No`=`loan_applications`.`Loan_Acct_No`"
//                    + "  GROUP BY  `loan_applications`.`Loan_Acct_No`  HAVING Balance <= 0;";
//            rs = state.executeQuery(query);
//            // int count = -1;
//            while (rs.next()) {
//                //++count;
//                balance = rs.getInt("Balance");
//                connectionString.Connection2();
//                state = connectionString.conect.createStatement();
//               // JOptionPane.showMessageDialog(null, "Count " + count + " " + rs.getString("loan_applications.Client_Name") + "   " + rs.getString("loan_applications.KYC_ID"));
//
//                String Update = "UPDATE `loan_applications` SET `Is_Active`=0 where `Loan_Acct_No`="
//                        + "'" + rs.getString("loan_applications.Loan_Acct_No") + "' AND `KYC_ID`='" + rs.getString("loan_applications.KYC_ID") + "'";
//                state.executeUpdate(Update);
//            }
//
//            /* do {
//                balance = rs.getInt("Balance");
//                JOptionPane.showMessageDialog(null, rs.getString("loan_applications.Client_Name") + "   " + rs.getString("loan_applications.KYC_ID"));
//                String Update = "UPDATE `loan_applications` SET `Is_Active`=0 where `Loan_Acct_No`="
//                        + "'" + rs.getString("loan_applications.Loan_Acct_No") + "' AND `KYC_ID`='" + rs.getString("loan_applications.KYC_ID") + "'";
//                state.executeUpdate(Update);
//
//               // ++count;
//
//            } while (rs.next());*/
//            state.close();
//            connectionString.conect.close();
//
//        } catch (SQLException ex) {
//           // JOptionPane.showMessageDialog(null, ex);
//
//        } 
//
//    }

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
    }
}
