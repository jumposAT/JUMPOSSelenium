package com.jumpos.test.utils;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class KillProcess {
	public static void killProcess(String process) {
		try {
			String str = "";
			do {
				Process p = Runtime.getRuntime().exec("cmd.exe /c taskkill /f /im " + process);
				p.waitFor();
				p = Runtime.getRuntime().exec("cmd.exe /c tasklist /FI \"IMAGENAME eq " + process + "\"");
				InputStream in = p.getInputStream();
				str = IOUtils.toString(in, "GBK");
			} while (str.indexOf(process) != -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
     public static void KillIEDriverServer(){
		 killProcess("IEDriverServer.exe");
		 killProcess("iexplore.exe");
		 killProcess("uploadFile.exe");
		 killProcess("JUMPOSTest_AutoIT.exe");
     }
     
     public static void killUploadAndAutoIT(){
		 killProcess("uploadFile.exe");
		 killProcess("JUMPOSTest_AutoIT.exe");
     }
     
     public static void main(String[] args) {
    	 KillIEDriverServer();
	}
}
