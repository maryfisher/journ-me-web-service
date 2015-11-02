package com.journme.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
@Document(collection = "state")
public class State {

    @Id
    private String id;
}
