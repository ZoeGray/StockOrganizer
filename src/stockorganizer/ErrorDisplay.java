/**
 * Error Display gets called whenever there is user input error. Makes call to
 * RobustChecker() class which sets the error message that needs to be displayed.
 * Only purpose is to make user aware of input error
 */
package stockorganizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Error frame that adapts to different error messages
 */
public class ErrorDisplay extends JFrame implements ActionListener {
    private final JPanel messagePanel; // Panel intended for both JLabels
    
    private final JLabel errorLabel; // Says "error"
    private final JLabel messageLabel; // Labels error message
    
    private final JButton okButton; // Closes frame when pressed
    
    private final Font errorFont;
    private final Font messageFont;
    private final Font buttonFont;
    
    private final Dimension okSize; // Button size
    
    public ErrorDisplay(RobustChecker rcObj){
        super("Error Popup"); // Calling super
                
        // SETTING JFrame Components
        this.setBounds(500,300,400,300); // Sets frame to width of entire screen !!!
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Closes error frame only
        this.setLayout(new BorderLayout()); // Sets frame to border layout
        this.setBackground(Color.lightGray); // Background color of frame
        
        messagePanel = new JPanel(new BorderLayout());
        
        errorLabel = new JLabel("<html>There was an ERROR when performing this action.</html>", SwingConstants.CENTER);
        messageLabel = new JLabel("<html>"+rcObj.getErrorMessage()+"</html>", SwingConstants.CENTER); // Calling error message
        
        okButton = new JButton("OK");
        
        errorFont = new Font("Arial", Font.BOLD, 20);
        messageFont = new Font("Arial", Font.PLAIN, 15);
        buttonFont = new Font("Arial", Font.BOLD, 20);
        
        okSize = new Dimension(100,50); // Size of button
        
        messagePanel.setBackground(Color.WHITE);
        
        errorLabel.setFont(errorFont);
        messageLabel.setFont(messageFont);
        
        okButton.setFont(buttonFont);
        
        okButton.setPreferredSize(okSize);
        
        okButton.setHorizontalTextPosition(SwingConstants.CENTER);
        
        // ADDING Action Listener
        okButton.addActionListener(this);
        
        // ADDING JFrame Components
        messagePanel.add(errorLabel, BorderLayout.NORTH); // Adding labels to message frame
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        
        this.add(messagePanel, BorderLayout.CENTER);
        this.add(okButton, BorderLayout.SOUTH);
        
        this.setVisible(true);
    }

    /**
     * Action performed method, listens and responds to user pressing OK button
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        if(command.equals("OK")) {
            this.dispose();
        }
    }
    
//    public static void main(String[] args) {
//        ErrorDisplay edObj = new ErrorDisplay();
//    }
    
}
