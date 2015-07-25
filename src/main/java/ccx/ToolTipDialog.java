package ccx;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public class ToolTipDialog extends JDialog {
	public ToolTipDialog() {
		setResizable(false);
		setUndecorated(true);
		
		lblToolTip = new JLabel("");
		lblToolTip.setBorder(new EmptyBorder(2, 2, 2, 2));
		getContentPane().add(lblToolTip, BorderLayout.CENTER);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lblToolTip;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
	
	public JLabel getLblToolTip() {
//		System.out.println("Accessed");
		return lblToolTip;
	}
}
