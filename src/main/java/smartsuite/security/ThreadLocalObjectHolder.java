package smartsuite.security;

public class ThreadLocalObjectHolder {
	private static final ThreadLocal<Object> THREAD_LOCAL = new ThreadLocal<Object>();
	   
	   
	   private ThreadLocalObjectHolder() {
	   }
	   
	   public static void set(Object value) {
		   THREAD_LOCAL.set(value);
	   }
	      
	   public static Object get() {
	       return THREAD_LOCAL.get();
	   }
	   
	   public static void clear() {
		   THREAD_LOCAL.remove();
	   }
}
