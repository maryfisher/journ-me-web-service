package com.journme.rest.alias.repository;

import com.journme.domain.Alias;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * <h1>DAO repository class</h1>
 * DAO repository to access MongoDB data store of Alias via JPA/Querydsl.
 *
 * @author PHT
 * @version 1.0
 * @since 26.10.2015
 */
public interface AliasRepository extends MongoRepository<Alias, String> {
}
