package test;

public class Example {

	public static void main(String[] args) throws Exception {
		Foo f = (Foo) Class.forName("test.Foo").newInstance();
		int return_val = f.bar(2, -1);
		System.out.println("TERMINOU EXECUTION val return: " + return_val);
	}
}