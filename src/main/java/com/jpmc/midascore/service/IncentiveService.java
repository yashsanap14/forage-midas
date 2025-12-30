package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IncentiveService {
    private static final Logger logger = LoggerFactory.getLogger(IncentiveService.class);

    private final RestTemplate restTemplate;
    private final String incentiveApiUrl;

    public IncentiveService(RestTemplate restTemplate,
            @Value("${incentive.api.url:http://localhost:8080/incentive}") String incentiveApiUrl) {
        this.restTemplate = restTemplate;
        this.incentiveApiUrl = incentiveApiUrl;
    }

    public float getIncentive(Transaction transaction) {
        try {
            logger.debug("Calling incentive API for transaction: {}", transaction);
            Incentive incentive = restTemplate.postForObject(incentiveApiUrl, transaction, Incentive.class);

            if (incentive != null) {
                logger.debug("Received incentive: {}", incentive.getAmount());
                return incentive.getAmount();
            }

            logger.warn("Received null incentive response");
            return 0;
        } catch (Exception e) {
            logger.error("Error calling incentive API: {}", e.getMessage());
            return 0;
        }
    }
}
