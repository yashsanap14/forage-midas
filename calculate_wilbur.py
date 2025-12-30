#!/usr/bin/env python3
import requests
import json

# User data (from lkjhgfdsa.hjkl)
users = {
    1: {"name": "bernie", "balance": 1200.23},
    2: {"name": "grommit", "balance": 2215.37},
    3: {"name": "maria", "balance": 2774.14},
    4: {"name": "mario", "balance": 12.34},
    5: {"name": "waldorf", "balance": 444.55},
    6: {"name": "whosit", "balance": 888.90},
    7: {"name": "whatsit", "balance": 777.60},
    8: {"name": "howsit", "balance": 68.70},
    9: {"name": "wilbur", "balance": 3476.21},
    10: {"name": "antonio", "balance": 2121.54},
    11: {"name": "calypso", "balance": 779421.33}
}

# Transactions (from alskdjfh.fhdjsk)
transactions = [
    (9, 10, 16),
    (4, 2, 166.75),
    (9, 5, 8),
    (6, 7, 63.55),
    (2, 6, 99.56),
    (8, 3, 108.1),
    (5, 1, 49.56),
    (8, 10, 33.39),
    (10, 8, 133.65),
    (3, 10, 105.96),
    (10, 5, 154.10),
    (5, 6, 75.67),
    (1, 5, 1.98),
    (6, 7, 112.43),
    (9, 1, 130.37),
    (7, 10, 197.5),
    (1, 7, 6.83),
    (9, 7, 128.47),
    (5, 6, 47.40),
    (9, 6, 103.95),
    (6, 5, 20.58),
    (8, 3, 168.57)
]

API_URL = "http://localhost:8080/incentive"

print("Processing transactions with incentive API...")
print("="*70)

for i, (sender_id, recipient_id, amount) in enumerate(transactions, 1):
    sender = users[sender_id]
    recipient = users[recipient_id]
    
    # Check if transaction is valid
    if sender["balance"] >= amount:
        # Call incentive API
        try:
            response = requests.post(API_URL, json={
                "senderId": sender_id,
                "recipientId": recipient_id,
                "amount": amount
            })
            incentive_data = response.json()
            incentive = incentive_data.get("amount", 0.0)
        except Exception as e:
            print(f"Error calling API: {e}")
            incentive = 0.0
        
        # Process transaction
        sender["balance"] -= amount
        recipient["balance"] += amount + incentive  # Add incentive to recipient
        
        print(f"Transaction {i:2d}: {sender['name']:10s} -> {recipient['name']:10s}, "
              f"${amount:7.2f}, incentive: ${incentive:7.2f} ✓")
        
        if recipient['name'] == 'wilbur':
            print(f"              Wilbur's balance: ${recipient['balance']:.2f}")
    else:
        print(f"Transaction {i:2d}: {sender['name']:10s} -> {recipient['name']:10s}, "
              f"${amount:7.2f} ✗ (insufficient funds)")

print("\n" + "="*70)
print("Final balances:")
print("="*70)
for user_id, user in users.items():
    marker = " <-- WILBUR" if user['name'] == 'wilbur' else ""
    print(f"{user_id:2d}. {user['name']:10s}: ${user['balance']:10.2f}{marker}")

print("\n" + "="*70)
print(f"Wilbur's final balance: ${users[9]['balance']:.2f}")
print(f"Rounded down to nearest integer: {int(users[9]['balance'])}")
print("="*70)
