package com.journme.rest.alias.repository;

import com.journme.domain.AliasImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AliasImageRepository extends MongoRepository<AliasImage, String> {
}
