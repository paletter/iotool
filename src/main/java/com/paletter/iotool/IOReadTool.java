package com.paletter.iotool;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOReadTool {

	public static int DEFAULT_BUFFERED_LENGTH = 1048576;// 1M
	
	public static String readLine(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		return reader.readLine();
	}
	
	public static String readContent(InputStream is) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[DEFAULT_BUFFERED_LENGTH];
		int ins = is.read(b);
		if (ins == -1) return null;
		
		baos.write(b, 0, ins);
		while(ins == DEFAULT_BUFFERED_LENGTH) {
			ins = is.read(b);
			if (ins == -1) break;
			baos.write(b, 0, ins);
		}
		
		b = null;
		return baos.toString();
	}
	
	public static String readContent(InputStream is, int bufferLength) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int ins;
		byte[] b = new byte[bufferLength];
		ins = is.read(b);
		if (ins <= 0) return null;
		baos.write(b, 0, ins);
		return baos.toString();
	}
}
