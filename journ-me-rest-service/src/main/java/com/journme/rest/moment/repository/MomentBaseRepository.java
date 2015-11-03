package com.journme.rest.moment.repository;

import com.journme.domain.MomentBase;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
public interface MomentBaseRepository extends MongoRepository<MomentBase, String> {
}