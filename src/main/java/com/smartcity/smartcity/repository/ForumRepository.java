// ForumRepository.java
package com.smartcity.smartcity.repository;
import com.smartcity.smartcity.model.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ForumRepository extends JpaRepository<ForumPost, Long> {
    List<ForumPost> findByCategory(String category);
    List<ForumPost> findByParentPostIsNull();
}