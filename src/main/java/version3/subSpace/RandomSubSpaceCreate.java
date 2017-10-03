package version3.subSpace;

import version3.run.RunParameter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by cellargalaxy on 17-10-1.
 */
public final class RandomSubSpaceCreate implements SubSpaceCreate {
	private final RunParameter runParameter;
	private int fnMax;
	
	public RandomSubSpaceCreate(RunParameter runParameter) {
		this.runParameter = runParameter;
	}
	
	public String getName() {
		return "随机子空间创建";
	}
	
	public List<List<Integer>> createSubSpaces(List<Integer> features, double[] impros) {
		runParameter.receiveCreateSubSpaces(impros);
		int fnMin = runParameter.getFnMin();
		fnMax = (int) (Math.pow(2, impros.length));
		if (fnMin > fnMax - 1) {
			throw new RuntimeException("最小子空间数fnMin:" + fnMin + ",不得大于最大子空间数fnMax:" + fnMax);
		}
		int fn = fnMin + (int) (Math.random() * (fnMax - fnMin));
		List<List<Integer>> subSpaces = new LinkedList<List<Integer>>();
		for (int i = 0; i < fn; i++) {
			List<Integer> subSpace = createSubSpace(features);
			if (!siContainSubSpace(subSpaces, subSpace)) {
				subSpaces.add(subSpace);
			} else {
				i--;
			}
		}
		return subSpaces;
	}
	
	private List<Integer> createSubSpace(List<Integer> features) {
		int i;
		do {
			i = (int) (fnMax * Math.random());
		} while (i == 0 && features.size() != 0);
		String string = Integer.toBinaryString(i);
		char[] chars = string.toCharArray();

//		System.out.println(i+"   "+ Arrays.toString(chars));
		
		List<Integer> subSpace = new LinkedList<Integer>();
		int j = chars.length - features.size();
		for (Integer feature : features) {
			if (j >= 0 && chars[j] == '1') {
				subSpace.add(feature);
			}
			j++;
		}
		return subSpace;
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
	
	private static boolean siContainSubSpace(List<List<Integer>> subSpaces, List<Integer> features) {
		main:
		for (List<Integer> subSpace : subSpaces) {
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
