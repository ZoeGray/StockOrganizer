/**
 * Frame that prompts the user to make a purchase into the purchase transaction database
 */
package stockorganizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 */
public class PurchaseInsertFrame extends JFrame implements ActionListener {

    private final JPanel buttonPanel;
    private final JPanel namePanel;
    private final JPanel tickerPanel;
    private final JPanel datePanel;
    private final JPanel quantityPanel;
    private final JPanel costBasisPanel;
    private final JPanel promptPanel;
    
    
    private final JButton closeButton;
    private final JButton insertButton;
    
    private final JLabel titleLabel;
    private final JLabel nameLabel;
    private final JLabel tickerLabel;
    private final JLabel dateLabel;
    private final JLabel quantityLabel;
    private final JLabel costBasisLabel;
    
    private final JTextField nameTf;
    private final JTextField tickerTf;
    private final JTextField dateYearTf;
    private final JTextField dateDayTf;
    private final JTextField quantityTf;
    private final JTextField costBasisTf;
    
    private final JComboBox monthBox;
    
    private final Font titleFont;
    private final Font promptFont;
    private final Font buttonFont;
    
    private final Color GREEN_BACKGROUND;
    
    private final Dimension buttonSize;
    private final String[] MONTHS = {"January","February","March","April","May",
        "June","July","August","September","October","November","December"};
    
    public PurchaseInsertFrame() {
        super("Insert Purchase Table");
        
        // Setting Color
        GREEN_BACKGROUND = new Color(30,142,84);
        
        // SETTING JFrame components
        this.setBounds(300,150,800,500);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setBackground(GREEN_BACKGROUND);
        
        buttonPanel = new JPanel(new FlowLayout());
        namePanel = new JPanel(new FlowLayout());
        tickerPanel = new JPanel(new FlowLayout());
        datePanel = new JPanel(new FlowLayout());
        quantityPanel = new JPanel(new FlowLayout());
        costBasisPanel = new JPanel(new FlowLayout());
        promptPanel = new JPanel(); // Box layout
        
        // Setting combobox labels
        monthBox = new JComboBox(MONTHS);
        monthBox.setSelectedIndex(0);
        
        // Setting button text
        closeButton = new JButton("Close");
        insertButton = new JButton("Insert");
        
        // Setting label text
        titleLabel = new JLabel("<html>Insert into Purchase Transactions</html>");
        nameLabel = new JLabel("Stock Name: ");
        tickerLabel = new JLabel("Stock Ticker: ");
        dateLabel = new JLabel("Date of Purchase: ");
        quantityLabel = new JLabel("Quantity Purchased: ");
        costBasisLabel = new JLabel("Cost Basis per Share: ");
        
        nameTf = new JTextField(20);
        tickerTf = new JTextField(7);
        dateYearTf = new JTextField(4);
        dateDayTf = new JTextField(2);
        quantityTf = new JTextField(7);
        costBasisTf = new JTextField(7);
        
        // Setting font details
        titleFont = new Font("Arial", Font.BOLD, 30);
        promptFont = new Font("Arial", Font.PLAIN, 20);
        buttonFont = new Font("Arial", Font.BOLD, 20);
        
        // Setting button dimensions
        buttonSize = new Dimension(200,70);
        
        promptPanel.setLayout(new BoxLayout(promptPanel, BoxLayout.Y_AXIS));
        
        buttonPanel.setBackground(GREEN_BACKGROUND);
        namePanel.setBackground(GREEN_BACKGROUND);
        tickerPanel.setBackground(GREEN_BACKGROUND);
        datePanel.setBackground(GREEN_BACKGROUND);
        quantityPanel.setBackground(GREEN_BACKGROUND);
        costBasisPanel.setBackground(GREEN_BACKGROUND);
        promptPanel.setBackground(GREEN_BACKGROUND);
        
        // Setting label fonts
        titleLabel.setFont(titleFont);
        nameLabel.setFont(promptFont);
        tickerLabel.setFont(promptFont);
        dateLabel.setFont(promptFont);
        quantityLabel.setFont(promptFont);
        costBasisLabel.setFont(promptFont);
        
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Setting button fonts
        closeButton.setFont(buttonFont);
        insertButton.setFont(buttonFont);
        
        // Setting button size
        closeButton.setPreferredSize(buttonSize);
        insertButton.setPreferredSize(buttonSize);
        
        // Setting button text positions
        closeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        insertButton.setHorizontalTextPosition(SwingConstants.CENTER);
        
        monthBox.setFont(promptFont);
        nameTf.setFont(promptFont);
        tickerTf.setFont(promptFont);
        dateYearTf.setFont(promptFont);
        dateDayTf.setFont(promptFont);
        quantityTf.setFont(promptFont);
        costBasisTf.setFont(promptFont);
        
        // Adding action listener to buttons
        closeButton.addActionListener(this);
        insertButton.addActionListener(this);
        monthBox.addActionListener(this);
        
        buttonPanel.add(closeButton);
        buttonPanel.add(insertButton);
        
        namePanel.add(nameLabel);
        namePanel.add(nameTf);
        
        tickerPanel.add(tickerLabel);
        tickerPanel.add(tickerTf);
        
        datePanel.add(dateLabel);
        datePanel.add(monthBox);
        datePanel.add(dateDayTf);
        datePanel.add(dateYearTf);
        
        quantityPanel.add(quantityLabel);
        quantityPanel.add(quantityTf);
        
        costBasisPanel.add(costBasisLabel);
        costBasisPanel.add(costBasisTf);
        
        promptPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        promptPanel.add(namePanel);
        promptPanel.add(tickerPanel);
        promptPanel.add(datePanel);
        promptPanel.add(quantityPanel);
        promptPanel.add(costBasisPanel);
        
        this.add(titleLabel, BorderLayout.NORTH);
        this.add(promptPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        
        this.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        EditDataDb edi = new EditDataDb();
        TableFill fill = new TableFill();
        RobustChecker rob = new RobustChecker();
        
        ArrayList userInfo = new ArrayList();
        String year, month, day, date = "0000-00-00";
        boolean robust = true, safe = true;
        
        if (command.equals("Insert")) {
            robust = true;
            safe = true;
            
            year = dateYearTf.getText();
            month = (String) monthBox.getSelectedItem();
            day = dateDayTf.getText();
            
            robust = rob.strToInt(year); // Checks input for correctness
            safe = (robust && safe); // Only true if both are true
            robust = rob.strToInt(day);
            safe = (robust && safe);
            robust = rob.validDay(day);
            safe = (robust && safe);
            robust = rob.validYear(year);
            safe = (robust && safe);
            robust = rob.strToDub((String) quantityTf.getText());
            safe = (robust && safe);
            robust = rob.strToDub((String) costBasisTf.getText());
            safe = (robust && safe);
            
            if (safe) {
                date = fill.createDateString(year, month, day);
                
                userInfo.add(nameTf.getText());
                userInfo.add(tickerTf.getText());
                userInfo.add(date);
                userInfo.add(quantityTf.getText());
                userInfo.add(costBasisTf.getText());

                edi.insertPurchase(userInfo);

                nameTf.setText("");
                tickerTf.setText("");
                dateDayTf.setText("");
                dateYearTf.setText("");
                quantityTf.setText("");
                costBasisTf.setText("");
                             
                RunningHoldingsTableFrame rhFrame = HomeFrame.getRunningHoldingFrame();
                if (rhFrame != null) {
                    rhFrame.refreshDataTable();
                }
                PurchaseTableFrame pFrame = HomeFrame.getPurchaseFrame();
                if (pFrame != null) {
                    pFrame.refreshDataTable();
                }
                SaleTableFrame sFrame = HomeFrame.getSaleFrame();
                if (sFrame != null) {
                    sFrame.refreshDataTable();
                }
                DividendTableFrame dFrame = HomeFrame.getDividendFrame();
                if (dFrame != null) {
                    dFrame.refreshDataTable();
                }
                
            } else {
                ErrorDisplay error = new ErrorDisplay(rob);
            }
        }
        if (command.equals("Close")) {
            this.dispose();
        }
        
    }
}