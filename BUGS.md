# BUGS.md - Outstanding Issues

## Cycle 10 - Game Window GUI Development

### Issue: Game Window Not Producing Window
**Status**: OPEN
**Priority**: CRITICAL
**Description**: The program does not produce a game window. The current implementation uses stdout for rendering instead of a proper GUI window.

### Root Cause
The Renderer class outputs to System.out instead of rendering to a Swing/JFC component. The Game class has a game loop but no actual visual window is created.

### Current State
- Renderer.render() prints to stdout
- No JFrame or Swing window exists
- HUD elements are printed to console instead of displayed
- Game loop runs but has no visual output

### Implementation Needed
1. Create GameWindow.java extending JFrame
2. Convert Renderer output to BufferedImage
3. Integrate swing's Component.repaint() in game loop
4. Display HUD elements as graphical components
5. Handle window resizing and repainting
6. Add close handler to stop game loop

### Progress
- [ ] Create GameWindow class
- [ ] Integrate Renderer with JFrame
- [ ] Implement frame repaint
- [ ] Display HUD elements
- [ ] Handle window events
- [ ] Stop loop on close

### Dependencies
- Renderer.java - Already implemented with render() method
- Game.java - Already implemented with game loop
- App.java - Entry point needs to create window

### Notes
- Use Swing for GUI (standard JDK, no external dependencies)
- Game should render to BufferedImage and display in JLabel or custom panel
- HUD elements need graphical representation
- Window should handle resize events

### Next Steps
1. Implement GameWindow class
2. Integrate with existing Renderer
3. Test window creation and rendering
4. Fix any compilation errors
5. Push to main branch

### TODO for Next Cycle
- Add collision response visualization
- Implement achievement system UI
- Add map editor GUI integration
- Save/load system with file dialogs
