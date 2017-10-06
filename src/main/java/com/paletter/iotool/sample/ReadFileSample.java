package com.paletter.iotool.sample;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ReadFileSample {

	public static long bufferLength = 10485760;// 10M
	
	public static void main(String[] args) throws Exception {

		readFileByStream("D:\\Zemp\\WebStorm 8.0.1.zip");
		readFileByChannel("D:\\Zemp\\WebStorm 8.0.1.zip");
	}
	
	public static void readFileByStream(String path) throws Exception {
		
		long startTime = System.currentTimeMillis();
		
		File file = new File(path);
		FileInputStream is = new FileInputStream(file);
		
		byte[] b = new byte[(int) bufferLength];
		while(true) {
			int ins = is.read(b);
			if(ins == -1) {
				is.close();
				break;
			}
		}
		
		System.out.println("Spent time: " + (System.currentTimeMillis() - startTime));
	}
	
	public static void readFileByChannel(String path) throws Exception {
		
		long startTime = System.currentTimeMillis();
		
		File file = new File(path);
		FileInputStream is = new FileInputStream(file);
		FileChannel fc = is.getChannel();
		
		ByteBuffer b = null;
		while(true) {
			if(fc.position() == fc.size()) {
				is.close();
				fc.close();
				break;
			}
			
			int length = 0;
			if(fc.size() - fc.position() > bufferLength) length = (int) bufferLength;
			else length = (int) (fc.size() - fc.position());
			
			b = ByteBuffer.allocateDirect(length);
			fc.read(b);
			b.flip();
		}
		
		System.out.println("Spent time: " + (System.currentTimeMillis() - startTime));
	}
}
