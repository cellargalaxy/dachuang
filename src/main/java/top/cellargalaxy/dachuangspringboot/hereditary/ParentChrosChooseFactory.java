package top.cellargalaxy.dachuangspringboot.hereditary;

import top.cellargalaxy.dachuangspringboot.run.RunParameter;

/**
 * Created by cellargalaxy on 17-11-3.
 */
public class ParentChrosChooseFactory {

	public static final ParentChrosChoose createParentChrosChoose() {
		String name = RunParameter.parentChrosChooseName;
		if (OrderParentChrosChoose.NAME.equals(name)) {
			return new OrderParentChrosChoose();
		}
		if (RouletteParentChrosChoose.NAME.equals(name)) {
			return new RouletteParentChrosChoose();
		}
		throw new RuntimeException("无效-ParentChrosChoose: " + name);
	}

}
