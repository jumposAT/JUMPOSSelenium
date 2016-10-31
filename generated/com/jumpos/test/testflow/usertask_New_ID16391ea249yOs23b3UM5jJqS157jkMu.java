
package com.jumpos.test.testflow;


import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.jumpos.test.utils.*;

public class usertask_New_ID16391ea249yOs23b3UM5jJqS157jkMu  {

	 public void performUserTask(String parentFlow, JUMPOSWebDriver driver,String assignee,StringBuilder overview) throws Exception {

		Logger.getLogger(Test0.class);
   		String strXPath = "";
   		String strTestData = "";
   		String strOperationName = "";
  		String strExpectionXpath ="";
		Boolean isDone = false;
  		Integer count = 0;
		
	 
		
		
			strOperationName = "open window";
strXPath = Param_Util.getXPath(strOperationName, "");
strTestData = Param_Util.getParam(strOperationName, "   ");
XmlLog.setOperationName(strOperationName,strTestData,strXPath);
if(	!Param_Util.IsInit){
driver.switchToWindowByTitle(strTestData);
}


		
			isDone = false;
count = 0;
while(!isDone && count <=10 ){
     strOperationName = "new";
     strXPath = Param_Util.getXPath(strOperationName, "");
     strTestData = Param_Util.getParam(strOperationName, "   ");
     strExpectionXpath = Param_Util.getExpectationXPath(strOperationName, "   ");
     XmlLog.setOperationName(strOperationName,strTestData,strXPath);
     if(	!Param_Util.IsInit){
driver.findElement(By.xpath(strXPath)).sendKeys("");
driver.findElement(By.xpath(strXPath)).click();
          count = count + 1;
          if (strExpectionXpath ==""){
               isDone = true;
          }else{
               if (driver.findElementOnce(By.xpath(strExpectionXpath)) != null){
                   isDone = true;
               }
          }
     }else{
          isDone = true;
     }
}
isDone = false;
count = 0;
while(!isDone && count <=10 ){
     strOperationName = "";
     strXPath = Param_Util.getXPath(strOperationName, "");
     strTestData = Param_Util.getParam(strOperationName, "   ");
     strExpectionXpath = Param_Util.getExpectationXPath(strOperationName, "   ");
     XmlLog.setOperationName(strOperationName,strTestData,strXPath);
     if(	!Param_Util.IsInit){
driver.findElement(By.xpath(strXPath)).sendKeys("");
driver.findElement(By.xpath(strXPath)).click();
          count = count + 1;
          if (strExpectionXpath ==""){
               isDone = true;
          }else{
               if (driver.findElementOnce(By.xpath(strExpectionXpath)) != null){
                   isDone = true;
               }
          }
     }else{
          isDone = true;
     }
}

	 
		 
		//actionName:null
		
		String flowButtonName = "";
		
		
		Test0.flowState = "";
Test0.userGroupTemp = "dept_48";
Test0.assignee = "";


	}
}