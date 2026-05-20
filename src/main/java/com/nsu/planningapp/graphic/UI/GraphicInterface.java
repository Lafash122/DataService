package com.nsu.planningapp.graphic.UI;

import com.nsu.planningapp.graphic.*;
import com.nsu.planningapp.planningapp.dto.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.plaf.ScrollBarUI;
import java.awt.*;
import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

public class GraphicInterface extends JFrame {
	private boolean isConnected = false;
	private int DEFAULT_TEXT_SIZE = 16;

	private static final Color menuColor = new Color(235, 235, 235);
	private static final Color buttonColor = new Color(220, 220, 220);
	private static final Color contourColor = new Color(195, 195, 195);
	private static final Color headerColor = new Color(175, 218, 252);	// Blue-blue frost
	private static final Color tableColor = new Color(221, 238, 255);	// Pale blue
	private static final Color hoveredColor = new Color(213, 213, 227);
	//private static final Color pressedColor = new Color(203, 203, 222);
	private static final Color fontColor = Color.BLACK;

	private static final Font menuFont = new Font("Arial", Font.PLAIN, 12);
	private static final Font toolTipFont = new Font("Dubai", Font.PLAIN, 12);
	private static final Font defaultTextFont = new Font("Arial", Font.BOLD, 14);
	private Font tableFont = new Font("Ubuntu", Font.PLAIN, DEFAULT_TEXT_SIZE);

	private JTable resultTable;
	private DefaultTableModel tableModel;

	private DataBaseListener dbListener;

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
		setIconImage((new ImageIcon("src/main/resources/ico64.png")).getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setJMenuBar(createMenuBar());

		add(createMainPanel(), BorderLayout.CENTER);
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
		userRequestItem.addActionListener(e -> showUserRequestDialogs());

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

		JMenuItem residentCapacityItem = createMenuItem("1.\tКоличество жилья");
		residentCapacityItem.addActionListener(e -> showTotalResidentCapacityDialogs());

		JMenuItem buildingsByTypeItem = createMenuItem("2.\tПеречень зданий по типу");
		buildingsByTypeItem.addActionListener(e -> showBuildingsByTypeDialogs());

		

		res.add(residentCapacityItem);
		res.add(buildingsByTypeItem);
		

		return res;
	}

	private JPanel createMainPanel() {
		tableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		resultTable = new JTable(tableModel);

		resultTable.getTableHeader().setBackground(headerColor);
		resultTable.getTableHeader().setForeground(fontColor);
		resultTable.getTableHeader().setReorderingAllowed(false);
		resultTable.getTableHeader().setResizingAllowed(false);
		resultTable.getTableHeader().setFont(tableFont);

		resultTable.setBackground(tableColor);
		resultTable.setForeground(fontColor);
		resultTable.setFont(tableFont);
		resultTable.setRowHeight(DEFAULT_TEXT_SIZE + 4);

		JScrollPane resultScroll = new JScrollPane(resultTable,
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


		resultScroll.getVerticalScrollBar().setUI(new CustomScrollBarUI("src/main/resources/", hoveredColor, menuColor));
		resultScroll.getHorizontalScrollBar().setUI(new CustomScrollBarUI("src/main/resources/", hoveredColor, menuColor));

		JPanel resultArea = new JPanel(new BorderLayout());
		resultArea.add(resultScroll, BorderLayout.CENTER);

		return resultArea;
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
				"database disconnection");

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

	private void showUserRequestDialogs() {
		if (!isConnected) {
			showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
				"Вы не подключены к базе данных",
				"user request");

			return;
		}

		JDialog userRequestDialog = new JDialog(this, "W&R:SR - data service: user request", true);

		userRequestDialog.setVisible(true);
	}

	private void showTotalResidentCapacityDialogs() {
		if (!isConnected) {
			showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
				"Вы не подключены к базе данных",
				"ready request 1");

			return;
		}

		try {
			List<String> settlements = dbListener.getAllSettlements();
			String[] chooseOptions = new String[settlements.size() + 1];
			chooseOptions[0] = "Вся страна";
			for (int i = 1; i <= settlements.size(); i++)
				chooseOptions[i] = settlements.get(i - 1);

			JComboBox<String> settlementCombo = createCustomComboBox(chooseOptions);

			JPanel panel = createCustomPanel(new GridLayout(1, 2, 10, 10));
			panel.add(createTextLabel("Населенный пункт:"));
			panel.add(settlementCombo);

			showCustomOkCancelOptionDialog(panel, "количество жилья", () -> {
				String selectedSettlement = (String) settlementCombo.getSelectedItem();
				try {

					Integer settlementId = null;
					if (!selectedSettlement.equals("Вся страна")) {
						settlementId = dbListener.getSettlementId(selectedSettlement);
						if (settlementId == null) {
							showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
								"Город " + selectedSettlement + " не найден в базе",
								"query error");
					
							return;
						}
					}

					int capacity = dbListener.getTotalResidentCapacity(settlementId);

					showSingleNumberResult("Общая жилая вместимость", capacity);
				}
				catch (Exception e) {
					showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
						"Ошибка выполнения запроса 1: " + e.getMessage(),
						"query error");
				}
			});
		}
		catch (Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
				"Ошибка выполнения запроса 1: " + e.getMessage(),
				"query error");
		}
	}

	private void showBuildingsByTypeDialogs() {
		if (!isConnected) {
			showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
				"Вы не подключены к базе данных",
				"ready request 2");

			return;
		}

		try {
			List<String> types = dbListener.getAllBlueprintTypes();
			if (types.isEmpty()) {
				showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
					"Нет типов зданий", "query error");

				return;
			}

			List<String> settlements = dbListener.getAllSettlements();
			String[] settlementOptions = new String[settlements.size() + 1];
			settlementOptions[0] = "Вся страна";
			for (int i = 1; i <= settlements.size(); i++)
				settlementOptions[i] = settlements.get(i - 1);

			JComboBox<String> typeCombo = createCustomComboBox(types.toArray(new String[0]));
			JComboBox<String> settlementCombo = createCustomComboBox(settlementOptions);

			JPanel panel = createCustomPanel(new GridLayout(2, 2, 10, 10));
			panel.add(createTextLabel("Тип здания:"));
			panel.add(typeCombo);
			panel.add(createTextLabel("Населенный пункт:"));
			panel.add(settlementCombo);

			showCustomOkCancelOptionDialog(panel, "перечень зданий по типу", () -> {
				try {
					String selectedType = (String) typeCombo.getSelectedItem();
					String selectedSettlement = (String) settlementCombo.getSelectedItem();
					if (selectedSettlement.equals("Вся страна"))
						selectedSettlement = null;

					List<BuildingDto> buildings = dbListener.getBuildingsByType(selectedType, selectedSettlement);
					if (buildings.isEmpty()) {
						showBuildingDtoList(new ArrayList<>());

 						showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
							"Нет зданий типа '" + selectedType + "'" +
							(selectedSettlement == null ? " по всей стране" : " в городе " + selectedSettlement),
							"ready request 2");
					}
					else
						showBuildingDtoList(buildings);
				}
				catch (Exception e) {
					showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
						"Ошибка выполнения запроса 2: " + e.getMessage(),
						"query error");
				}
			});
		}
		catch (Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
				"Ошибка выполнения запроса 2: " + e.getMessage(),
				"query error");
		}
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

		customDialog.pack();
		customDialog.setVisible(true); 
	}

	private void showCustomOkCancelOptionDialog(JPanel panel, String headMsg, Runnable onOk) {
		JOptionPane customPane = new JOptionPane(panel,
				JOptionPane.PLAIN_MESSAGE,
				JOptionPane.DEFAULT_OPTION,
				null,
				new Object[]{});

		JDialog customDialog = customPane.createDialog(this, "W&R:SR - data service: " + headMsg);

		JButton okButton = createButton("Выполнить");
		JButton cancelButton = createButton("Отмена");
		customPane.setOptions(new Object[] { okButton, cancelButton });

		okButton.addActionListener(e -> {
			customDialog.dispose();
			if (onOk != null)
				onOk.run();
		});
		cancelButton.addActionListener(e -> customDialog.dispose());

		customDialog.pack();
		customDialog.setVisible(true);
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

	private JButton createButton(String title) {
		JButton res = new JButton(title);

		res.setFocusPainted(false);

		return res;
	}

	private JComboBox<String> createCustomComboBox(String[] items) {
		JComboBox<String> res = new JComboBox<>(items);

		res.setBackground(menuColor);
		res.setForeground(fontColor);
		res.setFont(defaultTextFont);

		return res;
	}

	private JPanel createCustomPanel(LayoutManager layout) {
		JPanel res = new JPanel(layout);

		res.setBackground(menuColor);

		return res;
	}

	private JLabel createTextLabel(String title) {
		JLabel res = new JLabel(title);

		res.setBackground(menuColor);
		res.setForeground(fontColor);
		res.setFont(defaultTextFont);

		return res;
	}



	private void clearTable() {
		tableModel.setRowCount(0);
		tableModel.setColumnCount(0);
	}

	private void showSingleNumberResult(String title, Number value) {
		clearTable();

		tableModel.addColumn(title);
		tableModel.addRow(new Object[]{ value });
	}

	private void showBuildingDtoList(List<BuildingDto> buildings) {
		clearTable();

		tableModel.addColumn("ID");
		tableModel.addColumn("Населенный пункт");
		tableModel.addColumn("Здание");
		tableModel.addColumn("Тип здания");

		for (BuildingDto b : buildings)
			tableModel.addRow(new Object[]{
				b.id(),
				b.settlementName(),
				b.blueprintName(),
				b.blueprintType()
			});
	}



	private void recreateBD(Connection connection, JDialog dialog) {
		try {
			dbListener.createNewDatabase(connection);
			isConnected = true;
			dialog.dispose();
		}
		catch (Exception e) {
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
		catch (Exception e) {
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