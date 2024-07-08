package nz.net.ultraq.thymeleaf.expressionprocessor

import org.slf4j.LoggerFactory
import org.thymeleaf.context.IExpressionContext
import org.thymeleaf.standard.expression.FragmentExpression
import org.thymeleaf.standard.expression.IStandardExpression
import org.thymeleaf.standard.expression.StandardExpressions
import java.util.regex.Pattern

// TODO: Is broken in some way, be sure to fix
class ExpressionProcessor(private val context: IExpressionContext) {
	companion object {
		private val logger = LoggerFactory.getLogger(ExpressionProcessor::class.java)
		private val oldFragmentExpressions = HashSet<String>()
	}

	/**
	 * Parses an expression, returning the matching expression type.
	 *
	 * @param expression
	 * @return Matching expression type.
	 */
	fun parse(expression: String): IStandardExpression {
		return StandardExpressions.getExpressionParser(this.context.configuration)
			.parseExpression(this.context, expression)
	}

	/**
	 * Parses an expression under the assumption it is a fragment expression. This method will wrap
	 * fragment expressions written in Thymeleaf 2 syntax as a backwards compatibility measure for
	 * those migrating their web apps to Thymeleaf 3.  (This is because Thymeleaf 3 currently does the
	 * same, but expect this method to go away when Thymeleaf starts enforcing the new fragment
	 * expression syntax itself.)
	 *
	 * @param expression
	 * @return A fragment expression.
	 */

//if (!ScriptBytecodeAdapter.matchRegex((Object)expression, (Object)"(?s)^~\\{.+\\}$")) {
//		if (!ExpressionProcessor.oldFragmentExpressions.contains(expression)) {
//				ExpressionProcessor.logger.warn(StringGroovyMethods.plus(StringGroovyMethods.plus("Fragment expression \"{}\" is being wrapped as a Thymeleaf 3 fragment expression (~{...}) for backwards compatibility purposes.  ", (CharSequence)"This wrapping will be dropped in the next major version of the expression processor, so please rewrite as a Thymeleaf 3 fragment expression to future-proof your code.  "), (CharSequence)"See https://github.com/thymeleaf/thymeleaf/issues/451 for more information."), (Object)expression);
//				DefaultGroovyMethods.leftShift((Set)ExpressionProcessor.oldFragmentExpressions, (Object)expression);
//		}
//		expression = invokedynamic(cast:(Lgroovy/lang/GString;)Ljava/lang/String;, new GStringImpl(new Object[] { expression }, new String[] { "~{", "}" }));
//}
//return (FragmentExpression)invokedynamic(invoke:(Lorg/thymeleaf/standard/expression/IStandardExpression;Ljava/lang/Class;)Ljava/lang/Object;, this.parse(expression), FragmentExpression.class);
	fun parseFragmentExpression(expression: String?): FragmentExpression {
		if (expression == null) throw IllegalArgumentException("'expression' cannot be null")

		var newExpression = expression

		if (!(Pattern.matches("(?s)^~\\{.+}$", expression))) {
			if (!oldFragmentExpressions.contains(expression)) {
				logger.warn(
					"Fragment expression \"{}\" is being wrapped as a Thymeleaf 3 fragment expression (~{...}) for backwards compatibility purposes.  "
						+ "This wrapping will be dropped in the next major version of the expression processor, so please rewrite as a Thymeleaf 3 fragment expression to future-proof your code.  "
						+ "See https://github.com/thymeleaf/thymeleaf/issues/451 for more information.",
					expression)
				oldFragmentExpressions.add(expression)
			}

			newExpression = "~{$expression}"
		}

		// TODO: Is the string actually suppose to be nullable?
		//  the groovy code shows if (expression) which to my
		//  knowledge is a null check which means, if it is
		//  null, then a null string will get down here
		//  so how do we and/or how does groovy handle that under
		//  the hood, they aren't using ? but not sure if you can
		//  use ? on functions
		return parse(newExpression) as FragmentExpression
	}

	/**
	 * Parse and executes an expression, returning whatever the type of the expression result is.
	 *
	 * @param expression
	 * @return The result of the expression being executed.
	 */
	fun process(expression: String): Any {
		return this.parse(expression).execute(this.context)
	}

	/**
	 * Parse and execute an expression, returning the result as a string.  Useful for expressions that
	 * expect a simple result.
	 *
	 * @param expression
	 * @return The expression as a string.
	 */
	fun processAsString(expression: String): String {
		return process(expression).toString()
	}

	fun getContext(): IExpressionContext {
		return this.context;
	}
}