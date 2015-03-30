package ist.meic.pa;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@DoNotInspect
public class MyConsole {
	private boolean continueRead = true;
	private Command command;
	private boolean throwException = false;
	private Object returnValue;
	private String returnType;
	private boolean mustRetry = false;
	private Throwable exception;
	
	public MyConsole(String returnType){
		this.returnType = returnType;
		command = new Command(this);
	}
	
	public void execute(Throwable e) throws Throwable{
		Scanner in = new Scanner(System.in);
		continueRead = true;
		exception = e;
		
			while(continueRead){
				//e.printStackTrace();
				System.out.print("DebuggerCLI:> ");
				String line = in.nextLine();
				List<String> shellArgs = new ArrayList<String>();
				shellArgs.addAll(Arrays.asList(line.split(" ")));
				Method m;
				try {
					m = Command.class.getMethod(shellArgs.get(0), List.class );
					shellArgs.remove(0);
				} catch (Exception e1) {
					System.err.println("Invalid Command");
					continue;
				}
				try{
					m.invoke(command, shellArgs);
				} catch (InvocationTargetException e2){
					throw e2.getTargetException();
				}
			}
		
		//in.close();
	}
	
	public Throwable getException(){
		return exception;
	}
	
	public boolean shouldThrowException(){
		return throwException;
	}
	
	public Object getReturnValue(){
		return returnValue;
	}
	
	public void throwException(){
		throwException = true;
		stopConsole();
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

	public boolean isRetry() {
		return mustRetry;
	}
	
	public void retry(){
		mustRetry = true;
	}
	
}
