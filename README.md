# Medication Assistant API

A Spring Boot RESTful API that integrates with the [RxNorm API](https://lhncbc.nlm.nih.gov/RxNav/APIs/) and [OpenAI Chat API](https://platform.openai.com/docs/guides/gpt) to provide information about medications, including side effects, usage, and more.


-  **Search Medications** using the RxNorm API
-  **AI-Generated Descriptions** via OpenAI (ChatGPT)


- Java 17+
- Spring Boot 3.x
- Spring Web
- Spring Security with JWT
- OpenFeign for external APIs
- JUnit 5 & Mockito for testing
- Lombok for boilerplate reduction


## Update 06/16
- Added Spring Security JWT implementation just for practice.
- Now to call any endpoints you must create a user using user/signup endpoint or use the default user created in SecurityConfig

application.properties contains the following properties to store users in H2 Database
```
# Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Also, generate a 256-bit secret key from https://jwtsecrets.com/ and add the following to application.properties
(Or just use the one already in the application.properties, which I have pushed to the repo - not a production app, so
for convenience I have just pushed it).
```
medicationassistant.jwtsecret={YOUR_SECRET_KEY_HERE}
```

Call auth/login endpoint with valid credentials in request body to recieve a Bearer token to be used for other endpoints.
The bearer token will be located in the Authorization header with "Bearer " prefix.

I'll likely expand further in the future to incorporate some use cases for account creation (beyond just me wanting to practice with Spring Security)

## Walkthrough

Example:
api/v1/medication/search?query=Lipitor 

Returns the following list of valid medications from RxNorm:

![medassistant1](https://github.com/user-attachments/assets/77199625-1913-46f4-a48e-d9ae78a630b9)


The search `Lipitor` in then saved in the Database for the user sending the request. Any of the medications returned above can then be selected and passed to api/v1/information in the request body. 

Example:
```json
{
    "medicationName": "atorvastatin 80 MG Oral Tablet [Lipitor]",
    "medicationChatOption": "SIDE_EFFECTS"
}
```

`medicationChatOption` is an Enum with options: `GENERAL_INFORMATION`, `DOSAGE`, `SIDE_EFFECTS`

The request is then concatenated into a prompt and sent to OpenAI, which will return a response to the query.

If you want to clone the repository and test it, you will need a valid OpenAI API Key.

Unfortunately, OpenAI is not free. If you create a key but do not fund your account, you will recieve the following error:

![medassistant3](https://github.com/user-attachments/assets/b8846142-4fda-4810-a23e-d381c929a8fd)

I created a mock test for the getInformation() method to show the method works as intended if one does not have access to an API key for OpenAI:

![medassistant4](https://github.com/user-attachments/assets/c9c3a5ee-60bd-4b4e-ae2c-c8c1ba190c85)

All medications from the 'search' endpoint will return an rxcui, which is a unique ID for identfying
medications within the RxNorm API. Another endpoint has been recently added that will find related medications
given an rxcui. Here is an example of how to call this endpoint with an rxcui of 9801:


/api/v1/medication/related/9801
