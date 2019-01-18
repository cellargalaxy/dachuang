package top.cellargalaxy.dachuangspringboot.hereditary;

import top.cellargalaxy.dachuangspringboot.run.RunParameter;

/**
 * Created by cellargalaxy on 17-11-3.
 */
public class ParentChrosChooseFactory {
	private static ParentChrosChoose parentChrosChoose;

	public static final ParentChrosChoose getParentChrosChoose() {
		return parentChrosChoose;
	}

	public static final ParentChrosChoose createParentChrosChoose(RunParameter runParameter) {
		String name = runParameter.getParentChrosChooseName();
		if (OrderParentChrosChoose.NAME.equals(name)) {
			parentChrosChoose = new OrderParentChrosChoose();
			return parentChrosChoose;
		}
		if (RouletteParentChrosChoose.NAME.equals(name)) {
			parentChrosChoose = new RouletteParentChrosChoose();
			return parentChrosChoose;
		}
		throw new RuntimeException("无效-ParentChrosChoose: " + name);
	}

}
