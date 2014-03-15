package gui;

import java.awt.EventQueue;
import java.awt.ScrollPane;

import javax.swing.JFrame;
import javax.swing.JRadioButton;

import java.awt.BorderLayout;

import javax.swing.JTable;
import javax.swing.ButtonGroup;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import java.awt.event.ActionListener;

import javax.swing.JScrollBar;

public class GUI {

	private JFrame frmProiectIdp;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final Action action = new SwingAction();
	private JTable table;
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmProiectIdp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmProiectIdp = new JFrame();
		frmProiectIdp.setTitle("Proiect IDP");
		frmProiectIdp.setBounds(100, 100, 1280, 720);
		frmProiectIdp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmProiectIdp.getContentPane().setLayout(null);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("New radio button");
		rdbtnNewRadioButton.setBounds(463, 128, 400, 69);
		JRadioButton button2 = new JRadioButton("Button2");
		button2.setBounds(840, 185, 63, 54);
		button2.setForeground(Color.RED);
		button2.setBackground(Color.GREEN);
		buttonGroup.add(rdbtnNewRadioButton);
		buttonGroup.add(button2);
		frmProiectIdp.getContentPane().add(rdbtnNewRadioButton);
		frmProiectIdp.getContentPane().add(button2);
		
		JTextPane textPane = new JTextPane();
		textPane.setBounds(203, 253, 296, 40);
		frmProiectIdp.getContentPane().add(textPane);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(167, 128, 89, 23);
		frmProiectIdp.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton_1.setAction(action);
		btnNewButton_1.setBounds(298, 169, 89, 23);
		frmProiectIdp.getContentPane().add(btnNewButton_1);
		
		table = new JTable(new TableModel());
		table.setBounds(10, 449, 1055, 221);
		table.setFillsViewportHeight(true);
		table.getColumnModel().getColumn(4).setCellRenderer(new ProgressCellRender());
		
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 449, 1055, 221);
		frmProiectIdp.getContentPane().add(scrollPane);
		
	}
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println("Hello World!");
		}
	}
}
