/*
 * Frame that allows the user to reset all data in the database.
 */
package stockorganizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 */
public class ResetFrame extends JFrame implements ActionListener {
    private final JPanel resetPanel;
    private final JPanel buttonPanel;
    
    private final JLabel resetLabel;
    
    private final JButton resetButton;
    private final JButton goBackButton;
    
    private final Dimension buttonSize;
    
    private final Color GREEN_BACKGROUND;
    
    private final Font warningFont;
    private final Font buttonFont;
    
    public ResetFrame() {
        super("Reset Frame");
        
        // Setting Color
        GREEN_BACKGROUND = new Color(30,142,84);
        
        // Setting Font
        warningFont = new Font("Arial", Font.BOLD, 30);
        buttonFont = new Font("Arial", Font.BOLD, 25);
        
        buttonSize = new Dimension(200, 50);
        
        this.setBounds(360, 200, 720, 450);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setBackground(GREEN_BACKGROUND);
        
        resetPanel = new JPanel();
        resetPanel.setLayout(new GridLayout(2,1));
        resetPanel.setBackground(GREEN_BACKGROUND);
        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(GREEN_BACKGROUND);
        
        resetLabel = new JLabel("<html>Are you sure you want to reset?</html>",SwingConstants.CENTER);
        resetLabel.setFont(warningFont);
//        resetLabel.setHorizontalTextPosition(SwingConstants.CENTER);
//        resetLabel.setBackground(GREEN_BACKGROUND);
        
        resetButton = new JButton("RESET");
        resetButton.setFont(buttonFont);
        resetButton.setPreferredSize(buttonSize);
        resetButton.setHorizontalTextPosition(SwingConstants.CENTER);
        
        goBackButton = new JButton("GO BACK");
        goBackButton.setFont(buttonFont);
        goBackButton.setPreferredSize(buttonSize);
        goBackButton.setHorizontalTextPosition(SwingConstants.CENTER);
        
        goBackButton.addActionListener(this);
        resetButton.addActionListener(this);
        
        buttonPanel.add(goBackButton);
        buttonPanel.add(resetButton);
        
//        resetPanel.add(Box.createRigidArea(new Dimension(0, 100)));
        resetPanel.add(resetLabel);
//        resetPanel.add(Box.createRigidArea(new Dimension(0, 60)));
        resetPanel.add(buttonPanel);
        
        this.add(resetPanel);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        if (command.equals("RESET")) {
            EditDataDb edi = new EditDataDb();
            
            edi.resetInfo();
            this.dispose();

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
        }
        if (command.equals("GO BACK")) {
            this.dispose();
        }
    }
}
