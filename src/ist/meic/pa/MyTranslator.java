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
		if(cc.getClass().getName().contains("ist.meic") || cc.getClass().getName().contains("javassist")){
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
			m.insertBefore("{ist.meic.pa.MetaStack.addInitialInformation($class,"+o+",\""+ m.getName() + "\", $args); }");// inserir informação sobre a classe do metodo
			
			//m.insertAfter("", true);
			String console = MyConsole.class.getName();
			System.out.println(console);
			m.addCatch("{ "+ console +".execute($e);"
					+ "if("+console+".throwException()){"
							+ "throw $e;"
					+ "}else if("+ console +".returnTypeIsVoid()){"
							+ " return;"
					+ "}else{"
							+ "return "+ console +".getReturnValue();"
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
