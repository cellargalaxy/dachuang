package top.cellargalaxy.dachuangspringboot.main;

import org.json.JSONObject;
import top.cellargalaxy.dachuangspringboot.dataSet.DataSetParameter;
import top.cellargalaxy.dachuangspringboot.evaluation.EvaluationExecutorFactory;
import top.cellargalaxy.dachuangspringboot.run.Run;
import top.cellargalaxy.dachuangspringboot.run.RunParameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cellargalaxy on 18-2-24.
 */
public class Main {
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");

	public static void main(String[] args) {
		main(new File("D:\\g\\dachuang-springboot\\src\\main\\resources\\conf.json"), 1);
	}


	public static final void main(File jsonFile, int count) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(jsonFile));
			StringBuffer jsonString = new StringBuffer();
			String string;
			while ((string = reader.readLine()) != null) {
				jsonString.append(string);
			}
			reader.close();
			JSONObject jsonObject = new JSONObject(jsonString.toString());

			DataSetParameter dataSetParameter = new DataSetParameter();
			RunParameter runParameter = new RunParameter(dataSetParameter, jsonObject);
			string = runParameter.check();
			if (string != null) {
				System.out.println("check: " + string);
				System.exit(0);
			}

			for (int i = 0; i < count; i++) {
				runParameter.getTrainDataSet().outputDataSet(dataSetParameter, new File((runParameter.getDataSetFile() != null ? runParameter.getDataSetFile().getName() : runParameter.getTrainDataSetFile().getName()) + "-trainDataSet(" + DATE_FORMAT.format(new Date()) + ").csv"));
				runParameter.getTestDataSet().outputDataSet(dataSetParameter, new File((runParameter.getDataSetFile() != null ? runParameter.getDataSetFile().getName() : runParameter.getTestDataSetFile().getName()) + "-testDataSet(" + DATE_FORMAT.format(new Date()) + ").csv"));

				File trainOutputFile = new File((runParameter.getDataSetFile() != null ? runParameter.getDataSetFile().getName() : runParameter.getTrainDataSetFile().getName()) + "-trainSubSpaceDataSet(" + DATE_FORMAT.format(new Date()) + ").csv");
				File testOutputFile = new File((runParameter.getDataSetFile() != null ? runParameter.getDataSetFile().getName() : runParameter.getTestDataSetFile().getName()) + "-testSubSpaceDataSet(" + DATE_FORMAT.format(new Date()) + ").csv");
				Run.runOutputFile(runParameter, trainOutputFile, testOutputFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		EvaluationExecutorFactory.shutdown();
	}

}
