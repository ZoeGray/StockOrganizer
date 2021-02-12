/**
 * Frame that displays users annualized return results. Prompts no new pages
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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 */
public class AnnualizedReturnOutputFrame extends JFrame implements ActionListener {
    private final JPanel answerPanel;
    private final JPanel buttonPanel;
    private final JPanel contentPanel;
    
    private final JLabel infoLabel;
    private final JLabel answerLabel;
    private final JLabel percentLabel;
    
    private final JButton backButton;
    private final JButton closeButton;
    
    private final Dimension buttonSize;
    
    private final Font infoFont;
    private final Font buttonFont;
    private final Font answerFont;
    
    private final Color GREEN_BACKGROUND;
    
    public AnnualizedReturnOutputFrame(String answer) {
        super("Annualized Return Output");
        
        GREEN_BACKGROUND = new Color(30,142,84);
        
        this.setBounds(300,150,800,500);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setBackground(GREEN_BACKGROUND);
        
        contentPanel = new JPanel();
        buttonPanel = new JPanel();
        answerPanel = new JPanel(new FlowLayout());
        contentPanel.setLayout(new GridLayout(2,1));
        buttonPanel.setLayout(new FlowLayout());
        
        buttonSize = new Dimension(200,70);
        
        infoLabel = new JLabel("<html>Your Annualized Return for the period is </html>", SwingConstants.CENTER);
        answerLabel = new JLabel(answer); // Uses arguement passed in constructor
        percentLabel = new JLabel("%");
        
        infoFont = new Font("Arial", Font.PLAIN, 25);
        buttonFont = new Font("Arial", Font.BOLD, 20);
        answerFont = new Font("Arial", Font.PLAIN, 50);
        
        closeButton = new JButton("Close");
        backButton = new JButton("Back");
        
        answerPanel.setBackground(GREEN_BACKGROUND);
        contentPanel.setBackground(GREEN_BACKGROUND);
        buttonPanel.setBackground(GREEN_BACKGROUND);
        
        closeButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);
        
        closeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        backButton.setHorizontalTextPosition(SwingConstants.CENTER);
        
        closeButton.setFont(buttonFont);
        backButton.setFont(buttonFont);
        infoLabel.setFont(infoFont);
        answerLabel.setFont(answerFont);
        percentLabel.setFont(answerFont);
        
        backButton.addActionListener(this);
        closeButton.addActionListener(this);
        
        answerPanel.add(answerLabel);
        answerPanel.add(percentLabel);
        
        buttonPanel.add(backButton);
        buttonPanel.add(closeButton);
        
        contentPanel.add(infoLabel);
        contentPanel.add(answerPanel);
        
        this.add(contentPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        
        this.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        if (command.equals("Close")) {
            this.dispose();
        }
        if (command.equals("Back")) {
            
        }
            
    }
}
