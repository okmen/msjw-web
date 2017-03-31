package cn.web.front.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public final class EmojiUtils {

    public static String remove(final String emojiSource) {

        final Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE
                | Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(emojiSource);

        return matcher.replaceAll(StringUtils.EMPTY);
    }
    
    public static boolean containsEmoji(final String emojiSource) {
        if(StringUtils.isBlank(emojiSource))
            return false;
        
        final Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE
                | Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(emojiSource);
        
        return matcher.find();
    }
}
