package com.jumpos.test.utils;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import com.jumpos.test.testflow.Test0;

public class XmlLog {


	private static String reportPath = "C:/javaReport";
	private static boolean initFlag = true;
	// Error Info list
	public static List<Exception> errorList = new ArrayList<Exception>();
	// Java file list for error info
	public static List<String> javaPathList = new ArrayList<String>();
	// Line code for error info 
	private static List<Integer> errorRowList = new ArrayList<Integer>();
	// Screenshot for error info
	public static List<String> imgLinkList = new ArrayList<String>();
	// Encoding format for test report
	private static String reportCharset = "GB2312";

	private static String javaFileNameSp = "usertask_shape_";
	private static String javaFileNameSp2 = "Test0.java";
	
	private static Object javaFileObj = new Test0();
	private static String userTaskName;
	private static String operationName;

	private static String operationFlag = "OK";
	private static String operationErrorFlag = "Failed";
	private static String msg = "None";
	private static String errorMsg = "<a href=\"INF/detail.html\">error details</a>";

	// Background color for correct status
	private static String bgcolor = "bgcolor=\"#00FF00\"";
	// Background color for error status
	private static String errorBgcolor = "bgcolor=\"#FF0001\"";
	// Font color for error status
	private static String errorFontColor = "style=\"color:white;\"";

	// background color if the status of checkpoint is correct
	private static String checkBgcolor = "bgcolor=\"#00BFFF\"";
	// background color if the status of checkpoint is error
	private static String errorCheckBgcolor = "bgcolor=\"#FF1493\"";
	//report header
	private static String reportStr = "";

	public static void setReportSetting(){
		try{
			reportStr = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" "
					+ "\"http://www.w3.org/TR/html4/loose.dtd\"><html><head><meta "
					+ "http-equiv=\"Content-Type\" content=\"text/html; charset=" + reportCharset + "\">"
					+ "<title>"
					+ Param_Util.getSysParam("Report", "Report Header", "Test Report")
					+"</title></head>"
					+ "<body bgcolor=\"#99FFFF\"><h1 align=\"center\">"
					+ Param_Util.getSysParam("Report", "Report Header", "Test Report")
					+"</h1><table border=\"1\" width=\"100%\"><tr><th>"
					+ Param_Util.getSysParam("Report", "Test Pages", "Test Pages")
					+"</th><th>"
					+ Param_Util.getSysParam("Report", "Test Steps", "Test Steps")
					+"</th><th>"
					+ Param_Util.getSysParam("Report", "Test Data", "Test Data")
					+"</th><th>"
					+ Param_Util.getSysParam("Report", "Xpath", "Xpath")
					+"</th><th>"
					+ Param_Util.getSysParam("Report", "Status", "Status")
					+"</th><th>"
					+ Param_Util.getSysParam("Report", "Notes", "Notes")
					+"</th></tr>";
		}catch(Exception e){
			reportStr = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" "
					+ "\"http://www.w3.org/TR/html4/loose.dtd\"><html><head><meta "
					+ "http-equiv=\"Content-Type\" content=\"text/html; charset=" + reportCharset + "\">"
					+ "<title>Test Report</title></head>"
					+ "<body bgcolor=\"#99FFFF\"><h1 align=\"center\">Test Report</h1><table border=\"1\" width=\"100%\"><tr><th>Test Pages</th><th>Test Steps</th>"
					+"<th>Test Data</th>"
					+"<th>Xpath</th>"
					+"<th>Status</th>"
					+"<th>Notes</th></tr>";

		}
		


	}

	public static void setUserTaskName(String userTaskName) {
		XmlLog.userTaskName = userTaskName;
	}

	public static String getUserTaskName() {
		return userTaskName;
	}

	/**
	 * last step is in correct status and current step is error status
	 */
	public static void setOperationName(String operationName, Object... args) {
		XmlLog.operationName = operationName;
			reportStr = reportStr.replace(operationErrorFlag, operationFlag);
			reportStr = reportStr.replace(errorMsg, msg);
			reportStr = reportStr.replace(errorBgcolor, bgcolor);
			reportStr = reportStr.replace(errorFontColor, "");
			if(args.length ==0){
				reportStr += "<tr " + errorBgcolor + "><td " + errorFontColor + ">" + XmlLog.userTaskName + "</td><td "
						+ errorFontColor + ">" + XmlLog.operationName + "</td><td "
						+ errorFontColor + ">" + "" + "</td><td "
						+ errorFontColor + ">" + "" + "</td><td " + errorFontColor + ">Failed</td><td "
						+ errorFontColor + ">" + errorMsg + "</td></tr>";
			}else{
				reportStr += "<tr " + errorBgcolor + "><td " + errorFontColor + ">" + XmlLog.userTaskName + "</td><td "
						+ errorFontColor + ">" + XmlLog.operationName + "</td><td "
						+ errorFontColor + ">" + args[0].toString() + "</td><td "
						+ errorFontColor + ">" + args[1].toString() + "</td><td " + errorFontColor + ">Failed</td><td "
						+ errorFontColor + ">" + errorMsg + "</td></tr>";
				
			}
	}

	public static void setCheck(boolean checkFlag, String checkValue, String realValue) {
		String color = "";
		String flag = "";
		checkValue = xmlWrap("Expected Result:"+checkValue,40);
		realValue = xmlWrap("Actual Result:"+realValue,40);
		if (checkFlag) {
			color = checkBgcolor;
			flag = "Pass";
		} else {
			color = errorCheckBgcolor;
			flag = "Error";
		}
		reportStr += "<tr " + color + "><td>" + XmlLog.userTaskName + "</td><td " + ">" + XmlLog.operationName
				+ "</td><td>" + flag + "</td><td>" + checkValue + "<br>" + realValue + "</td></tr>";
	}
	
	private static String xmlWrap(String str, int len){
		if(str.length()<=len)
			return str;
		int myLen = 0;
		String reStr = "";
		while(str.length()-myLen>len){
			reStr += str.substring(myLen, myLen+len)+"<br>";
			myLen+=len;
		}
		reStr += str.substring(myLen);
		return reStr;
	}

	private static void setOperation(boolean flag) {
		if (flag) {
			reportStr = reportStr.replace(operationErrorFlag, operationFlag);
			reportStr = reportStr.replace(errorMsg, msg);
			reportStr = reportStr.replace(errorBgcolor, bgcolor);
			reportStr = reportStr.replace(errorFontColor, "");
		}
		reportStr += "</table></body></html>";
	}

	public static void setReportPath(String reportPath) {
		XmlLog.reportPath = reportPath;
		init();
	}

	public static void getReport(String status) {
		if ("ok".equals(status)) {
			setOperation(true);
		} else {
			setOperation(false);
		}
		saveLog();
		FileOutputStream fileWriter = null;
		FileOutputStream fileWriter_1 = null;
		OutputStreamWriter bufferedWriter = null;
		OutputStreamWriter bufferedWriter_1 = null;
		String fileName = reportPath + "/Report.html";
		SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd-HH-mm-ss");
		String Report_Time = format.format(new Date(System.currentTimeMillis()));
		String fileName_1 = reportPath + "/Report"+Report_Time+".html";
		try {
			fileWriter = new FileOutputStream(fileName);
			bufferedWriter = new OutputStreamWriter(fileWriter, reportCharset);
			bufferedWriter.write(reportStr);
			bufferedWriter.flush();
			
			fileWriter_1 = new FileOutputStream(fileName_1);
			bufferedWriter_1 = new OutputStreamWriter(fileWriter_1, reportCharset);
			bufferedWriter_1.write(reportStr);
			bufferedWriter_1.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			imgLinkList.removeAll(imgLinkList);
			javaPathList.removeAll(javaPathList);
			errorList.removeAll(errorList);
			IOClose(bufferedWriter);
			IOClose(fileWriter);
			IOClose(bufferedWriter_1);
			IOClose(fileWriter_1);
		}
	}

	private static void saveLog() {
		init();
		String msg = "";
		FileOutputStream fileWriter = null;
		OutputStreamWriter bufferedWriter = null;
		String fileName = reportPath + "/INF/detail.html";
		try {
			fileWriter = new FileOutputStream(fileName);
			bufferedWriter = new OutputStreamWriter(fileWriter, reportCharset);
			msg = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" "
					+ "\"http://www.w3.org/TR/html4/loose.dtd\"><html><head><meta "
					+ "http-equiv=\"Content-Type\" content=\"text/html; charset=" + reportCharset + "\">"
					+ "<title>Test error report</title></head>" + "<body>";
			bufferedWriter.write(msg + "\t\n");
			msg = "<h3>Error Info：</h3>";
			bufferedWriter.write(msg + "\t\n");

			for (int li = 0; li < errorList.size(); li++) {
				errorRowList.removeAll(errorRowList);
				javaPathList.removeAll(javaPathList);
				msg = "----------------------------------------- <br>";
				bufferedWriter.write(msg + "\t\n");
				msg = "    #" + (li + 1) + "error message：" + imgLinkList.get(li) + "<br>";
				bufferedWriter.write(msg + "\t\n");
				msg = "    " + errorList.get(li).toString() + "<br>";
				bufferedWriter.write(msg + "\t\n");
				for (int a = 0; a < errorList.get(li).getStackTrace().length; a++) {
					msg = "        at " + errorList.get(li).getStackTrace()[a].toString() + "<br>";
					if (msg.contains(javaFileNameSp) || msg.contains(javaFileNameSp2)) {
						try {
							errorRowList.add(Integer.valueOf(msg.substring(msg.indexOf(":") + 1, msg.indexOf(")"))));
							javaPathList.add(getJavaFilePath(msg.substring(msg.indexOf("(") + 1, msg.indexOf(":"))));
						} catch (Exception e) {
						}
					}
					bufferedWriter.write(msg + "\t\n");
				}
				msg = "----------------------------------------- <br>";
				bufferedWriter.write(msg + "\t\n");
				for (int ii = 0; ii < errorRowList.size(); ii++) {
					msg = javaPathList.get(ii).split("/")[javaPathList.get(ii).split("/").length - 1] + " line "
							+ errorRowList.get(ii) + " caused error：";
					bufferedWriter.write(msg + "\t\n");
					msg = "    " + readErrorLine(errorRowList.get(ii), javaPathList.get(ii)) + "<br>";
					bufferedWriter.write(msg + "\t\n");
				}
			}
			msg = "</body></html>";
			bufferedWriter.write(msg + "\t\n");
			bufferedWriter.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOClose(bufferedWriter);
			IOClose(fileWriter);
		}
	}

	private static String readErrorLine(int rowNo, String filePath) {
		String str = "";
		File f = new File(filePath);
		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileInputStream = new FileInputStream(f);
			inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);

			String line = "";
			int i = 1;
			while (i++ <= rowNo) {
				line = bufferedReader.readLine();
			}
			str = line;
		} catch (FileNotFoundException e1) {
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOClose(bufferedReader);
			IOClose(inputStreamReader);
			IOClose(fileInputStream);
		}
		return str;
	}

	private static void init() {
		if (initFlag) {
			File f = new File(reportPath);
			if (f.exists() && f.isDirectory()) {
				deleteDirectory(reportPath);
			}
			f.mkdirs();
			f = new File(reportPath + "/INF/img");
			f.mkdirs();
			initFlag = false;
		}
	}


	public static void deleteDirectory(String sPath) {
		File dirFile = new File(sPath);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return;
		}
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				files[i].delete();
			} 
			else {
				deleteDirectory(files[i].getAbsolutePath());
			}
		}
		dirFile.delete();
	}

	/**
	 * Get Java file path for error checking
	 */
	public static String getJavaFilePath(String str) {
		String path = "";
		if (!str.contains(".java") || javaFileObj == null)
			return "";

		path = javaFileObj.getClass().getResource("").toString().replace("file:/", "") + str;
		return path;
	}

	/**
	 * get screenshot and save under folder report/INF/img
	 */
	public static String getImgFile() {
		init();
		String str = "";
		Exception ex = null;
		try {
			int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(); 
			int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight(); 
			Robot robot = new Robot();
			BufferedImage image = robot.createScreenCapture(new Rectangle(width, height));
			image = image.getSubimage(0, 0, width, height);
			String date = new SimpleDateFormat("MMdd-HHmmss").format(new Date());
			ImageIO.write(image, "jpg", new File(reportPath + "/INF/img/" + date + ".jpg"));
			str = "<a href=\"img/" + date + ".jpg\">Image Info</a>";
		} catch (AWTException e) {
			ex = e;
		} catch (IOException e) {
			ex = e;
		} catch (Exception e) {
			ex = e;
		}
		if ("".equals(str)) {
			str = "Error happened for screenshot capturing : " + ex.toString();
		}
		return str;
	}


	private static void IOClose(Object obj) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		Reader reader = null;
		Writer writer = null;
		if (obj == null) {
			return;
		}

		if (obj instanceof InputStream) {
			inputStream = (InputStream) obj;
		}

		if (obj instanceof OutputStream) {
			outputStream = (OutputStream) obj;
		}

		if (obj instanceof Reader) {
			reader = (Reader) obj;
		}

		if (obj instanceof Writer) {
			writer = (Writer) obj;
		}

		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
