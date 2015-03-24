package ist.meic.pa;

import java.lang.reflect.Method;
import java.util.Stack;

import javassist.CtClass;

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
		for (StackLayer stackLayer : stack) {
			stackLayer.print();
		}
	}

	public static Object getCurrentInstance() {
		return stack.peek().getInstance();
	}

	public static Object invokeLastMethod() {
		try {
			return stack.peek().getMethod().invoke(stack.peek().getInstance(), stack.peek().getArgs());
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static void popStack() {
		stack.pop();
	}
}
