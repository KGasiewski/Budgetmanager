package program2;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Budget2 {
	static String initialAmount;
	static double sumAll = 0;

	static public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
	static String[] options = { "House", "Food", "Clothes", "Travel", "Entertainment", "Other" };

	public static void main(String[] args) {
		// ************************************
		JFrame frameStart = new JFrame();
		JLabel label = new JLabel("", JLabel.CENTER);

		frameStart.setVisible(true);
		JButton buttOk = new JButton("OK");
		JPanel startPanel = new JPanel();
		startPanel.add(new JLabel("enter the basic balance: "));
		JTextField sumAllStart = new JTextField(10);
		startPanel.add(sumAllStart);
		startPanel.add(buttOk, BorderLayout.SOUTH);
		frameStart.add(startPanel);

		frameStart.pack();
		frameStart.setLocation(450, 300);

		JFrame frame = new JFrame();

		ActionListener startOK = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				sumAll = checkAmount(sumAllStart);

				if (sumAll != 0) {
					initialAmount = Double.toString(sumAll);

					frameStart.dispose();
					frame.setVisible(true);
					viewAmounts(label, initialAmount, sumAll);
				}

			}

		};

		buttOk.addActionListener(startOK);

		JPanel panel = new JPanel();

		// DefaultListModel<String> listModel = new DefaultListModel<>();
		DefaultListModel<Expense> listModel = new DefaultListModel<>();
		JList<Expense> list = new JList<>(listModel);
		ArrayList<Expense> list2 = new ArrayList<>();

		DefaultListModel<Expense> listModel3 = new DefaultListModel<>();

		// Adding the list to the main panel

		frame.setSize(600, 600);
		frame.setLocation(450, 300);

		JButton butt1 = new JButton("Add an expense");
		JButton butt2 = new JButton("Delete the expense");
		JButton butt3 = new JButton("Save file");
		JButton butt4 = new JButton("Load file");
		JButton butt5 = new JButton("Close");

		panel.add(butt1);
		panel.add(butt2);
		panel.add(butt3);
		panel.add(butt4);
		panel.add(butt5);
		frame.add(panel, BorderLayout.NORTH);
		frame.add(label, BorderLayout.SOUTH);

		// Add an expense

		ActionListener myLisner1 = new ActionListener() {
			JFrame frame2 = new JFrame();

			public void actionPerformed(ActionEvent e) {
				
				frame2.setVisible(true);
				frame2.setSize(300, 300);
				frame2.setLocation(450, 300);
				frame2.setTitle("Program with List and Text Fields");
				JButton addExpense = new JButton("Add an expense");

				frame2.add(addExpense, BorderLayout.SOUTH);

				// Create a list to choose from
				JComboBox listExpenses = new JComboBox(options);

				// Creating text boxes
				JTextField poleTekstowe = new JTextField("", 20);
				JTextField fieldAmountsEuro = new JTextField("", 20);

				// Creating a panel
				JPanel panel2 = new JPanel(new FlowLayout());
				panel2.add(new JLabel("Select expense type:"));
				panel2.add(listExpenses);
				panel2.add(new JLabel("Details:"));
				panel2.add(poleTekstowe);
				panel2.add(new JLabel("Euro amount:"));
				panel2.add(fieldAmountsEuro);

				// Adding a panel to the main window

				frame2.add(panel2);

				ActionListener myLisner10 = new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						String selectedElement = (String) listExpenses.getSelectedItem();
						String enteredText = poleTekstowe.getText();
						String EuroAmountEntered = fieldAmountsEuro.getText();

						double tmp = checkAmount(fieldAmountsEuro);

						if (tmp != 0) {
							sumAll = sumAll - tmp;
						
							list2.add(AddCost(selectedElement, enteredText, tmp));
							listModel.clear();
							for (Expense expense : list2) {
								listModel.addElement(expense);
							}
							
			//				dodajStream(listModel, list2);
				//			System.out.println(list2);

							viewAmounts(label, initialAmount, sumAll);

							poleTekstowe.setText("");
							fieldAmountsEuro.setText("");
							listExpenses.setSelectedItem("House");
							frame2.dispose();
						}

					}
				};
				addExpense.addActionListener(myLisner10);

			}
		};
		// saving to file

		ActionListener myLisner3 = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				saveToFile(initialAmount, sumAll, listModel);

			}
		};
		// file loading
		ActionListener myLisner4 = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				loadFromFile(label, listModel, list2);

			}
		};

		// deleting an expense

		ActionListener myLisner2 = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				sumAll = sumAll + DeleteCost(list, list2);

				viewAmounts(label, initialAmount, sumAll);

				listModel.clear();
				for (Expense expense : list2) {
					listModel.addElement(expense);

				}
				
		//		dodajStream(listModel, list2);

			}
		};
		ActionListener myLisner5 = new ActionListener() { // zamkniecie programu

			public void actionPerformed(ActionEvent e) {
				int option  = JOptionPane.showConfirmDialog(butt5, "Do you want to close the program?",
						"Close Confirm", JOptionPane.YES_NO_OPTION);

				if (option  == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		};

		butt1.addActionListener(myLisner1);
		butt2.addActionListener(myLisner2);
		butt3.addActionListener(myLisner3);
		butt4.addActionListener(myLisner4);
		butt5.addActionListener(myLisner5);

		// Create a list based on the model

		frame.add(list);

		// ********************************

	}

	static void viewAmounts(JLabel label, String basic, Double sumAll) {

		label.setText("Starting balance:" + basic + " €    Current balance:" + sumAll + " €");

	}

	static double checkAmount(JTextField Text) {

		try {
			double result = Double.parseDouble(Text.getText());
			String s = Double.toString(result);

			if (s.contains(".")) {
				int decimalPlaces = s.length() - s.indexOf('.') - 1;
				if (decimalPlaces <= 2) {
					JOptionPane.showMessageDialog(null, "Introduced " + result, " Result",
							JOptionPane.INFORMATION_MESSAGE);
					return result;
				} else {
					JOptionPane.showMessageDialog(null, "An incorrect amount has been entered", "Error",
							JOptionPane.ERROR_MESSAGE);

				}
			} else {
				JOptionPane.showMessageDialog(null, "Introduced " + result, "result", JOptionPane.INFORMATION_MESSAGE);
				return result;

			}

		} catch (NumberFormatException ex) {

			JOptionPane.showMessageDialog(null, "An incorrect amount has been entered", "Error", JOptionPane.ERROR_MESSAGE);
			Text.setText("");
			return 0;
		}
		return 0;

	}

	static double DeleteCost(JList<Expense> list, ArrayList<Expense> list2) {

		int[] selected = list.getSelectedIndices();

		if (selected.length == 0) {
			JOptionPane.showMessageDialog(null, "Please select the item to be removed.", "Error",
					JOptionPane.ERROR_MESSAGE);

			return 0;

		} else {
			double x = list2.get(list.getSelectedIndex()).getCost();

			list2.remove(list.getSelectedIndex());

			return x;
		}

	}

	static Expense AddCost(String grup, String typ, double amount) {
		LocalTime x = LocalTime.now();// .format(DateTimeFormatter.ofPattern("HH:mm"));
		// format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));

		Expense expense = new Expense(grup, typ, amount, x);
		return expense;

	}

	static void saveToFile(String initial, Double sumAll, DefaultListModel<Expense> listModel) {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files (*.txt)", "txt");
		fileChooser.setFileFilter(filter);
		fileChooser.setSelectedFile(new File("BudgetManager.txt"));

		int option = fileChooser.showSaveDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {

				writer.write("Initial account balance " + initial + "€");
				writer.newLine();
				writer.write("Current account balance: " + sumAll + "€");
				writer.newLine();

				// Save data from DefaultListModel
				for (int i = 0; i < listModel.size(); i++) {
					writer.write((i + 1) + ": " + listModel.getElementAt(i));
					writer.newLine();
				}

				JOptionPane.showMessageDialog(null, "The data has been saved to the file.", "Sukces",
						JOptionPane.INFORMATION_MESSAGE);

			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "An error occurred while writing to the file.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
//	static void dodajStream(DefaultListModel<Expense> listModel, ArrayList<Expense> list) {
//		double x;
//		
//		for(String s:options) {
//		x=list.stream()
//		.filter(e->e.getgroup().equals(s))
//		.mapToDouble(e->e.getCost())
//		.sum();
//		
//		Expense tmp=new Expense(s, null);
//		tmp.setGroup(s);
//		tmp.setCost(x);
//		if(x!=0) {
//		listModel.addElement(tmp);
//		}
//		
//		}
//		
//	}

	static void loadFromFile(JLabel label, DefaultListModel<Expense> listModel, ArrayList<Expense> list) {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files (*.txt)", "txt");
		fileChooser.setFileFilter(filter);

		int option = fileChooser.showOpenDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();

			try {
				String s;
				BufferedReader read = new BufferedReader(new FileReader(selectedFile));

				while ((s = read.readLine()) != null) {

					String[] parts = s.split(" ");

					switch (parts[0]) {
					case "Initial": {
						initialAmount = parts[3].substring(0, parts[3].length() - 1);
						System.out.println("initial: " + initialAmount);
					}
						break;
					case "Current": {
						sumAll = Double.parseDouble(parts[3].substring(0, parts[3].length() - 1));
						System.out.println("Current: " + sumAll);
					}
						break;

					default: {
						Expense e = new Expense();
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < parts.length; i++) {
							if (i == 2) {
								e.setGroup(parts[i]);
							}
							if (i > 3 && i < (parts.length - 4)) {
								sb.append(parts[i] + " ");
							}
							if (i == (parts.length - 1)) {
								e.setTime(LocalTime.parse(parts[i]));
							}
							if (i == (parts.length - 3)) {
								e.setCost(Double.parseDouble(parts[i]));
							}

						}
						e.setTyp(sb.toString().trim());
						// Expense e = new Expense(parts[2], parts[2], parts[2], parts[2]);

						list.add(e);
						System.out.println(e);

					}
						break;
					}

				}
				read.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Error loading.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
				e.printStackTrace();
			}

			listModel.clear();
			for (Expense expense : list) {
				listModel.addElement(expense);
			}
			System.out.println(list);

			viewAmounts(label, initialAmount, sumAll);

			JOptionPane.showMessageDialog(null, "The data has been loaded from a file.", "Sukces",
					JOptionPane.INFORMATION_MESSAGE);

		}
	}

}
