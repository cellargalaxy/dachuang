//package version3.feature;
//
//import util.CloneObject;
//import version3.evaluation.Evaluation;
//import version3.hereditary.Hereditary;
//import version3.hereditary.HereditaryParameter;
//import version3.hereditary.ParentChrosChoose;
//
//import java.io.IOException;
//import java.util.LinkedList;
//import java.util.Map;
//import java.util.TreeMap;
//
///**
// * Created by cellargalaxy on 17-9-8.
// */
//public class FeatureSelection {
//
//	public static final Map<Double, Integer> featureSelection(Evaluation evaluation, HereditaryParameter hereditaryParameter,
//	                                                          ParentChrosChoose parentChrosChoose, FeatureSeparation featureSeparation,
//	                                                          double stop, Hereditary hereditary) throws IOException, ClassNotFoundException {
//		double aucFull = hereditary.getMaxAuc();
//		TreeMap<Double, Integer> aucImprotences = new TreeMap<Double, Integer>();
//		for (Integer integer : hereditary.getDataSet().getEvidenceNums()) {
//			//这样减就负的越多越好，好的在前面
//			hereditary.evolution(hereditaryParameter, parentChrosChoose, evaluation, integer);
//			double auc = hereditary.getMaxAuc() - aucFull;
//			aucImprotences.put(auc, integer);
//		}
//
//		LinkedList<Integer> imroEvid = new LinkedList<Integer>();
//		LinkedList<Integer> unInproEvid = new LinkedList<Integer>();
//		featureSeparation.separationFeature(aucImprotences, imroEvid, unInproEvid);
//
//		hereditary.evolution(hereditaryParameter, parentChrosChoose, evaluation, imroEvid);
//		double auc = hereditary.getMaxAuc();
//		while (Math.abs(aucFull - auc) > stop) {
//			double aucJ = -1;
//			int evidenceNum = -1;
//			for (Integer integer : unInproEvid) {
//				LinkedList<Integer> newImroEvid = CloneObject.clone(imroEvid);
//				newImroEvid.add(integer);
//				hereditary.evolution(hereditaryParameter, parentChrosChoose, evaluation, newImroEvid);
//				if (hereditary.getMaxAuc() > aucJ) {
//					aucJ = hereditary.getMaxAuc();
//					evidenceNum = integer;
//				}
//			}
//
//			imroEvid.add(evidenceNum);
//			unInproEvid.remove(new Integer(evidenceNum));
//			auc = aucJ;
//		}
//
//		aucImprotences.put(Double.MAX_VALUE, imroEvid.size());
//		return aucImprotences;
//	}
//}
