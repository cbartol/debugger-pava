package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.Translator;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class ThirdTranslator implements Translator {

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
			instrumentForStack(cc,m);
		}
	}
	
	private void instrumentForStack(CtClass ctClass, CtMethod ctMethod) throws CannotCompileException, NotFoundException {
		final String template = "{"
				+ "ist.meic.pa.MetaStack.addInitialInformation($class,$0, $args, \"%s\", $sig);"  // o , ctMethod.getName()
				+ "$_ = $proceed($$);"
				+ "ist.meic.pa.MetaStack.popStack();"
			+ "}";
		
		System.out.println(template);
		
		ctMethod.instrument(new ExprEditor(){
			@Override
			public void edit(MethodCall m) throws CannotCompileException {
				m.replace(String.format(template, m.getMethodName()));
				//super.edit(m);
			}
		});

	}

	private void addTryCatch(CtClass ctClass, CtMethod ctMethod) throws CannotCompileException, NotFoundException {
		String name = ctMethod.getName();
		final boolean isVoid = ctMethod.getReturnType().equals(CtClass.voidType);
		ctMethod.setName(name + "$original");
		
		String o = "$0";
		if(Modifier.isStatic(ctMethod.getModifiers())){
			o = "null";
		}
		boolean isMain = name.equals("main");
		ctMethod = CtNewMethod.copy(ctMethod, name, ctClass, null);
		final String template = "{"
				+ ((isMain)?"ist.meic.pa.MetaStack.addInitialInformation($class,%s, $args, \"%s\", $sig);":"")
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
				+ "} finally {"
						+ "ist.meic.pa.MetaStack.popStack();"
				+ "}"
			+ "}";
		
		String callMethod = name + "$original($$)";
		String returnStatement = "return";
		
		if(!isVoid){
			callMethod = "return " + callMethod;
			returnStatement += " ($r) console.getReturnValue()";
		}
		String code;
		if(isMain){
			code = String.format(template, o, name, callMethod, ctMethod.getReturnType().getName(), returnStatement);
		} else {
			code = String.format(template, callMethod, ctMethod.getReturnType().getName(), returnStatement);
		}
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
