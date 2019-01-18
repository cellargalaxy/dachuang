package top.cellargalaxy.dachuangspringboot.dataSet;

import lombok.Data;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by cellargalaxy on 17-9-7.
 */
@Data
public class DataSetParameter {
	private String coding;
	private String idColumnName;
	private String evidenceColumnName;
	private String fraudColumnName;
	private String unfraudColumnName;
	private String labelColumnName;
	private Collection<String> withoutEvidences;

	public DataSetParameter() {
		coding = "UTF-8";
		idColumnName = "id";
		evidenceColumnName = "evidence";
		fraudColumnName = "fraud";
		unfraudColumnName = "unfraud";
		labelColumnName = "label";
		withoutEvidences = Arrays.asList("total");
	}
}
