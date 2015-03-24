package test;

public class Example {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Foo f = (Foo) Class.forName("test.Foo").newInstance();
		try {
			f.bar(2, -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//		System.out.println(new A().foo(new B()));
	}
}