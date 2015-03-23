package ist.meic.pa;

public class MyConsole {
	
	
	public static void execute(Exception e){
		System.out.println("executing");
	}
	
	public static boolean throwException(){
		System.out.println("throw Exception?");
		return false;
	}
	
	public static boolean returnTypeIsVoid(){
		System.out.println("is void?");
		return false;
	}
	
	public static Object getReturnValue(){
		System.out.println("returning");
		return null;
	}
}
