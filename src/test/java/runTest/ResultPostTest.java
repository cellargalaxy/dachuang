package runTest;

import org.json.JSONObject;
import top.cellargalaxy.controlor.CodingFilter;
import top.cellargalaxy.service.ResultPost;
import top.cellargalaxy.util.Printer;

/**
 * Created by cellargalaxy on 17-11-26.
 */
public class ResultPostTest {
	public static void main(String[] args) {
		System.out.println("CodingFilter.getCoding():" + CodingFilter.getCoding());
		
		JSONObject jsonObject = new JSONObject();
		JSONObject data=new JSONObject();
		data.put("data","我是数据");
		if (data == null) {
			data = new JSONObject();
			data.put("message", "计算异常");
			jsonObject.put("status", 1);
		} else {
			jsonObject.put("status", 0);
		}
		jsonObject.put("data", data);
		Printer.print("回复结果："+ ResultPost.postResult("http://202.116.148.193:2248/solutionResult" ,jsonObject));
	}
}
