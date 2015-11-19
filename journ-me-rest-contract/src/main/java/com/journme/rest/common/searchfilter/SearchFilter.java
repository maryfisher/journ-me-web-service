package com.journme.rest.common.searchfilter;

public abstract class SearchFilter {

    public enum TextMatch {
        ANY_WORD_IGNORE_CASE,
        ALL_WORDS_IGNORE_CASE,
        CONTAINS_PHRASE_IGNORE_CASE,
        EXACT_PHRASE_IGNORE_CASE
    }

    public enum CollectionMatch {
        ANY_ELEMENT,
        ALL_ELEMENTS
    }

    public enum ElementMatch {
        EQUAL,
        UNEQUAL
    }

}
