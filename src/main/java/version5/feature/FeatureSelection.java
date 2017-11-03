package version5.feature;

import util.CloneObject;
import version5.evaluation.Evaluation;
import version5.hereditary.Hereditary;
import version5.hereditary.HereditaryParameter;
import version5.hereditary.ParentChrosChoose;

import java.io.IOException;
import java.util.*;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public class FeatureSelection {
	
	public static final LinkedList<AucImprotence> featureSelection(FeatureSeparation featureSeparation, double stop,
	                                                               Hereditary hereditary, HereditaryParameter hereditaryParameter,
	                                                               ParentChrosChoose parentChrosChoose, Evaluation evaluation) throws IOException, ClassNotFoundException {
		hereditary.evolution(hereditaryParameter, parentChrosChoose, evaluation);
		double aucFull = hereditary.getMaxAuc();
		LinkedList<AucImprotence> aucImprotences = new LinkedList<AucImprotence>();
		for (Integer integer : hereditary.getDataSet().getEvidenceNums()) {
			//这样减就负的越多越好，好的在前面
			hereditary.evolution(hereditaryParameter, parentChrosChoose, evaluation, integer);
			double auc = hereditary.getMaxAuc() - aucFull;
			aucImprotences.add(new AucImprotence(auc, integer));
		}
		Collections.sort(aucImprotences);
		
		LinkedList<Integer> imroEvid = new LinkedList<Integer>();
		LinkedList<Integer> unInproEvid = new LinkedList<Integer>();
		featureSeparation.separationFeature(aucImprotences, imroEvid, unInproEvid);
		
		hereditary.evolution(hereditaryParameter, parentChrosChoose, evaluation, imroEvid);
		double auc = hereditary.getMaxAuc();
		while (aucFull - auc > stop && unInproEvid.size() > 0) {
			double aucJ = -1;
			int evidenceNum = -1;
			for (Integer integer : unInproEvid) {
				LinkedList<Integer> newImroEvid = CloneObject.clone(imroEvid);
				newImroEvid.add(integer);
				hereditary.evolution(hereditaryParameter, parentChrosChoose, evaluation, newImroEvid);
				if (hereditary.getMaxAuc() > aucJ) {
					aucJ = hereditary.getMaxAuc();
					evidenceNum = integer;
				}
			}
			
			imroEvid.add(evidenceNum);
			unInproEvid.remove(new Integer(evidenceNum));
			auc = aucJ;
		}
		
		aucImprotences.add(new AucImprotence(Double.MAX_VALUE, imroEvid.size()));
		return aucImprotences;
	}
}
