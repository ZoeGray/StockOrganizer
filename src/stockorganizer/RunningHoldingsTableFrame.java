/**
 * RunningHoldingsTableFrame displays the table for user's current stocks
 * in their portfolio. Includes buttons linking to other tables and for
 * inserting/deleting entries
 */
package stockorganizer;

// Imports for GUI
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableModel;


/**
 * Creates frame. Database actions on the left, traversing table options on the
 * right. Options for resetting and help buttons on the top. Table and title
 * are displayed in the middle.
 */
public class RunningHoldingsTableFrame extends JFrame implements ActionListener {

    private final JPanel controlPanel;
    private final JPanel dataPanel;
    private final JPanel actionPanel;
    private final JPanel displayPanel;

    private final JButton resetButton;
    private final JButton helpButton;
    private final JButton buyButton;
    private final JButton sellButton;
    private final JButton deletePurchaseButton;
    private final JButton deleteSaleButton;
    private final JButton dividendButton;
    private final JButton marketValueButton;
    private final JButton saleTableButton;
    private final JButton runningHoldingButton;
    private final JButton purchaseTableButton;
    private final JButton dividendTableButton;
    private final JButton annualizedRetButton;

    private final JLabel tableNameLabel;
    
    private final Font controlFont;
    private final Font actionFont;
    private final Font displayFont;
    private final Font titleFont;
    
    private final Color GREEN_BACKGROUND;

    private final Dimension actionSize;
    private final Dimension displaySize;

    // Table storing current running holdings for every stock in portfolio
    private final JTableHeader tableHeader;
    private Object[][] data;
    private DefaultTableModel dataModel;
    private final String[] tableHeaders = {"Stock Ticker","Date of First Purchase",
        "Quantity of Shares Held","Average Cost per Share",
        "Current Market Value","Unrealized Gain or Loss"};
    private final JScrollPane scrollPane;
    private JTable holdingsTable; 
    
    private LinkDb link;
    
    /**
     * Constructor initializing components of JFrame
     */
    public RunningHoldingsTableFrame() {
        super("Running Holdings Table Frame"); 
        
        link = new LinkDb(InfoDb.DB_NAME);

        // SETTING JFrame components
        this.setBounds(0, 0, 1440, 900); // Sets frame to width of entire screen !!!
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout()); //

        // Setting panel layout
        controlPanel = new JPanel(new BorderLayout()); 
        dataPanel = new JPanel(new BorderLayout());
        actionPanel = new JPanel(); // Box layout
        displayPanel = new JPanel(); // Box layout

        // Setting button text
        resetButton = new JButton("RESET");
        helpButton = new JButton("?");
        buyButton = new JButton("<html>Purchase Shares</html>");
        sellButton = new JButton("<html>Sell Shares</html>");
        dividendButton = new JButton("<html>Manage Dividends</html>");
        deletePurchaseButton = new JButton("<html>Delete Purchase</html>");
        deleteSaleButton = new JButton("<html>Delete Sale</html>");
        marketValueButton = new JButton("<html>Update Market Values</html>");
        runningHoldingButton = new JButton("<html>RUNNING HOLDINGS TABLE</html>");
        dividendTableButton = new JButton("<html>DIVIDEND HOLDINGS TABLE</html>");
        purchaseTableButton = new JButton("<html>PURCHASE TRANSACTION TABLE</html>");
        saleTableButton = new JButton("<html>SALE TRANSACTION TABLE</html>");
        annualizedRetButton = new JButton("<html>Calculate Annualized Return</html>");

        // Setting label text
        tableNameLabel = new JLabel("<html>Running Stock Holdings Table</html>", SwingConstants.CENTER);

        // Setting font characteristics
        titleFont = new Font("Arial", Font.BOLD, 40);
        controlFont = new Font("Arial", Font.BOLD, 20);
        actionFont = new Font("Arial", Font.BOLD, 18);
        displayFont = new Font("Arial", Font.BOLD, 18);

        // Setting button sizes
        actionSize = new Dimension(250, 200);
        displaySize = new Dimension(250, 250);
        
        // Setting color
        GREEN_BACKGROUND = new Color(30,142,84);

        // Getting data from database
        data = link.getDbData(InfoDb.RUNNING_TABLE, InfoDb.RUNNING_HEADER);
        dataModel = new DefaultTableModel(data, tableHeaders);

        // Putting data into JTable
        holdingsTable = new JTable(dataModel);

        // Setting panel layout
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        
        actionPanel.setBackground(GREEN_BACKGROUND);
        displayPanel.setBackground(GREEN_BACKGROUND);
        controlPanel.setBackground(GREEN_BACKGROUND);
        dataPanel.setBackground(GREEN_BACKGROUND);

        // Setting label font
        tableNameLabel.setFont(titleFont);
        
        //Setting button font
        resetButton.setFont(controlFont);
        helpButton.setFont(controlFont);
        
        buyButton.setFont(actionFont);
        sellButton.setFont(actionFont);
        dividendButton.setFont(actionFont);
        deletePurchaseButton.setFont(actionFont);
        deleteSaleButton.setFont(actionFont);
        marketValueButton.setFont(actionFont);
        annualizedRetButton.setFont(actionFont);

        saleTableButton.setFont(displayFont);
        purchaseTableButton.setFont(displayFont);
        dividendTableButton.setFont(displayFont);
        runningHoldingButton.setFont(displayFont);

        // Setting button  text position
        resetButton.setHorizontalTextPosition(SwingConstants.CENTER);
        helpButton.setHorizontalTextPosition(SwingConstants.CENTER);
        
        buyButton.setHorizontalTextPosition(SwingConstants.CENTER);
        sellButton.setHorizontalTextPosition(SwingConstants.CENTER);
        dividendButton.setHorizontalTextPosition(SwingConstants.CENTER);
        marketValueButton.setHorizontalTextPosition(SwingConstants.CENTER);
        deleteSaleButton.setHorizontalTextPosition(SwingConstants.CENTER);
        deletePurchaseButton.setHorizontalTextPosition(SwingConstants.CENTER);
        annualizedRetButton.setHorizontalTextPosition(SwingConstants.CENTER);
        
        saleTableButton.setHorizontalTextPosition(SwingConstants.CENTER);
        purchaseTableButton.setHorizontalTextPosition(SwingConstants.CENTER);
        dividendTableButton.setHorizontalTextPosition(SwingConstants.CENTER);
        runningHoldingButton.setHorizontalTextPosition(SwingConstants.CENTER);
        
        // Setting button size
        buyButton.setPreferredSize(actionSize);
        sellButton.setPreferredSize(actionSize);
        dividendButton.setPreferredSize(actionSize);
        deletePurchaseButton.setPreferredSize(actionSize);
        deleteSaleButton.setPreferredSize(actionSize);
        marketValueButton.setPreferredSize(actionSize);
        annualizedRetButton.setPreferredSize(actionSize);
        
        runningHoldingButton.setPreferredSize(displaySize);
        saleTableButton.setPreferredSize(displaySize);
        purchaseTableButton.setPreferredSize(displaySize);
        dividendTableButton.setPreferredSize(displaySize);
        
        runningHoldingButton.setEnabled(false);
        
        // Setting JTable characteristics
        holdingsTable.setBounds(30, 40, 200, 300);
        holdingsTable.setBackground(Color.WHITE);
        holdingsTable.setGridColor(Color.BLACK);
        holdingsTable.setRowHeight(30);
        holdingsTable.setFillsViewportHeight(true);
        holdingsTable.setDefaultEditor(Object.class, null);
        // set font
        
        // Setting table headers
        tableHeader = holdingsTable.getTableHeader();
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setForeground(Color.BLACK);
        // set font
        tableHeader.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
        
        // Set scrollpane
        scrollPane = new JScrollPane(holdingsTable);

        // ADDING
        // Adding action listener to buttons
        resetButton.addActionListener(this);
        helpButton.addActionListener(this);
        buyButton.addActionListener(this);
        sellButton.addActionListener(this);
        dividendButton.addActionListener(this);
        deletePurchaseButton.addActionListener(this);
        deleteSaleButton.addActionListener(this);
        marketValueButton.addActionListener(this);
        annualizedRetButton.addActionListener(this);
        dividendTableButton.addActionListener(this);
        purchaseTableButton.addActionListener(this);
        saleTableButton.addActionListener(this);
        runningHoldingButton.addActionListener(this);

        // Adding reset and help buttons to panel
        controlPanel.add(resetButton, BorderLayout.WEST);
        controlPanel.add(helpButton, BorderLayout.EAST);
        
        // Adding the buy, sell, and edit dividend options to panel
        actionPanel.add(Box.createRigidArea(new Dimension(0, 42)));  
        actionPanel.add(buyButton);
//        actionPanel.add(Box.createRigidArea(new Dimension(0, 20)));   
//        actionPanel.add(deletePurchaseButton);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 20)));  
        actionPanel.add(sellButton);
//        actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));  
//        actionPanel.add(deleteSaleButton);
//        actionPanel.add(Box.createRigidArea(new Dimension(0, 20)));  
//        actionPanel.add(dividendButton);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 20)));  
        actionPanel.add(marketValueButton);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 20)));  
        actionPanel.add(annualizedRetButton);

        dataPanel.add(tableNameLabel, BorderLayout.NORTH);
        dataPanel.add(scrollPane, BorderLayout.CENTER);

        // Adding table selection button to panel
        displayPanel.add(Box.createRigidArea(new Dimension(0, 42)));  
        displayPanel.add(runningHoldingButton);
        displayPanel.add(Box.createRigidArea(new Dimension(0, 20)));  
        displayPanel.add(purchaseTableButton);
        displayPanel.add(Box.createRigidArea(new Dimension(0, 20)));  
        displayPanel.add(saleTableButton);
        displayPanel.add(Box.createRigidArea(new Dimension(0, 20)));   
        displayPanel.add(dividendTableButton);
        
        // Adding all panels to frame
        this.add(controlPanel, BorderLayout.NORTH);
        this.add(dataPanel, BorderLayout.CENTER);
        this.add(actionPanel, BorderLayout.WEST);
        this.add(displayPanel, BorderLayout.EAST);
        
        this.setVisible(true); // Making frame visible
        HomeFrame.setRunningHoldingFrame(this);
    }
    
    public void refreshDataTable() {
        // Getting data from database
        this.data = this.link.getDbData(InfoDb.RUNNING_TABLE, InfoDb.RUNNING_HEADER);
        this.dataModel.setDataVector(data, this.tableHeaders);
        this.dataModel.fireTableDataChanged();
    }
    
    /**
     * Action listener listening and responding to button presses
     * @param e 
     */
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // Change table frame
        if (command.equals("<html>PURCHASE TRANSACTION TABLE</html>")) {
            PurchaseTableFrame ptfObj = new PurchaseTableFrame();
            this.dispose();
        }
        
        // Changing table frame
        if (command.equals("<html>SALE TRANSACTION TABLE</html>")) {
            SaleTableFrame stfObj = new SaleTableFrame();
            this.dispose();
        }
        
        // Changing table frame
        if (command.equals("<html>DIVIDEND HOLDINGS TABLE</html>")) {
            DividendTableFrame dtfObj = new DividendTableFrame();
            this.dispose();
        }
        
        // Purchase stock
        if (command.equals("<html>Purchase Shares</html>")) {
            PurchaseInsertFrame pitf = new PurchaseInsertFrame();
        }
        
        if (command.equals("<html>Sell Shares</html>")) {
            SaleInsertFrame sif = new SaleInsertFrame();
        }
        
        if (command.equals("<html>Delete Purchase</html>")) {
            PurchaseDeleteFrame pdf = new PurchaseDeleteFrame();
        }
        
        if (command.equals("<html>Delete Sale</html>")) {
            SaleDeleteFrame sdf = new SaleDeleteFrame();
        }
        
        if (command.equals("<html>Manage Dividends</html>")) {
            DividendUpdateFrame duf = new DividendUpdateFrame();
        }
        
        if (command.equals("<html>Update Market Values</html>")) {
            RunningUpdateFrame ruf = new RunningUpdateFrame();
        }
        
        if (command.equals("<html>Calculate Annualized Return</html>")) {
            TableFill fill = new TableFill();
            String[] tickerList = fill.getTickerList();
            
            if (tickerList[0].equals("-Empty-")) {
                RobustChecker rob = new RobustChecker("Error! There are no stocks present.");
                ErrorDisplay error = new ErrorDisplay(rob);
            } else {
                AnnualizedReturnInsertFrame arif = new AnnualizedReturnInsertFrame(tickerList);
            }
        }
        
        // Reset button
        if (command.equals("RESET")) {
            ResetFrame reset = new ResetFrame();
        }
        
        // Help button
        if (command.equals("?")) {
            HelpFrame help = new HelpFrame();
        }
    }
}
