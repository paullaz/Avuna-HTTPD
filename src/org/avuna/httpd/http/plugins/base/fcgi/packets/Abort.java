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

package org.avuna.httpd.http.plugins.base.fcgi.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.avuna.httpd.http.plugins.base.fcgi.Type;

public class Abort extends FCGIPacket {
	
	public Abort(DataInputStream in, int l) throws IOException {
		super(Type.FCGI_ABORT_REQUEST);
		readContent(in, l);
	}
	
	public Abort(int id) {
		super(Type.FCGI_ABORT_REQUEST, id);
	}
	
	@Override
	protected void readContent(DataInputStream in, int l) throws IOException {
		
	}
	
	@Override
	protected void writeContent(DataOutputStream out) throws IOException {
		
	}
	
}
