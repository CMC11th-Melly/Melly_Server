package cmc.mellyserver.common.aop.lock;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class CustomSpringELParser {

	private CustomSpringELParser() {
	}

	/*
	 * key는 #groupId parameterNames : userId, groupId args : 실제 값들 - 1, 1
	 */
	public static Object getDynamicValue(String[] parameterNames, Object[] args, String key) {

		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();

		for (int i = 0; i < parameterNames.length; i++) {
			context.setVariable(parameterNames[i], args[i]);
		}

		// #groupId라는 식에서 groupId
		Expression expression = parser.parseExpression(key);
		return expression.getValue(context, Object.class);
	}

}
