// NewsRepository.java
package com.smartcity.smartcity.repository;
import com.smartcity.smartcity.model.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface NewsRepository extends JpaRepository<NewsArticle, Long> {
    List<NewsArticle> findByCategory(String category);
    List<NewsArticle> findByIsPremium(boolean isPremium);
}