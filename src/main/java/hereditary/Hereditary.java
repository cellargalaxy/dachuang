package hereditary;

import auc.AUC;
import dataSet.DataSet;
import auc.Id;
import util.CloneObject;

import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * Created by cellargalaxy on 2017/5/7.
 */
public class Hereditary {


	public Hereditary(LinkedList<Id> ids) {

		//种群
		chrosNum =30;
		saveChroPro=0.5;
		saveChroNum=(int)(chrosNum*saveChroPro);
		//染色体
		chroLen=ids.getFirst().getEvidences().size()*2;
		geneExPro= 0.5;
		geneExNum=(int)(chroLen*geneExPro);
		geneMutNumPro= 0.5;
		geneMutNum=(int)(chroLen*geneMutNumPro);
		geneMutPro=0.3;
		step=0.1;
		//进化控制
		iterNum=100;
		yetIterCount=0;
		sameNum=30;
		yetSameCount=0;
		latestAUC=-1;
		sameDeviation=0.000001;

		maxAUC=-1;
		maxChro=null;
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		LinkedList<Id> ids = DataSet.createIds(new File("F:/xi/dachuang/test 合成与AUC 去除totle.csv"), ",",0,1,2,4,6);

//		LinkedList<int[]> labelSet = LabelSet.readLabelSet(new File("F:/xi/大创/test 合成与AUC_Outlabel.csv"), ",");
		Hereditary hereditary=new Hereditary(ids);
		hereditary.evolution(ids,-1,Hereditary.USE_Roulette);
		System.out.println("=========================");
		System.out.println("maxAUC:"+hereditary.maxAUC);
		System.out.println("maxChro:"+Arrays.toString(hereditary.maxChro));

	}
	
	/**
	 * 进化
	 * @param ids
	 * @param exceptEvidenceNum
	 * @param methodNum
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void evolution(LinkedList<Id> ids,int exceptEvidenceNum,int methodNum) throws IOException, ClassNotFoundException {
		double[][] chros=createInitChros();
		int count=0;
		do {
			System.out.println(count+":auc："+maxAUC);
			count++;
//			System.out.println(count+":chros:");
//			for (double[] chro : chros) {
//				System.out.println(Arrays.toString(chro));
//			}
//			count++;
//			System.out.println("-------------------------------------------");

			chros=createNewChros(ids,exceptEvidenceNum,chros,methodNum);
			if (chros==null) {
				break;
			}
		}while (true);
	}
	
	/**
	 * 生育
	 * @param ids
	 * @param exceptEvidenceNum
	 * @param oldChros
	 * @param methodNum
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private double[][] createNewChros(LinkedList<Id> ids,int exceptEvidenceNum,double[][] oldChros,int methodNum) throws IOException, ClassNotFoundException {
		yetIterCount++;
		if (yetIterCount>iterNum) {
			System.out.println("到达最大迭代次数，跳出迭代");
			return null;
		}
		Map<Double,double[]> map=mulChros(ids,exceptEvidenceNum,oldChros);

//		System.out.println("每个个体的auc：");
//		for (Map.Entry<Double, double[]> entry : map.entrySet()) {
//			System.out.println(entry.getKey()+":"+Arrays.toString(entry.getValue()));
//		}

		double auc=-1;
		double[] chro;
		double[] aucs=new double[saveChroNum];
		double[][] newChros=new double[chrosNum][];
		int i=0;
		for (Map.Entry<Double, double[]> entry : map.entrySet()) {

			if (auc==-1) {
				auc=entry.getKey();
				chro=entry.getValue();

//				System.out.println("前后两次auc比较："+(auc-latestAUC));
//				System.out.println(auc);
//				System.out.println(latestAUC);
//				System.out.println("本次进化最大chro：");
//				System.out.println(Arrays.toString(chro));
//				System.out.println("------------");

				if (Math.abs(auc-latestAUC)<sameDeviation) {
					yetSameCount++;
				}else {
					yetSameCount=0;
				}
				if(yetSameCount>=sameNum) {
					System.out.println("达到相同最大迭代次数，跳出迭代");
					return null;
				}

				if (auc>maxAUC) {
//					System.out.println("更新全局最大auc："+auc+">"+maxAUC);
					maxAUC=auc;
					maxChro=chro;
				}

				latestAUC=auc;
			}

			aucs[i]=entry.getKey();
			newChros[i]=entry.getValue();
			i++;
			if (i>=saveChroNum) {
				break;
			}
		}
		
		
		if (methodNum==USE_Roulette) {
			double aucCount=0;
			for (double v : aucs) {
				aucCount+=v;
			}
			for (int j = saveChroNum; j < newChros.length; j++) {
				double[][] ds=chooseRouletteParentsGene(newChros,aucs,aucCount);
				ds=geneEx( ds[0] , ds[1] );
				newChros[j]=ds[0];
				j++;
				if(j >= newChros.length) break;
				newChros[j]=ds[1];
			}
		}else if (methodNum==USE_ORDER){
			for (int j = saveChroNum; j < newChros.length; j++) {
				double[][] ds=chooseOrderParentsGene(newChros,j-saveChroNum);
				ds=geneEx( ds[0] , ds[1] );
				newChros[j]=ds[0];
				j++;
				if(j >= newChros.length) break;
				newChros[j]=ds[1];
			}
		}else {
			throw new RuntimeException("选择父母染色体方法参数异常");
		}

		for (int k = 0; k < newChros.length; k++) {
			newChros[k]=geneMul(newChros[k]);
		}

		return newChros;
	}

	private double[] geneMul(double[] chro){
		int count=0;
		for (int i = 0; i < chro.length; i++) {
			if (count>=geneMutNum) {
				return chro;
			}else if(Math.random()<geneMutPro){
				chro[i]=createRandomGene();
				count++;
			}
		}
		return chro;
	}

	private double[][] chooseOrderParentsGene(double[][] chros,int point){
		double[][] ds={chros[point],chros[point+1]};
		return ds;
	}

	private double[][] chooseRouletteParentsGene(double[][] chros,double[] aucs,double aucCount){
		int point1=Roulette.roulette(aucs,aucCount);
		int point2;
		do {
			point2=Roulette.roulette(aucs,aucCount);
		}while (point1==point2);
		double[][] ds={chros[point1],chros[point2]};
		return ds;
	}

	private double[][] geneEx(double[] chro1,double[] chro2) throws IOException, ClassNotFoundException {
		double[] c1=CloneObject.clone(chro1);
		double[] c2=CloneObject.clone(chro2);

//		System.out.println("=========================================");
//		System.out.println("挑选了这两条染色体进行交换：");
//		System.out.println(Arrays.toString(c1));
//		System.out.println(Arrays.toString(c2));
//		System.out.println("----------------------------");


		double[][] ds=new double[2][];
		LinkedList<Integer> points=createRandomIntSet(c1.length,geneExNum);
		for (Integer point : points) {
//			System.out.println("交换："+point);
			double d=c1[point];
			c1[point]=c2[point];
			c2[point]=d;
		}
		ds[0]=c1;
		ds[1]=c2;

//		System.out.println("交换结果：");
//		System.out.println(Arrays.toString(ds[0]));
//		System.out.println(Arrays.toString(ds[1]));
//		System.out.println("=========================================");

		return ds;
	}

	private LinkedList<Integer> createRandomIntSet(int len,int count){
		LinkedList<Integer> points=new LinkedList<Integer>();
		while (points.size() < count) {
			int point=(int)(Math.random()*len);
			if (!points.contains(point)) {
				points.add(point);
			}
		}
		return points;
	}

	private Map<Double,double[]> mulChros(LinkedList<Id> ids,int exceptEvidenceNum,double[][] chros) throws IOException, ClassNotFoundException {
		Map<Double,double[]> map= new TreeMap<Double, double[]>(new Comparator<Double>(){
			public int compare(Double a,Double b){ if (a>b) { return -1; }else if(a<b){ return 1; }else { return 0; } } } );
		for (double[] chro : chros) {
			LinkedList<Id> newIds=mulChro(ids,chro);
			map.put(AUC.countAUC(newIds,exceptEvidenceNum),chro);
		}
		return map;
	}


	private LinkedList<Id> mulChro(LinkedList<Id> oldIds,double[] chro) throws IOException, ClassNotFoundException {
		LinkedList<Id> newIds=CloneObject.clone(oldIds);
		for (Id id : newIds) {
			int i=0;
			for (double[] evidence : id.getEvidences()) {
				evidence[0]*=chro[i];
				i++;
				evidence[1]*=chro[i];
				i++;
			}
		}
		return newIds;
	}

	private double[][] createInitChros(){
		double[][] chros=new double[chrosNum][chroLen];
		for (int i = 0; i < chros.length; i++) {
			for (int j = 0; j < chros[i].length; j++) {
				chros[i][j]=createRandomGene();
			}
		}
		return chros;
	}

	private double createRandomGene(){
		double d=Math.random();
		return d-(d%step)+step;
	}








	public static final int USE_Roulette=1;
	public static final int USE_ORDER=2;
	//种群
	private int chrosNum;
	private double saveChroPro;
	private int saveChroNum;
	//染色体
	private int chroLen;
	private double geneExPro;
	private int geneExNum;
	private double geneMutNumPro;
	private int geneMutNum;
	private double geneMutPro;
	private double step;
	//进化控制
	private int iterNum;
	private int yetIterCount;
	private int sameNum;
	private int yetSameCount;
	private double latestAUC;
	private double sameDeviation;
	//进化结果
	private double maxAUC;
	private double[] maxChro;
}
