package test;

public class Foo {
	
	private int field1 = 999;

	public String bar(int a, int b) throws Exception{
		System.out.println("yooo1");
		xxx();
		return "" + baz(a);
	}
	
	public void xxx() throws Exception{
		throw new Exception("super exception");
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
