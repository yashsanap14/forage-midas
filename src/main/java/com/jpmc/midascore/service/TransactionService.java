package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final IncentiveService incentiveService;

    public TransactionService(UserRepository userRepository,
            TransactionRepository transactionRepository,
            IncentiveService incentiveService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.incentiveService = incentiveService;
    }

    @Transactional
    public void processTransaction(Transaction transaction) {
        logger.debug("Processing transaction: {}", transaction);

        // Validate sender exists
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        if (sender == null) {
            logger.warn("Invalid sender ID: {}", transaction.getSenderId());
            return;
        }

        // Validate recipient exists
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());
        if (recipient == null) {
            logger.warn("Invalid recipient ID: {}", transaction.getRecipientId());
            return;
        }

        // Validate sender has sufficient balance
        if (sender.getBalance() < transaction.getAmount()) {
            logger.warn("Insufficient balance for sender {}: balance={}, amount={}",
                    sender.getName(), sender.getBalance(), transaction.getAmount());
            return;
        }

        // Get incentive from API
        float incentive = incentiveService.getIncentive(transaction);
        logger.info("Incentive for transaction: {}", incentive);

        // Process the transaction
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        // Add both transaction amount AND incentive to recipient (incentive is not
        // deducted from sender)
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentive);

        // Save updated balances
        userRepository.save(sender);
        userRepository.save(recipient);

        // Create and save transaction record with incentive
        TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, transaction.getAmount(),
                incentive);
        transactionRepository.save(transactionRecord);

        logger.info("Transaction processed successfully: {} -> {}, amount: {}, incentive: {}",
                sender.getName(), recipient.getName(), transaction.getAmount(), incentive);
    }
}
