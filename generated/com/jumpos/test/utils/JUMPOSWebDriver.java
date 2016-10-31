package com.jumpos.test.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.support.ui.Select;

public class JUMPOSWebDriver extends InternetExplorerDriver {

	Logger logger = Logger.getLogger(JUMPOSWebDriver.class);
	
	public static String strCertificationErrorWindowTitle= "Cerfitication Error";

	public JUMPOSWebDriver() {
		super();
	}

	public JUMPOSWebDriver(Capabilities capabilities) {
		super(capabilities);
	}

	public JUMPOSWebDriver(int port) {
		super(port);
	}

	public JUMPOSWebDriver(InternetExplorerDriverService service, Capabilities capabilities) {
		super(service, capabilities);
	}

	public JUMPOSWebDriver(InternetExplorerDriverService service) {
		super(service);
	}

	public Alert alert() {
		Alert alert = null;
		boolean findFlag = false;
		int countIndex = 0;
		do {
			try {
				alert = this.switchTo().alert();
				findFlag = true;
			} catch (NoAlertPresentException e) {
			}
			int sleepCount = 1000;
			if (!findFlag) {
				try {
					Thread.sleep(sleepCount);
				} catch (InterruptedException e) {
				}
			}
			if (countIndex > 0) {
				String sleepInfo = "driver.switchTo().alert smart wait #：" + countIndex + "，waiting time:" + sleepCount;
				System.out.println(sleepInfo);
				logger.info(sleepInfo);
			}
		} while (!findFlag && countIndex++ < 30);
		return alert;
	}

	public boolean findFrame(By by) {
		boolean findFlag = false;
		try {
			int countIndex = 0;

			do {
				List<WebElement> frameList = new ArrayList<WebElement>();
				frameList.addAll(this.findElements(By.tagName("iframe")));
				frameList.addAll(this.findElements(By.tagName("frame")));

				String byStr = by.toString();
				String byValue = byStr.substring(byStr.lastIndexOf(":") + 1).trim();

				int sleepCount = 2 * 1000;
				Thread.sleep(sleepCount);
				if (countIndex > 0) {
					String sleepInfo = by.toString() + " smart wait #：" + countIndex + ",waiting time:" + sleepCount;
					System.out.println(sleepInfo);
					logger.info(sleepInfo);
				}

				for (int i = 0; i < frameList.size(); i++) {
					WebElement webEle = (WebElement) frameList.get(i);
					String name = webEle.getAttribute("name");
					if (name.equals(byValue)) {
						findFlag = true;
						break;
					}
				}
			} while (!findFlag && countIndex++ < 30);

			if (!findFlag) { 
				String info = this.getPageSource();
				logger.info("pageSource:" + info);
				System.out.println("pageSource:" + info);
			}
		} catch (Exception e) {

		}
		return findFlag;
	}

	public WebElement findElementOnce(By by) {
		return this.findElementOnce(by, false);
	}

	private WebElement findElementOnce(By by, boolean flag) {
		try {

			isFind = false;

			int countIndex = 0;
			do {
				SearchElement(this, by, flag);
				int sleepCount = 1 * 1000;
				if (!isFind) {
					Thread.sleep(sleepCount);

					if (countIndex > 0) {
						String sleepInfo = by.toString() + " smart wait #：" + countIndex + ",waiting time:" + sleepCount;
						System.out.println(sleepInfo);
						logger.info(sleepInfo);
					}
				}
			} while (!isFind && countIndex++ < 3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return super.findElement(by);
	}
	
	@Override
	public WebElement findElement(By by) {
		return this.findElement(by, false);
	}

	private WebElement findElement(By by, boolean flag) {
		try {
			isFind = false;
			int countIndex = 0;
			do {
				SearchElement(this, by, flag);
				int sleepCount = 1 * 1000;
				if (!isFind) {
					Thread.sleep(sleepCount);

					if (countIndex > 0) {
						String sleepInfo = by.toString() + " smart wait #：" + countIndex + ", waiting time:" + sleepCount;
						System.out.println(sleepInfo);
						logger.info(sleepInfo);
					}
				}
			} while (!isFind && countIndex++ < 30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return super.findElement(by);
	}

	public WebElement findElement(By by, int index) {
		WebElement ret;
		isFind = false;
		List<WebElement> ls = this.findElements(by);
		if (ls != null && !ls.isEmpty()) {
			if (index > ls.size()) {
				ret = ls.get(ls.size() - 1);
			} else if (index <= 0) {
				ret = ls.get(0);
			} else {
				ret = ls.get(index - 1);
			}
		} else {
			ret = super.findElement(by);
		}
		return ret;
	}


	public WebElement findElementNative(By by) {
		return super.findElement(by);
	}

	private boolean isFind = false;

	private void SearchElement(WebDriver driver, By by, boolean isDisplayedflag) {
		List<WebElement> switchToList = new ArrayList<WebElement>();
		switchToList.add(null);
		isFind = this.findElementFromFrame(switchToList, by, isDisplayedflag);
	}

	private boolean findElementFromFrame(List<WebElement> switchToList, By by, boolean isDisplayedflag) {
		boolean findFlag = false;
		try {
			if (isDisplayedflag) {
				super.findElement(by);
				findFlag = true;
				return findFlag;
			} else if (super.findElement(by).isDisplayed()) {
				if (!super.findElement(by).isEnabled()) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
					}
				}
				findFlag = true;
				return findFlag;
			}
		} catch (NoSuchElementException e) {
		} catch (ElementNotVisibleException e) {
		} catch (Exception e) {
		}
		this.switchToFrame(switchToList);
		try {
			if (isDisplayedflag) {
				super.findElement(by);
				findFlag = true;
				return findFlag;
			} else if (super.findElement(by).isDisplayed()) {
				if (!super.findElement(by).isEnabled()) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
					}
				}
				findFlag = true;
				return findFlag;
			}
		} catch (NoSuchElementException e) {
		} catch (ElementNotVisibleException e) {
		}

		List<WebElement> list = findFrames();
		if (list.size() == 0) {
			return findFlag;
		}
		for (int i = 0; i < list.size(); i++) {
			switchToList.add(list.get(i));

			findFlag = findElementFromFrame(switchToList, by, isDisplayedflag);
			if (!findFlag) {
				switchToList.remove(switchToList.size() - 1);
			} else {
				return findFlag;
			}
		}
		return findFlag;
	}

	private List<WebElement> findFrames() {
		List<WebElement> list = new ArrayList<WebElement>();
		try {
			list.addAll(this.findElements(By.tagName("iframe")));
		} catch (NoSuchElementException e) {
		}
		try {
			list.addAll(this.findElements(By.tagName("frame")));
		} catch (NoSuchElementException e) {
		}
		return list;
	}

	private void switchToFrame(List<WebElement> list) {
		for (int i = 0; i < list.size(); i++) {
			int count = 0;
			boolean flag = false;
			do {
				try {
					if (list.get(i) == null) {
						this.switchTo().defaultContent();
						flag = true;
					} else {
						this.switchTo().frame(list.get(i));
						flag = true;
					}
				} catch (Exception e) {
				}
				if (!flag) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			} while (!flag && count++ < 30);
		}
	}


	public boolean IsElementPresent(WebDriver driver, By by) {
		try {
			driver.manage().timeouts().implicitlyWait(200, TimeUnit.MICROSECONDS);
			return ((JUMPOSWebDriver) driver).findElementNative(by).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void get(String url) {
		try {
			super.get(url);
			Thread.sleep(3000);
			if (this.IsElementPresent(this, By.id("overridelink"))) {
				Thread.sleep(1000);
				this.findElementNative(By.id("overridelink")).click();
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void switchToWindowByTitle(String title) throws Exception {
		boolean handlesFlag = true;
		int countIndex = 0;
		do {
			try {
				if (this.getWindowHandles() != null && this.getWindowHandles().size() > 0) {
					for (String str : this.getWindowHandles()) {
						this.switchTo().window(str);
						if (this.getTitle().contains(title)) {
							System.out.println("Switch to Window :" + title);
							this.manage().window().maximize();
							handlesFlag = false;
							break;
						}
					}
					if (handlesFlag) {
						for (String str : this.getWindowHandles()) {
							this.switchTo().window(str);
							if (this.getTitle().contains(strCertificationErrorWindowTitle)) {
								System.out.println("Switch to Certification Error Window:" + str);
								this.manage().window().maximize();
								if (this.IsElementPresent(this, By.id("overridelink"))) {
									this.findElementNative(By.id("overridelink")).click();
								}
							}
						}
						int sleepCount = 1000;
						Thread.sleep(sleepCount);
						String sleepInfo = "driver.getWindowHandles /Smart wait ：#" + countIndex + "，waiting time:" + sleepCount;
						if (countIndex > 0) {
							System.out.println(sleepInfo);
							logger.info(sleepInfo);
						}
					}
				} else {
					int sleepCount = 1000;
					Thread.sleep(sleepCount);
					String sleepInfo = "driver.getWindowHandles /Smart wait ：#" + countIndex + "，waiting time:" + sleepCount;
					if (countIndex > 0) {
						System.out.println(sleepInfo);
						logger.info(sleepInfo);
					}
				}
			} catch (NoSuchWindowException e) {
				int sleepCount = 1000;
				Thread.sleep(sleepCount);
				String sleepInfo = "driver.getWindowHandles /Smart wait ：#" + countIndex + "，waiting time:" + sleepCount;
				if (countIndex > 0) {
					System.out.println(sleepInfo);
					logger.info(sleepInfo);
				}
			}
		} while (handlesFlag && countIndex++ < 30);
		if (countIndex > 30) {
			throw new Exception("It has been waiting over 30 times for WindowHandle " + title);
		}
	}

	public void switchToWindowByUrl(String url) throws Exception {
		boolean handlesFlag = true;
		int countIndex = 0;
		do {
			try {
				if (this.getWindowHandles() != null && this.getWindowHandles().size() > 0) {
					for (String str : this.getWindowHandles()) {
						this.switchTo().window(str);
						if (this.getCurrentUrl().contains(url)) {
							System.out.println("Switch to Window :" + url);
							this.manage().window().maximize();
							handlesFlag = false;
							break;
						}
					}
					if (handlesFlag) {
						for (String str : this.getWindowHandles()) {
							this.switchTo().window(str);
							if (this.getTitle().contains(strCertificationErrorWindowTitle)) {
								System.out.println("Switch to certification error window :" + str);
								this.manage().window().maximize();
								Thread.sleep(3000);
								if (this.IsElementPresent(this, By.id("overridelink"))) {
									Thread.sleep(1000);
									this.findElementNative(By.id("overridelink")).click();
									Thread.sleep(1000);
								}
							}
						}
						int sleepCount = 1000;
						Thread.sleep(sleepCount);
						String sleepInfo = "driver.getWindowHandles smart wait #：" + countIndex + "，waiting time:" + sleepCount;
						if (countIndex > 0) {
							System.out.println(sleepInfo);
							logger.info(sleepInfo);
						}
					}
				} else {
					int sleepCount = 1000;
					Thread.sleep(sleepCount);
					String sleepInfo = "driver.getWindowHandles smart wait #：" + countIndex + "，waiting time:" + sleepCount;
					if (countIndex > 0) {
						System.out.println(sleepInfo);
						logger.info(sleepInfo);
					}
				}
			} catch (NoSuchWindowException e) {
				int sleepCount = 1000;
				Thread.sleep(sleepCount);
				String sleepInfo = "driver.getWindowHandles smart wait #：" + countIndex + "，waiting time:" + sleepCount;
				if (countIndex > 0) {
					System.out.println(sleepInfo);
					logger.info(sleepInfo);
				}
			}
		} while (handlesFlag && countIndex++ < 30);
		if (countIndex > 30) {
			throw new Exception("It has been waiting over 30 times for WindowHandle " + url);
		}
	}

	public boolean IsAlertPresent() {
		boolean findFlag = false;
		int countIndex = 0;
		do {
			try {
				this.switchTo().alert();
				findFlag = true;
			} catch (NoAlertPresentException e) {
			}
			int sleepCount = 1000;
			if (!findFlag) {
				try {
					Thread.sleep(sleepCount);
				} catch (InterruptedException e) {
				}
			}
			if (countIndex > 0) {
				String sleepInfo = "Waiting for alert window #：" + countIndex + "waiting time:" + sleepCount;
				System.out.println(sleepInfo);
				logger.info(sleepInfo);
			}
		} while (!findFlag && countIndex++ < 3);
		return findFlag;
	}

	public void selectByValue(By by, String value) {
		WebElement ele = this.findElement(by);
		boolean flag = false;
		int time = 1;
		do {
			try {
				Select select = new Select(ele);
				select.selectByValue(value);
				if (!(flag = select.getOptions()
						.get(Integer.valueOf(this.findElement(by).getAttribute("selectedIndex"))).getAttribute("value")
						.equals(value))) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					flag = select.getOptions().get(Integer.valueOf(this.findElement(by).getAttribute("selectedIndex")))
							.getAttribute("value").equals(value);
				}
			} catch (ElementNotVisibleException e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				}
			}
		} while (!flag && ++time < 5);
	}


	public void selectByVisibleText(By by, String text) {
		WebElement ele = this.findElement(by);
		boolean flag = false;
		int time = 1;
		do {
			try {
				Select select = new Select(ele);
				select.selectByVisibleText(text);
				if (!(flag = select.getOptions()
						.get(Integer.valueOf(this.findElement(by).getAttribute("selectedIndex"))).getText()
						.equals(text))) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					flag = select.getOptions().get(Integer.valueOf(this.findElement(by).getAttribute("selectedIndex")))
							.getText().equals(text);
				}
			} catch (ElementNotVisibleException e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				}
			}
		} while (!flag && ++time < 5);
	}

	public void selectByIndex(By by, int index) {
		WebElement ele = this.findElement(by);
		boolean flag = false;
		int time = 1;
		do {
			try {
				Select select = new Select(ele);
				select.selectByIndex(index);
				if (!(flag = Integer.valueOf(this.findElement(by).getAttribute("selectedIndex")) == index)) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					flag = Integer.valueOf(this.findElement(by).getAttribute("selectedIndex")) == index;
				}
			} catch (ElementNotVisibleException e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				}
			}
		} while (!flag && ++time < 5);
	}


	public void selectRadio(By by) {
		selectCheckbox(by);
	}

	public void selectCheckbox(By by) {
		boolean flag = false;
		int time = 1;
		do {
			try {
				this.findElement(by).click();
				if (!(flag = this.findElement(by).isSelected())) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					flag = this.findElement(by).isSelected();
				}
			} catch (ElementNotVisibleException e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				}
			}
		} while (!flag && ++time < 5);
	}

	public void unSelectCheckbox(By by) {
		boolean flag = false;
		int time = 1;
		do {
			try {
				this.findElement(by).click();
				if (flag = this.findElement(by).isSelected()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					flag = this.findElement(by).isSelected();
				}
			} catch (ElementNotVisibleException e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				}
			}
		} while (flag && ++time < 5);
	}

	public void sendKeys(By by, String str) {
		boolean flag = true;
		int time = 1;
		do {
			try {
				this.findElement(by).clear();
				this.findElement(by).sendKeys(str);
				if (flag = "".equals(this.findElement(by).getAttribute("value"))) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					flag = "".equals(this.findElement(by).getAttribute("value"));
				}
			} catch (ElementNotVisibleException e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				}
			}
		} while (flag && ++time < 5);
	}


	public void check(String checkType, By by, String attr, String value, boolean flag) {
		boolean checkFlag = false;
		int time = 1;
		String str = "";
		do {
			str = getAttr(by, attr);
			checkFlag = check(checkType, str, value);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		} while (!checkFlag && ++time < 5);
		XmlLog.setCheck(checkFlag, value, str);
		if (!checkFlag) {
			if (flag) {
				throw new RuntimeException("Checkpoint failed. The test process will be terminated. Please see the report for the failure details.");
			}
		}
	}

	private boolean check(String checkType, String realValue, String checkValue) {
		if (checkType.toUpperCase().equals("EQUALS")) {
			return realValue.equals(checkValue);
		} else if (checkType.toUpperCase().equals("NOTEQUALS")) {
			return !realValue.equals(checkValue);
		} else if (checkType.toUpperCase().equals("CONTAINS")) {
			return realValue.contains(checkValue);
		} else if (checkType.toUpperCase().equals("NOTCONTAINS")) {
			return !realValue.contains(checkValue);
		}
		return false;
	}


	private String getAttr(By by, String attr) {
		String str = "";
		try {
			if (attr.toUpperCase().equals("TEXT")) {
				if (by == null) {
					str = this.alert().getText();
				} else {
					str = this.findElement(by, true).getText();
				}
				str = str == null ? "" : str;
			} else if (attr.toUpperCase().equals("DISPLAYED")) {
				str = String.valueOf(this.findElement(by, true).isDisplayed());
			} else if (attr.toUpperCase().equals("ENABLED")) {
				str = String.valueOf(this.findElement(by, true).isEnabled());
			} else if (attr.toUpperCase().equals("SELECTED")) {
				str = String.valueOf(this.findElement(by, true).isSelected());
			} else {
				str = this.findElement(by, true).getAttribute(attr);
				str = str == null ? "" : str;
			}
		} catch (ElementNotVisibleException e) {
		}
		return str;
	}

}
