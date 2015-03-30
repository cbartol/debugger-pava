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
//			System.out.println(cc.toString());
			if(m.isEmpty()){
				continue;
			}
			instrumentForStack(cc,m);
			if(m.getName().equals("main")){
				String name = m.getName();
				m.setName(name + "\u200B");
				m = CtNewMethod.copy(m, name, cc, null);
				final String code = "{"
						+ name + "\u200B($$);"
						+ "}";
				m.setBody(code);
				cc.addMethod(m);
				instrumentForStack(cc,m);
			}
		}
	}
	
	private void instrumentForStack(CtClass ctClass, CtMethod ctMethod) throws CannotCompileException, NotFoundException {
		ctMethod.instrument(new MyExprEditor());
	}

	@Override
	public void start(ClassPool pool) throws NotFoundException,
			CannotCompileException {
	}

	public static Object superMethodCall(Class clazz, Object o, String returnType, Class[] argTypes, String methodName, Object... args) throws Throwable{
		MetaStack.pushInformation(clazz, o, methodName, argTypes, args);
		try {
			Object result = clazz.getMethod(methodName, argTypes).invoke(o, args); 
			MetaStack.popStack();
			return result; 
		}catch(NullPointerException e){
			MetaStack.popStack();
			throw e;
		} catch (InvocationTargetException e1) {
			Throwable e = e1.getTargetException();
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
			MyConsole console = new MyConsole(returnType); // return type
			console.execute(e);
			if (console.shouldThrowException()) {
				MetaStack.popStack();
				throw e;
			} else {
				if(console.isRetry()){
					return MetaStack.retryThisMethod();
				}
				MetaStack.popStack();
				return console.getReturnValue();
			}
		}
	}
}
