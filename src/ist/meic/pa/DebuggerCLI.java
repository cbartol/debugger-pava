package ist.meic.pa;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.Translator;

public class DebuggerCLI {
	public static int value = 0;
	
	public static void main(String[] args) throws Throwable {
		
		ClassPool pool = ClassPool.getDefault();
		Loader cl = new Loader();
		Translator t = new MyTranslator();
		cl.addTranslator(pool, t);
		cl.run("test.Example", args);
		
		
		
		
		//CtClass cc = pool.get("ist.meic.pa.Foo");
//		CtClass etype = ClassPool.getDefault().get("java.lang.Exception");
		
//		CtMethod m = cc.getDeclaredMethod("bar");
//		m.insertAfter("{ ist.meic.pa.DebuggerCLI.value = $1;}", true);
//		m.addCatch("{ System.out.println(ist.meic.pa.DebuggerCLI.value); throw $e; }", etype);
//		m = cc.getDeclaredMethod("baz");
//		m.addCatch("{ System.out.println(ist.meic.pa.DebuggerCLI.value); throw $e; }", etype);
//		cc.writeFile();
//		Class clazz = cc.toClass();
//		
//		//Class clazz = Class.forName("ist.meic.pa.Foo");
//		clazz.getMethod("bar", int.class).invoke(clazz.newInstance(),2);

	}
}
