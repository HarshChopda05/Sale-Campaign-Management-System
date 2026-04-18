package com.example.salecampaign.Repository;

import com.example.salecampaign.Model.Campaign;
import com.example.salecampaign.Model.CampaignProduct;
import com.example.salecampaign.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CampaignProductRepository extends JpaRepository<CampaignProduct, Integer> {

    List<CampaignProduct> findByCampaign(Campaign campaign);

    List<CampaignProduct> findByProduct(Product product);

    @Query(
            value = """
                    SELECT cp.*
                            FROM campaign_product cp
                            JOIN campaign c ON cp.campaign_id = c.campaign_id
                            WHERE cp.product_id = :productId
                            AND c.start_date <= :date
                            AND c.end_date >= :date
                    """,
            nativeQuery = true
    )
    List<CampaignProduct> findActiveCampaignsByProduct(
            @Param("productId") Integer productId,
            @Param("date") LocalDate date
    );

}

//    @Query(
//            value = """
//        SELECT cp.* FROM campaign_product cp
//        JOIN campaign c ON cp.campaign_id = c.campaign_id
//        WHERE cp.product_id = :productId
//        AND c.start_date <= :date
//        AND c.end_date >= :date
//    """,
//            nativeQuery = true
//    )
//    List<CampaignProduct> findActiveCampaignsByProduct(
//            @Param("productId") Integer productId,
//            @Param("date") LocalDate date
//    );


