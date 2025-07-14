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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JRViewer;

/**
 *
 * @author Ayoma
 */
public class reporting {
    
    
    DBsConnection connectionString = new DBsConnection();
    Connection conn;
    Statement state;
    ResultSet rs;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    LocalDate dt=LocalDate.now();
    comboFill fillCr = new comboFill();
    
    public void generateReport(JPanel panelLoadingReport, String query, String File) throws JRException
    {
         connectionString.Connection2();
       
         String bsName = null, addres = null, town = null, email = null, slogan = null;
        try {

            String sql1 = "SELECT `Bs_Name`, `Address`, `Town`, `Email`, `Slogan` FROM `shop_info`;";
            state = connectionString.conect.createStatement();
            rs = state.executeQuery(sql1);

            while (rs.next()) {

                bsName = rs.getString("Bs_Name");
                addres = rs.getString("Address");
                town = rs.getString("Town");
                email = rs.getString("Email");
                slogan = rs.getString("Slogan");
            }

        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, "There was error,Receipt may not be printed well");
        }
        Map params = new HashMap();
        params.put("bsName", bsName);
        params.put("addres", addres);
        params.put("town", town);
        params.put("email", email);
        params.put("slogan", slogan);
        JRViewer viewer;
        panelLoadingReport.removeAll();
        JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("/reports/"+File));
        JRDesignQuery newQuery = new JRDesignQuery();
        newQuery.setText(query);
        jasperDesign.setQuery(newQuery);
        JasperReport jsReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jsPrint = JasperFillManager.fillReport(jsReport, params, connectionString.conect);
        viewer = new net.sf.jasperreports.view.JRViewer(jsPrint);
        viewer.setOpaque(true);
        panelLoadingReport.add(viewer);
        panelLoadingReport.revalidate();
        
    }
    
    
    
    
    
    
}
