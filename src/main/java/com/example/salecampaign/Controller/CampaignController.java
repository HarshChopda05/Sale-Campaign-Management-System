package com.example.salecampaign.Controller;

import com.example.salecampaign.DTOs.CampaignAddRequestDTO;
import com.example.salecampaign.DTOs.CampaignDTO;
import com.example.salecampaign.Service.CampaignScheduler;
import com.example.salecampaign.Service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaigns")
public class CampaignController {

    @Autowired
    CampaignService campaignService;
    @Autowired
    CampaignScheduler campaignScheduler;

    @PostMapping("/add-campaign")
    public ResponseEntity<?> createCampaign(@RequestBody CampaignAddRequestDTO addCompaignRequest){
        campaignService.addCampaign(addCompaignRequest);
        return new ResponseEntity<>("Campaign is Created.", HttpStatus.CREATED);
    }

    @GetMapping("/run-scheduler")
    public ResponseEntity<?> runSchedulerNow() {
        campaignScheduler.runScheduler();
        return new ResponseEntity<>("Scheduler executed", HttpStatus.OK);
    }

    @GetMapping("/get-all-campaigns")
    public ResponseEntity<List<CampaignDTO>> getCampaigns() {
        return new ResponseEntity<>(campaignService.getCampaigns(), HttpStatus.OK);
    }
}
