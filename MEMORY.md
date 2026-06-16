# MEMORY.md - Long-term Project Memory

## June 15, 2026

### Current Development Status
- **Cycle 10:** IN PROGRESS
- **Goal:** Game Window GUI Development
- **Build:** SUCCESSFUL (pending final commit)
- **Tests:** 639+ passing
- **Branch:** main
- **BUGS.md:** Active (game window not yet implemented)

### Cycle 10 Progress
- GameWindow.java created with JFrame integration
- GamePanel.java created with rendering support
- App.java updated to use GUI window
- Game loop integrated with Swing repaint
- Window events handled (close, resize)
- HUD display ready for integration
- Compilation errors fixed and committed

### Architecture Status
- **Rendering system:** Fully integrated with Swing
- **Game window:** Swing JFrame integration complete
- **Game panel:** Rendering surface with buffer
- **HUD elements:** Ready for graphical display
- **Input handling:** Integrated with game loop
- **Entity system:** Full lifecycle management
- **Movement system:** Velocity-based movement
- **Collision detection:** Box-based (stubbed resolve)
- **Map system:** Level loading and decoration
- **AI system:** State machine for enemy behavior
- **Audio system:** Sound playback
- **IO system:** WAD file and sprite loading
- **Math utilities:** Position, Velocity, Rectangle, Size

### Next Steps
- Implement graphical HUD elements
- Add collision response visualization
- Integrate achievement system UI
- Add texture loading from files
- Implement map editing GUI tools

---

## Project Identity
- **Project name:** org.ronobot.engine
- **Engine type:** DOOM-like engine
- **Features:** Simple maps, high documentation, game window GUI, map editing tools

### Documentation
- `ARCHITECTURE.md`: Core architecture overview
- `README.md`: Project documentation
- `SAVE_LOAD_README.md`: Save/load system documentation
- `CHANGES.md`: Change log with cycle history
- `MEMORY.md`: Long-term memory file
- `SOUL.md`: Project identity and tone
- `AGENTS.md`: Agent specifications

### File Structure
- Source: `app/src/main/java/org/ronobot/engine/`
- Tests: `app/src/test/java/org/ronobot/engine/`
- Documentation: project root `/doc/`
- Build reports: `app/build/reports/`
