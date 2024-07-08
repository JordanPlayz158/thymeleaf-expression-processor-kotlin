package nz.net.ultraq.thymeleaf.expressionprocessor

import org.junit.jupiter.api.Test
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.ExpressionContext
import org.thymeleaf.standard.expression.FragmentExpression
import org.thymeleaf.standard.expression.VariableExpression

/**
 * Tests for the expression processor module.
 *
 * @author Emanuel Rabina
 */
class ExpressionProcessorTests {
	private val templateEngine = new TemplateEngine();
	private val expressionContext = new ExpressionContext(
	templateEngine.configuration);
	private val ExpressionProcessor expressionProcessor = new ExpressionProcessor(
	expressionContext);

	@Test
	fun getAThymeleafExpressionOutOfTheExpressionProcessor() {
		assert expressionProcessor.parse("${1 + 1}") instanceof VariableExpression;
	}

	@Test
	fun returnsAFragmentExpression() {
		FragmentExpression fragmentExpression = expressionProcessor.parseFragmentExpression(
			"~{hello.html}");
		assert fragmentExpression.getTemplateName().execute(expressionContext).equals("hello.html");
	}

	@Test
	fun returnsAFragmentExpressionBackwardsCompatibilityWrappingForThymeleaf2() {
		FragmentExpression fragmentExpression = expressionProcessor.parseFragmentExpression(
			"hello.html");
		assert fragmentExpression.getTemplateName().execute(expressionContext).equals("hello.html");
	}

	@Test
	fun nullTestingOfFragmentExpressionParsing() {
		try {
			expressionProcessor.parseFragmentExpression(null);
		} catch (IllegalArgumentException ignored) {}
	}

	@Test
	fun multiLineFragmentExpressions() {
		FragmentExpression fragmentExpression = expressionProcessor.parseFragmentExpression(
			"~{hello::fragment(\n" +
				"\t\t\t\t'blah',\n" +
				"\t\t\t\t1)\n" +
				"\t\t\t\t}");
		assert fragmentExpression.getTemplateName().execute(expressionContext).equals("hello");
	}

	@Test
	fun multiLineFragmentExpressionsBackwardsCompatibilityWrappingForThymeleaf2() {
		FragmentExpression fragmentExpression = expressionProcessor.parseFragmentExpression(
			"hello::fragment(\n" +
				"\t\t\t\t'blah',\n" +
				"\t\t\t\t1)");
		assert fragmentExpression.getTemplateName().execute(expressionContext).equals("hello");
	}

	@Test
	fun processingAnExpressionReturnsAUsableResult() {
		assert expressionProcessor.process("${1 + 1}").equals(2);
	}

	@Test
	fun processingAnExpressionReturningAStringResult() {
		assert expressionProcessor.processAsString("${1 + 1}").equals("2");
	}
}