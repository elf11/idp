package gui;


import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
		
		ActionListener startAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String fileName = (String)filesList.getSelectedValue();
				mediator.newOutgoingTransfer(selectedUser, fileName);
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
		
	    AddFileListener addFileListener = new AddFileListener(addFileButton);
		addFileButton.setActionCommand(addFileString);
		addFileButton.setEnabled(false);

		removeFileButton.setActionCommand(removeFileString);
//		if (selectedUser.equals(currentUser)) {
			removeFileButton.addActionListener(new RemoveFileListener());
//		}
		
        JButton startTranButton = new JButton(startTranString);
        startTranButton.addActionListener(startAction);

        fileNameToAdd = new JTextField(10);
        fileNameToAdd.addActionListener(addFileListener);
        fileNameToAdd.getDocument().addDocumentListener(addFileListener);


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
	}
	
	public void addFileToUser(String fileName) {
		
	}
	
	public void removeFileFromUser(String fileName) {
		if (selectedUser == currentUser) {
			removeFileButton.addActionListener(new RemoveFileListener());
		}
	}
	

	public int addTransfer(String source, String dest, String fileName, boolean sending) {
		Status status;
		if (sending) {
			status = Status.Sending;
		} else {
			status = Status.Receiving;
		}
		
		RowData newRow = new RowData(source, dest, fileName, 0f, status);
		transferTableData.addRow(newRow);
		return transferTableData.getRowCount() - 1;
	}

	public void updateProgress(int row, Float i) {
		transferTableData.updateProgressBar(i, row);	
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
	
	class AddFileListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public AddFileListener(JButton button) {
            this.button = button;
        }

        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            String name = fileNameToAdd.getText();

            //User didn't type in a unique name...
            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                fileNameToAdd.requestFocusInWindow();
                fileNameToAdd.selectAll();
                return;
            }

            int index = filesList.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }

            filesModel.insertElementAt(fileNameToAdd.getText(), index);
            //If we just wanted to add to the end, we'd do this:
//            filesModel.addElement(fileNameToAdd.getText());

            fileNameToAdd.requestFocusInWindow();
            fileNameToAdd.setText("");

            filesList.setSelectedIndex(index);
            filesList.ensureIndexIsVisible(index);
        }

        protected boolean alreadyInList(String name) {
            return filesModel.contains(name);
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }
}
