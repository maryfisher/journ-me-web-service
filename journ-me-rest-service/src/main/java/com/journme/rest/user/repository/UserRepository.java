package com.journme.rest.user.repository;

import com.journme.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * <h1>DAO repository class</h1>
 * DAO repository to access MongoDB data store of User via JPA/Querydsl.
 *
 * @author PHT
 * @version 1.0
 * @since 26.10.2015
 */
public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);

}
