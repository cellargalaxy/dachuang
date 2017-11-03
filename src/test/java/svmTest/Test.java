package svmTest;

import java.util.Arrays;

/**
 * Created by cellargalaxy on 17-10-31.
 */
public class Test {
	public static void main(String[] args) {
		int[] ints={5,3,1,2,4};
		for (int i = 0; i < ints.length; i++) {
			int index=-1;
			for (int j = i+1; j < ints.length; j++) {
				if (ints[j]>ints[i]) {
					index=j-i;
					break;
				}
			}
			ints[i]=index;
		}
		System.out.println(Arrays.toString(ints));
	}
}
