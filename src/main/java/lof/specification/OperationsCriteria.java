package lof.specification;

/**
 * Class for BD search operations constants
 * @author matheusm
 * 16/10/2019
 *
 */
public class OperationsCriteria {

	private OperationsCriteria() {
		super();
	}
	/**
	 * Equals operator
	 */
	public static final String EQ = "EQ";
	/**
	 * Like operator
	 */
	public static final String LK = "LK";
	/**
	 * Greater Than operator
	 */
	public static final String GT = "GT";
	/**
	 * Less Than operator
	 */
	public static final String LT = "LT";
	/**
	 * In  operator
	 */
	public static final String IN = "IN";
	
	/**
	 * Order operator
	 */
	public static final String ORDER = "ORDER";
	
	/**
	 * REGEX FOR SPLIT IN OPERATION
	 */
	public static final String REGEX_SPLIT_OP = "([+]" + EQ + "[+]|[+]" + LT + "[+]|[+]" + GT + "[+]|[+]" + LK + "[+]|[+]" + ORDER + "[+])";
	
}
