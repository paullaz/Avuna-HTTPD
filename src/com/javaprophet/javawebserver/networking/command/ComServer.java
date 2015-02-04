package com.javaprophet.javawebserver.networking.command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import com.javaprophet.javawebserver.JavaWebServer;
import com.javaprophet.javawebserver.util.Logger;

public class ComServer extends Thread {
	public ComServer() {
		
	}
	
	public void run() {
		try {
			HashMap<String, Object> com = (HashMap<String, Object>)JavaWebServer.mainConfig.get("com");
			ServerSocket server = new ServerSocket(Integer.parseInt((String)com.get("bindport")), 10, InetAddress.getByName((String)com.get("bindip")));
			boolean doAuth = com.get("doAuth").equals("true");
			boolean isAuth = false;
			String auth = (String)com.get("auth");
			while (!server.isClosed()) {
				String ip = "";
				try {
					Socket s = server.accept();
					DataOutputStream out = new DataOutputStream(s.getOutputStream());
					out.flush();
					DataInputStream in = new DataInputStream(s.getInputStream());
					out.write(("Java Web Server(JWS) Version " + JavaWebServer.VERSION + JavaWebServer.crlf + (doAuth ? "Please Authenticate." + JavaWebServer.crlf : "")).getBytes());
					out.flush();
					Scanner scan = new Scanner(in);
					PrintStream ps = new PrintStream(out);
					ip = s.getInetAddress().getHostAddress();
					while (!s.isClosed()) {
						String cs = scan.nextLine();
						if (doAuth && !isAuth) {
							if (cs.equals(auth)) {
								isAuth = true;
								Logger.log("com[" + s.getInetAddress().getHostAddress() + "] Authenticated.");
								out.write(("Authenticated." + JavaWebServer.crlf).getBytes());
							}else {
								Logger.log("com[" + s.getInetAddress().getHostAddress() + "] NOAUTH/DENIED: " + cs);
								out.write(("Please Authenticate." + JavaWebServer.crlf).getBytes());
							}
						}else {
							Logger.log("com[" + s.getInetAddress().getHostAddress() + "]: " + cs);
							CommandProcessor.process(cs, ps, scan, true);
							out.write(("Command Completed." + JavaWebServer.crlf).getBytes());
						}
						out.flush();
					}
				}catch (IOException se) {
					Logger.log("com[" + ip + "] Closed.");
				}finally {
					isAuth = false;
				}
			}
		}catch (Exception e) {
			Logger.logError(e);
		}
	}
}
