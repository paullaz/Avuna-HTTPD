/*	Avuna HTTPD - General Server Applications
    Copyright (C) 2015 Maxwell Bruce

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package org.avuna.httpd.mail.smtp;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import org.avuna.httpd.AvunaHTTPD;
import org.avuna.httpd.mail.mailbox.EmailAccount;
import org.avuna.httpd.util.Logger;

public class SMTPWork {
	public Socket s;
	public DataInputStream in;
	public DataOutputStream out;
	public boolean ssl;
	public int state = 0;
	public boolean isExtended = false;
	public EmailAccount authUser = null;
	public String mailFrom = "";
	public long sns = 0L;
	public int tos = 0;
	public ArrayList<String> rcptTo = new ArrayList<String>();
	public ArrayList<String> data = new ArrayList<String>();
	public ByteArrayOutputStream sslprep = null;
	
	public SMTPWork(Socket s, DataInputStream in, DataOutputStream out, boolean ssl) {
		this.s = s;
		this.in = in;
		this.out = out;
		this.ssl = ssl;
		if (ssl) {
			sslprep = new ByteArrayOutputStream();
		}
	}
	
	public void writeMLine(int response, String data) throws IOException {
		Logger.log(hashCode() + ": " + response + "-" + data);
		out.write((response + "-" + data + AvunaHTTPD.crlf).getBytes());
	}
	
	public void writeLine(int response, String data) throws IOException {
		Logger.log(hashCode() + ": " + response + " " + data);
		out.write((response + " " + data + AvunaHTTPD.crlf).getBytes());
	}
}
