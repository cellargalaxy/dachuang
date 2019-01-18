package top.cellargalaxy.dachuangspringboot.hereditary;

import top.cellargalaxy.dachuangspringboot.run.RunParameter;

/**
 * Created by cellargalaxy on 17-11-3.
 */
public class ParentChrosChooseFactory {
	public static final String[] NAMES = {OrderParentChrosChoose.NAME, RouletteParentChrosChoose.NAME};
	private static ParentChrosChoose parentChrosChoose;

	public static final ParentChrosChoose getParentChrosChoose(RunParameter runParameter) {
		if (parentChrosChoose == null) {
			parentChrosChoose = createParentChrosChoose(runParameter);
		}
		return parentChrosChoose;
	}

	public static void setParentChrosChoose(ParentChrosChoose parentChrosChoose) {
		ParentChrosChooseFactory.parentChrosChoose = parentChrosChoose;
	}

	public static final ParentChrosChoose createParentChrosChoose(RunParameter runParameter) {
		String name = runParameter.getParentChrosChooseName();
		parentChrosChoose = createParentChrosChoose(name, runParameter);
		return parentChrosChoose;
	}

	public static final ParentChrosChoose createParentChrosChoose(String name, RunParameter runParameter) {
		if (OrderParentChrosChoose.NAME.equals(name)) {
			return new OrderParentChrosChoose();
		}
		if (RouletteParentChrosChoose.NAME.equals(name)) {
			return new RouletteParentChrosChoose();
		}
		throw new RuntimeException("无效-ParentChrosChoose: " + name);
	}
}
