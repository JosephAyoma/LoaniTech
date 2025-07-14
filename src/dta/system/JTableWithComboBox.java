/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dta.system;

/**
 *
 * @author Ayoma
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JTableWithComboBox {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(JTableWithComboBox::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        // Frame setup
        JFrame frame = new JFrame("JTable with ComboBox Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        
        // Create table model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Item");
        model.addColumn("Category");

        // Create JTable
        JTable table = new JTable(model);

        // Create ComboBox for drop-down
        String[] categories = {"Category 1", "Category 2", "Category 3"};
        JComboBox<String> comboBox = new JComboBox<>(categories);
        
        // Set combo box as editor for the Category column (column index 1)
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));
        
        // Scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Create a button to add a row dynamically
        JButton addButton = new JButton("Add Row");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add a new row with a default value for Item and a combo box for Category
                model.addRow(new Object[]{"", categories[0]});
            }
        });

        // Layout setup
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(addButton, BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}