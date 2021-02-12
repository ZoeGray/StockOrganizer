/**
 * Displays prompts in JFrame for user to change value of dividend per share
 * for a dividend in dividend tables.
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
public class DividendUpdateFrame extends JFrame implements ActionListener {
    private final JPanel buttonPanel;
    private final JPanel tickerPanel;
    private final JPanel dividendSharePanel;
    private final JPanel promptPanel;
    
    private final JButton closeButton;
    private final JButton updateButton;
    
    private final JLabel titleLabel;
    private final JLabel tickerLabel;
    private final JLabel toLabel;
    private final JLabel dividendShareLabel;
    
    private final JTextField dividendShareField;
    
    private final JComboBox tickerBox;
    
    private final Font titleFont;
    private final Font promptFont;
    private final Font buttonFont;
    
    private final Color GREEN_BACKGROUND;
    
    private final Dimension buttonSize;
    
    String[] tickerList;
    
    public DividendUpdateFrame() {
        super("Update Dividend Per Share");
        
        // Setting Color
        GREEN_BACKGROUND = new Color(30,142,84);
        
        // Setting font details
        titleFont = new Font("Arial", Font.BOLD, 30);
        promptFont = new Font("Arial", Font.PLAIN, 20);
        buttonFont = new Font("Arial", Font.BOLD, 20);
        
        // Setting button dimensions
        buttonSize = new Dimension(200,70);
        
        // SETTING JFrame components
        this.setBounds(300,150,800,500);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setBackground(GREEN_BACKGROUND);
        
        buttonPanel = new JPanel(new FlowLayout());
        tickerPanel = new JPanel(new FlowLayout());
        dividendSharePanel = new JPanel(new FlowLayout());
        promptPanel = new JPanel();
        promptPanel.setLayout(new BoxLayout(promptPanel, BoxLayout.Y_AXIS));
        
        TableFill fill = new TableFill();
        tickerList = fill.getTickerList();
        tickerBox = new JComboBox(tickerList);
        
        // Setting button text
        closeButton = new JButton("Close");
        updateButton = new JButton("Update");
        
        // Setting label text
        titleLabel = new JLabel("<html>Update Dividend Per Share Information</html>");
        tickerLabel = new JLabel("Update dividend of ");
        toLabel = new JLabel(" to $");
        dividendShareLabel = new JLabel(" per share owned ");
        
        dividendShareField = new JTextField(5);
        
        buttonPanel.setBackground(GREEN_BACKGROUND);
        tickerPanel.setBackground(GREEN_BACKGROUND);
        dividendSharePanel.setBackground(GREEN_BACKGROUND);
        promptPanel.setBackground(GREEN_BACKGROUND);
        
        titleLabel.setFont(titleFont);
        tickerLabel.setFont(promptFont);
        toLabel.setFont(promptFont);
        dividendShareLabel.setFont(promptFont);
        
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Setting button fonts
        closeButton.setFont(buttonFont);
        updateButton.setFont(buttonFont);
        
        // Setting button size
        closeButton.setPreferredSize(buttonSize);
        updateButton.setPreferredSize(buttonSize);
        
        // Setting button text positions
        closeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        updateButton.setHorizontalTextPosition(SwingConstants.CENTER);
        
        tickerBox.setFont(promptFont);
        dividendShareField.setFont(promptFont);
        
        // Adding action listener to buttons
        closeButton.addActionListener(this);
        updateButton.addActionListener(this);
        tickerBox.addActionListener(this);
        
        buttonPanel.add(closeButton);
        buttonPanel.add(updateButton);
        
        tickerPanel.add(tickerLabel);
        tickerPanel.add(tickerBox);
        
        dividendSharePanel.add(toLabel);
        dividendSharePanel.add(dividendShareField);
        dividendSharePanel.add(dividendShareLabel);
        
        promptPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        promptPanel.add(tickerPanel);
        promptPanel.add(dividendSharePanel);
        
        this.add(titleLabel, BorderLayout.NORTH);
        this.add(promptPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        EditDataDb edi = new EditDataDb();
        RobustChecker rob = new RobustChecker();
        
        ArrayList infoList = new ArrayList();
        String ticker, newDividend;
        boolean safe;
        
        if (command.equals("Update")) {
            ticker = (String) tickerBox.getSelectedItem();
            newDividend = (String) dividendShareField.getText();
            
            safe = !ticker.equals("-EMPTY-");
            safe = safe && rob.strToDub(newDividend);
            
            if (safe) {
                infoList.add(ticker);
                infoList.add(newDividend);
                
                edi.updateDividend(infoList);
                
                dividendShareField.setText("");
                
                DividendTableFrame dFrame = HomeFrame.getDividendFrame();
                if (dFrame != null) {
                    dFrame.refreshDataTable();
                }
            } else {
                rob = new RobustChecker("Error! Data could not be updated.");
                ErrorDisplay error = new ErrorDisplay(rob);
            }
        }
        
        if (command.equals("Close")) {
            this.dispose();
        }
    }
}
