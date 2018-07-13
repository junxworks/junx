package io.github.junxworks.junx.test.spel.function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.spel.SpelNode;
import org.springframework.expression.spel.ast.FunctionReference;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.junxworks.junx.core.util.ObjectUtils;
import io.github.junxworks.junx.spel.Expression;
import io.github.junxworks.junx.spel.ExpressionEvaluationContext;
import io.github.junxworks.junx.spel.NodeVisitor;
import io.github.junxworks.junx.spel.function.FunctionRepository;
import io.github.junxworks.junx.test.spel.Application;
import io.github.junxworks.junx.test.spel.compiler.Function;
import io.github.junxworks.junx.spel.JCompiler;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CompilerTest {

	private static final String exp1 = "#max(4,2,100.1f)+#min(1,3,-123)";

	@Test
	public void test() throws Exception {
		Expression exp = JCompiler.parse(exp1);
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariables(FunctionRepository.currentMethodMap());
		SpelExpression spel = exp.getExpression();
		System.out.println(spel.getValue(context));
	}

	private static final String exp2 = "#sum(#max(4,2,100.1f),#min(1,3,-123),'100.1234')";

	@Test
	public void test2() throws Exception {
		Expression exp;
		exp = JCompiler.parse(exp2);
		ExpressionEvaluationContext context = Expression.createContext();
		System.out.println(exp.execute(context));
	}

	private static final String exp3 = "#sum(#max(4,2,100.1f),#min(1,3,-123),'100.1234')";

	@Test
	public void test3() throws Exception {
		Expression exp;
		exp = JCompiler.parse(exp3);
		ExpressionEvaluationContext context = Expression.createContext();
		System.out.println(exp.execute(context, String.class));
	}

	private static final String exp4 = "#f.plus(1,2)";

	@Test
	public void test4() throws Exception {
		Expression exp;
		exp = JCompiler.parse(exp4);
		ExpressionEvaluationContext context = Expression.createContext();
		context.setVariable("f", new Function());
		System.out.println(exp.execute(context));
	}

	@Test
	public void test5() throws Exception {
		Expression exp = JCompiler.parse(exp3);
		JCompiler.analyzeExpression(exp, new NodeVisitor() {

			@Override
			public void visit(SpelNode node) {
				if (node instanceof FunctionReference) {
					FunctionReference f = (FunctionReference) node;
					System.out.println(ObjectUtils.mirror().on(f).get().field("name"));
				}
			}

		});
	}

	private static final String exp6 = "#devide(2.1,0,10)";

	@Test
	public void test6() throws Exception {
		Expression exp = JCompiler.parse(exp6);
		ExpressionEvaluationContext context = Expression.createContext();
		System.out.println(exp.execute(context));
	}

	private static final String exp7 = "#min(#mutiply(2.1,12.341,10),#devide(2.1,0.01234,10))";

	@Test
	public void test7() throws Exception {
		Expression exp = JCompiler.parse(exp7);
		ExpressionEvaluationContext context = Expression.createContext();
		System.out.println(exp.execute(context));
	}

	private static final String exp8 = "#sub(1.0 , 0.42)";

	@Test
	public void test8() throws Exception {
		Expression exp = JCompiler.parse(exp8);
		ExpressionEvaluationContext context = Expression.createContext();
		System.out.println(exp.execute(context));
	}
}
