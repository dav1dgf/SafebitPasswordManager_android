# Password Manager - Android Version

This is the Android version of a password manager created by me.

## Features
- **Secure Login**: Accounts are protected with a hashed password using [BCrypt](https://github.com/spring-projects/spring-security/blob/main/crypto/src/main/java/org/springframework/security/crypto/bcrypt/BCrypt.java).
- **Encryption**: Credentials stored in the database are encrypted using **AES-256**.
- **Decryption**: Data is decrypted using a key derived from your **key password** via the **PBKDF2 (Password-Based Key Derivation Function 2)** algorithm.
- **Credential Management**:
  - Add new credentials.
  - Delete existing credentials.
  - Copy stored credentials to the clipboard.

## Usage
1. Log into your account or create one if you don’t have an existing account.
2. Access your credentials, add new ones, delete them, or copy them when needed.

## Screenshots
Some screenshots of its functionality:

<div style="display: flex; gap: 20px; margin-bottom: 20px;">
  <img src="https://github.com/user-attachments/assets/a96460ca-3959-424e-bf1f-64337f0087be" alt="register" width="400">
  <img src="https://github.com/user-attachments/assets/6e2548be-cd28-4102-9c39-01c29e6be9c1" alt="login" width="400">
</div>

<div style="display: flex; gap: 20px; margin-bottom: 20px;">
  <img src="https://github.com/user-attachments/assets/c01d861b-199e-4243-b277-da47ae395c8e" alt="creed_detail" width="400">
  <img src="https://github.com/user-attachments/assets/f6ff1ce3-0d08-4389-92d9-407ab6860dea" alt="cred_list" width="400">
</div>

<div style="margin-bottom: 20px;">
  <img src="https://github.com/user-attachments/assets/ad22accb-b48a-4cf4-9eaf-65bbeb404d8f" alt="add" width="400">
</div>
