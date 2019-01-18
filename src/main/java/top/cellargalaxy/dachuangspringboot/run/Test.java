package top.cellargalaxy.dachuangspringboot.run;

import org.yaml.snakeyaml.Yaml;
import top.cellargalaxy.dachuangspringboot.dataSet.DataSetParameter;

import java.util.Arrays;

/**
 * @author cellargalaxy
 * @time 2019/1/18
 */
public class Test {

	public static void main(String[] args) {
		RunParameter runParameter = new RunParameter();
		DataSetParameter dataSetParameter = runParameter.getDataSetParameter();
		dataSetParameter.setIdColumnName("id");
		dataSetParameter.setEvidenceColumnName("evidence");
		dataSetParameter.setFraudColumnName("fraud");
		dataSetParameter.setUnfraudColumnName("unfraud");
		dataSetParameter.setLabelColumnName("collusion_transaction");
		dataSetParameter.setWithoutEvidences(Arrays.asList("total"));
		runParameter.setDataSetParameter(dataSetParameter);
		runParameter.setDataSetPath("g:/test.txt");


		System.out.println(runParameter);

		Yaml yaml = new Yaml();
		String string = yaml.dump(runParameter);

		runParameter = yaml.loadAs(string, RunParameter.class);
		System.out.println(runParameter);
	}
}
