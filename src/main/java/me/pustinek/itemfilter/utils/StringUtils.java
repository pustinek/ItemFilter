package me.pustinek.itemfilter.utils;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private static StringBuffer replaceAll(String templateText, Pattern pattern,
                                           Function<Matcher, String> replacer) {
        Matcher matcher = pattern.matcher(templateText);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(result, replacer.apply(matcher));
        }
        matcher.appendTail(result);
        return result;
    }
}
