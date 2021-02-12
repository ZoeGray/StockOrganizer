/**
 * Frame that prompts the user to insert a sale transaction (Sell shares)
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
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 */
public class SaleInsertFrame extends JFrame implements ActionListener {

    private final JPanel buttonPanel;
    private final JPanel tickerPanel;
    private final JPanel datePanel;
    private final JPanel quantityPanel;
    private final JPanel salesPricePanel;
    private final JPanel promptPanel;
    
    private final JButton closeButton;
    private final JButton insertButton;
    
    private final JLabel titleLabel;
    private final JLabel tickerLabel;
    private final JLabel dateLabel;
    private final JLabel quantityLabel;
    private final JLabel salesPriceLabel;
    
    private final JTextField dateYearTf;
    private final JTextField dateDayTf;
    private final JTextField quantityTf;
    private final JTextField salesPriceTf;
    
    private final JComboBox monthBox;
    private final JComboBox tickerBox;
    
    private final Font titleFont;
    private final Font promptFont;
    private final Font buttonFont;
    
    private final Color GREEN_BACKGROUND;
    
    private final Dimension buttonSize;
    private final String[] MONTHS = {"January","February","March","April","May",
        "June","July","August","September","October","November","December"};
    
    String[] tickerList;
    
    public SaleInsertFrame() {
        super("Insert Sale Table");
        
        // Setting Color
        GREEN_BACKGROUND = new Color(30,142,84);
        
        // SETTING JFrame components
        this.setBounds(300,150,800,500);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setBackground(GREEN_BACKGROUND);
        
        buttonPanel = new JPanel(new FlowLayout());
        tickerPanel = new JPanel(new FlowLayout());
        datePanel = new JPanel(new FlowLayout());
        quantityPanel = new JPanel(new FlowLayout());
        salesPricePanel = new JPanel(new FlowLayout());
        promptPanel = new JPanel(); // Box layout
        
        // Setting combobox labels
        monthBox = new JComboBox(MONTHS);
        monthBox.setSelectedIndex(0);
        
        TableFill fill = new TableFill();
        tickerList = fill.getTickerList();
        tickerBox = new JComboBox(tickerList);
        
        // Setting button text
        closeButton = new JButton("Close");
        insertButton = new JButton("Insert");
        
        // Setting label text
        titleLabel = new JLabel("<html>Insert into Sale Transactions</html>");
        tickerLabel = new JLabel("Stock Ticker: ");
        dateLabel = new JLabel("Date of Sale: ");
        quantityLabel = new JLabel("Quantity Sold: ");
        salesPriceLabel = new JLabel("Sales Price per Share: ");
        
        dateYearTf = new JTextField(4);
        dateDayTf = new JTextField(2);
        quantityTf = new JTextField(7);
        salesPriceTf = new JTextField(7);
        
        // Setting font details
        titleFont = new Font("Arial", Font.BOLD, 30);
        promptFont = new Font("Arial", Font.PLAIN, 20);
        buttonFont = new Font("Arial", Font.BOLD, 20);
        
        // Setting button dimensions
        buttonSize = new Dimension(200,70);
        
        promptPanel.setLayout(new BoxLayout(promptPanel, BoxLayout.Y_AXIS));
        
        buttonPanel.setBackground(GREEN_BACKGROUND);
        tickerPanel.setBackground(GREEN_BACKGROUND);
        datePanel.setBackground(GREEN_BACKGROUND);
        quantityPanel.setBackground(GREEN_BACKGROUND);
        salesPricePanel.setBackground(GREEN_BACKGROUND);
        promptPanel.setBackground(GREEN_BACKGROUND);
        
        // Setting label fonts
        titleLabel.setFont(titleFont);
        tickerLabel.setFont(promptFont);
        dateLabel.setFont(promptFont);
        quantityLabel.setFont(promptFont);
        salesPriceLabel.setFont(promptFont);
        
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
        tickerBox.setFont(promptFont);
        dateYearTf.setFont(promptFont);
        dateDayTf.setFont(promptFont);
        quantityTf.setFont(promptFont);
        salesPriceTf.setFont(promptFont);
        
        // Adding action listener to buttons
        closeButton.addActionListener(this);
        insertButton.addActionListener(this);
        monthBox.addActionListener(this);
        tickerBox.addActionListener(this);
        
        buttonPanel.add(closeButton);
        buttonPanel.add(insertButton);
        
        tickerPanel.add(tickerLabel);
        tickerPanel.add(tickerBox);
        
        datePanel.add(dateLabel);
        datePanel.add(monthBox);
        datePanel.add(dateDayTf);
        datePanel.add(dateYearTf);
        
        quantityPanel.add(quantityLabel);
        quantityPanel.add(quantityTf);
        
        salesPricePanel.add(salesPriceLabel);
        salesPricePanel.add(salesPriceTf);
        
        promptPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        promptPanel.add(tickerPanel);
        promptPanel.add(datePanel);
        promptPanel.add(quantityPanel);
        promptPanel.add(salesPricePanel);
        
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
        String ticker, quantity, salesPrice;
        boolean robust = true, safe = true;
        
        if (command.equals("Insert")) {
            robust = true;
            safe = true;
            
            year = dateYearTf.getText();
            month = (String) monthBox.getSelectedItem();
            day = dateDayTf.getText();
            
            robust = rob.validDay(day);
            safe = (robust && safe);
            robust = rob.validYear(year);
            safe = (robust && safe);
            robust = rob.strToDub((String) quantityTf.getText());
            safe = (robust && safe);
            robust = rob.strToDub((String) salesPriceTf.getText());
            safe = (robust && safe);
            robust = (!tickerBox.getSelectedItem().equals("-Empty-"));
            safe = (robust && safe);
            
            // testing if code works
            if (safe) {
                ticker = (String) tickerBox.getSelectedItem();
                date = fill.createDateString(year, month, day);
                quantity = (String) quantityTf.getText();
                salesPrice = (String) salesPriceTf.getText();
                
                robust = rob.simNewSale(ticker, date, quantity, salesPrice);
            }
            
            safe = (robust && safe);
            
            if (safe) {
                
                userInfo.add(tickerBox.getSelectedItem());
                userInfo.add(date);
                userInfo.add(quantityTf.getText());
                userInfo.add(salesPriceTf.getText());

                edi.insertSale(userInfo);

                dateDayTf.setText("");
                dateYearTf.setText("");
                quantityTf.setText("");
                salesPriceTf.setText("");
                
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
                rob = new RobustChecker("Error! Data could not be inserted");
                ErrorDisplay error = new ErrorDisplay(rob);
            }
        }
        if (command.equals("Close")) {
            this.dispose();
        }
    }
}
