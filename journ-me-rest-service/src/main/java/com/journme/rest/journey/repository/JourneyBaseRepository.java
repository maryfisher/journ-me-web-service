package com.journme.rest.journey.repository;

import com.journme.domain.JourneyBase;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * <h1>DAO repository class</h1>
 * DAO repository to access MongoDB data store of Journey base via JPA/Querydsl.
 *
 * @author PHT
 * @version 1.0
 * @since 24.10.2015
 */
public interface JourneyBaseRepository extends MongoRepository<JourneyBase, String> {

}
