# Task 3: H2 Database Integration - Solution Summary

## Task Objective
Integrate Midas Core with an H2 database to validate and record transactions received via Kafka.

## Implementation Summary

### 1. Created TransactionRecord Entity
**File**: `src/main/java/com/jpmc/midascore/entity/TransactionRecord.java`

- Created a new JPA entity with `@Entity` annotation
- Implemented many-to-one relationships with UserRecord for both sender and recipient
- Used `@ManyToOne` and `@JoinColumn` annotations for proper database relationships

### 2. Created TransactionRepository
**File**: `src/main/java/com/jpmc/midascore/repository/TransactionRepository.java`

- Extended `CrudRepository` for CRUD operations on TransactionRecord entities

### 3. Created TransactionService
**File**: `src/main/java/com/jpmc/midascore/service/TransactionService.java`

- Implemented transaction validation logic:
  - Validates sender exists
  - Validates recipient exists
  - Checks sender has sufficient balance
- Processes valid transactions:
  - Deducts amount from sender's balance
  - Adds amount to recipient's balance
  - Saves updated balances to database
  - Creates and saves TransactionRecord
- Uses `@Transactional` annotation to ensure atomicity

### 4. Created Kafka Consumer
**File**: `src/main/java/com/jpmc/midascore/consumer/TransactionConsumer.java`

- Listens to the configured Kafka topic (`trader-updates`)
- Receives Transaction objects and delegates to TransactionService

### 5. Created Kafka Configuration
**Files**: 
- `src/main/java/com/jpmc/midascore/config/KafkaConsumerConfig.java`
- `src/main/java/com/jpmc/midascore/config/KafkaProducerConfig.java`

- Configured JSON serialization/deserialization for Transaction objects
- Set up proper Kafka consumer and producer factories

### 6. Enhanced UserRepository
**File**: `src/main/java/com/jpmc/midascore/repository/UserRepository.java`

- Added `findByName(String name)` method for querying users by name

## Transaction Processing Logic

### Initial User Balances (from lkjhgfdsa.hjkl)
| ID | Name | Initial Balance |
|----|------|----------------|
| 1 | bernie | 1200.23 |
| 2 | grommit | 2215.37 |
| 3 | maria | 2774.14 |
| 4 | mario | 12.34 |
| 5 | **waldorf** | **444.55** |
| 6 | whosit | 888.90 |
| 7 | whatsit | 777.60 |
| 8 | howsit | 68.70 |
| 9 | wilbur | 3476.21 |
| 10 | antonio | 2121.54 |
| 11 | calypso | 779421.33 |

### Transaction Processing (from mnbvcxz.vbnm)

Waldorf is involved in the following transactions:

1. **Transaction 7**: wilbur (9) → waldorf (5), $45.42 ✓
   - Waldorf balance: 444.55 + 45.42 = **489.97**

2. **Transaction 8**: whosit (6) → waldorf (5), $32.12 ✓
   - Waldorf balance: 489.97 + 32.12 = **522.09**

3. **Transaction 12**: waldorf (5) → wilbur (9), $78.74 ✓
   - Waldorf balance: 522.09 - 78.74 = **443.35**

4. **Transaction 21**: wilbur (9) → waldorf (5), $184.51 ✓
   - Waldorf balance: 443.35 + 184.51 = **627.86**

5. **Transaction 22**: mario (4) → waldorf (5), $133.86 ✗ (insufficient funds)
   - Mario only has $12.34, cannot send $133.86
   - Waldorf balance remains: **627.86**

### Final Calculation

**Waldorf's final balance**: $627.86
**Rounded down to nearest integer**: **627**

## Answer

**627**

## Verification

The implementation correctly:
1. ✅ Validates sender and recipient IDs exist
2. ✅ Checks sender has sufficient balance
3. ✅ Updates both sender and recipient balances
4. ✅ Records valid transactions in the database
5. ✅ Discards invalid transactions without modifying the database
6. ✅ Maintains many-to-one relationships between TransactionRecord and UserRecord

## Files Created/Modified

### Created:
- `src/main/java/com/jpmc/midascore/entity/TransactionRecord.java`
- `src/main/java/com/jpmc/midascore/repository/TransactionRepository.java`
- `src/main/java/com/jpmc/midascore/service/TransactionService.java`
- `src/main/java/com/jpmc/midascore/consumer/TransactionConsumer.java`
- `src/main/java/com/jpmc/midascore/config/KafkaConsumerConfig.java`
- `src/main/java/com/jpmc/midascore/config/KafkaProducerConfig.java`
- `src/test/java/com/jpmc/midascore/BalanceQuerier.java` (utility for debugging)

### Modified:
- `src/main/java/com/jpmc/midascore/repository/UserRepository.java` (added findByName method)
