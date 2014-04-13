package junitTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import mediator.Mediator;
import mediator.TransferInfo;
import network.Network;
import network.OutgoingTransfer;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class NetworkComponentTest {

	@Test
	public void testListener() {
		
		new Network(null, "test/", "127.0.0.1", 30000);
		Socket sock = new Socket();
		try {
			sock.connect(new InetSocketAddress("127.0.0.1", 30000), 1000);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} finally {
			try {
				sock.close();
			} catch (IOException e) {
				e.printStackTrace();
				fail();
			}
		}
	}
	
	@Test
	public void testRequestFile() {
		
		new Network(null, "test/dir1/", "127.0.0.1", 30001);
		
		OutgoingTransfer transfer = new OutgoingTransfer(null, "test/dir2/", "inexistentFile");
		SocketChannel sock;
			
		
		try {
			sock = SocketChannel.open();
			sock.connect(new InetSocketAddress("127.0.0.1", 30001));
			transfer.requestTransfer(sock);
			assertEquals(-1, sock.read(ByteBuffer.allocate(10)));
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void transferFile() {
		
		TransferInfo trInfo = mock(TransferInfo.class);
		Mediator mediator = mock(Mediator.class);
		
		new Network(mediator, "test/dir1/", "127.0.0.1", 30002);
		
		OutgoingTransfer transfer = new OutgoingTransfer(trInfo, "test/dir2/", "testFile.txt");
		SocketChannel sock;
			
		
		try {
			sock = SocketChannel.open();
			sock.connect(new InetSocketAddress("127.0.0.1", 30002));
			transfer.requestTransfer(sock);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			transfer.processData(sock);
			
			sock.close();
			
			File file1 = new File("test/dir1/testFile.txt");
			File file2 = new File("test/dir2/testFile.txt");
			
			assertEquals("The files differ!", 
				    FileUtils.readFileToString(file1, "utf-8"), 
				    FileUtils.readFileToString(file2, "utf-8"));
			
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
}
