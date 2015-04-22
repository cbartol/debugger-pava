# Project of Advanced Programming.

## Goal of this project:
Implementation of a debugger for Java programs. The debugger should, at load time, instrument a Java
program so that, when an exception is thrown, the program is stopped and the programmer is presented with
a command-line interface providing several debugging mechanisms.
The debugger must support the following set of commands:
* Abort: Terminates the execution of the application.
* Info: Presents detailed information about the called object, its fields, and the call stack. The presented
information follows the format described in the next section.
* Throw: Re-throws the exception, so that it may be handled by the next handler.
* Return <value>: Ignores the exception and continues the execution of the application assuming that the
current method call returned <value>. For calls to methods returning void the <value> is ignored. Note
that <value> should be of a primitive type.
* Get <field name>: Reads the field named <field name> of the called object.
* Set <field name> <new value>: Writes the field named <field name> of the called object, assigning
it the <new value>.
* Retry: Repeats the method call that was interrupted.

## 

check debugger.pdf for more details about the project.
