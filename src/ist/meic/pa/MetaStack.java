package ist.meic.pa;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Stack;

@DoNotInspect
public class MetaStack {
	private static Stack<StackLayer> stack = new Stack<StackLayer>();
	
	public static void addInitialInformation(Class c, Object o, Object[] args, String method, Class[] args_types){

		
		System.err.println("RECEBEU uni: " + args_types);
		
		System.out.println("changing stack");
	
		try {
			Method m = c.getMethod(method, args_types);

			stack.push(new StackLayer(o, m, args));
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public static Object invokeLastMethod() throws Throwable{
		try{
			StackLayer layer = stack.peek();
			return layer.getMethod().invoke(layer.getInstance(), layer.getArgs());
		} catch (InvocationTargetException e){
			throw e.getTargetException();
		}
	}

	public static void popStack() {
		stack.pop();
	}

	public static Class getCurrentClass() {
		return stack.peek().getMethod().getDeclaringClass();
	}
}
