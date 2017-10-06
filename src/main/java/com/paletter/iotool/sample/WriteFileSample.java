package com.paletter.iotool.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class WriteFileSample {

	public static long bufferLength = 2097152;// 2M
	
	public static void main(String[] args) throws Exception {
		
		File file = new File("D:\\Zemp\\sqlResult_1429495.csv");
		InputStream is = new FileInputStream(file);
		
		writeFileByStream(is, "D:\\Zemp\\temp1.csv");
		cleanFile("D:\\Zemp\\temp1.csv");
		
		is = new FileInputStream(file);
		writeFileByChannel(is, "D:\\Zemp\\temp1.csv");
		
		cleanFile("D:\\Zemp\\temp1.csv");
		is = new FileInputStream(file);
		writeFileByChannel2((FileInputStream) is, "D:\\Zemp\\temp1.csv");
	}
	
	public static void cleanFile(String path) {
		File file = new File(path);
		file.delete();
	}
	
	public static void writeFileByStream(InputStream is, String destPath) throws Exception {
		
		long startTime = System.currentTimeMillis();
		
		File targetFile = new File(destPath);
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
	
	public static void writeFileByChannel(InputStream is, String destPath) throws Exception {
		
		long startTime = System.currentTimeMillis();
		
		File targetFile = new File(destPath);
		FileOutputStream fos = new FileOutputStream(targetFile);
		FileChannel fco = fos.getChannel();
		
		byte[] buffer = new byte[(int) bufferLength];
		while(true) {
			
			int ins = is.read(buffer);

			// Termination
			if(ins == -1) {
				is.close();
				fos.close();
				fco.close();
				break;
			}
			
			ByteBuffer b = ByteBuffer.wrap(buffer, 0, ins);
			fco.write(b);
			fco.force(false);
		}
		
		System.out.println("Spent time: " + (System.currentTimeMillis() - startTime));
	}
	
	public static void writeFileByChannel2(FileInputStream fis, String destPath) throws Exception {
		
		long startTime = System.currentTimeMillis();
		
		FileChannel fci = fis.getChannel();
		
		File targetFile = new File(destPath);
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
