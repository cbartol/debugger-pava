package ist.meic.pa;

import java.lang.reflect.InvocationTargetException;

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

@DoNotInspect
public class ThirdTranslator implements Translator {

	@Override
	public void onLoad(ClassPool pool, String classname)
			throws NotFoundException, CannotCompileException {
		
		CtClass cc = pool.get(classname);
		try {
			if(cc.getAnnotation(DoNotInspect.class) != null){
				return;
			}
		} catch (ClassNotFoundException e) {
		}
		if(classname.startsWith("javassist.")){
			return;
		}
		for (CtMethod m : cc.getDeclaredMethods()) {
			System.out.println(cc.toString());
			if(m.isEmpty()){
				continue;
			}
			instrumentForStack(cc,m);
		}
	}
	
	private void instrumentForStack(CtClass ctClass, CtMethod ctMethod) throws CannotCompileException, NotFoundException {
		ctMethod.instrument(new ExprEditor(){
			@Override
			public void edit(MethodCall m) throws CannotCompileException {
				String template;
				try {
					if(m.getMethod().getReturnType().equals(CtClass.voidType)){
						template = "{"
								+ "ist.meic.pa.ThirdTranslator.superMethodCallVoid($class, $0,  \"%s\", $sig, \"%s\", $args);"
								+ "}";
					} else {
						template = "{"
								+ "$_ = ($r) ist.meic.pa.ThirdTranslator.superMethodCall($class, $0, \"%s\", $sig, \"%s\", $args);"
								+ "}";
					}
					String code = String.format(template, m.getMethod().getReturnType().getName(), m.getMethodName());
//					System.out.println(code);
					m.replace(code);
				} catch (NotFoundException e) {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void start(ClassPool pool) throws NotFoundException,
			CannotCompileException {
	}

	public static Object superMethodCall(Class clazz, Object o, String returnType, Class[] argTypes, String methodName, Object... args) throws Throwable{
//		System.out.println("--------------------------------");
//		System.out.println(clazz);
//		System.out.println(o);
//		for (Class class1 : argTypes) {
//			System.out.println(class1);
//		}
//		System.out.println(methodName);
//		for (Object arg : args) {
//			System.out.println(arg);
//		}
//		System.out.println("--------------------------------");
		MetaStack.pushInformation(clazz, o, methodName, argTypes, args);
		try {
			return clazz.getMethod(methodName, argTypes).invoke(o, args);
		} catch (InvocationTargetException e1) {
			Throwable e = e1.getTargetException();
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
			MyConsole console = new MyConsole(returnType); // return type
			console.execute(e);
			if (console.shouldThrowException()) {
				throw e;
			} else {
				return console.getReturnValue();
			}
		} finally {
			MetaStack.popStack();
		}
	}
	
	public static void superMethodCallVoid(Class clazz, Object o, String returnType, Class[] argTypes, String methodName, Object... args) throws Throwable{
//		System.out.println("--------------------------------");
//		System.out.println(clazz);
//		System.out.println(o);
//		for (Class class1 : argTypes) {
//			System.out.println(class1);
//		}
//		System.out.println(methodName);
//		for (Object arg : args) {
//			System.out.println(arg);
//		}
//		System.out.println("--------------------------------");
		MetaStack.pushInformation(clazz, o, methodName, argTypes, args);
		try {
			clazz.getMethod(methodName, argTypes).invoke(o, args);
		} catch (InvocationTargetException e1) {
			Throwable e = e1.getTargetException();
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
			MyConsole console = new MyConsole(returnType); // return type
			console.execute(e);
			if (console.shouldThrowException()) {
				throw e;
			} else {
				return;
			}
		} finally {
			MetaStack.popStack();
		}
	}
}
