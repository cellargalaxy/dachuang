package hereditary;

import auc.AUC;
import dataSet.DataSet;
import util.CloneObject;

import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * Created by cellargalaxy on 2017/5/7.
 * 遗传算法对象类
 */
public class Hereditary {
	private DataSet dataSet;

	public Hereditary(DataSet dataSet) {
		this.dataSet = dataSet;

		//染色体数
		chrosNum = 30;
		//保留染色体比例
		saveChroPro = 0.5;
		//保留染色体数
		saveChroNum = (int) (chrosNum * saveChroPro);

		//染色体长度
		chroLen = dataSet.getEvidenceNums().size() * 2;
		//基因交换比例
		geneExPro = 0.5;
		//基因交换数量
		geneExNum = (int) (chroLen * geneExPro);
		//基因突变比例
		geneMutNumPro = 0.5;
		//基因突变数量
		geneMutNum = (int) (chroLen * geneMutNumPro);
		//基因突变概率
		geneMutPro = 0.3;
		//基因步长
		step = 0.01;

		//迭代次数
		iterNum = 100;
		//已迭代次数
		yetIterCount = 0;
		//最多相同最大解次数
		sameNum = 50;
		//已相同最大解数
		yetSameCount = 0;
		//相同解范围
		sameDeviation = 0.001;
		//最新AUC
		latestAUC = -1;

		//最大AUC
		maxAUC = AUC.countAUCAll(dataSet);
		//最大AUC对应的基因
		maxChro = new double[chroLen];
		for (int i = 0; i < maxChro.length; i++) {
			maxChro[i] = 1;
		}
	}


	public static double[] superEvolutionChro(DataSet dataSet, int evolutionMethodNum, int count) throws IOException, ClassNotFoundException {
		DataSet dataSet1 = CloneObject.clone(dataSet);
		double[] chro = null;
		for (int i = 0; i < count; i++) {
			Hereditary hereditary = new Hereditary(dataSet1);
			hereditary.evolution(evolutionMethodNum);
			dataSet1.mulChro(hereditary.getMaxChro());
			if (chro == null) {
				chro = hereditary.getMaxChro();
			} else {
				int j = 0;
				for (double v : hereditary.getMaxChro()) {
					chro[j] *= v;
					j++;
				}
			}
//			System.out.println(hereditary.getMaxAUC());
		}
		return chro;
	}

	public static double superEvolutionAUC(DataSet dataSet, LinkedList<Integer> evidenceNums,int evolutionMethodNum, int count) throws IOException, ClassNotFoundException {
		DataSet dataSet1 = CloneObject.clone(dataSet);
		dataSet1.allSaveEvidence(evidenceNums);
		Hereditary hereditary=null;
		for (int i = 0; i < count; i++) {
			hereditary = new Hereditary(dataSet1);
			hereditary.evolution(evolutionMethodNum);
			dataSet1.mulChro(hereditary.getMaxChro());
//			System.out.println(hereditary.getMaxAUC());
		}
		return hereditary.getMaxAUC();
	}

	public static double superEvolutionAUC(DataSet dataSet, int evidenceNum,int evolutionMethodNum, int count) throws IOException, ClassNotFoundException {
		DataSet dataSet1 = CloneObject.clone(dataSet);
		dataSet1.removeEvidence(evidenceNum);
		Hereditary hereditary=null;
		for (int i = 0; i < count; i++) {
			hereditary = new Hereditary(dataSet1);
			hereditary.evolution(evolutionMethodNum);
			dataSet1.mulChro(hereditary.getMaxChro());
//			System.out.println(hereditary.getMaxAUC());
		}
		return hereditary.getMaxAUC();
	}

	public static double superEvolutionAUC(DataSet dataSet, int evolutionMethodNum, int count) throws IOException, ClassNotFoundException {
		DataSet dataSet1 = CloneObject.clone(dataSet);
		Hereditary hereditary=null;
		for (int i = 0; i < count; i++) {
			hereditary = new Hereditary(dataSet1);
			hereditary.evolution(evolutionMethodNum);
			dataSet1.mulChro(hereditary.getMaxChro());
//			System.out.println(hereditary.getMaxAUC());
		}
		return hereditary.getMaxAUC();
	}

	/**
	 * 进化
	 *
	 * @param evolutionMethodNum
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void evolution(int evolutionMethodNum) throws IOException, ClassNotFoundException {
		if (dataSet.getEvidenceNums().size() > 1) {
			double[][] chros = createInitChros();
			do {
				chros = createNewChros(dataSet, chros, evolutionMethodNum);
				if (chros == null) {
					break;
				}
			} while (true);
		} else {
			System.out.println("特征数为1，无法进化");
		}
	}

	/**
	 * 生育
	 *
	 * @param oldChros
	 * @param methodNum
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private double[][] createNewChros(DataSet dataSet, double[][] oldChros, int methodNum) throws IOException, ClassNotFoundException {
		yetIterCount++;
		if (yetIterCount > iterNum) {
//			System.out.println("到达最大迭代次数，跳出迭代");
			return null;
		}

		Map<Double, double[]> map = mulChros(dataSet, oldChros);
		if (map.size() < 2) {
			System.out.println("多样性进化失败，跳出迭代");
			return null;
		}

		int startPoint;
		if (map.size() > saveChroNum) {
			startPoint = saveChroNum;
		} else {
			startPoint = map.size();
		}
		double auc = -1;
		double[] chro;
		double[] aucs = new double[startPoint];
		double[][] newChros = new double[chrosNum][];
		int i = 0;
		for (Map.Entry<Double, double[]> entry : map.entrySet()) {
			if (auc == -1) {
				auc = entry.getKey();
				chro = entry.getValue();

				if (auc <= maxAUC + sameDeviation) {
					yetSameCount++;
				} else {
					yetSameCount = 0;
				}
				if (yetSameCount >= sameNum) {
//					System.out.println("达到相同最大迭代次数，跳出迭代");
					return null;
				}

				if (auc > maxAUC) {
					maxAUC = auc;
					maxChro = CloneObject.clone(chro);
				}

				latestAUC = auc;
			}

			aucs[i] = entry.getKey();
			newChros[i] = entry.getValue();
			i++;
			if (i >= startPoint) {
				break;
			}
		}


		if (methodNum == USE_Roulette) {
			double aucCount = 0;
			for (double v : aucs) {
				aucCount += v;
			}
			for (int j = startPoint; j < newChros.length; j++) {
				double[][] ds = chooseRouletteParentsGene(newChros, aucs, aucCount);
				ds = geneEx(ds[0], ds[1]);
				newChros[j] = ds[0];
				j++;
				if (j >= newChros.length) break;
				newChros[j] = ds[1];
			}
		} else if (methodNum == USE_ORDER) {
			for (int j = startPoint; j < newChros.length; j++) {
				double[][] ds = chooseOrderParentsGene(newChros, j - startPoint);
				ds = geneEx(ds[0], ds[1]);
				newChros[j] = ds[0];
				j++;
				if (j >= newChros.length) break;
				newChros[j] = ds[1];
			}
		} else {
			throw new RuntimeException("选择父母染色体方法参数异常");
		}


		for (int k = 0; k < newChros.length; k++) {
			newChros[k] = geneMul(newChros[k]);
		}

		return newChros;
	}

	/**
	 * 基因突变
	 *
	 * @param chro
	 * @return
	 */
	private double[] geneMul(double[] chro) {
		int count = 0;
		for (int i = 0; i < chro.length; i++) {
			if (count >= geneMutNum) {
				return chro;
			} else if (Math.random() < geneMutPro) {
				chro[i] = createRandomGene();
				count++;
			}
		}
		return chro;
	}

	/**
	 * 用降序选择两个父母基因
	 *
	 * @param chros
	 * @param point
	 * @return
	 */
	private double[][] chooseOrderParentsGene(double[][] chros, int point) {
		double[][] ds = {chros[point], chros[point + 1]};
		return ds;
	}

	/**
	 * 用轮盘算法选择两个父母基因
	 *
	 * @param chros
	 * @param aucs
	 * @param aucCount
	 * @return
	 */
	private double[][] chooseRouletteParentsGene(double[][] chros, double[] aucs, double aucCount) {
		int point1 = Roulette.roulette(aucs, aucCount);
		int point2;
		do {
			point2 = Roulette.roulette(aucs, aucCount);
		} while (point1 == point2);
		double[][] ds = {chros[point1], chros[point2]};
		return ds;
	}

	/**
	 * 父母基因进行基因交换
	 *
	 * @param chro1
	 * @param chro2
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private double[][] geneEx(double[] chro1, double[] chro2) throws IOException, ClassNotFoundException {
		double[] c1 = CloneObject.clone(chro1);
		double[] c2 = CloneObject.clone(chro2);
		double[][] ds = new double[2][];
		LinkedList<Integer> points = createRandomIntSet(c1.length, geneExNum);
		for (Integer point : points) {
			double d = c1[point];
			c1[point] = c2[point];
			c2[point] = d;
		}
		ds[0] = c1;
		ds[1] = c2;
		return ds;
	}

	/**
	 * 产生一个随机数集合
	 *
	 * @param len
	 * @param count
	 * @return
	 */
	private LinkedList<Integer> createRandomIntSet(int len, int count) {
		LinkedList<Integer> points = new LinkedList<Integer>();
		while (points.size() < count) {
			int point = (int) (Math.random() * len);
			if (!points.contains(point)) {
				points.add(point);
			}
		}
		return points;
	}

	/**
	 * 依次将各个基因与嫌疑人的证据相乘，算出新的AUC，降序储存到Map中
	 *
	 * @param dataSet
	 * @param chros
	 * @return Map<AUC,基因>
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Map<Double, double[]> mulChros(DataSet dataSet,  double[][] chros) throws IOException, ClassNotFoundException {
		Map<Double, double[]> map = new TreeMap<Double, double[]>(new Comparator<Double>() {
			public int compare(Double a, Double b) {
				if (a > b) {
					return -1;
				} else if (a < b) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		for (double[] chro : chros) {
			DataSet newDataSet = CloneObject.clone(dataSet);
			newDataSet.mulChro(chro);
			map.put(AUC.countAUCAll(newDataSet), chro);
		}
		return map;
	}


	private double[][] createInitChros() {
		double[][] chros = new double[chrosNum][chroLen];
		for (int i = 0; i < chros.length; i++) {
			for (int j = 0; j < chros[i].length; j++) {
				chros[i][j] = createRandomGene();
			}
		}
		return chros;
	}

	private double createRandomGene() {
		double d = Math.random();
		return d - (d % step) + step;
	}

	public double getMaxAUC() {
		return maxAUC;
	}

	public void setMaxAUC(double maxAUC) {
		this.maxAUC = maxAUC;
	}

	public double[] getMaxChro() {
		return maxChro;
	}

	public void setMaxChro(double[] maxChro) {
		this.maxChro = maxChro;
	}

	public static final int USE_Roulette = 1;
	public static final int USE_ORDER = 2;
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
	//基因突变概率
	private int geneMutNum;
	//基因突变概率
	private double geneMutPro;
	//基因步长
	private double step;

	//迭代次数
	private int iterNum;
	//已迭代次数
	private int yetIterCount;
	//最多相同最大解次数
	private int sameNum;
	//已相同最大解数
	private int yetSameCount;
	//相同解范围
	private double sameDeviation;
	//最新AUC
	private double latestAUC;

	//最大AUC
	private double maxAUC;
	//最大AUC对应的基因
	private double[] maxChro;

}
