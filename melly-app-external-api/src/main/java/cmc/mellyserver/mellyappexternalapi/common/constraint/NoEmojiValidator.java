package cmc.mellyserver.mellyappexternalapi.common.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

import com.vdurmont.emoji.EmojiParser;

public class NoEmojiValidator implements ConstraintValidator<NoEmoji, String> {
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		if (StringUtils.isEmpty(value) == true) {
			return true;
		}

		return EmojiParser.parseToAliases(value).equals(value);
	}
}