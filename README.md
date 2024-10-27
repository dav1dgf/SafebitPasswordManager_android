This is the Android Version of the password manager made by me. You can log into your account protected with a hashed password using BCrypt ([https://github.com/spring-projects/spring-security/blob/main/crypto/src/main/java/org/springframework/security/crypto/bcrypt/BCrypt.java](url)), and then using your key password the data inside the databse is decrypted deriving a cryptographic key from a password using the PBKDF2 (Password-Based Key Derivation Function 2) algorithm.

![Screenshot from 2024-10-27 18-29-24](https://github.com/user-attachments/assets/cfba313b-b8c1-446f-a6bb-9ded1ee0d958)

The software allows you to log into your account or create one if you do not have one, and then you can access your credentials, whhere ypu can add, delete and copy them.
