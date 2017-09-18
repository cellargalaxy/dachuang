package version2.hereditary;

import util.CloneObject;
import version2.ds.AucCount;
import version2.dataSet.DataSet;

import java.io.IOException;
import java.util.*;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public class Hereditary {
	//已迭代次数
	private int yetIterCount;
	//已相同最大解数
	private int yetSameCount;
	//最大AUC
	private double maxAuc;
	//最大AUC对应的基因
	private double[] maxChro;
	
	private HereditaryParameter hereditaryParameter;
	private final DataSet dataSet;
	
	public Hereditary(DataSet dataSet) {
		this.dataSet = dataSet;
	}
	
	
	public final void evolution(HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose, AucCount aucCount) throws IOException, ClassNotFoundException {
		initEvolution(hereditaryParameter);
		//最大AUC
		maxAuc = aucCount.countIndexAuc(dataSet, maxChro);
		double[][] chros = createInitChros();
		do {
			chros = createIndexNewChros(dataSet, chros, aucCount, parentChrosChoose);
//			System.out.println("进化auc:"+maxAuc);
		} while (chros != null);
	}
	
	public final void evolution(HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose, AucCount aucCount, Integer withoutEvidNum) throws IOException, ClassNotFoundException {
		initEvolution(hereditaryParameter);
		//最大AUC
		maxAuc = aucCount.countIndexAuc(dataSet, withoutEvidNum, maxChro);
		double[][] chros = createInitChros();
		do {
			chros = createIndexNewChros(dataSet, chros, aucCount, parentChrosChoose, withoutEvidNum);
//			System.out.println("进化auc:"+maxAuc);
		} while (chros != null);
	}
	
	public final DataSet evolution(HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose, AucCount aucCount, List<Integer> withEvidNums) throws IOException, ClassNotFoundException {
		initEvolution(hereditaryParameter);
		DataSet cloneDataSet=CloneObject.clone(dataSet);
		cloneDataSet.removeNotEqual(withEvidNums);
		//最大AUC
		maxAuc = aucCount.countOrderAuc(dataSet, withEvidNums, maxChro);
		double[][] chros = createInitChros();
		do {
			chros = createOrderNewChros(cloneDataSet, chros, aucCount, parentChrosChoose);
//			System.out.println("进化auc:"+maxAuc);
		} while (chros != null);
		return cloneDataSet;
	}
	
	private void initEvolution(HereditaryParameter hereditaryParameter) {
		this.hereditaryParameter = hereditaryParameter;
		hereditaryParameter.init(dataSet.getEvidenceNums().size());
		//已迭代次数
		yetIterCount = 0;
		//已相同最大解数
		yetSameCount = 0;
		//最大AUC对应的基因
		maxChro = new double[hereditaryParameter.getChroLen()];
		for (int i = 0; i < maxChro.length; i++) {
			maxChro[i] = 1;
		}
	}
	
	private final double[][] createIndexNewChros(DataSet dataSet, double[][] oldChros, AucCount aucCount, ParentChrosChoose parentChrosChoose) throws IOException, ClassNotFoundException {
		yetIterCount++;
		if (yetIterCount > hereditaryParameter.getIterNum()) {
//			System.out.println("到达最大迭代次数，跳出迭代");
			return null;
		}
		
		Map<Double, double[]> map = indexDataSetMulChros(dataSet, aucCount, oldChros);
		return doCreateNewChros(map, parentChrosChoose);
	}
	
	private final double[][] createIndexNewChros(DataSet dataSet, double[][] oldChros, AucCount aucCount, ParentChrosChoose parentChrosChoose, Integer withoutEvidNum) throws IOException, ClassNotFoundException {
		yetIterCount++;
		if (yetIterCount > hereditaryParameter.getIterNum()) {
//			System.out.println("到达最大迭代次数，跳出迭代");
			return null;
		}
		
		Map<Double, double[]> map = indexDataSetMulChros(dataSet, aucCount, oldChros, withoutEvidNum);
		return doCreateNewChros(map, parentChrosChoose);
	}
	
	private final double[][] createOrderNewChros(DataSet cloneDataSet, double[][] oldChros, AucCount aucCount, ParentChrosChoose parentChrosChoose) throws IOException, ClassNotFoundException {
		yetIterCount++;
		if (yetIterCount > hereditaryParameter.getIterNum()) {
//			System.out.println("到达最大迭代次数，跳出迭代");
			return null;
		}
		
		Map<Double, double[]> map = orderDataSetMulChros(cloneDataSet, aucCount, oldChros);
		return doCreateNewChros(map, parentChrosChoose);
	}
	
	private final double[][] doCreateNewChros(Map<Double, double[]> map, ParentChrosChoose parentChrosChoose) throws IOException, ClassNotFoundException {
		if (map.size() < 2) {
//			System.out.println("多样性进化失败，跳出迭代");
			return null;
		}
		
		int startPoint;
		if (map.size() > hereditaryParameter.getSaveChroNum()) {
			startPoint = hereditaryParameter.getSaveChroNum();
		} else {
			startPoint = map.size();
		}
		double auc = -1;
		double[] chro;
		double[] aucs = new double[startPoint];
		double[][] newChros = new double[hereditaryParameter.getChrosNum()][];
		int i = 0;
		for (Map.Entry<Double, double[]> entry : map.entrySet()) {
			if (auc == -1) {
				auc = entry.getKey();
				chro = entry.getValue();
				
				if (auc <= maxAuc + hereditaryParameter.getSameDeviation()) {
					yetSameCount++;
				} else {
					yetSameCount = 0;
				}
				if (yetSameCount >= hereditaryParameter.getSameNum()) {
//					System.out.println("达到相同最大迭代次数，跳出迭代");
					return null;
				}
				
				if (auc > maxAuc) {
					maxAuc = auc;
					maxChro = CloneObject.clone(chro);
				}
			}
			
			aucs[i] = entry.getKey();
			newChros[i] = entry.getValue();
			
			i++;
			if (i >= startPoint) {
				break;
			}
		}
		
		double[][] parentchros = parentChrosChoose.chooseParentChros(aucs, newChros);
		for (int j = startPoint; j < newChros.length; j++) {
			double[][] ds = geneEx(parentchros[j - startPoint], parentchros[j - startPoint + 1]);
			newChros[j] = ds[0];
			j++;
			if (j >= newChros.length) {
				break;
			}
			newChros[j] = ds[1];
		}
		
		for (int k = 0; k < newChros.length; k++) {
			newChros[k] = geneMul(newChros[k]);
		}
		
		return newChros;
	}
	
	private final Map<Double, double[]> indexDataSetMulChros(DataSet dataSet, AucCount aucCount, double[][] chros) throws IOException, ClassNotFoundException {
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
			map.put(aucCount.countIndexAuc(dataSet, chro), chro);
		}
		return map;
	}
	
	private final Map<Double, double[]> indexDataSetMulChros(DataSet dataSet, AucCount aucCount, double[][] chros, Integer withoutEvidNum) throws IOException, ClassNotFoundException {
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
			map.put(aucCount.countIndexAuc(dataSet, withoutEvidNum, chro), chro);
		}
		return map;
	}
	
	private final Map<Double, double[]> orderDataSetMulChros(DataSet cloneDataSet, AucCount aucCount, double[][] chros) throws IOException, ClassNotFoundException {
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
			map.put(aucCount.countOrderAuc(cloneDataSet, chro), chro);
		}
		return map;
	}
	
	
	private final double[] geneMul(double[] chro) {
		int count = 0;
		for (int i = 0; i < chro.length; i++) {
			if (count >= hereditaryParameter.getGeneMutNum()) {
				return chro;
			} else if (Math.random() < hereditaryParameter.getGeneMutPro()) {
				chro[i] = createRandomGene();
				count++;
			}
		}
		return chro;
	}
	
	private final double[][] geneEx(double[] chro1, double[] chro2) throws IOException, ClassNotFoundException {
		double[] c1 = CloneObject.clone(chro1);
		double[] c2 = CloneObject.clone(chro2);
		LinkedList<Integer> points = createRandomIntSet(c1.length, hereditaryParameter.getGeneExNum());
		for (Integer point : points) {
			double d = c1[point];
			c1[point] = c2[point];
			c2[point] = d;
		}
		double[][] ds = new double[2][];
		ds[0] = c1;
		ds[1] = c2;
		return ds;
	}
	
	private final LinkedList<Integer> createRandomIntSet(int len, int count) {
		LinkedList<Integer> points = new LinkedList<Integer>();
		while (points.size() < count) {
			int point = (int) (Math.random() * len);
			if (!points.contains(point)) {
				points.add(point);
			}
		}
		return points;
	}
	
	private final double[][] createInitChros() {
		double[][] chros = new double[hereditaryParameter.getChrosNum()][hereditaryParameter.getChroLen()];
		for (int i = 0; i < chros.length; i++) {
			for (int j = 0; j < chros[i].length; j++) {
				chros[i][j] = createRandomGene();
			}
		}
		return chros;
	}
	
	private final double createRandomGene() {
		double d = Math.random();
		return d - (d % hereditaryParameter.getStep()) + hereditaryParameter.getStep();
	}
	
	public double getMaxAuc() {
		return maxAuc;
	}
	
	public void setMaxAuc(double maxAuc) {
		this.maxAuc = maxAuc;
	}
	
	public double[] getMaxChro() {
		return maxChro;
	}
	
	public void setMaxChro(double[] maxChro) {
		this.maxChro = maxChro;
	}
	
	public DataSet getDataSet() {
		return dataSet;
	}
}
