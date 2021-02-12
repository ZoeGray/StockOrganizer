/**
 * Displays basic information in a short guide to help the user become familiar
 * with the application
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 */
public class HelpFrame extends JFrame implements ActionListener{
    private final JPanel helpPanel;
    
    private final JLabel helpLabel;
    
    private final JButton goBackButton;
    
    private final Dimension buttonSize;
    
    private final Color GREEN_BACKGROUND;
    
    private final Font helpFont;
    private final Font buttonFont;

    public HelpFrame() {
        super("Help Frame");
        
        // Setting Color
        GREEN_BACKGROUND = new Color(30,142,84);
        
        // Setting Font
        helpFont = new Font("Arial", Font.PLAIN, 20);
        buttonFont = new Font("Arial", Font.BOLD, 25);
        
        buttonSize = new Dimension(200, 50);
        
        // SETTING JFrame components
        this.setBounds(360, 200, 720, 450);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setBackground(GREEN_BACKGROUND);
        
        helpPanel = new JPanel();
        helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));
        helpPanel.setBackground(GREEN_BACKGROUND);
        
        helpLabel = new JLabel("<html><p>Stock Portfolio Manager makes managing "
                + "your stocks easy.</p><p> Look to the left to see buttons "
                + "that allow you to modify the database. </p><p> Look to the right "
                + "to see buttons that control which table to veiw.</p></html>");
        helpLabel.setFont(helpFont);
        helpLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        
        goBackButton = new JButton("I GOT IT");
        goBackButton.setFont(buttonFont);
        goBackButton.setPreferredSize(buttonSize);
        goBackButton.setHorizontalTextPosition(SwingConstants.CENTER);
        goBackButton.addActionListener(this);
        
        helpPanel.add(Box.createRigidArea(new Dimension(0, 100)));
        helpPanel.add(helpLabel);
        helpPanel.add(Box.createRigidArea(new Dimension(30, 60)));
        helpPanel.add(goBackButton);
        
        this.add(helpPanel);
        
        this.setVisible(true);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        if (command.equals("I GOT IT")) {
            this.dispose();
        }
    }
}
