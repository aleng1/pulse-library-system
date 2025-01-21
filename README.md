# Pulse Library System

A modern library management system built with Spring Boot and Thymeleaf, designed to streamline library operations and enhance user experience.

## Features

### Book Management
- Add, edit, and delete books
- Track book availability and location
- Search books by title, author, or category
- Manage book copies and status

### Member Management
- Register and manage library members
- Track membership status and expiry dates
- Generate member cards
- Manage member profiles

### Circulation Management
- Book borrowing and returns
- Due date tracking
- Renewal management
- Overdue notifications

### Room Management
- Manage library rooms and facilities
- Room booking system
- Track room availability
- Capacity management

### Event Management
- Create and manage library events
- Track event participants
- Event calendar
- Capacity control

### Fine Management
- Automatic fine calculation
- Fine payment tracking
- Generate fine reports
- Fine status management

## Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA
- Thymeleaf
- MySQL Database
- Bootstrap 5
- Maven

## Setup and Installation

1. Clone the repository
```bash
git clone https://github.com/aleng1/pulse-library-system.git
```

2. Configure MySQL database
- Create a new database named `pulse_library`
- Update `application.properties` with your database credentials

3. Build the project
```bash
mvn clean install
```

4. Run the application
```bash
mvn spring-boot:run
```

5. Access the application at `http://localhost:8080`

## Screenshots

[You can add screenshots of your UI here]

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Authors

- [@aleng1](https://github.com/aleng1) 
