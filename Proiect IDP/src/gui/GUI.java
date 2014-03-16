package gui;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



import mediator.Mediator;

public class GUI {
	
	
	private final Mediator mediator;
	private JFrame frmProiectIdp;

	private JTable table;
	private JScrollPane scrollPane;
	private HashMap<String, Vector<String>> users;
	private JScrollPane usersPane;
	private JScrollPane filesPane;
	private JList<String> usersList;
	private JList<String> filesList;
	private DefaultListModel<String> usersModel = new DefaultListModel<String>();
	private DefaultListModel<String> filesModel = new DefaultListModel<String>();
	private TableModel transferTableData = new TableModel();
	
	private String selectedUser;
	private StatusBar statusBar;
	
	public GUI(Mediator mediator) {
		this.mediator = mediator;
		this.users = mediator.getUsers();
		initialize();
	}
	
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
		filesList = new JList<String>(filesModel);
		usersList = new JList<String>(usersModel);
		
		ListSelectionListener userListSelectionListener = new ListSelectionListener()  {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				String userName = (String)usersList.getSelectedValue();
				if (userName != null) {
					selectedUser = userName;
					filesModel.clear();
					for (String file : users.get(userName))
						filesModel.addElement(file);
				}
			}
		};
		
		MouseAdapter mouseFileListener = new MouseAdapter() {
			 @Override
			 public void mouseClicked(MouseEvent evt) {
				 if (evt.getClickCount() == 2) {
					 String fileName = (String)filesList.getSelectedValue();
					for (int i = 0; i < table.getRowCount(); i++) {
						if (transferTableData.getValueAt(i, RowData.SOURCE).equals(selectedUser) && 
								transferTableData.getValueAt(i, RowData.NAME).equals(fileName)) {
							if (transferTableData.getValueAt(i, RowData.STATUS) != Status.Completed) {
								return;
							} else {
								transferTableData.removeRow(i);
								continue;
							}
						}
					}
					mediator.newOutgoingTransfer(selectedUser, fileName);
				 }
			 }
		};
		
		usersList.addListSelectionListener(userListSelectionListener);
		filesList.addMouseListener(mouseFileListener);
		
		frmProiectIdp = new JFrame();
		frmProiectIdp.setTitle("Proiect IDP");
		frmProiectIdp.setBounds(100, 100, 1280, 720);
		frmProiectIdp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmProiectIdp.getContentPane().setLayout(null);
		
		
		filesPane = new JScrollPane(filesList);
		filesPane.setBounds(0, 33, 1065, 350);
		frmProiectIdp.getContentPane().add(filesPane);
		
		table = new JTable(new TableModel());
		
		usersPane = new JScrollPane(usersList);
		usersPane.setBounds(1077, 0, 180, 680);
		frmProiectIdp.getContentPane().add(usersPane);
		
		
		table = new JTable(transferTableData);
		table.setBounds(10, 449, 1055, 221);
		table.setFillsViewportHeight(true);
		table.getColumnModel().getColumn(RowData.PROGRESS).setCellRenderer(new ProgressCellRender());
		table.addMouseListener(new TableMenu(table));
		
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(0, 394, 1065, 262);
		frmProiectIdp.getContentPane().add(scrollPane);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(0, 0, 1065, 27);
		frmProiectIdp.getContentPane().add(toolBar);
		
		statusBar = new StatusBar();
		statusBar.setBounds(10, 667, 678, 14);
		frmProiectIdp.getContentPane().add(statusBar);
	}
	
	public void addUser(String username, Vector<String> files) {
		users.put(username, files);
		usersModel.addElement(username);
		statusBar.setText(username + " has logged in.");
	}
	

	public int addTransfer(String source, String dest, String fileName, boolean sending) {
		Status status;
		if (sending) {
			status = Status.Sending;
			statusBar.setText("Sending file \"" + fileName + "\" to " + dest + ".");
		} else {
			status = Status.Receiving;
			statusBar.setText("Receiving file \"" + fileName + "\" from " + source + ".");
		}
		
		RowData newRow = new RowData(source, dest, fileName, 0f, status);
		transferTableData.addRow(newRow);
		return newRow.getId();
	}

	public void updateProgress(int id, Float val) {
		transferTableData.updateProgressBar(val, id);
	}
}
