package com.javaprophet.javawebserver.dns;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import com.javaprophet.javawebserver.util.Logger;

public class RecordHolder {
	public RecordHolder() {
		
	}
	
	public RecordHolder(File f) {
		Scanner fin = null;
		try {
			f.getParentFile().mkdirs();
			f.createNewFile();
			fin = new Scanner(new FileInputStream(f));
			int ttl = 3600;
			while (fin.hasNextLine()) {
				String line = fin.nextLine().trim();
				if (line.startsWith("ttl")) {
					ttl = Integer.parseInt(line.substring(3).trim());
				}else {
					String[] ls = line.split(" ");
					if (ls.length < 3) continue;
					String domain = ls[0];
					Type type = Type.getType(ls[1].toUpperCase());
					String data = line.substring(ls[0].length() + ls[1].length() + 2).trim();
					byte[] fd = null;
					if (type == Type.A || type == Type.NS) {
						fd = Util.ipToByte(data);
					}else if (type == Type.AAAA) {
						fd = Util.ip6ToByte(data);
					}else {
						fd = data.getBytes(); // TODO: ???
					}
					records.add(new DNSRecord(domain, type, ttl, fd));
				}
			}
		}catch (IOException e) {
			Logger.logError(e);
		}finally {
			if (fin != null) fin.close();
		}
	}
	
	private final ArrayList<DNSRecord> records = new ArrayList<DNSRecord>();
	
	public void addRecord(DNSRecord record) {
		records.add(record);
	}
	
	protected ArrayList<DNSRecord> getRecords() {
		return records;
	}
}
