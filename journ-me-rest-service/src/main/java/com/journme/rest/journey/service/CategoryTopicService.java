package com.journme.rest.journey.service;

import com.journme.domain.Category;
import com.journme.domain.Topic;
import com.journme.domain.repository.CategoryRepository;
import com.journme.domain.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class CategoryTopicService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TopicRepository topicRepository;

    private Set<String> categoriesCache;

    public Set<String> toValidCategory(Set<String> inputCategories) {
        Set<String> supportedCategories = getSupportedCategories();
        Iterator<String> iterator = inputCategories.iterator();
        while (iterator.hasNext()) {
            if (!supportedCategories.contains(iterator.next())) {
                iterator.remove();
            }
        }
        return inputCategories;
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
