package top.cellargalaxy.dachuangspringboot.hereditary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public interface ParentChrosChoose {
	Chromosome[] chooseParentChros(ArrayList<HereditaryResult> hereditaryResults);
}
