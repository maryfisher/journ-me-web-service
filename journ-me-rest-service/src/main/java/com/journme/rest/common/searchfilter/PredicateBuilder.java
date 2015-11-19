package com.journme.rest.common.searchfilter;

import com.journme.domain.QJourneyDetails;
import com.journme.rest.common.searchfilter.SearchFilter.CollectionMatch;
import com.journme.rest.common.searchfilter.SearchFilter.ElementMatch;
import com.journme.rest.common.searchfilter.SearchFilter.TextMatch;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpressionBase;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.path.CollectionPathBase;

import java.util.Collection;

public abstract class PredicateBuilder {

    private static final String WORD_SEPARATOR_CHARACTER = " ";

    private PredicateBuilder() {
    }

    public static Predicate fromSearchFilter(JourneySearchFilter sf) {
        BooleanBuilder predicate = new BooleanBuilder();
        QJourneyDetails qJourney = QJourneyDetails.journeyDetails;

        if (sf.text != null && sf.textMatcher != null) {
            predicate.and(
                    matchTextFieldAgainstText(sf.textMatcher, qJourney.name, sf.text)
                            .or(matchTextFieldAgainstText(sf.textMatcher, qJourney.descript, sf.text))
            );
        }
        if (sf.join != null && sf.joinMatcher != null) {
            predicate.and(matchFieldAgainstValue(sf.joinMatcher, qJourney.join, sf.join));
        }
        if (sf.categories != null && sf.categoriesMatcher != null) {
            predicate.and(matchFieldAgainstValues(sf.categoriesMatcher, qJourney.categoryWeights.any().category, sf.categories));
        }
        if (sf.topics != null && sf.topicsMatcher != null) {
            predicate.and(matchCollectionFieldAgainstValues(sf.topicsMatcher, qJourney.topics, sf.topics));
        }

        return predicate;
    }

    private static BooleanExpression matchCollectionFieldAgainstValues(CollectionMatch matcher, CollectionPathBase field, Collection values) {
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

    private static BooleanExpression matchCollectionFieldAgainstValue(ElementMatch matcher, CollectionPathBase field, Object value) {
        switch (matcher) {
            case UNEQUAL:
                return field.contains(value).not();
            case EQUAL:
            default:
                return field.contains(value);
        }
    }

    private static BooleanExpression matchFieldAgainstValues(CollectionMatch matcher, ComparableExpressionBase field, Collection values) {
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

    private static BooleanExpression matchFieldAgainstValue(ElementMatch matcher, ComparableExpressionBase field, Object value) {
        switch (matcher) {
            case UNEQUAL:
                return field.ne(value);
            case EQUAL:
            default:
                return field.eq(value);
        }
    }

    private static BooleanExpression matchTextFieldAgainstText(TextMatch matcher, StringExpression field, String textValue) {
        BooleanExpression[] exps = null;
        if (matcher == TextMatch.ANY_WORD_IGNORE_CASE || matcher == TextMatch.ALL_WORDS_IGNORE_CASE) {
            textValue = textValue.replaceAll("\\s+", WORD_SEPARATOR_CHARACTER); // remove all double whitespaces
            String[] textWords = textValue.split(WORD_SEPARATOR_CHARACTER);
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
