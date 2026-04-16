package graphic;

import managers.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

public class GraphicInterface extends JFrame {
	private static final Color menuColor = new Color(235, 235, 235);
	private static final Color buttonColor = new Color(220, 220, 220);
	private static final Color contourColor = new Color(195, 195, 195);
	private static final Color hoveredColor = new Color(213, 213, 227);
	private static final Color pressedColor = new Color(203, 203, 222);
	private static final Color fontColor = Color.BLACK;

	private static final Font menuFont = new Font("Arial", Font.PLAIN, 12);
	private static final Font toolTipFont = new Font("Dubai", Font.PLAIN, 12);

	private DataInfoListener dataInfoListener;

	private void setGlobalStyle() {
		UIManager.put("Button.background", buttonColor);
		UIManager.put("Button.foreground", fontColor);
		UIManager.put("Button.font", menuFont);

		UIManager.put("Menu.foreground", fontColor);
		UIManager.put("Menu.font", menuFont);

		//UIManager.put("PopupMenu.background", auxiliaryColor);

		UIManager.put("MenuItem.background", menuColor);
		UIManager.put("MenuItem.foreground", fontColor);
		UIManager.put("MenuItem.font", menuFont);
		//UIManager.put("MenuItem.acceleratorFont", menuFont);
		//UIManager.put("MenuItem.acceleratorForeground", fontColor);
		//UIManager.put("MenuItem.acceleratorSelectionForeground", fontColor);

		//UIManager.put("TextArea.background", mainColor);
		//UIManager.put("TextArea.foreground", fontColor);
		//UIManager.put("TextArea.font", textFont);

		//UIManager.put("ScrollPane.background", backColor);
	}

	public GraphicInterface(DataInfoListener dil) {
		dataInfoListener = dil;

		setGlobalStyle();
		setTitle("W&R:SR - data service");
		setSize(720, 480);
		setMinimumSize(new Dimension(720, 480));
		setIconImage((new ImageIcon("resources/ico64.png")).getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setJMenuBar(createMenuBar());
	}

	private JMenuBar createMenuBar() {
		JMenuBar res = new JMenuBar();

		res.setBackground(Color.WHITE);
		res.setBorder(null);
		res.setBorderPainted(false);

		JMenu fileMenu = createFileMenu();
		JMenu dataHandlingMenu = createDataHandlingMenu();
		JMenu helpMenu = createHelpMenu();

		res.add(fileMenu);
		res.add(dataHandlingMenu);
		res.add(helpMenu);

		return res;
	}

	private JMenu createMainMenu(String title) {
		JMenu res = new JMenu(title);

		res.setBorderPainted(false);
		res.getPopupMenu().setOpaque(true);
		res.getPopupMenu().setBorder(BorderFactory.createLineBorder(contourColor, 1));

		return res;
	}

	private JMenu createSubMenu(String title, String toolTip) {
		JMenu res = new JMenu(title) {
			@Override
			public JToolTip createToolTip() {
				JToolTip tt = super.createToolTip();
				tt.setBackground(menuColor);
				tt.setBorder(BorderFactory.createLineBorder(contourColor, 1));
				tt.setForeground(fontColor);
				tt.setFont(toolTipFont);

				return tt;
			}
		};

		res.setToolTipText(toolTip);
		res.setBorderPainted(false);
		res.getPopupMenu().setOpaque(true);
		res.getPopupMenu().setBorder(BorderFactory.createLineBorder(contourColor, 1));

		return res;
	}
	
	private JMenuItem createMenuItem(String title) {
		JMenuItem res = new JMenuItem(title);

		res.setBorderPainted(false);

		return res;
	}

	private JMenuItem createMenuItem(String title, String toolTip) {
		JMenuItem res = new JMenuItem(title) {
			@Override
			public JToolTip createToolTip() {
				JToolTip tt = super.createToolTip();
				tt.setBackground(menuColor);
				tt.setBorder(BorderFactory.createLineBorder(contourColor, 1));
				tt.setForeground(fontColor);
				tt.setFont(toolTipFont);

				return tt;
			}
		};

		res.setBorderPainted(false);
		res.setToolTipText(toolTip);

		return res;
	}

	private JButton createButton(String title) {
		JButton res = new JButton(title);

		res.setFocusPainted(false);

		return res;
	}

	private JMenu createFileMenu() {
		JMenu res = createMainMenu("File");

		JMenuItem connectDataItem = createMenuItem("Connect", "connect to database");
		JMenuItem disconnectDataItem = createMenuItem("Disconnect", "disconnect from database");
		//JMenuItem generateDataItem = createMenuItem("Generate", "generates data example");

		JMenuItem exitProgramItem = createMenuItem("Exit");
		exitProgramItem.addActionListener(e -> dispose());

		res.add(connectDataItem);
		res.add(disconnectDataItem);
		//res.add(generateDataItem);
		res.add(exitProgramItem);

		return res;
	}

	private JMenu createDataHandlingMenu() {
		JMenu res = createMainMenu("Data");

		JMenuItem insertDataItem = createMenuItem("Insert", "create new data element");
		JMenuItem editDataItem = createMenuItem("Edit", "edit existing data element");
		JMenuItem userRequestItem = createMenuItem("User request", "write your own request");
		JMenu readyRequestItem = createReadyRequestsMenu();

		res.add(insertDataItem);
		res.add(editDataItem);
		res.add(userRequestItem);
		res.add(readyRequestItem);

		return res;
	}

	private JMenu createHelpMenu() {
		JMenu res = createMainMenu("Help");

		JMenuItem showHelpItem = createMenuItem("Show help");

		JMenuItem aboutProgramItem = createMenuItem("About");
		aboutProgramItem.addActionListener(e -> showAbout());

		res.add(showHelpItem);
		res.add(aboutProgramItem);

		return res;
	}

	private JMenu createReadyRequestsMenu() {
		JMenu res = createSubMenu("Ready request", "choose ready request");

		for (String rr : dataInfoListener.getReadyCategories())
			res.add(createMenuItem(rr));

		return res;
	}

	private JPanel createMainPanel() {
		JPanel res = new JPanel();

		return res;
	}

	//Возможно передел
	private void showAbout() {
		Object message = "Version v1.0.0";

		JOptionPane aboutInfoPane = new JOptionPane(
			message,
			JOptionPane.INFORMATION_MESSAGE,
			JOptionPane.OK_OPTION,
			null,
			new Object[] {}				
		);

		JDialog aboutInfoDialog = aboutInfoPane.createDialog(this, "W&R:SR - data service: information");

		JButton okButton = createButton("OK");
		okButton.addActionListener(e -> aboutInfoDialog.dispose());

		aboutInfoPane.setOptions(new Object[] { okButton });

		aboutInfoDialog.setVisible(true); 
	}

	private void showReadyRequestDialog(String category) {
		
	}
}