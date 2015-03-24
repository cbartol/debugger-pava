package ist.meic.pa;

import java.lang.reflect.Field;
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
		Object instance = MetaStack.getCurrentInstance();
		System.out.println("Called Object:"+instance);
		if(instance != null){
			try {
				String fieldsPrint = "\tFields:";
				for (Field field : instance.getClass().getDeclaredFields()) {
					field.setAccessible(true);
					fieldsPrint += field.getName()+",";
				}
				System.out.println(fieldsPrint.substring(0, fieldsPrint.length()-1));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Call stack:");
		MetaStack.printStack();
	}
	
	public void Throw(List<String> args){
		console.throwException();
	}
	
	public void Return(List<String> args){
		System.err.println("ENTROU RETURN COMMAND.JAVA");
		if(args.size() == 0){
			console.stopConsole();
			return;
		}
		console.setReturnValue(magicStringConverter(console.getReturnType(), args.get(0)));
		
	}
	
	public void Get(List<String> args){
		Object instance = MetaStack.getCurrentInstance();
		try {
			Field field = instance.getClass().getDeclaredField(args.get(0));
			field.setAccessible(true);
			System.out.println(field.get(instance));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Set(List<String> args){
		Object instance = MetaStack.getCurrentInstance();
		try {
			if (instance == null){
				System.err.println("NOT IMPLEMENTED YET, SET STATIC FIELDS");
			} else{
				Field field = instance.getClass().getDeclaredField(args.get(0));
				field.setAccessible(true);
				String fieldType = field.getType().getName();
				field.set(instance, magicStringConverter(fieldType, args.get(1)));
				System.out.println("set done!");
			}
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
			if (returnType.contains("Java.lang.String")){
				return value;
			} else {
				return Class.forName("java.lang." + returnType).getMethod(methodName, String.class).invoke(null, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	
}
