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
				// TODO Auto-generated method stub
				String userName = (String)usersList.getSelectedValue();
				if (userName != null) {
					selectedUser = userName;
					System.out.println(userName);
					System.out.println(users.get(userName).size());
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
		filesPane.setBounds(0, 33, 1065, 404);
		frmProiectIdp.getContentPane().add(filesPane);
		
		table = new JTable(new TableModel());
		
		usersPane = new JScrollPane(usersList);
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
	
	public void addUser(String username, Vector<String> files) {
		users.put(username, files);
		usersModel.addElement(username);
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
