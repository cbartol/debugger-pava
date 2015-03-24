package ist.meic.pa;

import java.lang.reflect.Method;

public class StackLayer {
	private Object instance;
	private Method method;
	private Object[] args;
	
	public StackLayer(Object instance, Method method, Object[] args) {
		this.instance = instance;
		this.method = method;
		this.args = args;
	}

	public Object getInstance() {
		return instance;
	}

	public Method getMethod() {
		return method;
	}

	public Object[] getArgs() {
		return args;
	}
	
	public void print(){
		String className = method.getDeclaringClass().getName();
		String methodName = method.getName();
		String argValues = "(";
		for(Object arg : args){
			argValues += arg + ",";
		}
		argValues = argValues.substring(0, argValues.length()-1) + ")";
		System.out.println(className+"."+methodName+argValues);
	}
}
