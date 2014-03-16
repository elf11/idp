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

import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import mediator.Mediator;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JLabel;

public class GUI {
	
	
	private final Mediator mediator;
	private JFrame frmProiectIdp;

	private JTable table;
	private JScrollPane scrollPane;
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
	JButton startTranButton = new JButton(startTranString);
	private JTextField fileNameToAdd;
	
	private String selectedUser ="";
	private StatusBar statusBar;
	private String currentUser;
	private JPanel buttonPane;
	private JLabel nameLabel;
	
	public GUI(Mediator mediator) {
		this.mediator = mediator;
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
					for (String file : mediator.getUsers().get(userName))
						filesModel.addElement(file);
					
					if (selectedUser.equals(currentUser)) {
						addFileButton.setEnabled(true);
					} else {
						addFileButton.setEnabled(false);
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
				
				if (filesList.getSelectedIndex() != -1) {
					startTranButton.setEnabled(true);
				} else {
					startTranButton.setEnabled(false);
				}
				
				if (!transferExists(selectedUser, fileName)) {
					mediator.newOutgoingTransfer(selectedUser, fileName);
				}
			}
		};
		
		
		ListSelectionListener addListSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
				 if (filesList.getSelectedIndex() != -1) {
					 boolean check = currentUser.equals(selectedUser);
					 removeFileButton.setEnabled(check);
					 startTranButton.setEnabled(!check);
				 } else {
					 removeFileButton.setEnabled(false);
					 startTranButton.setEnabled(false);
				 }
            }
        };
		
		usersList.addListSelectionListener(userListSelectionListener);
		filesList.addMouseListener(mouseFileListener);
		filesList.addListSelectionListener(addListSelectionListener);
		
		frmProiectIdp = new JFrame();
		frmProiectIdp.setTitle("NanoShare");
		frmProiectIdp.setBounds(100, 100, 1280, 720);
		frmProiectIdp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmProiectIdp.getContentPane().setLayout(null);
		
		
		filesPane = new JScrollPane(filesList);
		filesPane.setBounds(0, 33, 1065, 350);
		frmProiectIdp.getContentPane().add(filesPane);
		
		table = new JTable(new TableModel());
		
		usersPane = new JScrollPane(usersList);
		usersPane.setBounds(1077, 0, 180, 656);
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
		statusBar.setBounds(10, 660, 678, 14);
		frmProiectIdp.getContentPane().add(statusBar);

		fileNameToAdd = new JTextField(10);
		
		addFileButton.setEnabled(false);
		addFileButton.addActionListener(new AddFile());
		addFileButton.setActionCommand(addFileString);
		
		
		removeFileButton.setActionCommand(removeFileString);
		removeFileButton.addActionListener(new RemoveFileListener());
		removeFileButton.setEnabled(false);
		
		startTranButton.setEnabled(false);
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
        
        nameLabel = new JLabel("Logged in as " + currentUser);
        nameLabel.setBounds(1077, 660, 180, 14);
        frmProiectIdp.getContentPane().add(nameLabel);
	}
	
	public void addUser(String username) {
		usersModel.addElement(username);
		statusBar.setText(username + " has logged in.");
	}
	
	public void addFileToUser(String user, String fileName) {
		if (user.equals(selectedUser)) {
			filesModel.addElement(fileName);
			statusBar.setText(user + " has shared file " + fileName + ".");
		}
	}
	
	public void removeFileFromUser(String user, String fileName) {
		int index = filesList.getSelectedIndex();
        filesModel.remove(filesList.getSelectedIndex());
        statusBar.setText(user + " has stopped sharing file " + fileName + ".");
        
        if (filesModel.isEmpty()) {
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
		if (val == 1) {
			RowData transfer = transferTableData.getById(id);
			statusBar.setText("Transfer of file \"" + transfer.getCol(RowData.NAME) + "\" is complete.");
		}
		transferTableData.updateProgressBar(val, id);
	}
	
	class AddFile implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String text = "";
			text = fileNameToAdd.getText();
			fileNameToAdd.setText("");
			
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
			mediator.addFileToUser(selectedUser, text);
		}
	};
	
	class RemoveFileListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			mediator.removeFileFromUser(selectedUser, filesList.getSelectedValue());
		}
    };
}
