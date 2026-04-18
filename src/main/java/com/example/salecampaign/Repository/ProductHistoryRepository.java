package com.example.salecampaign.Repository;

import com.example.salecampaign.Model.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Integer> {

    @Query(value = """
            SELECT
                ph.product_id,
                ph.price,
                ph.date
            FROM product_history ph
            ORDER BY ph.product_id, ph.date DESC;
            """, nativeQuery = true)
    List<Object[]> getProductHistory();

}
