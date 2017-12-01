package top.cellargalaxy.version5.subSpace;


import top.cellargalaxy.util.Roulette;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cellargalaxy on 17-11-1.
 */
public abstract class AbstractSubSpaceCreate implements SubSpaceCreate {
	int countC(int n, int m) {
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
	
	List<Integer> createRandomSubSpace(List<Integer> features) {
		int i;
		do {
			i = (int) (Math.pow(2, features.size()) * Math.random());
		} while (i == 0);
		String string = Integer.toBinaryString(i);
		char[] chars = string.toCharArray();
		List<Integer> subSpace = new LinkedList<Integer>();
		int j = chars.length - features.size();
		for (Integer feature : features) {
			if (j >= 0 && chars[j] == '1') {
				subSpace.add(feature);
			}
			j++;
		}
		Collections.sort(subSpace);
		return subSpace;
	}
	
	List<Integer> createSnRandomSubSpace(List<Integer> oldFeatures, int len) {
		List<Integer> newFeatures = new LinkedList<Integer>();
		for (Integer oldFeature : oldFeatures) {
			newFeatures.add(oldFeature);
		}
		List<Integer> subSpace = new LinkedList<Integer>();
		int i = 0;
		while (i < len) {
			Iterator<Integer> iterator = newFeatures.iterator();
			while (i < len && iterator.hasNext()) {
				if (Math.random() < 1.0 / oldFeatures.size()) {
					subSpace.add(iterator.next());
					iterator.remove();
					i++;
				} else {
					iterator.next();
				}
			}
		}
		Collections.sort(subSpace);
		return subSpace;
	}
	
	List<Integer> createFeatureSelectionSubSpace(List<Integer> oldFeatures, List<Double> oldImpros, int len) {
		List<Integer> newFeatures = new LinkedList<Integer>();
		for (Integer oldFeature : oldFeatures) {
			newFeatures.add(oldFeature);
		}
		List<Double> newImpros = new LinkedList<Double>();
		for (Double oldImpro : oldImpros) {
			newImpros.add(oldImpro);
		}
		List<Integer> subSpace = new LinkedList<Integer>();
		for (int i = 0; i < len; i++) {
			int point = Roulette.roulette(newImpros);
			subSpace.add(newFeatures.get(point));
			newFeatures.remove(point);
			newImpros.remove(point);
		}
		Collections.sort(subSpace);
		return subSpace;
	}
	
	boolean siContainSubSpace(List<List<Integer>> subSpaces, List<Integer> features) {
		main:
		for (List<Integer> subSpace : subSpaces) {
			if (subSpace.size() == features.size()) {
				Iterator<Integer> iterator1 = subSpace.iterator();
				Iterator<Integer> iterator2 = features.iterator();
				while (iterator1.hasNext() && iterator2.hasNext()) {
					if (!iterator1.next().equals(iterator2.next())) {
						continue main;
					}
				}
				return true;
			}
		}
		return false;
	}
}
