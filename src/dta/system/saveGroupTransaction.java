/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dta.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author Ayoma
 */
public class saveGroupTransaction {

    DBsConnection connectionString = new DBsConnection();
    DBsConnection DBclass = new DBsConnection();
    Connection conn;
    Statement state;
    PreparedStatement pstmt = null;
    ResultSet rs;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    LocalDate dt = LocalDate.now();
    comboFill fillCr = new comboFill();
    DefaultTableModel model;

    public void saveLoanTransaction(JComboBox comboSecProcess, JXDatePicker paidDate, JTable tblGrpSheet) {
        int rowCount = tblGrpSheet.getRowCount();
        connectionString.Connection2();
        
        for (int i = 0; i < rowCount; i++) {
            if (tblGrpSheet.getValueAt(i, 6).equals(0)) {
                
            } else {
                String txtAccNo = tblGrpSheet.getValueAt(i, 0).toString();
                String txtpayDate = tblGrpSheet.getValueAt(i, 8).toString();
                float Instalment_Balance = 0;
                float outStandBa = Float.parseFloat(tblGrpSheet.getValueAt(i, 5).toString());
                float advanceAmount = 0;
                float IntalmentTopay = Float.parseFloat(tblGrpSheet.getValueAt(i, 4).toString());
                float amountCollected = Float.parseFloat(tblGrpSheet.getValueAt(i, 6).toString());
                int txtTrackNo = Integer.parseInt(tblGrpSheet.getValueAt(i, 7).toString());

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
                                    + "('" + txtAccNo + "',"
                                    + "'" + txtTrackNo+ "',"
                                    + "'" + outStandBa + "',"
                                    + "'" + IntalmentTopay + "',"
                                    + "'" + Instalment_Balance + "',"
                                    + "'" + txtpayDate+ "',"
                                    + "" + amountCollected + ","
                                    + "'" + format.format(paidDate.getDate()) + "',"
                                    + "" + advanceAmount + ","
                                    + "'" + comboSecProcess.getSelectedItem().toString() + "');";
                            state.addBatch(Insertuser);

                            String UpdatePayStatus = "UPDATE `loan_payment_track` SET `Pay_Status`='Paid' where `Track_Number`=" + txtTrackNo + " AND "
                                    + "`Loan_Account_No`='" + txtAccNo + "'";
                            state.addBatch(UpdatePayStatus);

                            do {

                                try {
                                    String getNextInstallment = "SELECT `Track_Number`, `ID_Number`, `Loan_Account_No`, `Cycle_Period`, `Pay_Date`, `Old_Balance`, "
                                            + "`Instalment`, `Interest_Paid`, `Principal_Paid`, `New_Balance`, `Disburse_Date`, `Pay_Status`"
                                            + " FROM `loan_payment_track` where `Track_Number`=" + txtTrackNo + " AND  "
                                            + "`Loan_Account_No`='" + txtAccNo + "'";
                                    rs = state.executeQuery(getNextInstallment);

                                    while (rs.next()) {
                                        outStandBa = rs.getInt("Old_Balance");
                                        txtTrackNo=rs.getInt("Track_Number");
                                        txtAccNo = rs.getString("Loan_Account_No");
                                        IntalmentTopay = rs.getFloat("Instalment");
                                        txtpayDate=String.valueOf(rs.getDate("Pay_Date"));
                                        amountCollected = advanceAmount;

                                        IntalmentTopay = rs.getFloat("Instalment");
                                        txtTrackNo = rs.getInt("Track_Number");

                                        amountCollected = advanceAmount;
                                        advanceAmount = amountCollected - IntalmentTopay;
                                        outStandBa = outStandBa - advanceAmount;

                                    }

                                    String UpdateNextOldBalanc = "UPDATE `loan_payment_track` SET `Pay_Status`='Paid', `Old_Balance`='" + outStandBa + "' where "
                                            + "`Track_Number`=" + (txtTrackNo + 1) + " AND  `Loan_Account_No`='" + txtAccNo + "'";
                                    state.addBatch(UpdateNextOldBalanc);
                                } catch (SQLException ex) {
                                    JOptionPane.showMessageDialog(null, "Some error " + ex);

                                }
                                if (amountCollected < IntalmentTopay) {

                                    txtTrackNo = txtTrackNo + 1;

                                    String nextData = "SELECT `Track_Number`, `ID_Number`, `Loan_Account_No`, `Cycle_Period`, `Pay_Date`, `Old_Balance`, "
                                            + "`Instalment`, `Interest_Paid`, `Principal_Paid`, `New_Balance`, `Disburse_Date`, `Pay_Status`"
                                            + " FROM `loan_payment_track` where `Track_Number`=" + txtTrackNo + " AND  "
                                            + "`Loan_Account_No`='" + txtAccNo + "'";
                                    rs = state.executeQuery(nextData);
                                    while (rs.next()) {
                                        outStandBa = rs.getFloat("Old_Balance");
                                        IntalmentTopay = rs.getFloat("Instalment");

                                        outStandBa = outStandBa - amountCollected;
                                        Instalment_Balance = IntalmentTopay - amountCollected;
                                    }

                                    String UpdateNextOldBalanc = "UPDATE `loan_payment_track` SET `Old_Balance`='" + outStandBa + "',`Pay_Status`='Not Paid',`Instalment`= '" + Instalment_Balance + "'where "
                                            + "`Track_Number`=" + txtTrackNo + " AND  `Loan_Account_No`='" + txtAccNo + "'";
                                    state.addBatch(UpdateNextOldBalanc);

                                    JOptionPane.showMessageDialog(null, "Track number" + txtTrackNo + "Advance Amount is less than installment" + Instalment_Balance);

                                }
                                txtTrackNo++;

                            } while (amountCollected >= IntalmentTopay);

                            state.executeBatch();
                            JOptionPane.showMessageDialog(null, "Installment Saved");
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex);

                        }
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
                                + "('" + txtAccNo + "',"
                                + "'" + txtTrackNo + "',"
                                + "'" + outStandBa + "',"
                                + "'" + IntalmentTopay + "',"
                                + "'" + Instalment_Balance + "',"
                                + "'" + txtpayDate + "',"
                                + "" + amountCollected + ","
                                + "'" + format.format(paidDate.getDate()) + "',"
                                + "0,"
                                + "'" + comboSecProcess.getSelectedItem().toString() + "');";
                        state.addBatch(Insertuser);

                        String UpdatePayStatus = "UPDATE `loan_payment_track` SET `Pay_Status`='Not Paid', `Instalment`='" + Instalment_Balance + "',"
                                + "`Old_Balance`='" + outStandBa + "' where `Track_Number`=" + txtTrackNo + " AND "
                                + "`Loan_Account_No`='" + txtAccNo + "'";
                        state.addBatch(UpdatePayStatus);

                        state.executeBatch();
                        JOptionPane.showMessageDialog(null, "Installment Saved");
                        //update the status paid and update the oustanding
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex);
                    } 
                    //update the oustanding balance for the current track number, no updating the status paid

                } else {
                    outStandBa = outStandBa - amountCollected;
                    try {
                        state = connectionString.conect.createStatement();
                        String Insertuser = "INSERT INTO `loan_instalment_paytrack`(`Loan_Acct_No`, `Track_No`, "
                                + "`OutStanding_Amount`, `Instalment`, `Instalment_Balance`, `Pay_Date`, `Amount_Paid`, "
                                + "`Date_Paid`, `Advance_Amount`, `Pay_Process`)"
                                + "Values"
                                + "('" + txtAccNo + "',"
                                + "'" + txtTrackNo + "',"
                                + "'" + outStandBa + "',"
                                + "'" + IntalmentTopay + "',"
                                + "0,"
                                + "'" + txtpayDate + "',"
                                + "" + amountCollected + ","
                                + "'" + format.format(paidDate.getDate()) + "',"
                                + "0,"
                                + "'" + comboSecProcess.getSelectedItem().toString() + "');";
                        state.addBatch(Insertuser);

                        String UpdatePayStatus = "UPDATE `loan_payment_track` SET `Pay_Status`='Paid' where `Track_Number`=" + txtTrackNo + " AND "
                                + "`Loan_Account_No`='" + txtAccNo + "'";
                        state.addBatch(UpdatePayStatus);

                        String UpdateNextOldBalance = "UPDATE `loan_payment_track` SET `Old_Balance`='" + outStandBa + "' where "
                                + "`Track_Number`=" + (txtTrackNo + 1) + " AND  `Loan_Account_No`='" + txtAccNo + "'";
                        state.addBatch(UpdateNextOldBalance);

                        state.executeBatch();
                        JOptionPane.showMessageDialog(null, "Installment Saved");
                        //update the status paid and update the oustanding
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex);

                    } 

                }
            }
        }
    }
}
