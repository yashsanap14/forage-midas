package com.jpmc.midascore;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class TaskFourVerificationTest {
    static final Logger logger = LoggerFactory.getLogger(TaskFourVerificationTest.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private UserRepository userRepository;

    @Test
    void verify_wilbur_balance() throws InterruptedException {
        userPopulator.populate();

        // Wait for consumer to be ready
        Thread.sleep(2000);

        String[] transactionLines = fileLoader.loadStrings("/test_data/alskdjfh.fhdjsk");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }

        // Wait for all transactions to be processed
        Thread.sleep(8000);

        // Query wilbur's balance
        UserRecord wilbur = userRepository.findByName("wilbur");
        assertNotNull(wilbur, "Wilbur user should exist");

        float balance = wilbur.getBalance();
        int balanceRounded = (int) balance;

        logger.info("=".repeat(60));
        logger.info("Wilbur's final balance: ${}", balance);
        logger.info("Rounded down to nearest integer: {}", balanceRounded);
        logger.info("=".repeat(60));

        logger.info("ANSWER: {}", balanceRounded);
    }
}
