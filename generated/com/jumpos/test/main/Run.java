package com.jumpos.test.main;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.jumpos.test.utils.*;

import com.jumpos.test.testflow.Test0;

public class Run {

	Logger logger = Logger.getLogger(Run.class);

	private Thread runThread = null;

	public static String runState = "";

	public static int errorEchorunCount = 2;

	public static boolean errorEchoFlag = false;

	public void run() {
		KillProcess.KillIEDriverServer();
		perform();// Perform Test Script
		int i = 0;
		do {
			try {
				Thread.sleep(1 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (runThread.isAlive() && ++i < 600 * 60);
		if (runThread.isAlive()) {
			String runInfo = "Execution failed(Exceed the time limitation)";
			System.out.println("Execution Result:" + runInfo + "\t");
			try {
				throw new Exception("Execution failed(Exceed the time limitation。");
			} catch (Exception e) {
				XmlLog.errorList.add(e);
				XmlLog.imgLinkList.add(XmlLog.getImgFile());
			}
			XmlLog.getReport(runInfo);
			KillProcess.KillIEDriverServer();
			System.out.println("All of threads are ended！");
		}
	}

	public void perform() {
		runThread = new Thread(new Runnable() {

			@Override
			public void run() {
				int runCount = 1; // Execution times
				for (int i = 0; i < runCount; i++) {

					XmlLog.setReportSetting();

					runState = "ok";
					long startTime = System.currentTimeMillis();

					Test0.invoke();

					// System.out.println("runState:" + runState);
					long endTime = System.currentTimeMillis();
					SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
					String runInfo = "#:" + (i + 1) + " execution，beginning time：" + format.format(new Date(startTime)) + "Ending Time："
							+ format.format(new Date(endTime)) + "Execution Result" + runState + "\t Execution time："
							+ (endTime - startTime) / 1000d;
					System.out.println(runInfo);
					logger.info(runInfo);
					XmlLog.getReport(runState);
					Param_Util.SaveExcel();
					try {
						KillProcess.KillIEDriverServer();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		runThread.setDaemon(true);
		runThread.start();
		System.out.println("Execution thread is launched！");
	}

	public static void main(String[] args) throws Exception {
		Param_Util.checkArgs(args);
		new Run().run();
	}
}
