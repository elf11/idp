package gui;

import java.awt.EventQueue;
import java.awt.ScrollPane;

import javax.swing.JFrame;
import javax.swing.JRadioButton;

import java.awt.BorderLayout;

import javax.swing.JTable;
import javax.swing.ButtonGroup;

import java.awt.Color;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JScrollBar;
import javax.swing.JToolBar;

import mediator.Mediator;

public class GUI {
	private static final long serialVersionUID = 1L;
	
	private final Mediator mediator;
	private JFrame frmProiectIdp;

	private JTable table;
	private JScrollPane scrollPane;
	
	private ProgressCellRender renderer;
	
	public GUI(Mediator mediator) {
		this.mediator = mediator;
		initialize();
	}
	
	/*
	 * panel for users and files, list of users and files
	 */
	private JScrollPane usersPane;
	private JScrollPane filesPane;
	private JList usersList;
	private JList filesList;
	private DefaultListModel usersModel = new DefaultListModel();
	private DefaultListModel filesModel = new DefaultListModel();
	private TableModel transferTableData = new TableModel();
	
	/*
	 * Hash map to associate each user with it's list of files
	 */
	private HashMap<String, Vector<String>> users = new HashMap<String, Vector<String>>();
	
	final int VERTICAL_SPLIT = 400;
	final int HORIZONTAL_SPLIT = 600;

	/**
	 * Launch the application.
	 */
	public void start() {
		final GUI window = this;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.frmProiectIdp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// initialize the content of the file and user lists
		filesList = new JList(filesModel);
		usersList = new JList(usersModel);
		
		frmProiectIdp = new JFrame();
		frmProiectIdp.setTitle("Proiect IDP");
		frmProiectIdp.setBounds(100, 100, 1280, 720);
		frmProiectIdp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmProiectIdp.getContentPane().setLayout(null);
		
		
		filesPane = new JScrollPane();
		filesPane.setBounds(0, 33, 1065, 404);
		frmProiectIdp.getContentPane().add(filesPane);
		
		table = new JTable(new TableModel());
		
		usersPane = new JScrollPane();
		usersPane.setBounds(1077, 0, 180, 680);
		frmProiectIdp.getContentPane().add(usersPane);
		
		
		table = new JTable(transferTableData);
		table.setBounds(10, 449, 1055, 221);
		table.setFillsViewportHeight(true);
		table.getColumnModel().getColumn(RowData.PROGRESS).setCellRenderer(new ProgressCellRender());
		
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(0, 449, 1065, 232);
		frmProiectIdp.getContentPane().add(scrollPane);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(0, 0, 1065, 27);
		frmProiectIdp.getContentPane().add(toolBar);
		
	}

	public int addTransfer(String source, String dest, String fileName, Float progress, boolean sending) {
		Status status;
		if (sending) {
			status = Status.Sending;
		} else {
			status = Status.Receiving;
		}
		
		RowData newRow = new RowData(source, dest, fileName, progress, status);
		transferTableData.addRow(newRow);
		return transferTableData.getRowCount() - 1;
	}

	public void updateProgress(int row, Float i) {
		transferTableData.updateProgressBar(i, row);	
	}
}
