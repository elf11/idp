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
import javax.swing.SwingWorker;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JScrollBar;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GUI {
	
	private JFrame frmProiectIdp;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final Action action = new SwingAction();
	private JTable table;
	private JScrollPane scrollPane;
	
	/*
	 * panel for users and files, list of users and files
	 */
	private JScrollPane usersPane;
	private JScrollPane filesPane;
	private JList usersList;
	private JList filesList;
	private DefaultListModel usersModel = new DefaultListModel();
	private DefaultListModel filesModel = new DefaultListModel();
	
	/*
	 * Hash map to associate each user with it's list of files
	 */
	private HashMap<String, Vector<String>> users = new HashMap<String, Vector<String>>();

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
		
		// initialize the content of the file and user lists
		filesList = new JList(filesModel);
		usersList = new JList(usersModel);
		
		Vector<String> files1 = new Vector<String>();
		files1.add("movie.mp4");
		files1.add("so.pdf");
		files1.add("idp.pdf");
		
		Vector<String> files2 = new Vector<String>();
		files2.add("movie1.mp4");
		files2.add("so1.pdf");
		files2.add("idp1.pdf");
		
		Vector<String> files3 = new Vector<String>();
		files3.add("movie2.mp4");
		files3.add("so2.pdf");
		files3.add("idp2.pdf");
		
		addUser("mihai", files1);
		addUser("ionut", files2);
		addUser("andrei", files3);
		
		ListSelectionListener userListSelectionListener = new ListSelectionListener()  {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				// TODO Auto-generated method stub
				String userName = (String)usersList.getSelectedValue();
				if (userName != null) {
					System.out.println(userName);
					System.out.println(users.get(userName).size());
					filesModel.clear();
					for (String file : users.get(userName))
						filesModel.addElement(file);
				}
			}
		};
		
		ListSelectionListener fileListSelectionListener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				String fileName = (String)filesList.getSelectedValue();
			}
		};
		
		usersList.addListSelectionListener(userListSelectionListener);
		filesList.addListSelectionListener(fileListSelectionListener);
		
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
		
		table = new JTable(new TableModel());
		table.setBounds(10, 449, 1055, 221);
		table.setFillsViewportHeight(true);
		table.getColumnModel().getColumn(4).setCellRenderer(new ProgressCellRender());
		
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
