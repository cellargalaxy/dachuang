package top.cellargalaxy.dachuangspringboot.hereditary;


import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.evaluation.Evaluation;
import top.cellargalaxy.dachuangspringboot.evaluation.EvaluationExecutor;
import top.cellargalaxy.dachuangspringboot.util.CloneObject;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public class Hereditary {


	public final void evolution(HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose, EvaluationExecutor evaluationExecutor) throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
		initEvolution(dataSet, hereditaryParameter);
		//最大AUC
		maxAuc = evaluationExecutor.countIndexEvaluation(dataSet, maxChro).get();
		double[][] chros = createInitChros();
		do {
			chros = createIndexNewChros(dataSet, chros, evaluationExecutor, parentChrosChoose);
//			Printer.print("进化auc:"+maxAuc);
		} while (chros != null);
	}

	public final void evolution(HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose, EvaluationExecutor evaluationExecutor, Integer withoutEvidNum) throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
		initEvolution(dataSet, hereditaryParameter);
		//最大AUC
		maxAuc = evaluationExecutor.countIndexEvaluationWithoutEvidNum(dataSet, withoutEvidNum, maxChro).get();
		double[][] chros = createInitChros();
		do {
			chros = createIndexNewChros(dataSet, chros, evaluationExecutor, parentChrosChoose, withoutEvidNum);
//			Printer.print("进化auc:"+maxAuc);
		} while (chros != null);
	}

	public final DataSet evolution(HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose, EvaluationExecutor evaluationExecutor, List<Integer> withEvidNums) throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
		DataSet cloneDataSet = CloneObject.clone(dataSet);
		cloneDataSet.removeNotEqual(withEvidNums);
		initEvolution(cloneDataSet, hereditaryParameter);
		//最大AUC
		maxAuc = evaluationExecutor.countOrderEvaluationWithEvidNums(cloneDataSet, withEvidNums, maxChro).get();
		double[][] chros = createInitChros();
		do {
			chros = createOrderNewChros(cloneDataSet, chros, evaluationExecutor, parentChrosChoose);
//			Printer.print("进化auc:"+maxAuc);
		} while (chros != null);
		return cloneDataSet;
	}

	private void initEvolution(DataSet dataSet, HereditaryParameter hereditaryParameter) {
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

	private double[][] createIndexNewChros(DataSet dataSet, double[][] oldChros, EvaluationExecutor evaluationExecutor, ParentChrosChoose parentChrosChoose) throws IOException, ClassNotFoundException {
		yetIterCount++;
		if (yetIterCount > hereditaryParameter.getIterNum()) {
//			Printer.print("到达最大迭代次数，跳出迭代");
			return null;
		}

		Map<Double, double[]> map = indexDataSetMulChros(dataSet, evaluationExecutor, oldChros);
		return doCreateNewChros(map, parentChrosChoose);
	}

	private double[][] createIndexNewChros(DataSet dataSet, double[][] oldChros, EvaluationExecutor evaluationExecutor, ParentChrosChoose parentChrosChoose, Integer withoutEvidNum) throws IOException, ClassNotFoundException {
		yetIterCount++;
		if (yetIterCount > hereditaryParameter.getIterNum()) {
//			Printer.print("到达最大迭代次数，跳出迭代");
			return null;
		}

		Map<Double, double[]> map = indexDataSetMulChros(dataSet, evaluationExecutor, oldChros, withoutEvidNum);
		return doCreateNewChros(map, parentChrosChoose);
	}

	private double[][] createOrderNewChros(DataSet cloneDataSet, double[][] oldChros, EvaluationExecutor evaluationExecutor, ParentChrosChoose parentChrosChoose) throws IOException, ClassNotFoundException {
		yetIterCount++;
		if (yetIterCount > hereditaryParameter.getIterNum()) {
//			Printer.print("到达最大迭代次数，跳出迭代");
			return null;
		}

		Map<Double, double[]> map = orderDataSetMulChros(cloneDataSet, evaluationExecutor, oldChros);
		return doCreateNewChros(map, parentChrosChoose);
	}

	private void evolution(HereditaryParameter hereditaryParameter, DataSet dataSet, ParentChrosChoose parentChrosChoose, Evaluation evaluation) throws IOException {
		Chromosome[] chromosomes = createRandomChros(dataSet, hereditaryParameter);
		ArrayList<HereditaryResult> hereditaryResults = countEvaluation(dataSet, chromosomes, evaluation);
		chromosomes = doCreateNewChros(hereditaryParameter, hereditaryResults, parentChrosChoose);

	}

	private Chromosome[] doCreateNewChros(HereditaryParameter hereditaryParameter, ArrayList<HereditaryResult> hereditaryResults, ParentChrosChoose parentChrosChoose) {
		hereditaryResults.sort((hereditaryResults1, hereditaryResults2) -> {
			if (hereditaryResults1.getEvaluationValue() < hereditaryResults2.getEvaluationValue()) {
				return 1;
			} else if (hereditaryResults1.getEvaluationValue() > hereditaryResults2.getEvaluationValue()) {
				return -1;
			} else {
				return 0;
			}
		});

		int startPoint;
		if (hereditaryResults.size() > hereditaryParameter.getSaveChroNum()) {
			startPoint = hereditaryParameter.getSaveChroNum();
		} else {
			startPoint = hereditaryResults.size();
		}
		Chromosome[] chromosomes = new Chromosome[hereditaryParameter.getChroNum()];
		for (int i = 0; i < startPoint; i++) {
			chromosomes[i] = hereditaryResults.get(i).getChromosome();
		}
		for (int i = startPoint; i < chromosomes.length; i++) {
			Chromosome[] newChromosomes = parentChrosChoose.chooseParentChros(hereditaryResults);
			newChromosomes = exchange(hereditaryParameter, newChromosomes[0], newChromosomes[1]);
			newChromosomes = mutation(hereditaryParameter, newChromosomes);
			chromosomes[i] = newChromosomes[0];
			i++;
			if (i < chromosomes.length) {
				chromosomes[i] = newChromosomes[1];
			}
		}
		return chromosomes;
	}


	private ArrayList<HereditaryResult> countEvaluation(DataSet dataSet, Chromosome[] chromosomes, Evaluation evaluation) throws IOException {
		ArrayList<HereditaryResult> list = new ArrayList<>();
		for (Chromosome chromosome : chromosomes) {
			list.add(new HereditaryResult(evaluation.countEvaluation(dataSet, chromosome), chromosome));
		}
		return list;
	}

	private Chromosome[] mutation(HereditaryParameter hereditaryParameter, Chromosome[] chromosomes) {
		Chromosome[] newChromosomes = new Chromosome[chromosomes.length];
		for (int i = 0; i < newChromosomes.length; i++) {
			newChromosomes[i] = mutation(hereditaryParameter, chromosomes[i]);
		}
		return newChromosomes;
	}

	private Chromosome mutation(HereditaryParameter hereditaryParameter, Chromosome chromosome) {
		chromosome = chromosome.clone();
		for (Gene gene : chromosome.getGenes()) {
			for (int i = 0; i < gene.getBases().length; i++) {
				if (Math.random() <= hereditaryParameter.getBaseMutPro()) {
					gene.getBases()[i] = createRandomBase(hereditaryParameter);
				}
			}
		}
		return chromosome;
	}

	private Chromosome[] exchange(HereditaryParameter hereditaryParameter, Chromosome chromosome1, Chromosome chromosome2) {
		chromosome1 = chromosome1.clone();
		chromosome2 = chromosome2.clone();
		Iterator<Gene> iterator1 = chromosome1.getGenes().iterator();
		while (iterator1.hasNext()) {
			Gene gene1 = iterator1.next();
			Gene gene2 = chromosome2.getGene(gene1.getEvidenceId());
			if (gene2 == null) {
				continue;
			}
			for (int i = 0; i < gene1.getBases().length; i++) {
				if (Math.random() <= hereditaryParameter.getBaseExPro()) {
					double base = gene1.getBases()[i];
					gene1.getBases()[i] = gene2.getBases()[i];
					gene2.getBases()[i] = base;
				}
			}
		}
		return new Chromosome[]{chromosome1, chromosome2};
	}

	public Chromosome[] createRandomChros(DataSet dataSet, HereditaryParameter hereditaryParameter) {
		Chromosome[] chromosomes = new Chromosome[hereditaryParameter.getChroNum()];
		for (int i = 0; i < chromosomes.length; i++) {
			Map<Integer, Gene> geneMap = new HashMap<>();
			for (Integer evidenceId : dataSet.getEvidenceName2EvidenceId().values()) {
				double[] bases = new double[hereditaryParameter.getBaseNum()];
				for (int j = 0; j < bases.length; j++) {
					bases[j] = createRandomBase(hereditaryParameter);
				}
				Gene gene = new Gene(evidenceId, bases);
				geneMap.put(gene.getEvidenceId(), gene);
			}
			chromosomes[i] = new Chromosome(geneMap);
		}
		return chromosomes;
	}

	private double createRandomBase(HereditaryParameter hereditaryParameter) {
		double d = Math.random();
		return d - (d % hereditaryParameter.getStep()) + hereditaryParameter.getStep();
	}
}
