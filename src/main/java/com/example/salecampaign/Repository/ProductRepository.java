package com.example.salecampaign.Repository;

import com.example.salecampaign.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = """
                SELECT DISTINCT cp.product FROM CampaignProduct cp
                WHERE cp.campaign.startDate = :date
                   OR cp.campaign.endDate = :date
            """, nativeQuery = true)
    List<Product> findProductsToUpdate(LocalDate date);

    @Query(value = """
        SELECT p.product_id, p.product_name, p.mrp, p.mrp - (p.mrp * 	COALESCE(MAX(cp.discount), 0) / 100) AS final_price,
                	COALESCE(MAX(cp.discount), 0) AS discount, p.inventory
                	FROM product p
                	
                LEFT JOIN campaign_product cp
                    ON p.product_id = cp.product_id

                LEFT JOIN campaign c
                    ON cp.campaign_id = c.campaign_id
                    AND c.status = 'CURRENT'

                GROUP BY p.product_id, p.product_name, p.mrp, p.inventory;
    """, nativeQuery = true)
    List<Object[]> getProductsWithDiscount();


    @Override
    Page<Product> findAll(Pageable pageable); //For pageable
}
