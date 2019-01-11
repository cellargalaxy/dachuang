package top.cellargalaxy.dachuangspringboot.hereditary;

import lombok.Data;

/**
 * Created by cellargalaxy on 17-9-8.
 */
@Data
public class HereditaryParameter {
	//染色体数
	private final int chroNum;
	//每个染色体的基因数
	private int geneNum;
	//每个基因的碱基数
	private final int baseNum;

	//保留染色体比例
	private final double saveChroPro;
	//保留染色体数
	private final int saveChroNum;

	//碱基交换比例
	private final double baseExPro;
	//碱基突变概率
	private final double baseMutPro;
	//碱基步长
	private final double step;

	//迭代次数
	private final int iterationCount;
	//相同解范围
	private final double sameDeviation;
	//最多相同最大解次数
	private final int sameCount;

	public HereditaryParameter() {
		//染色体数
		this.chroNum = 30;
		//每个基因的碱基数
		this.baseNum = 2;
		//保留染色体比例
		this.saveChroPro = 0.5;
		//保留染色体数
		this.saveChroNum = (int) (saveChroPro * chroNum);
		//碱基交换比例
		this.baseExPro = 0.5;
		//碱基突变概率
		this.baseMutPro = 0.2;
		//碱基步长
		this.step = 0.01;
		//迭代次数
		this.iterationCount = 500;
		//相同解范围
		this.sameDeviation = 0.001;
		//最多相同最大解次数
		this.sameCount = (int) (0.1 * iterationCount);
	}
}
