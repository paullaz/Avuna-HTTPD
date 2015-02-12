package com.javaprophet.javawebserver.dns;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by JavaProphet on 8/13/14 at 10:56 PM.
 */
public class TCPServer extends Thread implements IServer {
	public TCPServer() {
		
	}
	
	public void run() {
		try {
			ServerSocket server = new ServerSocket(53);
			while (!server.isClosed()) {
				final Socket s = server.accept();
				s.setSoTimeout(1000);
				final DataOutputStream out = new DataOutputStream(s.getOutputStream());
				out.flush();
				final DataInputStream in = new DataInputStream(s.getInputStream());
				ThreadDNSWorker.addWork(new WorkTCP(s, in, out));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
