package top.cellargalaxy.dachuangspringboot.subSpaceSynthesis;

import lombok.Data;
import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EvidenceSynthesis;

/**
 * @author cellargalaxy
 * @time 2019/1/18
 */
@Data
public class SubSpaceSynthesisResult {
	private final DataSet dataSet;
	private final EvidenceSynthesis evidenceSynthesis;
	private final double evaluationValue;
}
