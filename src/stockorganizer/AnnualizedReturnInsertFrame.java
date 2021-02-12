/**
 * Frame that allows use to input relevant information pertaining to the 
 * calculation of annualized return. Opens output frame displaying results
 */
package stockorganizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 */
public class AnnualizedReturnInsertFrame extends JFrame implements ActionListener {
    private final JPanel introPanel;
    private final JPanel datePanel;
    private final JPanel startDatePanel;
    private final JPanel endDatePanel;
    private final JPanel stockPanel;
    private final JPanel buttonPanel;
    
    private final JButton closeButton;
    private final JButton calButton;
    
    private final JLabel calLabel;
    private final JLabel fromLabel;
    private final JLabel toLabel;
    private final JLabel infoLabel;
    
    private final JLabel ticker;
    private final JLabel startValue;
    private final JLabel endValue;
    private final JLabel dividends;
    
    private final JComboBox startMonthBox;
    private final JComboBox endMonthBox;
    
    private final JTextField startDayField;
    private final JTextField startYearField;
    private final JTextField endDayField;
    private final JTextField endYearField;
    
    private final Font calFont;
    private final Font promptFont;
    private final Font buttonFont;
    private final Font infoFont;
    
    private final JScrollPane scrollPane;
    
    private final Color GREEN_BACKGROUND;
    
    private final Dimension buttonSize;
    
    private final String[] MONTHS = {"January","February","March","April","May",
        "June","July","August","September","October","November","December"};
    
    private ArrayList<JTextField> startValList;
    private ArrayList<JTextField> endValList;
    private ArrayList<JTextField> dividendList;
    
    private String[] tickers;
    
    public AnnualizedReturnInsertFrame(String[] tickerList) {
        super("Insert Annualized Return Data");
        
        tickers = tickerList;
        
        GREEN_BACKGROUND = new Color(30,142,84);
        
        this.setBounds(300,150,800,500);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setBackground(GREEN_BACKGROUND);
        
        introPanel = new JPanel(new FlowLayout());
        datePanel = new JPanel(new FlowLayout());
        startDatePanel = new JPanel(new FlowLayout());
        endDatePanel = new JPanel(new FlowLayout());
        stockPanel = new JPanel(); // Box layout
        buttonPanel = new JPanel(new FlowLayout());
        
        scrollPane = new JScrollPane();
        
        
        // Set layout of stock panel to GridLayout(# of stocks, 4)
        // Iterate over stocks {
        //     Add label for stock to first colum
        //     Add 3 text fields for data entry
        // }
        
        ticker = new JLabel("Ticker");
        startValue = new JLabel("Value at Start Date");
        endValue = new JLabel("Value at End Date");
        dividends = new JLabel("Dividend per Share");
        
        startValList = new ArrayList();
        endValList = new ArrayList();
        dividendList = new ArrayList();
        
        stockPanel.add(ticker);
        stockPanel.add(startValue);
        stockPanel.add(endValue);
        stockPanel.add(dividends);
        
        // Loops for every stock held
        for (int i=0; i<tickers.length; i++) {
            JLabel tickerLabel = new JLabel(tickers[i]); // Makes ticker label
            JTextField startValField = new JTextField(11); // Makes start value text field
            JTextField endValField = new JTextField(11); // Makes end value text field
            JTextField dividendField = new JTextField(11); // Makes dividend text field
            
            // Adds field to individual lists to call upon later in actionPerformed
            startValList.add(startValField);
            endValList.add(endValField);
            dividendList.add(dividendField);
            
            // Adds to stockPanel (GridLayout)
            stockPanel.add(tickerLabel);
            stockPanel.add(startValField);
            stockPanel.add(endValField);
            stockPanel.add(dividendField);
        }
        
        stockPanel.setLayout(new GridLayout(tickers.length+1, 4));
        scrollPane.add(stockPanel);
        
        buttonSize = new Dimension(200,70);
        
        closeButton = new JButton("Close");
        calButton = new JButton("Calculate");
        
        calLabel = new JLabel("<html>Calculate Annualized Return</html>");
        fromLabel = new JLabel("from");
        toLabel = new JLabel("to");
        infoLabel = new JLabel("<html>Text fields left blank will be recorded as 0.00.</html>");
        
        calFont = new Font("Arial", Font.BOLD, 25);
        promptFont = new Font("Arial", Font.PLAIN, 20);
        buttonFont = new Font("Arial", Font.BOLD, 20);
        infoFont = new Font("Arial", Font.PLAIN, 15);
        
        startMonthBox = new JComboBox(MONTHS);
        endMonthBox = new JComboBox(MONTHS);
        startMonthBox.setSelectedIndex(0);
        endMonthBox.setSelectedIndex(0);
        
        startDayField = new JTextField(2);
        startYearField = new JTextField(4);
        endDayField = new JTextField(2);
        endYearField = new JTextField(4);
        
        closeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        calButton.setHorizontalTextPosition(SwingConstants.CENTER);
        
        closeButton.setPreferredSize(buttonSize);
        calButton.setPreferredSize(buttonSize);
        
        closeButton.setFont(buttonFont);
        calButton.setFont(buttonFont);
        calLabel.setFont(calFont);
        fromLabel.setFont(calFont);
        toLabel.setFont(calFont);
        infoLabel.setFont(infoFont);
        startMonthBox.setFont(promptFont);
        endMonthBox.setFont(promptFont);
        startDayField.setFont(promptFont);
        startYearField.setFont(promptFont);
        endDayField.setFont(promptFont);
        endYearField.setFont(promptFont);
        
        closeButton.addActionListener(this);
        calButton.addActionListener(this);
        startMonthBox.addActionListener(this);
        endMonthBox.addActionListener(this);
        
        introPanel.setBackground(GREEN_BACKGROUND);
        datePanel.setBackground(GREEN_BACKGROUND);
        startDatePanel.setBackground(GREEN_BACKGROUND);
        endDatePanel.setBackground(GREEN_BACKGROUND);
        buttonPanel.setBackground(GREEN_BACKGROUND);

        buttonPanel.add(closeButton);
        buttonPanel.add(calButton);
        
        startDatePanel.add(startMonthBox);
        startDatePanel.add(startDayField);
        startDatePanel.add(startYearField);
        
        endDatePanel.add(endMonthBox);
        endDatePanel.add(endDayField);
        endDatePanel.add(endYearField);
        
        datePanel.add(fromLabel);
        datePanel.add(startDatePanel);
        datePanel.add(toLabel);
        datePanel.add(endDatePanel);
        
        introPanel.add(calLabel);
        introPanel.add(datePanel);
        introPanel.add(infoLabel);
        introPanel.add(stockPanel);
        
        this.add(introPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        
        this.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        // retrieve fields from startDataPanel, endDataPanel, stockPanel
        String command = e.getActionCommand();
        
        if (command.equals("Close")) {
            this.dispose();
        }
        if (command.equals("Calculate")) {
            RobustChecker rob = new RobustChecker();
            
            ArrayList<ArrayList> inputList = new ArrayList();
            boolean safe;
            String startDay, startMonth, startYear;
            String endDay, endMonth, endYear;
            String startVal, endVal, div;
            
            // Gets date inputs
            startDay = startDayField.getText();
            startMonth = (String) startMonthBox.getSelectedItem();
            startYear = startYearField.getText();
            
            endDay = endDayField.getText();
            endMonth = (String) endMonthBox.getSelectedItem();
            endYear = endYearField.getText();
            
            // Checks for robustness in dates
            safe = rob.validDay(startDay);
            safe = safe && rob.validDay(endDay);
            safe = safe && rob.validYear(startYear);
            safe = safe && rob.validYear(endYear);
            
            
            try {
                // For each ticker
                for (int i=0; i<tickers.length; i++) {
                    ArrayList temp = new ArrayList();

                    /**
                     * Test if text field is empty, if not empty, then save list of values for that stock in another list
                     */
                    temp.add(tickers[i]);
                    if (startValList.get(i).getText().equals("")) {
                        temp.add(0);
                    } else {
                        startVal = startValList.get(i).getText();
                        safe = safe && rob.strToDub(startVal);
                        temp.add(startVal);
                    }

                    if (endValList.get(i).getText().equals("")) {
                        temp.add(0);
                    } else {
                        endVal = endValList.get(i).getText();
                        safe = safe && rob.strToDub(endVal);
                        temp.add(endVal);
                    }

                    if (dividendList.get(i).getText().equals("")) {
                        temp.add(0);
                    } else {
                        div = dividendList.get(i).getText();
                        safe = safe && rob.strToDub(div);
                        temp.add(div);
                    }

                    inputList.add(temp);
                }

                if (safe) {
                    TableFill fill = new TableFill();
                    Calculation cal = new Calculation();
                    String startDate, endDate;
                    double annualizedReturn = -1;

                    startDate = fill.createDateString(startYear, startMonth, startDay);
                    endDate = fill.createDateString(endYear, endMonth, endDay);

                    annualizedReturn = cal.annualizedReturn(startDate, endDate, inputList);

                    AnnualizedReturnOutputFrame arof = new AnnualizedReturnOutputFrame(String.valueOf(annualizedReturn));

                } else {
                    ErrorDisplay error = new ErrorDisplay(rob);
                }
            } catch (BadQuantityException ex) {
                    ErrorDisplay error = new ErrorDisplay(rob);
                }
            catch (Exception err) {
                ErrorDisplay error = new ErrorDisplay(rob);
            }
        }
    }
}
