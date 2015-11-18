package com.journme.rest.journey.service;

import com.journme.domain.Category;
import com.journme.domain.QTopic;
import com.journme.domain.Topic;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.contract.JournMeExceptionDto.ExceptionCode;
import com.journme.domain.repository.CategoryRepository;
import com.journme.domain.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import javax.ws.rs.core.Response.Status;
import java.util.*;
import java.util.stream.Collectors;

public class CategoryTopicService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TopicRepository topicRepository;

    private Set<String> categoriesCache;

    public Page<Topic> getTopicByCategory(String category) {
        return topicRepository.findAll(new PageRequest(0, 100, Direction.DESC,
                QTopic.topic.categoryWeight.getMetadata().getName() + "." + category));
    }

    public String toValidCategory(String inputCategory) {
        if (getSupportedCategories().contains(inputCategory)) {
            return inputCategory;
        } else {
            throw new JournMeException("Unsupported journey category " + inputCategory,
                    Status.BAD_REQUEST,
                    ExceptionCode.CLIENT_SERVER_PROBLEM);
        }
    }

    public Set<String> toValidCategory(Set<String> inputCategories) {
        Set<String> supportedCategories = getSupportedCategories();
        Set<String> validCategories = new HashSet<>(inputCategories.size());
        validCategories.addAll(inputCategories.stream().filter(supportedCategories::contains).collect(Collectors.toList()));

        if (validCategories.isEmpty()) {
            throw new JournMeException("Unsupported journey categories " + inputCategories,
                    Status.BAD_REQUEST,
                    ExceptionCode.CLIENT_SERVER_PROBLEM);
        } else {
            return inputCategories;
        }
    }

    public Set<String> toValidTopic(Set<String> inputTopics, Set<String> inputCategories) {
        //TODO: this can be improved by batching DB access rather than accessing during a loop
        for (String topicTag : inputTopics) {
            Topic topic = topicRepository.findByTag(topicTag);
            if (topic == null) {
                topic = new Topic(topicTag);
            }

            for (String categoryName : inputCategories) {
                Double weight = topic.getCategoryWeight().get(categoryName);
                weight = weight != null ? weight + 1.0D : 1.0D;
                topic.getCategoryWeight().put(categoryName, weight);
            }
            topic.incrementCount();
            topicRepository.save(topic);
        }

        return inputTopics;
    }

    private Set<String> getSupportedCategories() {
        if (categoriesCache == null) {
            List<Category> categoryList = categoryRepository.findAll();
            List<String> categoryStrList = new ArrayList<>(categoryList.size());

            categoryStrList.addAll(categoryList.stream().map(Category::getCode).collect(Collectors.toList()));
            categoriesCache = Collections.unmodifiableSet(new HashSet<>(categoryStrList));
        }
        return categoriesCache;
    }

}
