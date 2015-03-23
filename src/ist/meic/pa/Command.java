package ist.meic.pa;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Command {
	private Object lastObject;
	private Map<String, Object> objects = new HashMap<String, Object>();
	private MyConsole console;
	
	public Command(MyConsole myConsole) {
		console = myConsole;
	}

	public void Abort(List<String> args){
		System.exit(0);
	}
	
	public void Info(List<String> args){
		MetaStack.printStack();
	}
	
	public void Throw(List<String> args){
		console.throwException();
	}
	
	public void Return(List<String> args){
		if(args.size() == 0){
			return;
		}
		console.setReturnValue(magicStringConverter(console.getReturnType(), args.get(0)));
		
	}
	
	public void Get(List<String> args){
		Object instance = MetaStack.getCurrentInstance();
		try {
			System.out.println(instance.getClass().getField(args.get(0)).get(instance));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void Set(List<String> args){
		Object instance = MetaStack.getCurrentInstance();
		try {
			String fieldType = instance.getClass().getField(args.get(0)).getType().getName();
			instance.getClass().getField(args.get(0)).set(instance, magicStringConverter(fieldType, args.get(0)));
			System.out.println("set done!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void Retry(List<String> args){
		console.setReturnValue(MetaStack.invokeLastMethod());
		console.stopConsole();
	}
	
	private Object magicStringConverter(String type, String value){
		String returnType;
		String methodName;
		if(type.contains("char")){
			return value.charAt(0);
		} else if(type.contains("int")){
			returnType = "Integer";
			methodName = "parseInt";
		} else {
			returnType = Character.toUpperCase(type.charAt(0)) + type.substring(1);
			methodName = "parse" + returnType;
		}
		try {
			return Class.forName("java.lang." + returnType).getMethod(methodName, String.class).invoke(null, value);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	
}
