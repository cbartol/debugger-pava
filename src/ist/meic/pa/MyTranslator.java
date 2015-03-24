package ist.meic.pa;

import java.io.IOException;
import java.lang.reflect.Method;

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
			System.err.println(m.toString());
			
			String insertBeforeInjectionString = "{ "
					+ "Class[] args_types = " + convertParametersTypes(m) + ";"
					+ "ist.meic.pa.MetaStack.addInitialInformation($class,"+o+", $args, \"" + m.getName() + "\", args_types); }";

			System.err.println("INSERT BEFORE: " + insertBeforeInjectionString);
						
			m.insertBefore(insertBeforeInjectionString);// inserir informa��o sobre a classe do metodo
			
			//m.insertAfter("", true);
			String consoleClassname = MyConsole.class.getName();
			System.out.println(consoleClassname);
			boolean isVoid = m.getReturnType().equals(CtClass.voidType);
			String returnVoid = "return;";
			System.out.println(m.getReturnType().getName());
			String returnNotVoid = "return console.getReturnValue_" + m.getReturnType().getSimpleName() + "();";
			
			
			

			String toAddCatchString = "{"
					+ "ist.meic.pa.MyConsole console = new ist.meic.pa.MyConsole(\"" + m.getReturnType().getName() + "\");"
					+ "console.execute($e);"
					+ "if(console.shouldThrowException()){"
							+ "throw $e;"
					+ "}else{"
							+ ((isVoid)?returnVoid:returnNotVoid)
					+ "}}";
			System.out.println(toAddCatchString);
			m.addCatch(toAddCatchString, etype);
		}
		try {
			cc.writeFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String convertParametersTypes(CtMethod m){
		String output = "";
		try {
			for (CtClass c : m.getParameterTypes()) {
				output += c.getName() + ".class,";
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return "{" + output.substring(0, output.length() - 1) + "}";	
	}

	@Override
	public void start(ClassPool pool) throws NotFoundException,
			CannotCompileException {
	}

}
