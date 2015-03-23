package ist.meic.pa;

import java.lang.reflect.Method;
import java.util.Stack;

public class MetaStack {
	private static Stack<StackLayer> stack = new Stack<StackLayer>();
	
	
	public static void addInitialInformation(Class c, Object o, String method, Object[] args){
		System.out.println("changing stack");
		Class[] argsType = new Class[args.length];
		for(int i = 0 ; i < args.length ; i++){
			argsType[i]= args[i].getClass();
		}
		try {
			Method m = c.getMethod(method, argsType);
			stack.push(new StackLayer(o, m, args));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void printStack() {
		System.out.println(stack.peek().getInstance());
		System.out.println("Call stack:");
		for (StackLayer stackLayer : stack) {
			System.out.println(stackLayer.getMethod().getName() + stackLayer.getArgs());
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