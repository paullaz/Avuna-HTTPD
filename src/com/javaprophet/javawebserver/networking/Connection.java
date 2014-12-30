package com.javaprophet.javawebserver.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import com.javaprophet.javawebserver.JavaWebServer;
import com.javaprophet.javawebserver.http.ContentEncoding;
import com.javaprophet.javawebserver.http.ResponseGenerator;
import com.javaprophet.javawebserver.networking.packets.RequestPacket;
import com.javaprophet.javawebserver.networking.packets.ResponsePacket;

/**
 * Handles a single connection.
 */
public class Connection extends Thread {
	public final Socket s;
	public final DataInputStream in;
	public final DataOutputStream out;
	public static final ResponseGenerator rg = new ResponseGenerator();
	
	public Connection(Socket s, DataInputStream in, DataOutputStream out) {
		this.s = s;
		this.in = in;
		this.out = out;
	}
	
	public void run() {
		while (!s.isClosed()) {
			try {
				RequestPacket incomingRequest = RequestPacket.read(in);
				if (incomingRequest == null) {
					s.close();
					return;
				}
				incomingRequest.userIP = s.getInetAddress().getHostAddress();
				incomingRequest.userPort = s.getPort();
				System.out.println(incomingRequest.toString());
				ResponsePacket outgoingResponse = new ResponsePacket();
				outgoingResponse.request = incomingRequest;
				JavaWebServer.pluginBus.processPacket(incomingRequest);
				rg.process(incomingRequest, outgoingResponse); // TODO: pipelining queue
				JavaWebServer.pluginBus.processPacket(outgoingResponse);
				ContentEncoding use = ContentEncoding.identity;
				if (incomingRequest.headers.hasHeader("Accept-Encoding")) {
					String[] ces = incomingRequest.headers.getHeader("Accept-Encoding").value.split(",");
					ContentEncoding[] ces2 = new ContentEncoding[ces.length];
					for (int i = 0; i < ces.length; i++) {
						ces2[i] = ContentEncoding.get(ces[i].trim());
					}
					
					for (ContentEncoding ce : ces2) {
						if (ce == ContentEncoding.gzip) {
							use = ce;
							break;
						}else if (ce == ContentEncoding.xgzip) {
							use = ce;
							break;
						}
					}
				}
				System.out.println(outgoingResponse.toString2(use));
				outgoingResponse.write(out, use);
			}catch (Exception ex) {
				ex.printStackTrace();
				try {
					s.close();
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		JavaWebServer.runningThreads.remove(this);
	}
}