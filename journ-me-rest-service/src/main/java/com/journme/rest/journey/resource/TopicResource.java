package com.journme.rest.journey.resource;

import com.journme.domain.Topic;
import com.journme.rest.common.resource.AbstractResource;
import com.journme.rest.journey.service.CategoryTopicService;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import java.util.List;

@Component
@Singleton
public class TopicResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopicResource.class);

    @Autowired
    private CategoryTopicService categoryTopicService;

    @GET
    public List<Topic> retrieveRelevantTopics(@NotBlank @QueryParam("category") String category) {
        LOGGER.info("Incoming call to retrieve relevant topics for category {}", category);
        return categoryTopicService.getTopicByCategory(categoryTopicService.toValidCategory(category)).getContent();
    }

}
