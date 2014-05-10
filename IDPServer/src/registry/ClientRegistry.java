package registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.*;

import javax.sql.RowSet;

import com.sun.rowset.JdbcRowSetImpl;

public class ClientRegistry {
	
	public static HashMap<String, User> clients = new HashMap<>();
	public static HashMap<String, ArrayList<String>> files = new HashMap<>();
	private static int id = 0;
	private static final String url = "jdbc:mysql://127.0.0.1:3306/NanoShareDB";
	private static final String user = "nanoshare";
	private static final String pass = "idp";
	
	public ArrayList<String> getFilesFromUser(String user) {
		
		ArrayList<String> files = new ArrayList<String>();
		 try {
			Class.forName("com.mysql.jdbc.Driver");
			RowSet rs = new JdbcRowSetImpl(url, ClientRegistry.user, pass);
			rs.setCommand("SELECT file FROM files WHERE user='" + user + "'");
			rs.execute();
			
			while (rs.next()) {
				files.add(rs.getString(1));
			}
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return files;
	}
	
	public ArrayList<User> getClients() {
		
		ArrayList<User> clients = new ArrayList<User>();
		 try {
			Class.forName("com.mysql.jdbc.Driver");
			RowSet rs = new JdbcRowSetImpl(url, user, pass);
			rs.setCommand("SELECT * FROM users");
			rs.execute();
			
			while (rs.next()) {
				clients.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
			}
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clients;
	}
	
	public void addFilesToUser(String user, String[] newFiles) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url, ClientRegistry.user, pass);
			PreparedStatement st;
			for (String file : newFiles) {
				st = conn.prepareStatement("INSERT INTO files VALUES ('" + user + "', '" + file + "')");
				st.execute();
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void removeFilesFromUser(String user, String[] deletedFiles) {
		try {
			Connection conn = DriverManager.getConnection(url, ClientRegistry.user, pass);
			PreparedStatement st;
			for (String file : deletedFiles) {
				st = conn.prepareStatement("DELETE FROM files WHERE user='" + user + "' and file='" + file + "'");
				st.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int addUser(String name, String ip, int port) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url, ClientRegistry.user, pass);
			PreparedStatement st;
			st = conn.prepareStatement("SELECT * FROM users WHERE name='" + name + "'");
			if (st.executeQuery().isBeforeFirst()) {
				return 0;
			}
			id++;
			st = conn.prepareStatement("INSERT INTO users VALUES (" + id + ", '" + name + "', '" + ip + "', " + 
											port + ")");
			st.execute();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public void removeUser(String name) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url, ClientRegistry.user, pass);
			PreparedStatement st;
			st = conn.prepareStatement("DELETE FROM files WHERE user='" + name + "'");
			st.execute();
			st = conn.prepareStatement("DELETE FROM users WHERE name='" + name + "'");
			st.execute();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
