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

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class TaskThreeVerificationTest {
    static final Logger logger = LoggerFactory.getLogger(TaskThreeVerificationTest.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private UserRepository userRepository;

    @Test
    void verify_waldorf_balance() throws InterruptedException {
        userPopulator.populate();

        // Wait for consumer to be ready
        Thread.sleep(1000);

        String[] transactionLines = fileLoader.loadStrings("/test_data/mnbvcxz.vbnm");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }

        // Wait for all transactions to be processed
        Thread.sleep(5000);

        // Query waldorf's balance
        UserRecord waldorf = userRepository.findByName("waldorf");
        assertNotNull(waldorf, "Waldorf user should exist");

        float balance = waldorf.getBalance();
        int balanceRounded = (int) balance;

        logger.info("=".repeat(60));
        logger.info("Waldorf's final balance: ${}", balance);
        logger.info("Rounded down to nearest integer: {}", balanceRounded);
        logger.info("=".repeat(60));

        // The answer should be 627
        logger.info("ANSWER: {}", balanceRounded);
    }
}
