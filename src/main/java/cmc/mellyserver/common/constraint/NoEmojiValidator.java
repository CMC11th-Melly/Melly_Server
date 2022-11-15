package cmc.mellyserver.common.constraint;


import com.vdurmont.emoji.EmojiParser;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NoEmojiValidator implements ConstraintValidator<NoEmoji, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isEmpty(value) == true) {
            return true;
        }

        return EmojiParser.parseToAliases(value).equals(value);
    }
}
