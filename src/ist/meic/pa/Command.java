package ist.meic.pa;

import java.lang.reflect.Field;
import java.util.List;

@DoNotInspect
public class Command {
	private MyConsole console;
	
	public Command(MyConsole myConsole) {
		console = myConsole;
	}

	public void Abort(List<String> args){
		System.exit(0);
	}
	
	public void Info(List<String> args){
		Object instance = MetaStack.getCurrentInstance();
		Class clazz = MetaStack.getCurrentClass();
		System.out.println("Called Object: "+instance);
		String fieldsPrint = "\tFields:";
		try {
			for (Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				fieldsPrint += field.getName()+" ";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(fieldsPrint);
		System.out.println("Call stack:");
		MetaStack.printStack();
		final Throwable exception = console.getException();
		System.out.println("Throwable: " + exception.getClass().getName() + ": " + exception.getMessage());
	}
	
	public void Throw(List<String> args){
		console.throwException();
	}
	
	public void Return(List<String> args){
		if(args.size() == 0){
			console.stopConsole();
			return;
		}
		console.setReturnValue(magicStringConverter(console.getReturnType(), args.get(0)));
		
	}
	
	public void Get(List<String> args){
		Object instance = MetaStack.getCurrentInstance();
		try {
			Field field = null;
			if (instance == null){
				field = MetaStack.getCurrentClass().getDeclaredField(args.get(0));
			} else{
				field = instance.getClass().getDeclaredField(args.get(0));
			}
			field.setAccessible(true);
			System.out.println(field.get(instance));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Set(List<String> args){
		Object instance = MetaStack.getCurrentInstance();
		try {
			Field field = null;
			if (instance == null){
				field = MetaStack.getCurrentClass().getDeclaredField(args.get(0));
			} else{
				field = instance.getClass().getDeclaredField(args.get(0));
			}
			field.setAccessible(true);
			String fieldType = field.getType().getName();
			field.set(instance, magicStringConverter(fieldType, args.get(1)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void Retry(List<String> args) throws Throwable{
		console.retry();
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
