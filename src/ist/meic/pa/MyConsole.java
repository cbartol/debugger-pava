package ist.meic.pa;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MyConsole {
	private boolean continueRead = true;
	private Command command;
	private boolean throwException = false;
	private Object returnValue;
	private String returnType;
	
	public MyConsole(String returnType){
		this.returnType = returnType;
		command = new Command(this);
	}
	
	public void execute(Exception e){
		System.out.println("executing");
		
		Scanner in = new Scanner(System.in);
		continueRead = true;
		
		
			while(continueRead){
				try {
				System.out.print("> ");
				String line = in.nextLine();
				List<String> shellArgs = new ArrayList<String>();
				shellArgs.addAll(Arrays.asList(line.split(" ")));
				
				Method m = Command.class.getMethod(shellArgs.get(0), List.class );
				shellArgs.remove(0);
				m.invoke(command, shellArgs);
				} catch (Exception e1) {
					e1.printStackTrace();
					System.err.println("Invalid Command");
				}
			}
		
		//in.close();
	}
	
	public boolean shouldThrowException(){
		return throwException;
	}
	
	public boolean returnTypeIsVoid(){
		System.out.println("is void?");
		return false;
	}
	
	/*public Object getReturnValue(){
		return returnValue;
	}*/
	
	public int getReturnValue(){
		return (int) returnValue;
	}
	
	public void throwException(){
		throwException = true;
		stopConsole();
		MetaStack.popStack();
	}
	
	public void stopConsole(){
		continueRead = false;
	}
	
	public void setReturnValue(Object o){
		returnValue = o;
		stopConsole();
	}
	public String getReturnType() {
		return returnType;
	}
}
