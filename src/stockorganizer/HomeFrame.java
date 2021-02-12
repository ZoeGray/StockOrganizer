/**
 * First frame the user sees when they open the application. Introduces the user
 * to the system and has a button to continue to the main frames.
 */
package stockorganizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 */
public class HomeFrame extends JFrame implements ActionListener {
    
    private final JPanel homePanel;
    
    private final JLabel welcomeLabel;
    
    private final JButton contButton;
    
    private final Font titleFont;
    private final Font buttonFont;
    
    private final Dimension buttonSize;
    
    private final Color GREEN_BACKGROUND;
    
    private static RunningHoldingsTableFrame runningHoldingFrame;
    private static PurchaseTableFrame purchaseFrame;
    private static SaleTableFrame saleFrame;
    private static DividendTableFrame dividendFrame;

    public static void setRunningHoldingFrame(RunningHoldingsTableFrame runningHoldingFrame) {
        HomeFrame.runningHoldingFrame = runningHoldingFrame;
    }

    public static void setPurchaseFrame(PurchaseTableFrame purchaseFrame) {
        HomeFrame.purchaseFrame = purchaseFrame;
    }

    public static void setSaleFrame(SaleTableFrame saleFrame) {
        HomeFrame.saleFrame = saleFrame;
    }

    public static void setDividendFrame(DividendTableFrame dividendFrame) {
        HomeFrame.dividendFrame = dividendFrame;
    }

    public static RunningHoldingsTableFrame getRunningHoldingFrame() {
        return runningHoldingFrame;
    }

    public static PurchaseTableFrame getPurchaseFrame() {
        return purchaseFrame;
    }

    public static SaleTableFrame getSaleFrame() {
        return saleFrame;
    }

    public static DividendTableFrame getDividendFrame() {
        return dividendFrame;
    }
    
    public HomeFrame() {
        super("Home Frame");
        
        // Setting Color
        GREEN_BACKGROUND = new Color(30,142,84);
        
        // Setting Font
        titleFont = new Font("Arial", Font.BOLD, 50);
        buttonFont = new Font("Arial", Font.BOLD, 30);
        
        buttonSize = new Dimension(200, 50);
        
        // SETTING JFrame components
        this.setBounds(360, 200, 720, 450);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBackground(GREEN_BACKGROUND);
        
        homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setBackground(GREEN_BACKGROUND);
        
        welcomeLabel = new JLabel("<html>Stock Portfolio Manager</html>");
        welcomeLabel.setFont(titleFont);
        welcomeLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        
        contButton = new JButton("GET STARTED");
        contButton.setFont(buttonFont);
        contButton.setPreferredSize(buttonSize);
        contButton.setHorizontalTextPosition(SwingConstants.CENTER);
        contButton.addActionListener(this);
        
        homePanel.add(Box.createRigidArea(new Dimension(0, 100)));
        homePanel.add(welcomeLabel);
        homePanel.add(Box.createRigidArea(new Dimension(30, 60)));
        homePanel.add(contButton);
        
        this.add(homePanel);
        
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        if (command.equals("GET STARTED")) {
            RunningHoldingsTableFrame rhtf = new RunningHoldingsTableFrame();
            this.dispose();
        }
    }
    
    public static void main(String[] args) {
//        InstallDb install = new InstallDb();
        HomeFrame home = new HomeFrame(); 
    }
}
