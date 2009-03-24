package jsf.util;

public class JPAUtil {
	private static JPAUtil instance;
	public static synchronized JPAUtil getInstance() {
		if (instance == null) instance = new JPAUtil();
		return instance;
	}
	
	private JPAUtil(){}
}
