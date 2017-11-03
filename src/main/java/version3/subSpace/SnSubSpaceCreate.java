//package version3.subSpace;
//
//import util.Roulette;
//import version3.run.RunParameter;
//
//import java.util.Arrays;
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * Created by cellargalaxy on 17-9-8.
// */
//public final class SnFeatureSelectionSubSpaceCreate implements SubSpaceCreate {
//	private final RunParameter runParameter;
//	private final ImprotenceAdjust improtenceAdjust;
//
//	public SnFeatureSelectionSubSpaceCreate(RunParameter runParameter, ImprotenceAdjust improtenceAdjust) {
//		this.runParameter = runParameter;
//		this.improtenceAdjust = improtenceAdjust;
//	}
//
//	public String getName() {
//		return "用户自定义子空间创建";
//	}
//
//	public List<List<Integer>> createSubSpaces(List<Integer> features, double[] impros) {
//		runParameter.receiveCreateSubSpaces(impros);
//		int[] sn = runParameter.getSn();
//		int fnMin = runParameter.getFnMin();
//		int fnMax = 0;
//		System.out.println(impros.length+" "+ Arrays.toString(sn));
//		for (int i : sn) {
//			fnMax += countC(i, features.size());
//		}
//		if (fnMin > fnMax) {
//			throw new RuntimeException("最小子空间数fnMin:" + fnMin + ",不得大于最大子空间数fnMax:" + fnMax);
//		}
//		int fn = fnMin + (int) (Math.random() * (fnMax - fnMin));
//
//		List<Double> listImpros = improtenceAdjust.adjustImprotence(impros);
//
//		List<List<Integer>> subSpaces = new LinkedList<List<Integer>>();
//		for (int i = 0; i < fn; i++) {
//			List<Integer> subSpace = createSubSpace(features, listImpros, sn[(int) (sn.length * Math.random())]);
//			if (!siContainSubSpace(subSpaces, subSpace)) {
//				subSpaces.add(subSpace);
//			} else {
//				i--;
//			}
//		}
//		return subSpaces;
//	}
//
//	private static int countC(int n, int m) {
//		int mm = 1;
//		for (int i = 0; i < n; i++) {
//			mm *= (m - i);
//		}
//		int nn = 1;
//		for (int i = 0; i < n; i++) {
//			nn *= (i + 1);
//		}
//		return mm / nn;
//	}
//
//	private static List<Integer> createSubSpace(List<Integer> oldFeatures, List<Double> oldImpros, int len) {
//		List<Integer> newFeatures = new LinkedList<Integer>();
//		for (Integer oldFeature : oldFeatures) {
//			newFeatures.add(oldFeature);
//		}
//		List<Double> newImpros = new LinkedList<Double>();
//		for (Double oldImpro : oldImpros) {
//			newImpros.add(oldImpro);
//		}
//		List<Integer> subSpace = new LinkedList<Integer>();
//		for (int i = 0; i < len; i++) {
//			int point = Roulette.roulette(newImpros);
//			subSpace.add(newFeatures.get(point));
//			newFeatures.remove(point);
//			newImpros.remove(point);
//		}
//		return subSpace;
//	}
//
//	private static boolean siContainSubSpace(List<List<Integer>> subSpaces, List<Integer> features) {
//		main:
//		for (List<Integer> subSpace : subSpaces) {
//			if (subSpace.size() == features.size()) {
//				for (Integer feature : features) {
//					if (!subSpace.contains(feature)) {
//						continue main;
//					}
//				}
//				return true;
//			}
//		}
//		return false;
//	}
//
//
//}
