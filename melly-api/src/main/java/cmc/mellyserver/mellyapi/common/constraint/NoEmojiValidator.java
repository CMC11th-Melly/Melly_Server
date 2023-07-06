package cmc.mellyserver.mellyapi.common.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

//import com.vdurmont.emoji.EmojiParser;

public class NoEmojiValidator implements ConstraintValidator<NoEmoji, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

//		if (StringUtils.isEmpty(value) == true) {
//			return true;
//		}
//
//		return EmojiParser.parseToAliases(value).equals(value);
        return true;
    }
}
