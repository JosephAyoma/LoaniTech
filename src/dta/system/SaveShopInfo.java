/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dta.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Dockman
 */
public class SaveShopInfo {

    DBsConnection DBclass = new DBsConnection();
    // DBsConnection connectionString = new DBsConnection();
    dbInsert save = new dbInsert();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    PreparedStatement pstmt = null;
    DefaultTableModel model;
    Connection conn = null;

    public boolean checkshop(JLabel lblItemCode) {
        //check if the product or item is already saved in the DB
        boolean itemIsFound = false;

        try {
            //connectionString.Connection2();
            DBclass.connecttoDB();
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
                //connectionString.Connection2();
                DBclass.connecttoDB();
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
                JOptionPane.showMessageDialog(null, "Information Updated Successfully");
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
                //connectionString.Connection2();
                DBclass.connecttoDB();
                String payaccounts = "INSERT INTO `shop_info`(`Bs_Name`, `Address`, `Town`, `Email`, `Slogan`)"
                        + "VALUES('" + txtShopName2.getText().trim() + "','" + txtShopLocation2.getText().trim() + "',"
                        + "'" + txtShopAddress2.getText().trim() + "','" + txtShopEmail2.getText().trim() + "','" + txtShopSlogan2.getText().trim() + "')";
                pstmt = DBclass.conect.prepareStatement(payaccounts);
                pstmt.executeUpdate(payaccounts);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "This error......" + ex);

            }
        }
        //LOAD THE DATA AT THE END OF SAVING AND UPDATING
        String selectItems = "SELECT `Serial_No`, `Bs_Name`, `Address`, `Town`, `Email`, `Slogan` FROM `shop_info` ";
        save.output(tblShopIn, selectItems);

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
}
