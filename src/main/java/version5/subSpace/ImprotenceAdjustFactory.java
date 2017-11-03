package version5.subSpace;

/**
 * Created by cellargalaxy on 17-11-3.
 */
public class ImprotenceAdjustFactory {
	/**
	 * 平方调整算法
	 */
	public static final int POWER_IMPROTENCE_ADJUST_NUM = 1;
	/**
	 * 减法调整算法
	 */
	public static final int SUBTRACTION_IMPROTENCE_ADJUST_NUM = 2;
	
	public static final ImprotenceAdjust createImprotenceAdjust(int improtenceAdjustNum, Double adjustD) {
		if (improtenceAdjustNum == POWER_IMPROTENCE_ADJUST_NUM) {
			return createPowerImprotenceAdjust();
		}
		if (improtenceAdjustNum == SUBTRACTION_IMPROTENCE_ADJUST_NUM) {
			return createSubtractionImprotenceAdjust(adjustD);
		}
		throw new RuntimeException("无效improtenceAdjustNum: " + improtenceAdjustNum);
	}
	
	public static final boolean check(Integer improtenceAdjustNum, Double adjustD) {
		if (improtenceAdjustNum == null) {
			return false;
		}
		if (improtenceAdjustNum.equals(POWER_IMPROTENCE_ADJUST_NUM)) {
			return true;
		}
		if (improtenceAdjustNum.equals(SUBTRACTION_IMPROTENCE_ADJUST_NUM)) {
			return adjustD != null;
		}
		return false;
	}
	
	private static final ImprotenceAdjust createSubtractionImprotenceAdjust(double adjustD) {
		return new SubtractionImprotenceAdjust(adjustD);
	}
	
	private static final ImprotenceAdjust createPowerImprotenceAdjust() {
		return new PowerImprotenceAdjust();
	}
}
