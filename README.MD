# Prerequisites for running
- Java open jdk 18
- Gradle or an IDE (IntelliJ)
- Time

# How to run this app in IDE
- Create new springboot configuration from `RocketLabTestApplication`
- Click run

# How to run with gradle
- Open terminal in the project directory
- Enter `gradle bootRun` to start the spring boot app

# NOTE:
- This uses an H2 database that will generate some data on intit the first time
- Check `LoadDatabase` for the data being added

# Possible app improvements
- Add documentation on apis if they are used publicly
- Add authentication to any apis with sensitive data
- Move off of h2 to use something like postgres, redis
- Create a docker container/image for the app
- Move to the cloud with some infrastructure code using something like Terraform
- Setup proper build process with CI/CD
- Some logic to handle bad ids or data send to the api

