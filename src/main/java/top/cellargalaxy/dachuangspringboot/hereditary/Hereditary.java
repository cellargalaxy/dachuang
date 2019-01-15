package top.cellargalaxy.dachuangspringboot.hereditary;


import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.evaluation.Evaluation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public class Hereditary {

	public static HereditaryResult evolution(DataSet dataSet, HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose, Evaluation evaluation) throws IOException {
		HereditaryResult best = null;
		int yetIteratorCount = 0;
		int yetSameCount = 0;
		Chromosome[] chromosomes = createRandomChros(dataSet, hereditaryParameter);
		do {
			ArrayList<HereditaryResult> hereditaryResults = countEvaluation(dataSet, chromosomes, evaluation);
			hereditaryResults = sortHereditaryResult(hereditaryResults);
			chromosomes = createNewChros(hereditaryParameter, hereditaryResults, parentChrosChoose);

			HereditaryResult hereditaryResult = hereditaryResults.get(0);
			yetIteratorCount++;
			if (best != null && Math.abs(best.getEvaluationValue() - hereditaryResult.getEvaluationValue()) <= hereditaryParameter.getSameDeviation()) {
				yetSameCount++;
			}
			if (best == null || hereditaryResult.getEvaluationValue() > best.getEvaluationValue()) {
				best = hereditaryResult;
			}
		}
		while (yetIteratorCount < hereditaryParameter.getIterationCount() && yetSameCount < hereditaryParameter.getSameCount());
		return best;
	}

	private static Chromosome[] createNewChros(HereditaryParameter hereditaryParameter, ArrayList<HereditaryResult> hereditaryResults, ParentChrosChoose parentChrosChoose) {
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
		Chromosome[][] newChromosomes = parentChrosChoose.chooseParentChros(hereditaryResults, chromosomes.length - startPoint);
		for (int i = startPoint; i < chromosomes.length; i++) {
			newChromosomes[i - startPoint] = exchangeAndMutation(hereditaryParameter, newChromosomes[i - startPoint][0], newChromosomes[i - startPoint][1]);
			chromosomes[i] = newChromosomes[i - startPoint][0];
			i++;
			if (i < chromosomes.length) {
				chromosomes[i] = newChromosomes[i - startPoint][1];
			}
		}
		return chromosomes;
	}

	private static ArrayList<HereditaryResult> sortHereditaryResult(ArrayList<HereditaryResult> hereditaryResults) {
		hereditaryResults.sort((hereditaryResults1, hereditaryResults2) -> {
			if (hereditaryResults1.getEvaluationValue() < hereditaryResults2.getEvaluationValue()) {
				return 1;
			} else if (hereditaryResults1.getEvaluationValue() > hereditaryResults2.getEvaluationValue()) {
				return -1;
			} else {
				return 0;
			}
		});
		return hereditaryResults;
	}

	private static ArrayList<HereditaryResult> countEvaluation(DataSet dataSet, Chromosome[] chromosomes, Evaluation evaluation) throws IOException {
		ArrayList<HereditaryResult> list = new ArrayList<>();
		for (Chromosome chromosome : chromosomes) {
			list.add(new HereditaryResult(evaluation.countEvaluation(dataSet, chromosome), chromosome));
		}
		return list;
	}

	private static Chromosome[] exchangeAndMutation(HereditaryParameter hereditaryParameter, Chromosome chromosome1, Chromosome chromosome2) {
		chromosome1 = chromosome1.clone();
		chromosome2 = chromosome2.clone();
		Iterator<Gene> iterator = chromosome1.getGenes().iterator();
		while (iterator.hasNext()) {
			Gene gene1 = iterator.next();
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
		for (Gene gene : chromosome1.getGenes()) {
			for (int i = 0; i < gene.getBases().length; i++) {
				if (Math.random() <= hereditaryParameter.getBaseMutPro()) {
					gene.getBases()[i] = createRandomBase(hereditaryParameter);
				}
			}
		}
		for (Gene gene : chromosome2.getGenes()) {
			for (int i = 0; i < gene.getBases().length; i++) {
				if (Math.random() <= hereditaryParameter.getBaseMutPro()) {
					gene.getBases()[i] = createRandomBase(hereditaryParameter);
				}
			}
		}
		return new Chromosome[]{chromosome1, chromosome2};
	}

	private static Chromosome[] createRandomChros(DataSet dataSet, HereditaryParameter hereditaryParameter) {
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

	private static double createRandomBase(HereditaryParameter hereditaryParameter) {
		double d = Math.random();
		return d - (d % hereditaryParameter.getStep()) + hereditaryParameter.getStep();
	}
}
