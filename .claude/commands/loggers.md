# Add Loggers Command

Add SLF4J loggers to Java classes with proper Lombok annotations and include relevant log statements in the code.

## Usage
`/loggers <class_name_or_path>`

## Behavior
- Add `@Slf4j` Lombok annotation to the specified class
- Import `lombok.extern.slf4j.Slf4j` if not already present
- Add appropriate log statements in methods where logging would be valuable:
  - Entry/exit points for public methods (DEBUG level)
  - Exception handling (ERROR/WARN level)
  - Important business logic steps (INFO level)
  - Parameter validation and data processing (DEBUG level)
- Use appropriate log levels (ERROR, WARN, INFO, DEBUG)
- Follow existing code style and formatting
- Only add what's necessary - don't modify existing logger implementations

## Examples
- `/loggers UserService` - Add logger to UserService class
- `/loggers src/main/java/com/example/UserController.java` - Add logger to specific file

## Requirements
- Must use Lombok's `@Slf4j` annotation
- Must follow Spring Boot logging conventions
- Must preserve existing code structure and style
- Must not create unnecessary methods or modifications