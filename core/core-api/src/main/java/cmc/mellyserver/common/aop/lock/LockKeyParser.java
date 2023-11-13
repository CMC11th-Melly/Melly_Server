package cmc.mellyserver.common.aop.lock;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public abstract class LockKeyParser {

  private static final String REDISSON_LOCK_PREFIX = "LOCK:";

  public static String parse(String[] parameterNames, Object[] args, String key) {

	ExpressionParser parser = new SpelExpressionParser();
	StandardEvaluationContext context = new StandardEvaluationContext();

		/*
		parameterNames에는 메서드 파라미터의 이름이 담겨있다
		args에는 해당 메서드 파라미터의 값이 담겨있다
		context내에 {파라미터 이름 : 실제 값} 형태의 map을 만든다
		 */
	for (int i = 0; i < parameterNames.length; i++) {
	  context.setVariable(parameterNames[i], args[i]);
	}

		/*
		#groupId를 Expression으로 만들고 이를 기반으로 groupId의 값을 파싱 후 반환한다
		 */
	Expression expression = parser.parseExpression(key);
	String keyValue = expression.getValue(context, String.class);
	return REDISSON_LOCK_PREFIX + keyValue;
  }

}
