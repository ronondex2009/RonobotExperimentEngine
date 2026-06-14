# BUGS.md - Compilation and Build Issues

## Cycle 28 Status

### Build Issues

- **JavaFX Dependency Issue**: OpenJFX artifacts are not available at Maven Central for Java 21, or require different coordinates. Core engine built without JavaFX GUI components. JavaFX will be re-enabled when proper dependency configuration is found.

### Completed Goals (Cycle 28)

1. Build achieved (clean, 403 tests passing)
2. Core engine built with map decoration support
3. BUGS.md deleted previously, re-created to document JavaFX issue

### Next Steps

- [ ] Resolve JavaFX dependency issue
  - Search for alternative JavaFX Maven coordinates
  - Consider using different JavaFX versions or repositories
  - Mock rendering for core engine functionality
  - GUI development paused until resolved

### Completed Work

- MapDecoration system fully implemented and tested
- Map decoration loading and rendering support
- Documentation extended
- All tests passing (403 total)
