package com.journme.rest.common.searchfilter;

import com.journme.rest.common.searchfilter.SearchFilter.CollectionMatch;
import com.journme.rest.common.searchfilter.SearchFilter.ElementMatch;
import com.journme.rest.common.searchfilter.SearchFilter.TextMatch;
import com.journme.rest.common.util.Constants;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpressionBase;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.path.CollectionPathBase;

import java.util.Collection;

public abstract class PredicateBuilder {

    public static BooleanExpression matchCollectionFieldAgainstValues(CollectionMatch matcher, CollectionPathBase field, Collection values) {
        BooleanExpression[] exps = new BooleanExpression[values.size()];
        int count = 0;
        for (Object value : values) {
            exps[count] = matchCollectionFieldAgainstValue(ElementMatch.EQUAL, field, value);
            count++;
        }

        switch (matcher) {
            case ALL_ELEMENTS:
                return BooleanExpression.allOf(exps);
            case ANY_ELEMENT:
            default:
                return BooleanExpression.anyOf(exps);
        }
    }

    public static BooleanExpression matchCollectionFieldAgainstValue(ElementMatch matcher, CollectionPathBase field, Object value) {
        switch (matcher) {
            case UNEQUAL:
                return field.contains(value).not();
            case EQUAL:
            default:
                return field.contains(value);
        }
    }

    public static BooleanExpression matchFieldAgainstValues(CollectionMatch matcher, ComparableExpressionBase field, Collection values) {
        BooleanExpression[] exps = new BooleanExpression[values.size()];
        int count = 0;
        for (Object value : values) {
            exps[count] = matchFieldAgainstValue(ElementMatch.EQUAL, field, value);
            count++;
        }


        switch (matcher) {
            case ALL_ELEMENTS:
                return BooleanExpression.allOf(exps);
            case ANY_ELEMENT:
            default:
                return BooleanExpression.anyOf(exps);
        }
    }

    public static BooleanExpression matchFieldAgainstValue(ElementMatch matcher, ComparableExpressionBase field, Object value) {
        switch (matcher) {
            case UNEQUAL:
                return field.ne(value);
            case EQUAL:
            default:
                return field.eq(value);
        }
    }

    public static BooleanExpression matchTextFieldAgainstText(TextMatch matcher, StringExpression field, String textValue) {
        BooleanExpression[] exps = null;
        if (matcher == TextMatch.ANY_WORD_IGNORE_CASE || matcher == TextMatch.ALL_WORDS_IGNORE_CASE) {
            textValue = textValue.replaceAll("\\s+", Constants.WORD_SEPARATOR_CHARACTER); // remove all double whitespaces
            String[] textWords = textValue.split(Constants.WORD_SEPARATOR_CHARACTER);
            exps = new BooleanExpression[textWords.length];
            int count = 0;
            for (String word : textWords) {
                exps[count] = matchTextFieldAgainstText(TextMatch.CONTAINS_PHRASE_IGNORE_CASE, field, word);
                count++;
            }
        }

        switch (matcher) {
            case ANY_WORD_IGNORE_CASE:
                return BooleanExpression.anyOf(exps);
            case ALL_WORDS_IGNORE_CASE:
                return BooleanExpression.allOf(exps);
            case EXACT_PHRASE_IGNORE_CASE:
                return field.equalsIgnoreCase(textValue);
            case CONTAINS_PHRASE_IGNORE_CASE:
            default:
                return field.containsIgnoreCase(textValue);
        }
    }

}
