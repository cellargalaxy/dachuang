package top.cellargalaxy.controlor;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;
import top.cellargalaxy.version5.dataSet.DataSetParameter;
import top.cellargalaxy.version5.dataSet.DataSetSeparation;
import top.cellargalaxy.version5.run.RunParameter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by cellargalaxy on 17-11-14.
 */
public class UploadDataSetServlet extends HttpServlet {
	public static final DataSetParameter DATA_SET_PARAMETER = new DataSetParameter();
	private String rootPath;
	private DiskFileItemFactory factory;
	
	@Override
	public void init() throws ServletException {
		//获得磁盘文件条目工厂
		factory = new DiskFileItemFactory();
		//获取文件需要上传到的路径
		rootPath = getServletContext().getRealPath("/");
		File repository = new File(rootPath);
		repository.mkdirs();
		//设置存储室
		factory.setRepository(repository);
		//设置缓存的大小，当上传文件的容量超过该缓存时，直接放到暂时存储室
		factory.setSizeThreshold(1024 * 1024);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json; charset=" + CodingFilter.getCoding());
		Writer writer = resp.getWriter();
		JSONObject jsonObject = new JSONObject();
		
		//文件上传处理
		ServletFileUpload upload = new ServletFileUpload(factory);
		File dataSetFile = null;
		try {
			List<FileItem> list = upload.parseRequest(req);
			//可以上传多个文件
			for (FileItem item : list) {
				//获取表单的属性名字
				String name = item.getFieldName();
				if (item.isFormField()) {//如果是普通的文本信息
				
				} else {//对传入的非简单的字符串进行处理
					if (dataSetFile != null) {//只接受第一个文件
						continue;
					}
					//获取路径名
					String filename = item.getName();
					if (filename == null || filename.length() == 0) {
						do {
							filename = rootPath + File.separator + "upload" + File.separator + (int) (Math.random() * 100000000) + ".noname";
							dataSetFile = new File(filename);
						} while (dataSetFile == null || dataSetFile.exists());
					} else {
						//索引到最后一个反斜杠
						int start = filename.lastIndexOf("\\");
						//截取上传文件的字符串名字，加1是去掉反斜杠
						filename = rootPath + "/" + filename.substring(start + 1);
						dataSetFile = new File(filename);
					}
					dataSetFile.getParentFile().mkdirs();
					InputStream inputStream = null;
					OutputStream outputStream = null;
					try {
						inputStream = new BufferedInputStream(item.getInputStream());
						outputStream = new BufferedOutputStream(new FileOutputStream(dataSetFile));
						int len;
						byte[] bytes = new byte[1024];
						while ((len = inputStream.read(bytes)) != -1) {
							outputStream.write(bytes, 0, len);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
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
					}
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
			JSONObject data = new JSONObject();
			data.put("message", "文件上传失败");
			jsonObject.put("status", 1);
			jsonObject.put("data", data);
		}
		
		
		try {
			if (dataSetFile == null) {
				JSONObject data = new JSONObject();
				data.put("message", "没有文件");
				jsonObject.put("status", 1);
				jsonObject.put("data", data);
			} else {
				RunParameter runParameter = new RunParameter(DATA_SET_PARAMETER, dataSetFile);
				DataSetSeparation dataSetSeparation = runParameter.getDataSetSeparation();
				JSONObject info = new JSONObject();
				info.put("com0", dataSetSeparation.getCom0Count());
				info.put("com1", dataSetSeparation.getCom1Count());
				info.put("miss0", dataSetSeparation.getMiss0Count());
				info.put("miss1", dataSetSeparation.getMiss1Count());
				JSONObject data = new JSONObject();
				data.put("message", "成功");
				data.put("info", info);
				jsonObject.put("status", 0);
				jsonObject.put("data", data);
			}
		} catch (Exception e) {
			JSONObject data = new JSONObject();
			data.put("message", "数据集解析失败");
			jsonObject.put("status", 1);
			jsonObject.put("data", data);
		} finally {
			writer.write(jsonObject.toString());
			writer.close();
			if (dataSetFile != null) {
				dataSetFile.delete();
			}
		}
	}
}
