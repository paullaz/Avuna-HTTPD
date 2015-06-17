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

package org.avuna.httpd.mail.imap.command;

import java.io.IOException;
import javax.xml.bind.DatatypeConverter;
import org.avuna.httpd.hosts.HostMail;
import org.avuna.httpd.mail.imap.IMAPCommand;
import org.avuna.httpd.mail.imap.IMAPWork;
import org.avuna.httpd.mail.mailbox.EmailAccount;

public class IMAPCommandAuthenticateCont extends IMAPCommand {
	
	public IMAPCommandAuthenticateCont(String comm, int minState, int maxState, HostMail host) {
		super(comm, minState, maxState, host);
	}
	
	@Override
	public void run(IMAPWork focus, String letters, String[] args) throws IOException {
		String up = new String(DatatypeConverter.parseBase64Binary(letters)).substring(1);
		String username = up.substring(0, up.indexOf(new String(new byte[]{0})));
		String password = up.substring(username.length() + 1);
		String letters2 = focus.authMethod.substring(5);
		// System.out.println(username + ":" + password);
		EmailAccount us = null;
		for (EmailAccount e : host.accounts) {
			if (e.email.equals(username) && e.password.equals(password)) {
				us = e;
				break;
			}
		}
		if (us != null) {
			focus.writeLine(focus, letters2, "OK");
			focus.authUser = us;
			focus.state = 2;
		}else {
			focus.writeLine(focus, letters2, "NO Authenticate Failed.");
			focus.state = 0;
		}
	}
	
}
