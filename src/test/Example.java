package test;

public class Example {
	private static int value = 0;
	
	
	public static void main(String[] args) throws Exception {
		Foo f = (Foo) Class.forName("test.Foo").newInstance();
		String return_val = f.bar(2, -1);
		System.out.println("static intern value: " + value);
		System.out.println("TERMINOU EXECUTION val return: " + return_val);
	}
}