// BusinessRepository.java
package com.smartcity.smartcity.repository;
import com.smartcity.smartcity.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface BusinessRepository extends JpaRepository<Business, Long> {
    List<Business> findByCategory(String category);
    List<Business> findByIsFeatured(boolean isFeatured);
}