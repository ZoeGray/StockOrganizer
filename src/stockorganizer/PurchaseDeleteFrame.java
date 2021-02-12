/**
 * Frame that prompts user to delete a purchase transaction from purchase
 * transaction database
 */
package stockorganizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 */
public class PurchaseDeleteFrame extends JFrame implements ActionListener {

    private final JPanel buttonPanel;
    private final JPanel idPanel;
    private final JPanel promptPanel;
    
    private final JButton closeButton;
    private final JButton deleteButton;
    
    private final JLabel titleLabel;
    private final JLabel idLabel;
    
    private final JComboBox idBox;
    private final Font titleFont;
    
    private final Font promptFont;
    private final Font buttonFont;
    
    private final Color GREEN_BACKGROUND;
    
    private final Dimension buttonSize;
    
    Object[][] data;
    String[] idList;
    
    public PurchaseDeleteFrame() {
        super("Delete Purchase Frame");
        
        TableFill fill = new TableFill();
        // Setting Color
        GREEN_BACKGROUND = new Color(30,142,84);
        
        // Setting font details
        titleFont = new Font("Arial", Font.BOLD, 30);
        promptFont = new Font("Arial", Font.PLAIN, 20);
        buttonFont = new Font("Arial", Font.BOLD, 20);
        
        // Setting button dimensions
        buttonSize = new Dimension(200,70);
        
        buttonPanel = new JPanel(new FlowLayout());
        idPanel = new JPanel(new FlowLayout());
        promptPanel = new JPanel();
        promptPanel.setLayout(new BoxLayout(promptPanel, BoxLayout.Y_AXIS));
        promptPanel.setBackground(GREEN_BACKGROUND);
        buttonPanel.setBackground(GREEN_BACKGROUND);
        idPanel.setBackground(GREEN_BACKGROUND);
        
        idList = fill.getIdList(InfoDb.PURCHASE_TABLE, InfoDb.PURCHASE_HEADER);
        idBox = new JComboBox(idList);
        if (! (Array.getLength(idList) == 0)) {
            idBox.setSelectedIndex(0);
        }
        idBox.setFont(promptFont);
        
        closeButton = new JButton("Close");
        deleteButton = new JButton("Delete");
        closeButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        closeButton.setFont(buttonFont);
        deleteButton.setFont(buttonFont);
        closeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        deleteButton.setHorizontalTextPosition(SwingConstants.CENTER);
        
        titleLabel = new JLabel("<html>Delete Row from Purchase Transactions</html>");
        idLabel = new JLabel("Delete row with ID number: ");
        titleLabel.setFont(titleFont);
        idLabel.setFont(promptFont);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        this.setBounds(300,150,800,500);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setBackground(GREEN_BACKGROUND);
        
        closeButton.addActionListener(this);
        deleteButton.addActionListener(this);
        
        buttonPanel.add(closeButton);
        buttonPanel.add(deleteButton);
        
        idPanel.add(idLabel);
        idPanel.add(idBox);
        
        promptPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        promptPanel.add(idPanel);
        
        this.add(titleLabel, BorderLayout.NORTH);
        this.add(promptPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        EditDataDb edi = new EditDataDb();
        RobustChecker rob = new RobustChecker();
        String id;
        boolean safe;
        
        if (command.equals("Delete")) {
            id = (String) idBox.getSelectedItem();
            
            safe = !idBox.equals("-Empty-");
            safe = safe && rob.simDeletePurchase(id);
            
            if (safe) {
                edi.deletePurchase(id);
                
                // Auto updates all tables, but must check if tables have been accesses and created first
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
