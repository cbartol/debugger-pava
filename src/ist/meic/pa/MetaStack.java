package ist.meic.pa;

import java.lang.reflect.Method;
import java.util.Stack;

@DoNotInspect
public class MetaStack {
	public static Stack<StackLayer> stack = new Stack<StackLayer>();
	public static void pushInformation(Class c, Object o, String method, Class[] args_types, Object[] args){
		boolean mustCheckSuperClass = true;
		do{
			try {
				Method m = c.getDeclaredMethod(method, args_types);
				mustCheckSuperClass = false;
				m.setAccessible(true);
				stack.push(new StackLayer(o, m, args));
			} catch (Exception e) {
				//e.printStackTrace();
				c = c.getSuperclass();
			}
		} while(mustCheckSuperClass);
	}
	
	public static void printStack() {
		Object[] stackArray = stack.toArray();
		for (int i = stackArray.length -1 ; i >= 0 ; i--) {
			((StackLayer) stackArray[i]).print();
		}
	}

	public static Object getCurrentInstance() {
		return stack.peek().getInstance();
	}

	public static Object retryThisMethod() throws Throwable{
		StackLayer layer = stack.pop();
		return ThirdTranslator.superMethodCall(layer.getMethod().getDeclaringClass(), layer.getInstance(), layer.getMethod().getReturnType().getName(), layer.getMethod().getParameterTypes(), layer.getMethod().getName(), layer.getArgs());
	}

	public static void popStack() {
		stack.pop();
	}

	public static Class getCurrentClass() {
		return stack.peek().getMethod().getDeclaringClass();
	}
}
