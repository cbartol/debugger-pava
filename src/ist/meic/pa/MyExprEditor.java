package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.expr.ConstructorCall;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;

@DoNotInspect
public class MyExprEditor extends ExprEditor {

	@Override
	public void edit(MethodCall m) throws CannotCompileException {
		String template = "{"
				+ "$_ = ($r) ist.meic.pa.ThirdTranslator.superMethodCall($class, $0, \"%s\", $sig, \"%s\", $args);"
				+ "}";
		String code;
		try {
			code = String.format(template, m.getMethod().getReturnType().getName(), m.getMethodName());
			m.replace(code);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}
}
