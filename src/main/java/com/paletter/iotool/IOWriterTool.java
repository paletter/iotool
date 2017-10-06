package com.paletter.iotool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class IOWriterTool {

	public static int DEFAULT_BUFFERED_LENGTH = 1048576;// 1M
	
	public static void writeContent(OutputStream os, String str) throws IOException {
		os.write(str.getBytes());
		os.flush();
	}
	
	@SuppressWarnings("restriction")
	public static void writeContentWithLine(OutputStream os, String str) throws IOException {
		os.write(str.getBytes());
		os.write(java.security.AccessController.doPrivileged(
				new sun.security.action.GetPropertyAction("line.separator")).getBytes());
		os.flush();
	}
	
	public static void printlnWithFlush(OutputStream os, String str) {
		PrintWriter writer = new PrintWriter(os);
		writer.println(str);
		writer.flush();
	}
	
	public static void write(OutputStream os, InputStream is) throws IOException {
		byte[] b = new byte[DEFAULT_BUFFERED_LENGTH];
		int ins;
		while((ins = is.read(b)) != -1) {
			os.write(b, 0, ins);
			os.flush();
		}
	}
}
