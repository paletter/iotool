package com.paletter.iotool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class IOFileTool {

	public static int DEFAULT_BUFFERED_LENGTH = 10485760;// 10M
	
	public static void copyFile(String oriPath, String destPath) throws IOException {

		File oriFile = new File(oriPath);
		File destFile = new File(destPath);
		
		copyFile(oriFile, destFile);
	}
	
	public static void copyFile(File oriFile, File destFile) throws IOException {
		
		FileInputStream fis = new FileInputStream(oriFile);
		FileChannel fci = fis.getChannel();
		
		FileOutputStream fos = new FileOutputStream(destFile);
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
			
			if(fci.size() - fci.position() >= DEFAULT_BUFFERED_LENGTH) {
				fci.transferTo(fci.position(), DEFAULT_BUFFERED_LENGTH, fco);
				fci.position(fci.position() + DEFAULT_BUFFERED_LENGTH);
			} else {
				long remainder = fci.size() - fci.position();
				fci.transferTo(fci.position(), remainder, fco);
				fci.position(fci.position() + remainder);
			}
		}
	}
	
	public static void writeFile(InputStream is, String destFilePath) throws IOException {

		File destFile = new File(destFilePath);
		writeFile(is, destFile);
	}
	
	public static void writeFile(FileInputStream fis, String destFilePath) throws IOException {

		File destFile = new File(destFilePath);
		writeFile(fis, destFile);
	}
	
	public static void writeFile(InputStream is, File destFile) throws IOException {
		
		OutputStream os = new FileOutputStream(destFile);
		
		byte[] buffer = new byte[DEFAULT_BUFFERED_LENGTH];
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
	}
	
	public static void writeFile(FileInputStream fis, File destFile) throws IOException {
		
		FileChannel fci = fis.getChannel();
		
		FileOutputStream fos = new FileOutputStream(destFile);
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
			if(fci.size() - fci.position() >= DEFAULT_BUFFERED_LENGTH) length = DEFAULT_BUFFERED_LENGTH;
			else length = fci.size() - fci.position();
			
			buffer = ByteBuffer.allocateDirect((int) length);
			fci.read(buffer);
			buffer.flip();
			fco.write(buffer);
			fco.force(false);
		}
	}
}
