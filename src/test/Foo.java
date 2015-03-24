package test;

public class Foo {
	
	public int field1 = 999;

	public int bar(int a, int b) throws Exception{
		System.out.println("yooo1");
		return baz(a);
	}
	
	public int baz(int a) throws Exception{
		System.out.println("yooo2");
		if(field1 < 0){
			return 0;
		}
		throw new Exception("my exception");
		//return 99;
	}
}
