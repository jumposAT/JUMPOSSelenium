package com.jumpos.test.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.jumpos.test.testflow.Test0;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class Param_Util {
	private static File excelFile = null;
	private static String columnName;
	public static String flowName;
	private static Sheet sheet;
	private static Workbook book;
	public static Boolean IsInit = true;
	private static Map<String, String> xmldata = new HashMap<String, String>();
	
	private static  WritableWorkbook wbook;
	private static WritableSheet wsheet;
	private static int sheetRowsCount = 0; 

	
    public static void main(String[] args) {


	}
    
	public static void GenerateExcel(String excelPath){
		try {
			wbook = Workbook.createWorkbook(new File(excelPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void SaveExcel(){
    	try {
    		if(wbook!=null){
    			wbook.write();
    	    	wbook.close();
    		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void CreateSheet(String sheetName){
			wbook.createSheet(sheetName, wbook.getSheets().length);
	}

	public static void CreateCell(String sheetName, String columnName){
		wsheet = wbook.getSheet(sheetName);
		jxl.write.Label content = new jxl.write.Label(0,0 ,columnName);
		try {
			wsheet.addCell(content);
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static String getSysParam(String sysName, String paramName, String scriptValue) throws Exception {
		
		if (excelFile == null) {
			if(wbook != null){
				wsheet = wbook.getSheet("Config");
				if (wsheet == null){
					wsheet = wbook.createSheet("Config", 0);
					sheetRowsCount = 0;
					wsheet.addCell( new jxl.write.Label(0,0 ,"SysName"));
					wsheet.addCell( new jxl.write.Label(1,0 ,"Param"));
					wsheet.addCell( new jxl.write.Label(2,0 ,"value"));
					sheetRowsCount = 1;
				}
				wsheet.addCell( new jxl.write.Label(0,sheetRowsCount ,"Report"));
				wsheet.addCell( new jxl.write.Label(1,sheetRowsCount , paramName));
				sheetRowsCount = sheetRowsCount+1;
				return "";
			}else{
				return scriptValue;
			}
		}
		if (isNULLString(sysName))
			throw new Exception("get system setting for [" + paramName + "]，value is empty！！");
		
		String str =  getOtherParamValue("Config", paramName, sysName);
		
		if(str =="") {
			throw new Exception("Can not find sheet name for" + sysName );
		}else{
			return str;
		}
	}

	public static String getParam(String paramName, String scriptValue) throws Exception {
		if (excelFile == null) {
			return scriptValue;
		}
		if (isNULLString(Test0.flowName))
			throw new Exception("While getting test data[" + paramName + "]，test process name is empty");
		if (isNULLString(XmlLog.getUserTaskName()))
			throw new Exception("While getting test data[" + paramName + "]，user page name is empty");
		return getOtherParamValue(Test0.flowName, paramName, XmlLog.getUserTaskName());
	}
	
	public static String getXPath(String paramName, String scriptValue) throws Exception {
		if (excelFile == null) {
			return scriptValue;
		}
		if (isNULLString(Test0.flowName))
			throw new Exception("While getting test data[" + paramName + "]，test process name is empty");
		if (isNULLString(XmlLog.getUserTaskName()))
			throw new Exception("While getting test data[" + paramName + "]，user page name is empty");
		return getOtherXPathValue(Test0.flowName, paramName, XmlLog.getUserTaskName());
	}

	private static String getOtherParamValue(String sheetName, String paramName, String paramScope) throws Exception {
		String str = "";
		//System.out.println("XMLDATA ["+"DATA|"+sheetName+"|"+ paramScope+"|"+paramName+"] "+xmldata.get("DATA|"+sheetName+"|"+ paramScope+"|"+paramName));		
	
		if(xmldata.containsKey("DATA|"+sheetName+"|"+ paramScope+"|"+paramName)){
			str = (String) xmldata.get("DATA|"+sheetName+"|"+ paramScope+"|"+paramName);
			System.out.println("DATA  [Cache/"+sheetName+"/"+ paramScope+"/"+paramName+"] "+str);
			return str;
		}

		
		if (excelFile == null)
			throw new Exception("excel name is empty ");
		if (isNULLString(sheetName))
			throw new Exception("sheet name is empty");
		if (isNULLString(paramName))
			throw new Exception("param name is empty");
		if (isNULLString(paramScope))
			throw new Exception("user page name is empty");
		try {
			book = Workbook.getWorkbook(excelFile);
			sheet = book.getSheet(sheetName);
			Param_Util.IsInit = false;
			if (sheet == null) {
				Param_Util.IsInit = true;
				return str;
//				throw new Exception("Can not find sheet name for" + sheetName );
			}
			int col = 0;
			if (sheetName.equals("SystemConfig")) {
				col = 2;
			} else {
				for (int i = 0; i < sheet.getColumns(); i++) {
					if (columnName.equals(sheet.getCell(i, 0).getContents())) {
						col = i;
						break;
					}
				}
			}
			if (col == 0) {
				throw new Exception("Can not find column for:" + columnName );
			}
			for (int j = 0; j < sheet.getRows(); j++) {
				if (paramScope.equals(sheet.getCell(0, j).getContents())
						&& paramName.equals(sheet.getCell(1, j).getContents()))
					str = sheet.getCell(col, j).getContents();
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (book != null) {
				try {
					book.close();
				} catch (Exception e) {
				}
			}
		}
		System.out.println("DATA  ["+excelFile+"/"+sheetName+"/"+ paramScope+"/"+paramName+"] "+str);
		xmldata.put("DATA|"+sheetName+"|"+ paramScope+"|"+paramName,str);
		//System.out.println("XMLDATA ["+"DATA|"+sheetName+"|"+ paramScope+"|"+paramName+"] "+xmldata.get("DATA|"+sheetName+"|"+ paramScope+"|"+paramName));		

		return str;
	}

	private static String getOtherXPathValue(String sheetName, String paramName, String paramScope) throws Exception {
		String str = "";
		//System.out.println("XMLDATA ["+"XAPTH|"+sheetName+"|"+ paramScope+"|"+paramName+"] "+xmldata.get("XAPTH|"+sheetName+"|"+ paramScope+"|"+paramName));		
		if(xmldata.containsKey("XAPTH|"+sheetName+"|"+ paramScope+"|"+paramName)){
			str = (String) xmldata.get("XAPTH|"+sheetName+"|"+ paramScope+"|"+paramName);
			System.out.println("XPATH [Cache/"+sheetName+"/"+ paramScope+"/"+paramName+"] "+str);
			return str;
		}
		if (excelFile == null)
			throw new Exception("excel name is empty ");
		if (isNULLString(sheetName))
			throw new Exception("sheet name is empty");
		if (isNULLString(paramName))
			throw new Exception("param name is empty");
		if (isNULLString(paramScope))
			throw new Exception("user page name is empty");
		try {
			book = Workbook.getWorkbook(excelFile);
			sheet = book.getSheet(sheetName);
			Param_Util.IsInit = false;
			if (sheet == null) {
				Param_Util.IsInit = true;
				return str;
//				throw new Exception("Can not find sheet name for" + sheetName );
			}
			int col = 0;
			if (sheetName.equals("SystemConfig")) {
				col = 2;
			} else {
				for (int i = 0; i < sheet.getColumns(); i++) {
					if (columnName.equals(sheet.getCell(i, 0).getContents())) {
						col = i;
						break;
					}
				}
			}
			if (col == 0) {
				throw new Exception("Can not find column for:" + columnName );
			}
			for (int j = 0; j < sheet.getRows(); j++) {
				if (paramScope.equals(sheet.getCell(0, j).getContents())
						&& paramName.equals(sheet.getCell(1, j).getContents()))
					str = sheet.getCell(col+1, j).getContents();
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (book != null) {
				try {
					book.close();
				} catch (Exception e) {
				}
			}
		}
		System.out.println("XPATH ["+excelFile+"/"+sheetName+"/"+ paramScope+"/"+paramName+"] "+str);
		xmldata.put("XAPTH|"+sheetName+"|"+ paramScope+"|"+paramName,str);
		//System.out.println("XMLDATA ["+"XAPTH|"+sheetName+"|"+ paramScope+"|"+paramName+"] "+xmldata.get("XAPTH|"+sheetName+"|"+ paramScope+"|"+paramName));		
		return str;
	}
	

	public static boolean isNULLString(String str) {
		if (str == null) {
			return true;
		}
		if ("".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	private static void setExcelFile(String excelPath) {
		File f = new File(excelPath);
		if (f.exists()) {
			excelFile = f;
		}else{
	    	GenerateExcel(excelPath);
		}
	}

	public static String getString(String string, String expression) {
		String str = "";
		str = java.util.regex.Pattern.compile(expression).matcher(string).replaceAll("");
		return str;

	}

	public static void checkArgs(String[] args) throws Exception {
		if (args == null || args.length < 1) {
			throw new Exception("Please enter params：test process name[、report path、Excel path、data column name]");
		}
		if (isNULLString(args[0])) {
			throw new Exception("Please enter params： ：test process name is empty！");
		}
		System.out.println("Process Name==》》" + args[0]);
		flowName = args[0];
		if (args.length >= 2 && !isNULLString(args[1])) {
			System.out.println("Report Path==》》" + args[1]);
			XmlLog.setReportPath(args[1]);
		}
		if (args.length >= 4 && !isNULLString(args[2])) {
			System.out.println("Excel Path==》》" + args[2]);
			setExcelFile(args[2]);
		}
		if (args.length >= 4 && !isNULLString(args[3])) {
			System.out.println("Data Column==》》" + args[3]);
			columnName = args[3];
		}
		if (args.length >= 5 && !isNULLString(args[4])) {
			System.out.println("AUTOEXE==》》" + args[4]+" closeIE");
			Test0.strAutoEXEPath = args[4]+" closeIE";
		}
	}

	public static String getExpectationXPath(String strOperationName,
			String string) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
