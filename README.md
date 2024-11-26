This is the Android Version of a password manager made by me. You can log into your account protected with a hashed password using BCrypt ([https://github.com/spring-projects/spring-security/blob/main/crypto/src/main/java/org/springframework/security/crypto/bcrypt/BCrypt.java](url)), and then using your key password the data inside the databse is decrypted deriving a cryptographic key from a password using the PBKDF2 (Password-Based Key Derivation Function 2) algorithm. With that keyyou decrypt the data stored inside the database, which is encrypted using AES-256.

![Screenshot from 2024-10-27 18-29-24](https://github.com/user-attachments/assets/b5fd7275-cadb-4efa-abf9-feceda8760af)


The software allows you to log into your account or create one if you do not have one, and then you can access your credentials, whhere ypu can add, delete and copy them.

Some screenshots of its functionality:

![register](https://github.com/user-attachments/assets/a96460ca-3959-424e-bf1f-64337f0087be)
![login](https://github.com/user-attachments/assets/6e2548be-cd28-4102-9c39-01c29e6be9c1)
![creed_detail](https://github.com/user-attachments/assets/c01d861b-199e-4243-b277-da47ae395c8e)
![cred_list](https://github.com/user-attachments/assets/f6ff1ce3-0d08-4389-92d9-407ab6860dea)
![add](https://github.com/user-attachments/assets/ad22accb-b48a-4cf4-9eaf-65bbeb404d8f)
