package com.journme.rest.journey.repository;

import com.journme.domain.Journey;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * <h1>DAO repository class</h1>
 * DAO repository to access MongoDB data store of Journeys via JPA/Querydsl.
 *
 * @author PHT
 * @version 1.0
 * @since 20.10.2015
 */
public interface JourneyRepository extends MongoRepository<Journey, String> {

    List<Journey> findByName(String name);

}
