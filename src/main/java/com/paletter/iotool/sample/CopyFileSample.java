package com.paletter.iotool.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CopyFileSample {

	public static long bufferLength = 2097152;// 2M
	
	public static void main(String[] args) throws Exception {
		
		String orgFilePath = "D:\\Zemp\\sqlResult_1429495.csv";
		String targetFilePath = "D:\\Zemp\\sqlResult_1429496.csv";
		
		copyByStream(orgFilePath, targetFilePath);
		cleanFile(targetFilePath);
		
		copyByChannel(orgFilePath, targetFilePath);
		cleanFile(targetFilePath);
		
		copyByChannel2(orgFilePath, targetFilePath);
	}
	
	public static void cleanFile(String path) {
		File file = new File(path);
		file.delete();
	}
	
	public static void copyByStream(String path1, String path2) throws Exception {

		long startTime = System.currentTimeMillis();
		
		File orgFile = new File(path1);
		InputStream is = new FileInputStream(orgFile);
		
		File targetFile = new File(path2);
		OutputStream os = new FileOutputStream(targetFile);
		
		byte[] buffer = new byte[(int) bufferLength];
		while(true) {
			
			int ins = is.read(buffer);
			
			// Termination
			if(ins == -1) {
				is.close();
				os.flush();
				os.close();
				break;
			}
			
			os.write(buffer, 0, ins);
		}
		
		System.out.println("Spent time: " + (System.currentTimeMillis() - startTime));
	}
	
	public static void copyByChannel(String path1, String path2) throws Exception {
		
		long startTime = System.currentTimeMillis();
		
		File orgFile = new File(path1);
		FileInputStream fis = new FileInputStream(orgFile);
		FileChannel fci = fis.getChannel();
		
		File targetFile = new File(path2);
		FileOutputStream fos = new FileOutputStream(targetFile);
		FileChannel fco = fos.getChannel();
		
		while(true) {
			
			// Termination
			if(fci.position() == fci.size()) {
				fci.close();
				fco.close();
				fis.close();
				fos.close();
				break;
			}
			
			if(fci.size() - fci.position() >= bufferLength) {
				fci.transferTo(fci.position(), bufferLength, fco);
				fci.position(fci.position() + bufferLength);
			} else {
				long remainder = fci.size() - fci.position();
				fci.transferTo(fci.position(), remainder, fco);
				fci.position(fci.position() + remainder);
			}
		}
		
		System.out.println("Spent time: " + (System.currentTimeMillis() - startTime));
	}
	
	public static void copyByChannel2(String path1, String path2) throws Exception {
		
		long startTime = System.currentTimeMillis();
		
		File orgFile = new File(path1);
		FileInputStream fis = new FileInputStream(orgFile);
		FileChannel fci = fis.getChannel();
		
		File targetFile = new File(path2);
		FileOutputStream fos = new FileOutputStream(targetFile);
		FileChannel fco = fos.getChannel();
		
		ByteBuffer buffer = null;
		
		while(true) {
			
			// Termination
			if(fci.position() == fci.size()) {
				fci.close();
				fco.close();
				fis.close();
				fos.close();
				break;
			}

			long length = 0;
			if(fci.size() - fci.position() >= bufferLength) length = bufferLength;
			else length = fci.size() - fci.position();
			
			buffer = ByteBuffer.allocateDirect((int) length);
			fci.read(buffer);
			buffer.flip();
			fco.write(buffer);
			fco.force(false);
		}
		
		System.out.println("Spent time: " + (System.currentTimeMillis() - startTime));
	}
}
