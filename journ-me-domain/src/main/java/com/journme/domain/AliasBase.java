package com.journme.domain;

public class AliasBase extends AbstractAlias {

    public AliasBase clone(AbstractAlias other) {
        super.clone(other);
        copy(other);
        return this;
    }
}
