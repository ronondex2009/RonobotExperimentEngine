# BUGS.md - Compilation and Build Issues

## Current Build Issues (Cycle 27)

### JavaFX Dependency Resolution
- **Issue**: Unable to resolve OpenJFX dependencies from Maven Central
- **Error**: `Could not find org.openjfx:javafx-window:21.0.2`
- **Cause**: OpenJFX repositories may require different authentication or direct URL access
- **Status**: TEMPORARILY DISABLED

### Current Approach
- Core engine built without JavaFX GUI components
- Focus on engine logic and game mechanics
- JavaFX will be re-enabled when proper dependency configuration is found
- Alternative: Use a lightweight graphics library or mock rendering for now

### TODO
- [ ] Configure proper JavaFX repository access
- [ ] Find working OpenJFX Maven coordinates for Linux
- [ ] Implement alternative rendering if needed
- [ ] Re-enable GUI once resolved

### Next Steps
- Fix JavaFX dependencies
- Focus on core engine functionality
- Write comprehensive unit tests for non-GUI components
- Rebuild with GUI once dependencies resolved
