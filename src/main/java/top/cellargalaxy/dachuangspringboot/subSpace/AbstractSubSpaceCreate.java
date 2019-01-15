package top.cellargalaxy.dachuangspringboot.subSpace;


import java.util.Iterator;
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
