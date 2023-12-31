# Spring Boot Auth Server

## Overview

The **Spring Boot Auth Server** is a Spring Boot application that handles user security by managing authentication and authorization processes. The server ensures secure access to protected resources. It's built using Java 17 and incorporates state-of-the-art security protocols like OAuth2 and OpenID Connect, offering a robust and secure environment for user management.

## Features

- OAuth2 Authorization Flow and OpenID Connect 1.0
- YAML configuration for easy customization
- Java 17 for the latest language features

### Prerequisites

Ensure that you have the following installed on your system:

- JDK 17 or later
- Maven 3.8.1 or later

### Building the project

To build the project, navigate to the project root directory and run the following command:

```bash
mvn clean install
```

This will compile the project, run tests, and package it as a JAR file.

### Running the project

This application can be run with various profiles to adapt to different environments and authentication methods. The available profiles are:

- **Environment Profiles**: These profiles control the general behavior and configuration of the application for different environments.
    - `local`, `test`, `prod`: Define the environment in which the application is running. These profiles configure settings such as the port number, logging levels, and more.

- **Authentication Profiles**: These profiles determine the authentication method used by the application.
    - `IN-MEMORY`, `LDAP`: Define the authentication method used by the application. These profiles configure settings such as the authentication server's URL, client ID, client secret, and more.

To run the project, execute the following command from the project root directory:
```bash
java -jar target/auth-server-0.0.1-SNAPSHOT.jar --spring.profiles.active={environment_profile},{authentication_profile}
```

Upon execution, the `auth-server` will start and be ready to handle authentication and authorization requests.

## Configuration

The project uses the `application.yml` file for its configuration settings, allowing for easy management and updates of the application's configuration.


## Authentication Options

### In-Memory Authentication
The In-Memory authentication profile is convenient for local testing and development. Users are pre-defined within the application, and it does not require any external authentication server:

- Username: `user`, Password: `user-password`, Roles: `USER`
- Username: `admin`, Password: `admin-password`, Roles: `ADMIN`
- To activate this profile, run with the following parameters:
```bash
--spring.profiles.active={environment_profile},IN-MEMORY
```

### LDAP Authentication
The LDAP authentication profile provides a way to authenticate users against an LDAP server, suitable for local testing and development with real-world-like scenarios. The "test-server.ldif" file includes the configuration and sample users for a test LDAP server:

- Username: `ldap-user`, Password: `ldap-user-password`
- To activate this profile, run with the following parameters:
```bash 
--spring.profiles.active={environment_profile},LDAP
```

For local testing, ensure you have the "test-server.ldif" file correctly set up. It contains the definitions for the test users and their corresponding passwords.

## OAuth2

### Proof Key for Code Exchange (PKCE)

Proof Key for Code Exchange (PKCE) is an OAuth 2.0 extension designed to enhance security for the authorization process in public clients, preventing the interception of authorization codes by malicious entities. It finds particular value in Single Page Applications (SPAs) and mobile applications.

To employ PKCE, the client must generate a unique **code_verifier** and a corresponding **code_challenge** for every authorization request. This generation is a part of the Authorization Code flow.

Here are some example values:

- **Example code_verifier**: "tClcOwZdqiPHHuMo0CyxMked9r1NJ_5_BicA0FI1Q0E"
- **Example code_challenge**: "Q0J7-pF3nmAP3XrTmUt6DEFL9vKG9_1V12fXXMTVIqk"
- **Example code_challenge_method**: "S256"

In the above example, the **code_challenge** is a Base64-URL-encoded string that results from a SHA256 hash operation performed on the **code_verifier**. The **code_challenge_method** should always be "S256".

Include these parameters when directing the user to the authorization URL and when exchanging the authorization code for an access token. It's crucial to remember that these values must be regenerated for each new authorization request to maintain a high level of security.

### Authorization Flow

1.  **Authorization Request URL:** The client application initiates the authorization request to the Authorization Server.

    ```url
    http://127.0.0.1:5500/oauth2/authorize?response_type=code&client_id=web-client-id&scope=openid&redirect_uri=http://127.0.0.1:4200/login/oauth2/code/web-client-oidc&code_challenge={code_challenge}&code_challenge_method={code_challenge_method}
    ```

2.  **Login URL:** The user is prompted to enter their login credentials.

    ```url
    http://127.0.0.1:5500/login
    ```

3.  **Authorization Grant URL:** After successful login, the user is redirected to this URL to give consent to the client application to access their protected resources.

    ```url
    http://127.0.0.1:5500/oauth2/authorize?response_type=code&client_id=web-client&scope=openid&redirect_uri=http://127.0.0.1:4200/login/oauth2/code/web-client-oidc&code_challenge={code_challenge}&code_challenge_method={code_challenge_method}&continue
    ```

4.  **Authorization Code URL:** Post user's consent, they are redirected to this URL equipped with a one-time authorization code. This URL will typically correspond to a route within your application, such as a Single Page Application (SPA), where you handle the authorization code.

    ```url
    http://127.0.0.1:4200/login/oauth2/code/web-client-oidc?code={ONE-TIME-CODE}
    ```

5.  **Exchange Authorization Code for Access Token:** The client application uses the following cURL command to exchange the authorization code for an access token.

    ```bash
    curl --location --request POST 'http://127.0.0.1:5500/oauth2/token' \
    --header 'Authorization: Basic d2ViLWNsaWVudDpjbGllbnQtc2VjcmV0LTE=' \
    --header 'Content-Type: application/x-www-form-urlencoded' \
    --data-urlencode 'code={one time code associated with the authentication event}' \
    --data-urlencode 'grant_type=authorization_code' \
    --data-urlencode 'redirect_uri=http://127.0.0.1:4200/login/oauth2/code/web-client-oidc' \
    --data-urlencode 'code_verifier={code_verifier}'
    ```

## Contributing

Please feel free to submit issues, fork the repository, and send pull requests to contribute to the project.

## License
This project is licensed under the terms of the MIT License.