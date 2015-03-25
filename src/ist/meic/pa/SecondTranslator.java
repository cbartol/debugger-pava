package ist.meic.pa;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.Translator;

public class SecondTranslator implements Translator {

	@Override
	public void onLoad(ClassPool pool, String classname)
			throws NotFoundException, CannotCompileException {
		
		CtClass cc = pool.get(classname);
		if(classname.contains("ist.meic") || classname.contains("javassist")){
			return;
		}
		for (CtMethod m : cc.getDeclaredMethods()) {
			System.out.println(cc.toString());
			if(m.isEmpty()){
				continue;
			}
			addTryCatch(cc,m);
			try {
				cc.writeFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void addTryCatch(CtClass ctClass, CtMethod ctMethod) throws CannotCompileException, NotFoundException {
		String name = ctMethod.getName();
		ctClass.defrost();
		final boolean isVoid = ctMethod.getReturnType().equals(CtClass.voidType);
		String o = "$0";
		if(Modifier.isStatic(ctMethod.getModifiers())){
			o = "null";
		}
		ctMethod.setName(name + "$original");
		ctMethod = CtNewMethod.copy(ctMethod, name, ctClass, null);
		final String template = "{"
				+ "Class[] args_types = %s;" // convertParametersTypes(ctMethod)
				+ "ist.meic.pa.MetaStack.addInitialInformation($class,%s, $args, \"%s\", args_types);" // o , ctMethod.getName()
				+ "try{"
					+ "%s;" // call to original method with or without return statement
				+ "} catch(java.lang.Exception e){"
					+ "System.out.println(e.getClass().getName() + \": \" + e.getMessage());"
					+ "ist.meic.pa.MyConsole console = new ist.meic.pa.MyConsole(\"%s\");" //return type
					+ "console.execute(e);"
					+ "if(console.shouldThrowException()){"
							+ "throw e;"
					+ "}else{"
							+ "%s;" //return statement ((isVoid)?returnVoid:returnNotVoid)
					+ "}"
				+ "}"
			+ "}";
		
		String callMethod = name + "$original($$)";
		String returnStatement = "return";
		
		if(!isVoid){
			callMethod = "return " + callMethod;
			returnStatement += " ($r) console.getReturnValue()";
		}
		final String code = String.format(template, convertParametersTypes(ctMethod), o, ctMethod.getName(), callMethod, ctMethod.getReturnType().getName(), returnStatement);
		System.out.println(code);
		ctMethod.setBody(code);
		ctClass.addMethod(ctMethod);

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
