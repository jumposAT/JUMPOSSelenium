
package com.jumpos.test.testflow;

import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.jumpos.test.utils.*;
import com.jumpos.test.main.Run;

public class Test0  {

	private StringBuilder overview = new StringBuilder("Execution started");
	
	static Logger logger = Logger.getLogger(Test0.class);
	static boolean driverIniFlag = false;	//Flag for whether the driver is initialized completed. Otherwise  the empty window will be shown.
	static boolean driverSucFlag = false; 	//Flag for whether the driver is initialized correctly
	static JUMPOSWebDriver driver = null;
	
	Map<String, String> assigneeMap = new HashMap<String, String>();	
	public static String strAutoEXEPath = "C:\\JUMPOSTest_AutoIT.exe closeIE";

	String userGroup = "";
	public static String userGroupTemp = "";
    public static String assignee = "";
    public static String flowState = "";
	public static String flowName = "";

	

	//If there is exception captured, the current test step will be perform again
	public void flowErrorRun(Test0 oldt) {
		boolean runFlag = false;
		int countIndex = 0;
		do {
			try {
				String info = "Exception captured, the current test step will be perform again";
				System.out.println(info);
				logger.info(info);
				
				String olduserGroupTemp = Test0.userGroupTemp;
				String oldflowState = oldt.assigneeMap.get(olduserGroupTemp);
				
				Test0 t = new Test0();
				Test0.userGroupTemp = olduserGroupTemp;
				Test0.flowState = oldflowState;
				t.perform();
				
				Thread.sleep(2 * 1000);
				Runtime.getRuntime().exec(strAutoEXEPath);
				
				Thread.sleep(5 * 1000);
				KillProcess.KillIEDriverServer();
				runFlag = true;		
				Run.runState = "ok";
			} catch (Exception e) {
				XmlLog.errorList.add(e);
				XmlLog.imgLinkList.add(XmlLog.getImgFile());
				try {
					Thread.sleep(2 * 1000);
					Runtime.getRuntime().exec(strAutoEXEPath);
					Thread.sleep(5 * 1000);
					KillProcess.KillIEDriverServer();
					Run.runState = "Failed becaused of the above exception";
					logger.error("Error", e);
					e.printStackTrace();
				} catch (Exception e3) {
					XmlLog.errorList.add(e3);
					XmlLog.imgLinkList.add(XmlLog.getImgFile());				
				}
				
			}
			
		} while(!runFlag && ++countIndex < Run.errorEchorunCount);
	}
	
	public static void invoke(){

		Test0 t = new Test0();

		try {
			t.perform();
			Thread.sleep(2 * 1000);
			Runtime.getRuntime().exec(strAutoEXEPath);

			Thread.sleep(5 * 1000);
			KillProcess.KillIEDriverServer();
		} catch (Exception e) {
			XmlLog.errorList.add(e);
			XmlLog.imgLinkList.add(XmlLog.getImgFile());
			
			try {
				Thread.sleep(2 * 1000);
				Runtime.getRuntime().exec(strAutoEXEPath);
				Thread.sleep(5 * 1000);
				KillProcess.KillIEDriverServer();
				Run.runState = "Failed becaused of the above exception";
				logger.error("Error", e);
				e.printStackTrace();
			} catch (Exception e1) {
				XmlLog.errorList.add(e1);
				XmlLog.imgLinkList.add(XmlLog.getImgFile());
				e1.printStackTrace();
			}
			
			if(Run.errorEchoFlag) {
				t.flowErrorRun(t);
			}
		}
	}

	private void perform() throws Exception{

		Thread.sleep(1000);
    	System.setProperty("webdriver.ie.driver","C:\\Program Files\\Internet Explorer\\IEDriverServer.exe");
    	DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
        ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
       //ieCapabilities.setCapability("initialBrowserUrl", "http://");

     
	    while (true) {
	     	if (!userGroup.equals(userGroupTemp)){
				assigneeMap.put(userGroupTemp, flowState);
				if(driver!=null){
					try{
							driver.close();
					}
					catch(Exception e){	}
					
					try{
							driver.quit() ;
					}
					catch(Exception e){}
					KillProcess.killProcess("IEDriverServer.exe");
					KillProcess.killProcess("iexplore.exe");

					driver = null;
				}
						
				driverIniFlag = false;	
				driverSucFlag = false; 	
				
				JumposTimerTest0 jumposTimerTest0 = new JumposTimerTest0(userGroupTemp, ieCapabilities);
				Timer timer = new Timer();
				timer.schedule(jumposTimerTest0, 1000, 20 * 1000);
				
				int iniIndex = 0;
				while((!(driverIniFlag && driverSucFlag )) && iniIndex++ < 180) {	
					System.out.println("driver instance timer sleep 10*1000");
					Thread.sleep(10 * 1000);
				}
			
				timer.cancel();
				XmlLog.setUserTaskName("Initialization");				
				run(userGroupTemp, ieCapabilities);
				userGroup = userGroupTemp;
	         }
	         
	
		if ("".equals(flowState)){
			XmlLog.setUserTaskName("开始");
	
			assigneeMap.put(userGroupTemp, flowState);	
Test0.flowState = "";
Test0.userGroupTemp = "dept_48";
Test0.assignee = "";
continue;

	
		}
	
		if ("".equals(flowState)){
			XmlLog.setUserTaskName("New");
	
			new usertask_New_ID16391ea249yOs23b3UM5jJqS157jkMu().performUserTask("Test0",driver,assignee,overview);
			continue;
	
	
		}
	
	
	     }
  }
  
  
  	public static void run(String laneid, DesiredCapabilities ieCapabilities) throws Exception{
	
   		String strXPath = "";
   		String strTestData = "";
   		String strOperationName = "";

	
		if("dept_48".equals(laneid)){
strOperationName = "Open Login screen";
strXPath = Param_Util.getXPath(strOperationName, "");
strTestData = Param_Util.getParam(strOperationName, "   ");
XmlLog.setOperationName(strOperationName,strTestData,strXPath);	
if(	!Param_Util.IsInit){
	driver.get(strTestData);
 }
strOperationName = "enter User Name";
strXPath = Param_Util.getXPath(strOperationName, "");
strTestData = Param_Util.getParam(strOperationName, "   ");
XmlLog.setOperationName(strOperationName,strTestData,strXPath);	
if(	!Param_Util.IsInit){
	driver.sendKeys(By.xpath(strXPath), strTestData);
}
strOperationName = "enter Password";
strXPath = Param_Util.getXPath(strOperationName, "");
strTestData = Param_Util.getParam(strOperationName, "   ");
XmlLog.setOperationName(strOperationName,strTestData,strXPath);	
if(	!Param_Util.IsInit){
			driver.sendKeys(By.xpath(strXPath), strTestData);
}
strOperationName = "press Login button";
strXPath = Param_Util.getXPath(strOperationName, "");
strTestData = Param_Util.getParam(strOperationName, "   ");
XmlLog.setOperationName(strOperationName,strTestData,strXPath);	
if(	!Param_Util.IsInit){
	driver.findElement(By.xpath(strXPath)).sendKeys("");
	Thread.sleep(1000);
	driver.findElement(By.xpath(strXPath)).click();
}	

}

	}
  
 
}

//Whether the driver is initialized completed
class JumposTimerTest0 extends TimerTask {
	
	Logger logger = Logger.getLogger(JumposTimerTest0.class);
	
	String laneid;
	DesiredCapabilities ieCapabilities;
	
	public JumposTimerTest0(String laneid, DesiredCapabilities ieCapabilities) {
		this.laneid = laneid;
		this.ieCapabilities = ieCapabilities;
	}
	
	@Override
	public void run() {
		String info = "TimerTask run in";
		System.out.println(info);
		logger.info(info);
		if(Test0.driverIniFlag && Test0.driverSucFlag) {	
			info = "timer cancel start";
			System.out.println(info);
			logger.info(info);
			
			this.cancel();
			info = "timer cancel success";
			System.out.println(info);
			logger.info(info);
		} else {
			if (Test0.driver != null) {
				info = "driver testNewWindows start";
				System.out.println(info);
				logger.info(info);
				
				testNewWindows(Test0.driver);
				Test0.driver.close();
				try {
					Thread.sleep(10000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				KillProcess.KillIEDriverServer();
				
				info = "driver testNewWindows success";
				System.out.println(info);
				logger.info(info);
			}
			try {
				info = "driver instance ini start";
				System.out.println(info);
				logger.info(info);
				
				Thread thread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						Test0.driver = new JUMPOSWebDriver(ieCapabilities);
					}
				});
				thread.start();
				int wait=0;
				while (Test0.driver == null && thread != null && wait ++<100){
					Thread.sleep(10 * 1000);
				}			
				if(Test0.driver == null && thread != null) {
					thread.interrupt();
					info = "TimerTask thread is interrupt";
					System.out.println(info);
					logger.info(info);
					Thread.sleep(3 * 1000);
					KillProcess.KillIEDriverServer();
				} else {
					Test0.driverIniFlag = true;		
					Test0.driver.manage().window().maximize();
					Test0.driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
					Test0.driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.MINUTES);
					Test0.driver.manage().timeouts().setScriptTimeout(30, TimeUnit.MINUTES);
					
					Test0.driverSucFlag = true;		
					
					info = "driver instance ini success";
					System.out.println(info);
					logger.info(info);
				}
			} catch (Exception e) {
				KillProcess.KillIEDriverServer();
				e.printStackTrace();
			}
		}
	}
	
	public static boolean testNewWindows(WebDriver driver) {
		try {
			String currentHandle = driver.getWindowHandle();
			Set<String> handles = driver.getWindowHandles();
			handles.remove(currentHandle);
			if (handles.size() > 0) {
				driver.switchTo().window(handles.iterator().next());
				driver.switchTo().alert().accept();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return false;
	}
}