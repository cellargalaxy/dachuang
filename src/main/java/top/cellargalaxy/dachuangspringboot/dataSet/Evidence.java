package top.cellargalaxy.dachuangspringboot.dataSet;

import lombok.Data;

/**
 * @author cellargalaxy
 * @time 2019/1/11
 */
@Data
public class Evidence {
	private final int evidenceId;
	private final String evidenceName;
	private final double[] values;

	public double getFraud() {
		return values[FromCsvReadDataSetImpl.FRAUD_INDEX];
	}

	public double getUnfraud() {
		return values[FromCsvReadDataSetImpl.UNFRAUD_INDEX];
	}

	@Override
	public Evidence clone() {
		return new Evidence(evidenceId, evidenceName, values.clone());
	}
}
