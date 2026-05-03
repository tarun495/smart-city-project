package com.smartcity.smartcity.service;

import com.smartcity.smartcity.model.NewsArticle;
import com.smartcity.smartcity.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    public List<NewsArticle> getAllNews() {
        return newsRepository.findAll();
    }

    public List<NewsArticle> getNewsByCategory(String category) {
        return newsRepository.findByCategory(category);
    }

    public List<NewsArticle> getPremiumArticles() {
        return newsRepository.findByIsPremium(true);
    }

    public List<NewsArticle> getFreeArticles() {
        return newsRepository.findByIsPremium(false);
    }

    public NewsArticle saveArticle(NewsArticle article) {
        article.setPublishedAt(LocalDateTime.now());
        return newsRepository.save(article);
    }

    public NewsArticle getById(Long id) {
        return newsRepository.findById(id).orElse(null);
    }

    public void deleteArticle(Long id) {
        newsRepository.deleteById(id);
    }
}