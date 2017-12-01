package top.cellargalaxy.service;

import org.json.JSONObject;
import top.cellargalaxy.util.Printer;
import top.cellargalaxy.version5.run.Run;
import top.cellargalaxy.version5.run.RunParameter;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by cellargalaxy on 17-11-14.
 */
public class RunThread extends Thread {
	private final RunParameter runParameter;
	private final String resultPostUrl;
	
	public RunThread(String name, RunParameter runParameter, String resultPostUrl) {
		super(name);
		this.runParameter = runParameter;
		this.resultPostUrl = resultPostUrl;
	}
	
	@Override
	public void run() {
		try{
			JSONObject data = null;
			try {
				data = Run.runOutputJson(runParameter);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			JSONObject jsonObject = new JSONObject();
			if (data == null) {
				data = new JSONObject();
				data.put("message", "计算异常");
				jsonObject.put("status", 1);
			} else {
				jsonObject.put("status", 0);
			}
			jsonObject.put("data", data);
			Printer.print("回复结果："+ResultPost.postResult(resultPostUrl, jsonObject));
		}finally {
			if (runParameter.getDataSetFile()!=null) {
				runParameter.getDataSetFile().delete();
			}
		}
		
	}
}
