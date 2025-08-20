# Javadocs Conventions

## General Guidelines
- Use complete sentences with proper capitalization and punctuation
- Start with a brief summary sentence (one line)
- Add detailed description if needed in subsequent paragraphs
- Use third person singular present tense ("Returns the value" not "Return the value")
- Avoid implementation details unless necessary for usage

## Required Tags
- `@param` - Document all parameters with name and description
- `@return` - Document return value (omit for void methods)
- `@throws` - Document checked and unchecked exceptions that callers should handle
- `@since` - Version when method/class was added
- `@author` - Class level only
- `@version` - Class level only

## Optional but Recommended Tags
- `@see` - References to related classes/methods
- `@deprecated` - Mark deprecated elements with replacement guidance
- `@implNote` - Implementation notes when relevant
- `@apiNote` - API usage notes
- `@implSpec` - Implementation requirements for subclasses

## Code Examples
- Use `{@code}` for inline code snippets
- Use `<pre>{@code ... }</pre>` for multi-line code blocks
- Include usage examples for complex methods

## Formatting
- Use `{@link}` for cross-references to other classes/methods
- Use `{@value}` to reference constant values
- Use `{@literal}` for text that contains special characters

## Class Level Documentation
```java
/**
 * Brief description of what the class does.
 * 
 * <p>Detailed description explaining the purpose, usage patterns,
 * and any important behavioral notes.</p>
 * 
 * @param <T> type parameter description
 * @author Author Name
 * @version 1.0
 * @since 1.0
 */
```

## Method Level Documentation
```java
/**
 * Brief description of what the method does.
 * 
 * <p>Additional details about behavior, constraints, or usage.</p>
 * 
 * @param paramName description of the parameter
 * @param anotherParam description with constraints if any
 * @return description of return value
 * @throws IllegalArgumentException if parameter is invalid
 * @throws IOException if file operation fails
 * @see RelatedClass#relatedMethod()
 * @since 1.0
 */
```

Write comprehensive javadocs for: $ARGUMENTS