package gui;


import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import mediator.Mediator;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JLabel;

import org.apache.log4j.Logger;

import net.miginfocom.swing.MigLayout;

/**
 * Class for rendering the Graphical User Interface.
 *
 */
public class GUI {
	
	
	private final Mediator mediator;
	private JFrame frmProiectIdp;

	private JTable table_1;
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
	private JButton addFileButton;//= new JButton(addFileString);
	private JButton removeFileButton;// = new JButton(removeFileString);
	JButton startTranButton;// = new JButton(startTranString);
	private JTextField fileNameToAdd;
	
	private String selectedUser ="";
	private StatusBar statusBar;
	private String currentUser = "";
	private JPanel buttonPane;
	private JLabel nameLabel;
	private static Logger log = Logger.getLogger("Network ");
	

	/**
	 * Constructor, receives an instance of the mediator class and gets the username of the current user.
	 * @param mediator
	 */
	public GUI(Mediator mediator) {
		this.mediator = mediator;
		this.currentUser = mediator.getUserName();
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
					frmProiectIdp.addWindowListener(new java.awt.event.WindowAdapter() {
				        public void windowClosing(WindowEvent winEvt) {
				        	mediator.logOut();
				            System.exit(0);
				        }
				    });
					window.frmProiectIdp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create image buttons for the add_file, delete_file and start_transfer options.
	 */
	private void createImageButton() {
		ImageIcon addF = new ImageIcon("images/file_add.png");
        ImageIcon thumbnailIcon = new ImageIcon(getScaledImage(addF.getImage(), 23, 35));
        
        addFileButton = new JButton(thumbnailIcon);
        ImageIcon addD = new ImageIcon("images/file_delete.png");
        thumbnailIcon = new ImageIcon(getScaledImage(addD.getImage(), 23, 35));
        removeFileButton = new JButton(thumbnailIcon);
        ImageIcon StartT = new ImageIcon("images/Play.png");
        thumbnailIcon = new ImageIcon(getScaledImage(StartT.getImage(), 23, 35));
        startTranButton = new JButton(thumbnailIcon);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		createImageButton();
		
		// initialize the content of the file and user lists
		filesList = new JList<String>(filesModel);
		
		ListSelectionListener userListSelectionListener = new ListSelectionListener()  {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				String userName = (String)usersList.getSelectedValue();
				if (userName != null) {
					selectedUser = userName;
					filesModel.clear();

					// asynchronous invocation - Callback will set files.
					mediator.getFilesFromUsers(selectedUser);
					
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
		filesList.addMouseListener(mouseFileListener);
		filesList.addListSelectionListener(addListSelectionListener);
		
		frmProiectIdp = new JFrame();
		frmProiectIdp.setTitle("NanoShare");
		frmProiectIdp.setBounds(100, 100, 1002, 560);
		frmProiectIdp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmProiectIdp.getContentPane().setLayout(new MigLayout("", "[1592px][282px]", "[32px][11px][485px][8px][476px][14px]"));
		
		
		filesPane = new JScrollPane(filesList);
		frmProiectIdp.getContentPane().add(filesPane, "cell 0 2,grow");
		
		new JTable(new TableModel());
		
		usersPane = new JScrollPane();
		frmProiectIdp.getContentPane().add(usersPane, "cell 1 0 1 5,grow");
		usersList = new JList<String>(usersModel);
		usersPane.setViewportView(usersList);
		
		usersList.addListSelectionListener(userListSelectionListener);
		
		scrollPane = new JScrollPane();
		frmProiectIdp.getContentPane().add(scrollPane, "cell 0 4,grow");
		
		
		table_1 = new JTable(transferTableData);
		scrollPane.setViewportView(table_1);
		table_1.setFillsViewportHeight(true);
		table_1.getColumnModel().getColumn(RowData.PROGRESS).setCellRenderer(new ProgressCellRender());
		table_1.addMouseListener(new TableMenu(table_1));
		
		JToolBar toolBar = new JToolBar();
		frmProiectIdp.getContentPane().add(toolBar, "cell 0 0,grow");
		
		statusBar = new StatusBar();
		frmProiectIdp.getContentPane().add(statusBar, "cell 0 5,alignx left,growy");

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
        frmProiectIdp.getContentPane().add(nameLabel, "cell 1 5,alignx left,aligny top");
        
        log.info("Initialized the GUI");
	}
	
	/**
     * Resizes an image to a desired width x height.
     * @param srcImg - source image to scale
     * @param w - desired width
     * @param h - desired height
     * @return - the new resized image
     */
    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
	
	/**
	 * Add an user to the application/data model and set the status bar to reflect it.
	 * @param username
	 */
	public void addUser(String username) {
		usersModel.addElement(username);
		statusBar.setText(username + " has logged in.");
		log.info("Added the user " + username);
	}
	
	/**
	 * Remove an user from the application/data model and set the status bar to reflect it.
	 * @param userName
	 */
	public void removeUser(String userName) {
		usersModel.removeElement(userName);
		if (userName.equals(selectedUser)) {
			filesModel.clear();
			selectedUser = "";
		}
		statusBar.setText(userName + " has logged out.");
		log.info("The user " + userName + " has logged out.");
	}
	
	/**
	 * Add a file to an user and set the status bar to reflect it.
	 * @param user
	 * @param fileName
	 */
	public void addFileToUser(String user, String fileName) {
		if (user.equals(selectedUser)) {
			filesModel.addElement(fileName);
			statusBar.setText(user + " has shared file " + fileName + ".");
			log.info("The user " + user + " has shared file " + fileName);
		}
	}
	
	/**
	 * Remove a file from an user and set the status bar to reflect it.
	 * @param user
	 * @param fileName
	 */
	public void removeFileFromUser(String user, String fileName) {
		if (user.equals(selectedUser)) {
			int index = filesList.getSelectedIndex();
	        filesModel.remove(filesList.getSelectedIndex());
	        statusBar.setText(user + " has stopped sharing file " + fileName + ".");
	        log.info("The user " + user + " has removed the shared file " + fileName);
	        
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
	}
	
	/**
	 * Check if an user has already transfered a file.
	 * @param user
	 * @param file
	 * @return boolean if the transfer exists in the downloads section already.
	 */
	private boolean transferExists(String user, String file) {
		for (int i = 0; i < table_1.getRowCount(); i++) {
			if (transferTableData.getValueAt(i, RowData.SOURCE).equals(user) && 
					transferTableData.getValueAt(i, RowData.NAME).equals(file)) {
				if (transferTableData.getValueAt(i, RowData.STATUS) != Status.Completed) {
					log.info("The transfer already exists");
					return true;
				} else {
					transferTableData.removeRow(i);
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * Add another transfer.
	 * @param source = source of the file transfer
	 * @param dest = destionation of the file transfer
	 * @param fileName = file to be transferred
	 * @param sending = if the file is being downloaded/uploaded by the user we are logged in with in the application
	 * @return = index of transfer being initialized
	 */
	public int addTransfer(String source, String dest, String fileName, boolean sending) {
		Status status;
		if (sending) {
			status = Status.Sending;
			statusBar.setText("Sending file \"" + fileName + "\" to " + dest + ".");
			log.info("Sending file \"" + fileName + "\" to " + dest);
		} else {
			status = Status.Receiving;
			statusBar.setText("Receiving file \"" + fileName + "\" from " + source + ".");
			log.info("Receiving file \"" + fileName + "\" from " + source);
		}
		
		final RowData newRow = new RowData(source, dest, fileName, 0f, status);
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				transferTableData.addRow(newRow);
			}
		});
		
		return newRow.getId();
	}

	/**
	 * Update the download progress bar.
	 * @param id = transfer id that we must update
	 * @param val
	 * @param speed = speed of download
	 */
	public void updateProgress(int id, Float val, int speed) {
		if (val == 1) {
			RowData transfer = transferTableData.getById(id);
			statusBar.setText("Transfer of file \"" + transfer.getCol(RowData.NAME) + "\" is complete.");
		}
		transferTableData.updateProgress(val, id, speed);
	}
	
	/**
	 * Add another file to the current user available to download file list.
	 * Check if the file already exists in the user's file list.
	 */
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
			log.info("Added the file " + text + " to the user " + selectedUser);
			mediator.addFileToUser(selectedUser, text);
		}
	};
	
	/**
	 * Remove a file from the current user available to download file list.
	 */
	class RemoveFileListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			log.info("Removed the file " + filesList.getSelectedValue() + " from the user " + selectedUser);
			mediator.removeFileFromUser(selectedUser, filesList.getSelectedValue());
		}
    }

	public void updateFilesForUser(String[] files) {
		for (String file : files)
			filesModel.addElement(file);
	};
}