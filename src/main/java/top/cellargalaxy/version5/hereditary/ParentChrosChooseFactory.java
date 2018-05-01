package top.cellargalaxy.version5.hereditary;

/**
 * Created by cellargalaxy on 17-11-3.
 */
public class ParentChrosChooseFactory {
	/**
	 * 顺序法父母染色体选择
	 */
	public static final int ORDER_PARENT_CHROS_CHOOSE_NUM = 1;
	/**
	 * 轮盘法父母染色体选择
	 */
	public static final int ROULETTE_PARENT_CHROS_CHOOSE_NUM = 2;
	
	public static final ParentChrosChoose createParentChrosChoose(int parentChrosChooseNum) {
		if (parentChrosChooseNum == ORDER_PARENT_CHROS_CHOOSE_NUM) {
			return createOrderParentChrosChoose();
		}
		if (parentChrosChooseNum == ROULETTE_PARENT_CHROS_CHOOSE_NUM) {
			return createRouletteParentChrosChoose();
		}
		throw new RuntimeException("无效parentChrosChooseNum: " + parentChrosChooseNum);
	}
	
	public static final boolean check(Integer parentChrosChooseNum) {
		if (parentChrosChooseNum == null) {
			return false;
		}
		if (parentChrosChooseNum.equals(ORDER_PARENT_CHROS_CHOOSE_NUM) || parentChrosChooseNum.equals(ROULETTE_PARENT_CHROS_CHOOSE_NUM)) {
			return true;
		}
		return false;
	}
	
	private static final ParentChrosChoose createOrderParentChrosChoose() {
		return new OrderParentChrosChoose();
	}
	
	private static final ParentChrosChoose createRouletteParentChrosChoose() {
		return new RouletteParentChrosChoose();
	}
}
