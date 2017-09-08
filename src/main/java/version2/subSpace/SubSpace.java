package version2.subSpace;

import util.CloneObject;
import util.Roulette;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public class SubSpace {
	
	
	public static LinkedList<LinkedList<Integer>> createSubSpaces(LinkedList<Integer> features, double[] impros, int[] sn, int fnMin, ImprotenceAdjust improtenceAdjust) throws IOException, ClassNotFoundException {
		int fnMax = 0;
		for (int i : sn) {
			fnMax += countC(i, features.size());
		}
		if (fnMin > fnMax) {
			throw new RuntimeException("最小子空间数fnMin:" + fnMin + ",不得大于最大子空间数fnMax:" + fnMax);
		}
		int fn = fnMin + (int) (Math.random() * (fnMax - fnMin));
		
		LinkedList<Double> listImpros = improtenceAdjust.adjustImprotence(impros);
		
		LinkedList<LinkedList<Integer>> subSpaces = new LinkedList<LinkedList<Integer>>();
		for (int i = 0; i < fn; i++) {
			LinkedList<Integer> subSpace = createSubSpace(features, listImpros, sn[(int) (sn.length * Math.random())]);
			if (!siContainSubSpace(subSpaces, subSpace)) {
				subSpaces.add(subSpace);
			} else {
				i--;
			}
		}
		return subSpaces;
	}
	
	private static int countC(int n, int m) {
		int mm = 1;
		for (int i = 0; i < n; i++) {
			mm *= (m - i);
		}
		int nn = 1;
		for (int i = 0; i < n; i++) {
			nn *= (i + 1);
		}
		return mm / nn;
	}
	
	private static LinkedList<Integer> createSubSpace(LinkedList<Integer> oldFeatures, LinkedList<Double> oldImpros, int len) throws IOException, ClassNotFoundException {
		LinkedList<Integer> newFeatures = CloneObject.clone(oldFeatures);
		LinkedList<Double> newImpros = CloneObject.clone(oldImpros);
		LinkedList<Integer> subSpace = new LinkedList<Integer>();
		for (int i = 0; i < len; i++) {
			int point = Roulette.roulette(newImpros);
			subSpace.add(newFeatures.get(point));
			newFeatures.remove(point);
			newImpros.remove(point);
		}
		return subSpace;
	}
	
	private static boolean siContainSubSpace(LinkedList<LinkedList<Integer>> subSpaces, LinkedList<Integer> features) {
		main:
		for (LinkedList<Integer> subSpace : subSpaces) {
			if (subSpace.size() == features.size()) {
				for (Integer feature : features) {
					if (!subSpace.contains(feature)) {
						continue main;
					}
				}
				return true;
			}
		}
		return false;
	}
}
