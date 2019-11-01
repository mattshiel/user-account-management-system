# User Account Management System
A distributed user account management system built with Java, Maven, Google Protocol Buffers and gRPC. The system consists of a gRPC password service and a RESTful user account service.

## About 
**Name:** Matthew Shiel

**Student ID:** G00338622 

This application was created as part of my Distributed Systems module in GMIT. The code here is adapted from many sources, all referenced within the source code or in this README file.  

## Given Problem Statement 

> Storing user passwords in plain text is poor security practice. Best practice is to store a salted hash of the user’s password, along with the salt used to generate the hash. When a user attempts to authenticate with their password, a system can check if the password is valid by generating a hash of the password along with the salt, and comparing it with the
stored hash. See https://crackstation.net/hashing-security.htm for more  details on password hashing.
>
> In Part 1 you’ll develop a Password Service which will provide the password hashing and verification  services  described  above. Your  service  will  expose  a  gRPC  API  with  two methods:
>
>|  Method  |              Input             |                Output                |
>|:--------:|:------------------------------:|:------------------------------------:|
>| hash     | userId, password               | userId, hashedPassword, salt         |
>| validate | password, hashedPassword, salt | boolean indicating password validity |
>
>• hash: Used to generate a hash of a user’s password. Takes a password as input,returns the hash of the password, along with the salt used to generate the hash. Includes userId on input and output because we will be calling the method asyn-chronously in Part 2 and will need the userId on the asynchronous response.
>
>• validate: Used to validate a user-entered password by comparing it to the storedhash. Takes a password, a hashed password and a salt as input. Uses the salt tohash the input password and compares the resulting hash to the hashed password.
>
>The userId and password input parameters to the hash and validate methods should be of type integer and string respectively.  You are free to determine the other types as you see fit. Note that in Part 2 you’ll be storing the hashed password and salt returned from the hash method. The hash method includes userId on input and output because we will be calling the method asynchronously in Part 2 and will need the userId on thea synchronous response.
 
## Problem Simplified  
  * Create a Java Maven program to hash and verify user passwords
  * Expose a gRPC API with hash and validate methods
    
## How it works 
 
    1. Prompts user for ID and password for hashing
    2. Sends hash request and returns a hash response with the password hash and it's salt
    3. Prompts user for ID and password for validation
    4. Sends validation request and returns a validation response confirming password is correct or incorrect    
 
 
## Running the program without an executable jar
[Maven](https://maven.apache.org/) must be installed to build the executable jar. Follow official guidelines here. 
 
### Clone this repo 
```bash 
git clone https://github.com/mattshiel/user-account-management-system.git 
``` 
### Navigate to the folder 
```bash 
cd user-account-management-system 
``` 
### Build an executable jar 
```bash 
mvn install package
``` 
### Navigate to the folder containing the jar 
```bash 
cd target
``` 
### Run server
```bash 
java -jar password-service-0.1.0-shaded
``` 
## Test with client in a separate terminal
```bash 
java -cp password-service-0.1.0-shaded ie.gmit.ds.PasswordClient
``` 

## Running the program with an executable jar
Running the program if you already have the jar.

### Navigate to the location containing the jar 
```bash 
cd mylocation
``` 
### Run server
```bash 
java -jar password-service-0.1.0-shaded
``` 
### Test with client in a separate terminal
```bash 
java -cp password-service-0.1.0-shaded ie.gmit.ds.PasswordClient
``` 

### References

 - https://crackstation.net/hashing-security.htm
 - https://www.javamex.com/tutorials/cryptography/pbe_salt.shtml
 - https://github.com/john-french/distributed-systems-labs
 
 ### Additional Information
 * Github Issues was used to track and record milestones and progress on the project - see issues for more details :)
 * Part 2 will implement the RESTful user client
