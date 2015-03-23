package ist.meic.pa;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.Translator;

public class MyTranslator implements Translator {

	@Override
	public void onLoad(ClassPool pool, String classname)
			throws NotFoundException, CannotCompileException {
		CtClass etype = ClassPool.getDefault().get("java.lang.Exception");
		
		CtClass cc = pool.get(classname);
		System.out.println(cc.toString());
		if(classname.contains("ist.meic") || classname.contains("javassist")){
			return;
		}
		for (CtMethod m : cc.getDeclaredMethods()) {
			if(m.isEmpty()){
				continue;
			}
			String o = "$0";
			if(Modifier.isStatic(m.getModifiers())){
				o = "null";
			}
			System.out.println(m.toString());
			m.insertBefore("{ ist.meic.pa.MetaStack.addInitialInformation($class,"+o+",\""+ m.getName() + "\", $args); }");// inserir informação sobre a classe do metodo
			
			//m.insertAfter("", true);
			String consoleClassname = MyConsole.class.getName();
			System.out.println(consoleClassname);
			boolean isVoid = m.getReturnType().equals(CtClass.voidType);
			String returnVoid = "return;";
			System.out.println(m.getReturnType().getName());
			String returnNotVoid = "return (" + m.getReturnType().getName() + ") "+ consoleClassname +".getReturnValue();";
			
			m.addCatch("{"
					+ "ist.meic.pa.MyConsole console = new ist.meic.pa.MyConsole(" + m.getReturnType().getName() + ");"
					+ "console.execute($e);"
					+ "if(console.shouldThrowException()){"
							+ "throw $e;"
					+ "}else{"
							+ ((isVoid)?returnVoid:returnNotVoid)
					+ "}}", etype);
		}
		try {
			cc.writeFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(ClassPool pool) throws NotFoundException,
			CannotCompileException {
	}

}
