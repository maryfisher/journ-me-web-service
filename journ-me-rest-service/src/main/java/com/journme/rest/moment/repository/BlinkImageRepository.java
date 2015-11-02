package com.journme.rest.moment.repository;

import com.journme.domain.BlinkImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlinkImageRepository extends MongoRepository<BlinkImage, String> {
}
