package formTest;

import javax.swing.*;

/**
 * Created by cellargalaxy on 17-10-1.
 */
public class NewForm {
	private JPanel panel;
	private JLabel label;
	private JButton buttonButtonButtonButtonButton;
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("NewForm");
		frame.setContentPane(new NewForm().panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
