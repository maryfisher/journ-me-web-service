// package-info.java indicates to Querydsl, which classpath classes to auto-generate Q classes for
// Note that all entities need to be annotated with org.mongodb.morphia.annotations.Entity, which can
// be simply achieved by annotating com.journme.domain.AbstractEntity (from which all are subclassing)
@QueryEntities(value = {
        AbstractIdEntity.class,
        AbstractEntity.class,
        AbstractEntity.AbstractImageEntity.class,
        AliasBase.class,
        AliasDetail.class,
        AliasImage.class,
        Blink.class,
        BlinkImage.class,
        Category.class,
        Feedback.class,
        JourneyBase.class,
        JourneyDetails.class,
        MomentBase.class,
        MomentDetail.class,
        MomentImage.class,
        State.class,
        User.class
})
package com.journme.rest.config;

import com.journme.domain.*;
import com.mysema.query.annotations.QueryEntities;
//import com.querydsl.core.annotations.QueryEntities; //Querydsl 4 namespaces