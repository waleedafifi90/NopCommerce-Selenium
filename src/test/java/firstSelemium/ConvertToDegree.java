package firstSelemium;

import static java.lang.Math.PI;
import static java.math.RoundingMode.HALF_EVEN;

import java.math.BigDecimal;

public class ConvertToDegree {
	/**
	 * Converts the radians to degrees
	 * 
	 * @param radians The {@code BigDecimal} value in radians
	 * @return A {@code BigDecimal} in degrees
	 */
	public static BigDecimal convertRadiansToDegrees(BigDecimal radians) {
		return multiplyBy(radians, new BigDecimal(180 / PI));
	}

	/**
	 * Returns a {@code BigDecimal} whose value is <tt>(this &times;
	  * multiplicand)</tt>, and whose scale is {@code (this.scale() +
	 * multiplicand.scale())}.
	 * 
	 * @return {@code this * multiplicand}
	 */
	protected static BigDecimal multiplyBy(BigDecimal multiplicand, BigDecimal multiplier) {
		return setScale(multiplicand.multiply(multiplier));
	}

	/**
	 * Returns a {@code BigDecimal} whose scale is the specified value, and whose
	 * unscaled value is determined by multiplying or dividing this
	 * {@code BigDecimal}'s unscaled value by the appropriate power of ten to
	 * maintain its overall value. If the scale is reduced by the operation, the
	 * unscaled value must be divided (rather than multiplied), and the value may be
	 * changed; in this case, the specified rounding mode is applied to the
	 * division.
	 * 
	 * @param bigDecimal The value of the BigDecimal
	 * @return a {@code BigDecimal} whose scale is the specified value, and whose
	 *         unscaled value is determined by multiplying or dividing this
	 *         {@code BigDecimal}'s unscaled value by the appropriate power of ten
	 *         to maintain its overall value.
	 */
	protected static BigDecimal setScale(BigDecimal bigDecimal) {
		return bigDecimal.setScale(5, HALF_EVEN);
	}
}
