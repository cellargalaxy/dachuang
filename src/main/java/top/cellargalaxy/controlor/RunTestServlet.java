package top.cellargalaxy.controlor;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by cellargalaxy on 17-11-15.
 */
public class RunTestServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StringWriter stringWriter=new StringWriter();
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(req.getInputStream()));
		String string;
		while ((string = bufferedReader.readLine()) != null) {
			stringWriter.write(string);
		}
		System.out.println("json: "+stringWriter.toString());
		Writer writer=resp.getWriter();
		JSONObject data=new JSONObject();
		data.put("message","成功解析返回结果，知道计算完毕之类的");
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("status",0);
		jsonObject.put("data",data);
		writer.write(jsonObject.toString());
		writer.close();
	}
}
