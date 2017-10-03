package version3.subSpace;

import java.util.List;

/**
 * Created by cellargalaxy on 17-10-1.
 */
public interface SubSpaceCreate {
	String getName();
	List<List<Integer>> createSubSpaces(List<Integer> features, double[] impros);
}
