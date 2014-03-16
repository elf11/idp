package gui;


import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import mediator.Mediator;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

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
	private String addFileString = "Add file";
	private String removeFileString = "Remove file";
	private String startTranString = "Start Transfer";
	private JButton addFileButton = new JButton(addFileString);
	private JButton removeFileButton = new JButton(removeFileString);
	private JTextField fileNameToAdd;
	
	private String selectedUser;
	private StatusBar statusBar;
	private String currentUser;
	private JPanel buttonPane;
	
	public GUI(Mediator mediator) {
		this.mediator = mediator;
		this.users = mediator.getUsers();
		this.currentUser = mediator.getUsername();
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
					if (selectedUser.equals(currentUser)) {
						removeFileButton.setEnabled(true);
						removeFileButton.setEnabled(true);
					} else {
						removeFileButton.setEnabled(false);
					}
				}
			}
		};
		
		MouseAdapter mouseFileListener = new MouseAdapter() {
			 @Override
			 public void mouseClicked(MouseEvent evt) {
				 if (evt.getClickCount() == 2) {
					 String fileName = (String)filesList.getSelectedValue();
					 if (currentUser.equals(selectedUser)) {
						 statusBar.setText("Cannot transfer file from logged in user.");
						 return;
					 }
					 if (!transferExists(selectedUser, fileName)) {
						 mediator.newOutgoingTransfer(selectedUser, fileName);
					 }
				 }
			 }
		};
		
		ActionListener startAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String fileName = (String)filesList.getSelectedValue();
				if (!transferExists(selectedUser, fileName)) {
					mediator.newOutgoingTransfer(selectedUser, fileName);
				}
			}
		};
		
		ActionListener addFile = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = "";
				text = fileNameToAdd.getText();
				
				if (text.isEmpty()) {
					JOptionPane.showMessageDialog(
							null, "Name is empty!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (filesModel.contains(text)) {
					JOptionPane.showMessageDialog(
							null, "Name is duplicated!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				filesModel.addElement(text);
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

		fileNameToAdd = new JTextField(10);
		
		addFileButton.addActionListener(addFile);
		addFileButton.setActionCommand(addFileString);

		removeFileButton.setActionCommand(removeFileString);
		removeFileButton.addActionListener(new RemoveFileListener());
		removeFileButton.setEnabled(false);
		
        JButton startTranButton = new JButton(startTranString);
        startTranButton.addActionListener(startAction);


        buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(addFileButton);
        buttonPane.add(Box.createHorizontalStrut(1));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(1));
        buttonPane.add(fileNameToAdd);
        buttonPane.add(removeFileButton);
        buttonPane.add(startTranButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		
        toolBar.add(buttonPane);
	}
	
	public void addUser(String username, Vector<String> files) {
		users.put(username, files);
		usersModel.addElement(username);
		statusBar.setText(username + " has logged in.");
	}
	
	public void addFileToUser(String fileName) {
		
	}
	
	public void removeFileFromUser(String fileName) {
		if (selectedUser == currentUser) {
			removeFileButton.addActionListener(new RemoveFileListener());
		}
	}
	
	private boolean transferExists(String user, String file) {
		for (int i = 0; i < table.getRowCount(); i++) {
			if (transferTableData.getValueAt(i, RowData.SOURCE).equals(user) && 
					transferTableData.getValueAt(i, RowData.NAME).equals(file)) {
				if (transferTableData.getValueAt(i, RowData.STATUS) != Status.Completed) {
					return true;
				} else {
					transferTableData.removeRow(i);
					return false;
				}
			}
		}
		return false;
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
	
	class RemoveFileListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int index = filesList.getSelectedIndex();
            filesModel.remove(index);
            
            int size = filesModel.getSize();
            if (size == 0) { //Nobody's left, disable firing.
                removeFileButton.setEnabled(false);
            } else { //Select an index.
            	if (index == filesModel.getSize()) {
            		//removed item in last position
            		index--;
            	}
                filesList.setSelectedIndex(index);
                filesList.ensureIndexIsVisible(index);
            }
		}
    };
}
