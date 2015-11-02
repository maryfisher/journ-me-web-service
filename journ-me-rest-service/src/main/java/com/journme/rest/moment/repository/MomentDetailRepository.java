package com.journme.rest.moment.repository;

import com.journme.domain.MomentDetail;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
public interface MomentDetailRepository extends MongoRepository<MomentDetail, String> {
}
