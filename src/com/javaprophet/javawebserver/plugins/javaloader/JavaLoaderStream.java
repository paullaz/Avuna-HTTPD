package com.javaprophet.javawebserver.plugins.javaloader;

import java.io.DataOutputStream;
import com.javaprophet.javawebserver.networking.packets.RequestPacket;
import com.javaprophet.javawebserver.networking.packets.ResponsePacket;

public abstract class JavaLoaderStream extends JavaLoader {
	public abstract void generate(DataOutputStream out, RequestPacket request, ResponsePacket response);
}
