package com.journme.rest.alias.repository;

import com.journme.domain.AliasDetail;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
public interface AliasDetailRepository extends MongoRepository<AliasDetail, String> {
}
