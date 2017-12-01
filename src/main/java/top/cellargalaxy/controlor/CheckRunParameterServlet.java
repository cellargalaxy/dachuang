package top.cellargalaxy.controlor;

import org.json.JSONObject;
import top.cellargalaxy.version5.run.RunParameter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by cellargalaxy on 17-11-14.
 */
public class CheckRunParameterServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json; charset=" + CodingFilter.getCoding());
		Writer writer = resp.getWriter();
		JSONObject jsonObject = new JSONObject();
		
		try {
			String runParameterJsonString = req.getParameter("runParameter");
			if (runParameterJsonString == null || runParameterJsonString.length() == 0) {
				JSONObject data = new JSONObject();
				data.put("message", "无参数提交");
				jsonObject.put("status", 1);
				jsonObject.put("data", data);
			} else {
				JSONObject runParameterJson = new JSONObject(runParameterJsonString);
				RunParameter runParameter = new RunParameter(null, null, runParameterJson);
				String string = runParameter.checkOnlyParameter();
				if (string == null) {
					JSONObject data = new JSONObject();
					data.put("message", "参数合法");
					jsonObject.put("status", 0);
					jsonObject.put("data", data);
				} else {
					JSONObject data = new JSONObject();
					data.put("message", string);
					jsonObject.put("status", 1);
					jsonObject.put("data", data);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject data = new JSONObject();
			data.put("message", "json字符串格式化失败");
			jsonObject.put("status", 1);
			jsonObject.put("data", data);
		} finally {
			writer.write(jsonObject.toString());
			writer.close();
		}
	}
}
