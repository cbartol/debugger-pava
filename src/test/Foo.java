package test;

public class Foo {

	public int bar(int a, int b) throws Exception{
		System.out.println("yooo1");
		baz(a);
		return 0;
	}
	
	public int baz(int a) throws Exception{
		System.out.println("yooo2");
		if(a < 0){
			return 0;
		}
		throw new Exception("my exception");
		//return 99;
	}
}
