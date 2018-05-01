package top.cellargalaxy.version5.hereditary;

import org.json.JSONObject;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public class HereditaryParameter {
	//染色体数
	private int chrosNum;
	//保留染色体比例
	private double saveChroPro;
	//保留染色体数
	private int saveChroNum;
	
	//染色体长度
	private int chroLen;
	//基因交换比例
	private double geneExPro;
	//基因交换数量
	private int geneExNum;
	//基因突变比例
	private double geneMutNumPro;
	//基因突变数量
	private int geneMutNum;
	//基因突变概率
	private double geneMutPro;
	//基因步长
	private double step;
	
	//迭代次数
	private int iterNum;
	//最多相同最大解次数
	private int sameNum;
	//相同解范围
	private double sameDeviation;
	
	public static final HereditaryParameter createHereditaryParameter(int chrosNum, double saveChroPro, double geneExPro, double geneMutNumPro, double geneMutPro, double step, int iterNum, int sameNum, double sameDeviation) {
		if (check(chrosNum, saveChroPro, geneExPro, geneMutNumPro, geneMutPro, step, iterNum, sameNum, sameDeviation)) {
			return new HereditaryParameter(chrosNum, saveChroPro, geneExPro, geneMutNumPro, geneMutPro, step, iterNum, sameNum, sameDeviation);
		}
		return null;
	}
	
	public static final HereditaryParameter createHereditaryParameter(JSONObject jsonObject) {
		try {
			int chrosNum = jsonObject.getInt("chrosNum");
			double saveChroPro = jsonObject.getDouble("saveChroPro");
			double geneExPro = jsonObject.getDouble("geneExPro");
			double geneMutNumPro = jsonObject.getDouble("geneMutNumPro");
			double geneMutPro = jsonObject.getDouble("geneMutPro");
			double step = jsonObject.getDouble("step");
			int iterNum = jsonObject.getInt("iterNum");
			int sameNum = jsonObject.getInt("sameNum");
			double sameDeviation = jsonObject.getDouble("sameDeviation");
			if (check(chrosNum, saveChroPro, geneExPro, geneMutNumPro, geneMutPro, step, iterNum, sameNum, sameDeviation)) {
				return new HereditaryParameter(chrosNum, saveChroPro, geneExPro, geneMutNumPro, geneMutPro, step, iterNum, sameNum, sameDeviation);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public final static boolean check(int chrosNum, double saveChroPro, double geneExPro, double geneMutNumPro, double geneMutPro, double step, int iterNum, int sameNum, double sameDeviation){
		if (chrosNum<=1) {
			return false;
		}
		if (saveChroPro<=0||saveChroPro>=1) {
			return false;
		}
		if (geneExPro<=0||geneExPro>=1) {
			return false;
		}
		if (geneMutNumPro<=0||geneMutNumPro>=1) {
			return false;
		}
		if (geneMutPro<=0||geneMutPro>=1) {
			return false;
		}
		if (step<=0||step>=1) {
			return false;
		}
		if (iterNum<=1) {
			return false;
		}
		if (sameNum<=1) {
			return false;
		}
		if (sameDeviation<=0||sameDeviation>=1) {
			return false;
		}
		return true;
	}
	
	public JSONObject toJSONObject() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("chrosNum", chrosNum);
		jsonObject.put("saveChroPro", saveChroPro);
		jsonObject.put("geneExPro", geneExPro);
		jsonObject.put("geneMutNumPro", geneMutNumPro);
		jsonObject.put("geneMutPro", geneMutPro);
		jsonObject.put("step", step);
		jsonObject.put("iterNum", iterNum);
		jsonObject.put("sameNum", sameNum);
		jsonObject.put("sameDeviation", sameDeviation);
		return jsonObject;
	}
	
	private HereditaryParameter(int chrosNum, double saveChroPro, double geneExPro, double geneMutNumPro, double geneMutPro, double step, int iterNum, int sameNum, double sameDeviation) {
		this.chrosNum = chrosNum;
		this.saveChroPro = saveChroPro;
		this.geneExPro = geneExPro;
		this.geneMutNumPro = geneMutNumPro;
		this.geneMutPro = geneMutPro;
		this.step = step;
		this.iterNum = iterNum;
		this.sameNum = sameNum;
		this.sameDeviation = sameDeviation;
		
		saveChroNum = (int) (chrosNum * saveChroPro);
	}
	
	public HereditaryParameter() {
		//染色体数
		chrosNum = 30;
		//保留染色体比例
		saveChroPro = 0.5;
		//保留染色体数
		saveChroNum = (int) (chrosNum * saveChroPro);
		
		//基因交换比例
		geneExPro = 0.5;
		//基因突变比例
		geneMutNumPro = 0.5;
		//基因突变概率
		geneMutPro = 0.3;
		//基因步长
		step = 0.01;
		
		//迭代次数
		iterNum = 500;
		//最多相同最大解次数
		sameNum = 100;
		//相同解范围
		sameDeviation = 0.001;
	}
	
	public final void init(int evidenceCount) {
		//染色体长度
		chroLen = evidenceCount * 2;
		//基因交换数量
		geneExNum = (int) (chroLen * geneExPro);
		//基因突变数量
		geneMutNum = (int) (chroLen * geneMutNumPro);
	}
	
	public int getChrosNum() {
		return chrosNum;
	}
	
	public void setChrosNum(int chrosNum) {
		this.chrosNum = chrosNum;
	}
	
	public int getSaveChroNum() {
		return saveChroNum;
	}
	
	public void setSaveChroNum(int saveChroNum) {
		this.saveChroNum = saveChroNum;
	}
	
	public int getChroLen() {
		return chroLen;
	}
	
	public void setChroLen(int chroLen) {
		this.chroLen = chroLen;
	}
	
	public int getGeneExNum() {
		return geneExNum;
	}
	
	public void setGeneExNum(int geneExNum) {
		this.geneExNum = geneExNum;
	}
	
	public int getGeneMutNum() {
		return geneMutNum;
	}
	
	public void setGeneMutNum(int geneMutNum) {
		this.geneMutNum = geneMutNum;
	}
	
	public double getGeneMutPro() {
		return geneMutPro;
	}
	
	public void setGeneMutPro(double geneMutPro) {
		this.geneMutPro = geneMutPro;
	}
	
	public double getStep() {
		return step;
	}
	
	public void setStep(double step) {
		this.step = step;
	}
	
	public int getIterNum() {
		return iterNum;
	}
	
	public void setIterNum(int iterNum) {
		this.iterNum = iterNum;
	}
	
	public int getSameNum() {
		return sameNum;
	}
	
	public void setSameNum(int sameNum) {
		this.sameNum = sameNum;
	}
	
	public double getSameDeviation() {
		return sameDeviation;
	}
	
	public void setSameDeviation(double sameDeviation) {
		this.sameDeviation = sameDeviation;
	}
	
	@Override
	public String toString() {
		return "HereditaryParameter{" +
				"chrosNum=" + chrosNum +
				", saveChroPro=" + saveChroPro +
				", saveChroNum=" + saveChroNum +
				", chroLen=" + chroLen +
				", geneExPro=" + geneExPro +
				", geneExNum=" + geneExNum +
				", geneMutNumPro=" + geneMutNumPro +
				", geneMutNum=" + geneMutNum +
				", geneMutPro=" + geneMutPro +
				", step=" + step +
				", iterNum=" + iterNum +
				", sameNum=" + sameNum +
				", sameDeviation=" + sameDeviation +
				'}';
	}
}
