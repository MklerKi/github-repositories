# Github Repositories

This project provides an API endpoint for retrieving GitHub repositories for a given user. It allows consumers to list all repositories that are not forks and includes information about each repository's name, owner login, and branches with their respective last commit SHA.

## Table of Contents

- [Introduction](#introduction)
- [Technologies](#technologies)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Introduction

The GitHub Repositories API allows consumers to retrieve information about GitHub repositories for a specific user. It provides a simple and convenient way to access repository data using a RESTful API.

## Technologies

List the technologies and frameworks used in the project:

- Java
- Spring Boot
- JSON
- Lombok
- Mockito
- Junit
- SLF4J (Simple Logging Facade for Java)

## Getting Started

To get started with the GitHub Repositories API, follow these steps:

### Prerequisites
- JDK (Java Development Kit) installed on your machine.
- Maven or Gradle installed to manage dependencies.

### Clone the Repository
Clone the GitHub Repositories API repository to your local machine using Git:

```bash
git clone https://github.com/MklerKi/github-repositories.git
```

### Build the Application
Navigate to the project directory and build the application using Maven:

```bash
cd github-repositories
mvn clean install
```

### Run the Application
Once the application is built successfully, you can run it using Maven or your preferred IDE:

```bash
mvn spring-boot:run
```

### Verify the Application
Once the application is running, you can access the API endpoints using tools like cURL, Postman, or your web browser. For example:
```bash
curl -X GET http://localhost:8080/api/v1/getAllForksForUser/{username} -H "Accept: application/json"
```


## Usage

### Getting All Forks for a User
To retrieve all non-forked repositories for a GitHub user, send a GET request to the following endpoint:

```bash
GET /api/v1/getAllForksForUser/{username}
```
Replace {username} with the GitHub username of the user whose repositories you want to retrieve.

#### Example Request
```bash
GET /api/v1/getAllForksForUser/johndoe
```
#### Example Response
```bash
[
    {
        "name": "game_robot_unicorn_attac_for_shool",
        "owner": "MklerKi",
        "brunches": [
            {
                "name": "main",
                "sha": "01865e7940524c02988a2392020090d9de696011"
            }
        ]
    },
    {
        "name": "small_world_Java",
        "owner": "MklerKi",
        "brunches": [
            {
                "name": "main",
                "sha": "8c892d8ce08bbd8ec2d5727b1a1b41f474504a8a"
            }
        ]
    },
    {
        "name": "web_app_for_shool",
        "owner": "MklerKi",
        "brunches": [
            {
                "name": "main",
                "sha": "0306fe009f759f0bf0ca500d7702ac9af828b163"
            }
        ]
    }
]
```

## Contributing

Contributions to this project are welcome and encouraged! If you'd like to contribute, please follow these steps:

1. Fork the repository on GitHub.
2. Clone the forked repository to your local machine.
3. Create a new branch for your feature or bug fix.
4. Make your changes and ensure that all existing tests pass.
5. Add new tests for any new functionality.
6. Commit your changes with clear and descriptive commit messages.
7. Push your changes to your fork on GitHub.
8. Submit a pull request to the main repository.
9. Please ensure that your pull request adheres to the following guidelines:

- Keep the code style consistent with the existing codebase.
- Write clear and concise commit messages.
- Ensure that your changes are well-tested.
- Update the README.md with any relevant changes.
- Ensure that your code is properly documented.
- By contributing to this project, you agree to license your contributions under the MIT License. Thank you for helping to improve this project!
## License

This project is licensed under the MIT License - see the [LICENSE]() file for details.