package com.example.salecampaign.Repository;

import com.example.salecampaign.Model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Integer> {

    @Query(value = "SELECT * FROM campaign WHERE start_date = :date",nativeQuery = true)
    List<Campaign> findCampaignsStartingToday(LocalDate date);

    @Query(value = "SELECT * FROM campaign WHERE end_date = :date", nativeQuery = true)
    List<Campaign> findCampaignsEndingToday(LocalDate date);

    @Query(value = "SELECT * FROM campaign WHERE start_date <= :date AND end_date >= :date", nativeQuery = true)
    List<Campaign> findActiveCampaigns(LocalDate date);

    Campaign[] findByStatus(String status); //

    @Query(value = """
    SELECT c.campaign_id, c.campaign_name, c.start_date, c.end_date,
                    CASE
                        WHEN CURRENT_DATE BETWEEN c.start_date AND c.end_date THEN 'CURRENT'
                        WHEN CURRENT_DATE < c.start_date THEN 'UPCOMING'
                        ELSE 'EXPIRED'
                    END AS status
                FROM campaign c
""", nativeQuery = true)
    List<Object[]> getAllCampaigns();



}
