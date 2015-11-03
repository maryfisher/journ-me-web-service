package com.journme.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "aliasImage")
public class AliasImage extends BaseEntity.BaseImageEntity {
}
