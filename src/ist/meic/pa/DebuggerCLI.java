package ist.meic.pa;

import javassist.ClassPool;
import javassist.Loader;
import javassist.Translator;

public class DebuggerCLI {
	
	public static void main(String[] args) throws Throwable {
		
		ClassPool pool = ClassPool.getDefault();
		Loader cl = new Loader();
		Translator t = new ThirdTranslator();
		cl.addTranslator(pool, t);
		String[] shellArgs = new String[args.length-1];
		
		for (int i = 1 ; i < args.length ; i++) {
			shellArgs[i-1] = args[i];
		}
		
		cl.run(args[0], shellArgs);
	}
}
