package test;

public class Foo implements ZZZ{

	public void bar(int a) throws Exception{
		System.out.println("yooo1");
		baz(a);
	}
	
	public void baz(int a) throws Exception{
		System.out.println("yooo2");
		throw new Exception("my exception");
	}
}
