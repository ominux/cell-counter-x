package ccx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import java.awt.Dimension;

public class MappingEditor extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1569143696586671526L;

	private final JPanel contentPanel = new JPanel();

	private ArrayList<Mapping> mappings = new ArrayList<Mapping>();
	private JButton okButton;
	private JButton cancelButton;
	private JTextArea textArea;
	private JLabel lblNumberdescription;
	
	private static ArrayList<Mapping> demoMappings = new ArrayList<Mapping>();
	private JComboBox cbxTemplates;
	private JLabel lblTemplate;
	private JPanel templatePanel;
	private JPanel panel;
	private JPanel buttonPanel;
	private JScrollPane scrollPane;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MappingEditor dialog = new MappingEditor();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			demoMappings.add(new Mapping("1","one"));
			demoMappings.add(new Mapping("2","two"));
			dialog.setMappings(demoMappings);
			dialog.setVisible(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MappingEditor() {
		setResizable(false);
		setPreferredSize(new Dimension(515, 400));
		setTitle("Edit Mappings");
		setBounds(100, 100, 515, 400);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				textArea = new JTextArea();
				scrollPane.setViewportView(textArea);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				GridBagLayout gbl_buttonPane = new GridBagLayout();
				gbl_buttonPane.columnWidths = new int[] { 93, 75, 0 };
				gbl_buttonPane.rowHeights = new int[] { 0, 29, 0 };
				gbl_buttonPane.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
				gbl_buttonPane.rowWeights = new double[] { 1.0, 0.0,
						Double.MIN_VALUE };
				buttonPane.setLayout(gbl_buttonPane);
				{
					templatePanel = new JPanel();
					templatePanel.setBorder(new TitledBorder(null, "Template", TitledBorder.LEADING, TitledBorder.TOP, null, null));
					GridBagConstraints gbc_templatePanel = new GridBagConstraints();
					gbc_templatePanel.insets = new Insets(0, 0, 5, 5);
					gbc_templatePanel.fill = GridBagConstraints.BOTH;
					gbc_templatePanel.gridx = 0;
					gbc_templatePanel.gridy = 0;
					buttonPane.add(templatePanel, gbc_templatePanel);
					GridBagLayout gbl_templatePanel = new GridBagLayout();
					gbl_templatePanel.columnWidths = new int[]{0, 150, 0};
					gbl_templatePanel.rowHeights = new int[]{0, 0};
					gbl_templatePanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
					gbl_templatePanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
					templatePanel.setLayout(gbl_templatePanel);
					{
						lblTemplate = new JLabel("Choose Template:");
						GridBagConstraints gbc_lblTemplate = new GridBagConstraints();
						gbc_lblTemplate.anchor = GridBagConstraints.WEST;
						gbc_lblTemplate.insets = new Insets(0, 5, 0, 5);
						gbc_lblTemplate.gridx = 0;
						gbc_lblTemplate.gridy = 0;
						templatePanel.add(lblTemplate, gbc_lblTemplate);
					}
					{
						cbxTemplates = new JComboBox();
						cbxTemplates.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								handleTemplateChooser(e);
							}
						});
						cbxTemplates.setModel(new DefaultComboBoxModel(new String[] {"<none>", "default classification", "default detection" }));
						cbxTemplates.setSelectedIndex(1);
						GridBagConstraints gbc_cbxTemplates = new GridBagConstraints();
						gbc_cbxTemplates.fill = GridBagConstraints.HORIZONTAL;
						gbc_cbxTemplates.anchor = GridBagConstraints.NORTH;
						gbc_cbxTemplates.gridx = 1;
						gbc_cbxTemplates.gridy = 0;
						templatePanel.add(cbxTemplates, gbc_cbxTemplates);
					}
				}
			}
			{
				{
					panel = new JPanel();
					panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Information", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
					GridBagConstraints gbc_panel = new GridBagConstraints();
					gbc_panel.fill = GridBagConstraints.BOTH;
					gbc_panel.insets = new Insets(0, 0, 5, 0);
					gbc_panel.gridx = 1;
					gbc_panel.gridy = 0;
					buttonPane.add(panel, gbc_panel);
					panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
					{
						lblNumberdescription = new JLabel(
								"Format: number=description");
						panel.add(lblNumberdescription);
						lblNumberdescription.setToolTipText("<html>Enter the mapping in the following format, one mapping in a line:<br/>\r\n'number=description'<br/>\r\nlike<br/>\r\n<p>\r\n1=class1<br/>\r\n2=class2<br/>\r\n3=class3<br/>\r\n.<br/>\r\n.<br/>\r\n.<br/>\r\nn=classn<br/>\r\n</p>\r\n</html>");
					}
				}
				{
					buttonPanel = new JPanel();
					GridBagConstraints gbc_buttonPanel = new GridBagConstraints();
					gbc_buttonPanel.fill = GridBagConstraints.BOTH;
					gbc_buttonPanel.gridx = 1;
					gbc_buttonPanel.gridy = 1;
					buttonPane.add(buttonPanel, gbc_buttonPanel);
					buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
					okButton = new JButton("OK");
					buttonPanel.add(okButton);
					okButton.setActionCommand("OK");
					okButton.addActionListener(this);
					getRootPane().setDefaultButton(okButton);
					cancelButton = new JButton("Cancel");
					buttonPanel.add(cancelButton);
					cancelButton.setActionCommand("Cancel");
					cancelButton.addActionListener(this);
				}
			}
		}
	}

	protected void handleTemplateChooser(ActionEvent e) {
		if (e.getSource() instanceof JComboBox){
			JComboBox source = (JComboBox) e.getSource();
			if (source.getSelectedItem().equals("default classification")){
				setMappings(new ArrayList<Mapping>(Arrays.asList(Mapping.DEFAULT_CLASSIFICATION_MAPPING)));
			}
			else if (source.getSelectedItem().equals("default detection")){
				setMappings(new ArrayList<Mapping>(Arrays.asList(Mapping.DEFAULT_DETECTION_MAPPING)));
			}
			else {
				setMappings(new ArrayList<Mapping>());
			}
		}
	}

	void addMapping(Mapping m) {
		mappings.add(m);
	}

	public void setMappings(ArrayList<Mapping> mappings) {
		System.out.println("Setting " + mappings.size() + 
				" mappings to the editor...");
		this.mappings = mappings;
		String allLines = "";
		for (Mapping m : mappings) {
			String line = m.getMarkerType() + "=" + m.getValue() + "\n";
			System.out.print(line);
			allLines = allLines + line;
		}
		this.textArea.setText(allLines);
		this.textArea.setCaretPosition(this.textArea.getText().length());
	}

	public ArrayList<Mapping> getMappings() {
		return mappings;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("OK".equals(e.getActionCommand())) {
			mappings = new ArrayList<Mapping>();

			if (!textArea.getText().equals("")) {
				// parse the lines
				String[] lines = textArea.getText().split("\n");
				for (String line : lines) {
					System.out.println(line);
					if (line == null || line.equals("")) {

					} else {
						Mapping m = new Mapping();
						m.setMarkerType((line.substring(0, line.indexOf("="))));
						m.setValue(line.substring(line.indexOf("=") + 1,
								line.length()));
						addMapping(m);
					}
				}
			} 
			this.setVisible(false);
		} else if ("Cancel".equals(e.getActionCommand())) {
			this.setVisible(false);
		}
	}

}