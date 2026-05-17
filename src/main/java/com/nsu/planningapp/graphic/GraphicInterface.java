package com.nsu.planningapp.graphic;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.sql.Connection;

public class GraphicInterface extends JFrame {
	private static final Color menuColor = new Color(235, 235, 235);
	private static final Color buttonColor = new Color(220, 220, 220);
	private static final Color contourColor = new Color(195, 195, 195);
	private static final Color hoveredColor = new Color(213, 213, 227);
	private static final Color pressedColor = new Color(203, 203, 222);
	private static final Color fontColor = Color.BLACK;

	private static final Font menuFont = new Font("Arial", Font.PLAIN, 12);
	private static final Font toolTipFont = new Font("Dubai", Font.PLAIN, 12);

	private DataBaseListener dbListener;

	private boolean isConnected = false;

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

	public GraphicInterface(DataBaseListener dbListener) {
		this.dbListener = dbListener;

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
		connectDataItem.addActionListener(e -> showConnectionDialogs());

		JMenuItem disconnectDataItem = createMenuItem("Disconnect", "disconnect from database");
		disconnectDataItem.addActionListener(e -> showDisconnectionDialogs());

		JMenuItem exitProgramItem = createMenuItem("Exit");
		exitProgramItem.addActionListener(e -> dispose());

		res.add(connectDataItem);
		res.add(disconnectDataItem);
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
		aboutProgramItem.addActionListener(e -> showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
										"Version v1.0.0",
										"information")
		);

		res.add(showHelpItem);
		res.add(aboutProgramItem);

		return res;
	}

	private JMenu createReadyRequestsMenu() {
		JMenu res = createSubMenu("Ready request", "choose ready request");

		return res;
	}

	private JPanel createMainPanel() {
		JPanel res = new JPanel();

		return res;
	}

	private void showConnectionDialogs() {
		if (isConnected) {
			showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
				"Вы уже подключены к базе данных: " + dbListener.getDBName(),
				"database connection");

			return;
		}

		try (Connection connection = dbListener.getConnection()) {
			Object message = "Добро пожаловать, Товарищ! ";

			JOptionPane connectionPane = new JOptionPane(
				null,
				JOptionPane.QUESTION_MESSAGE,
				JOptionPane.DEFAULT_OPTION,
				null,				//For Icon
				new Object[] {}
			);

			JDialog connectionDialog = connectionPane.createDialog(this, "W&R:SR - data service: database connection");

			Object[] buttons = null;

			JButton cancelButton = createButton("Отменить подключение");
			cancelButton.addActionListener(e -> connectionDialog.dispose());

			if (dbListener.areTablesExist(connection)) {
				message += "База данных уже существует.";

				JButton createDbButton = createButton("Пересоздать базу данных");
				createDbButton.addActionListener(e -> recreateBD(connection, connectionDialog));

				JButton useDbButton = createButton("Использовать существующую");
				useDbButton.addActionListener(e -> useBD(connection, connectionDialog));

				buttons = new Object[] { createDbButton, useDbButton, cancelButton };
			}
			else {
				message += "База данных пуста.";

				JButton createDbButton = createButton("Создать базу данных");
				createDbButton.addActionListener(e -> createBD(connection, connectionDialog));

				buttons = new Object[] { createDbButton, cancelButton };
			}


			connectionPane.setOptions(buttons);
			connectionPane.setMessage(message);
			connectionDialog.pack();
			connectionDialog.setVisible(true);

		}
		catch (Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
				"Возникли проблемы подключения к базе данных! Проверьте, что база данных "
				+ dbListener.getDBName() + "существует и к ней есть доступ.",
				"connection error");
		}
	}

	private void showDisconnectionDialogs() {
		if (!isConnected) {
			showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
				"Вы не подключены к базе данных",
				"database connection");

			return;
		}

		Object message = "Вы действительно хотите отключиться от базы данных: " + dbListener.getDBName() + "?";

		JOptionPane disconnectionPane = new JOptionPane(
			message,
			JOptionPane.QUESTION_MESSAGE,
			JOptionPane.DEFAULT_OPTION,
			null,				//For Icon
			new Object[] {}
		);

		JDialog disconnectionDialog = disconnectionPane.createDialog(this, "W&R:SR - data service: database disconnection");

		JButton yesButton = createButton("Да");
		yesButton.addActionListener(e -> {
			isConnected = false;
			disconnectionDialog.dispose();
		});

		JButton noButton = createButton("Нет");
		noButton.addActionListener(e -> disconnectionDialog.dispose());

		disconnectionPane.setOptions(new Object[] { yesButton, noButton });
		disconnectionDialog.pack();
		disconnectionDialog.setVisible(true);
	}

	private void showCustomOkOptionDialog(int msgType, String message, String headMsg) {
		JOptionPane customPane = new JOptionPane(
			message,
			msgType,
			JOptionPane.OK_OPTION,
			null,				//For Icon
			new Object[] {}
		);

		JDialog customDialog = customPane.createDialog(this, "W&R:SR - data service: " + headMsg);

		JButton okButton = createButton("OK");
		okButton.addActionListener(e -> customDialog.dispose());

		customPane.setOptions(new Object[] { okButton });

		customDialog.setVisible(true); 
	}



	private void recreateBD(Connection connection, JDialog dialog) {
		try {
			dbListener.createNewDatabase(connection);
			isConnected = true;
			dialog.dispose();
		}
		catch(Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
				"Возникли проблемы создания базы данных! Проверьте, что к базе данных есть доступ.",
				"create error");
		}
	}

	private void createBD(Connection connection, JDialog dialog) {
		try {
			dbListener.createAndFillDatabase(connection);
			isConnected = true;
			dialog.dispose();
		}
		catch(Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
				"Возникли проблемы создания базы данных! Проверьте, что к базе данных есть доступ.",
				"create error");
		}
	}

	private void useBD(Connection connection, JDialog dialog) {
		isConnected = true;
		dialog.dispose();
	}
}