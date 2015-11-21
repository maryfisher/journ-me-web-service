package com.journme.rest.journey.service;

import com.journme.domain.Category;
import com.journme.domain.JourneyBase.CategoryWeight;
import com.journme.domain.QTopic;
import com.journme.domain.Topic;
import com.journme.domain.repository.CategoryRepository;
import com.journme.domain.repository.TopicRepository;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.common.util.Constants;
import com.journme.rest.contract.JournMeExceptionDto.ExceptionCode;
import com.mysema.query.types.expr.BooleanExpression;
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

    public Page<Topic> getTopicByTagFragment(String tagFragment) {
        BooleanExpression predicate = QTopic.topic.tag.containsIgnoreCase(tagFragment);
        return topicRepository.findAll(predicate, new PageRequest(0, 100, Direction.DESC,
                QTopic.topic.count.getMetadata().getName()));
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

    public Set<CategoryWeight> toValidCategoryWeight(Set<CategoryWeight> categoryWeights) {
        Set<String> supportedCategories = getSupportedCategories();
        Set<CategoryWeight> validCategoryWeights = new HashSet<>(categoryWeights.size());
        validCategoryWeights.addAll(categoryWeights.stream().filter(categoryWeight -> supportedCategories.contains(categoryWeight.getCategory())).collect(Collectors.toList()));

//        if (validCategoryWeights.isEmpty()) {
//            throw new JournMeException("Unsupported journey categories " + categoryWeights,
//                    Status.BAD_REQUEST,
//                    ExceptionCode.CLIENT_SERVER_PROBLEM);
//        } else {
        return validCategoryWeights;
//        }
    }

    public Set<String> toValidTopic(Set<String> inputTopics, Set<CategoryWeight> categoryWeights) {
        //TODO: this can be improved by batching DB access rather than accessing during a loop
        for (String rawTopicTag : inputTopics) {
            String topicTag = rawTopicTag.trim().replaceAll("\\s+", Constants.WORD_SEPARATOR_CHARACTER);
            Topic topic = topicRepository.findByTag(topicTag);
            if (topic == null) {
                topic = new Topic(topicTag);
            }

            for (CategoryWeight categoryWeight : categoryWeights) {
                String categoryName = categoryWeight.getCategory();
                Double weight = topic.getCategoryWeight().get(categoryName);
                weight = weight != null ? (weight + categoryWeight.getWeight() / 100.0D) : 1.0D;
                topic.getCategoryWeight().put(categoryName, weight);
            }
            topic.incrementCount();
            topicRepository.save(topic);
        }

        return inputTopics;
    }

    private Set<String> getSupportedCategories() {
        // TODO: potential to cache the list of categories for a certain amount of time (since Admins won't change categories that often)
        if (categoriesCache == null) {
            List<Category> categoryList = categoryRepository.findAll();
            List<String> categoryStrList = new ArrayList<>(categoryList.size());

            categoryStrList.addAll(categoryList.stream().map(Category::getCode).collect(Collectors.toList()));
            categoriesCache = Collections.unmodifiableSet(new HashSet<>(categoryStrList));
        }
        return categoriesCache;
    }

}
