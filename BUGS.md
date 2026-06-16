# BUGS.md - Outstanding Issues

## Cycle 11 - HUD Graphics Conversion

### Issue: Renderer Outputs to stdout Instead of Swing Graphics
**Status**: OPEN
**Priority**: HIGH
**Description**: The Renderer class currently prints rendering instructions to System.out instead of drawing to a BufferedImage/G2D component. This prevents the game window from displaying any graphics.

### Root Cause
- Renderer.render() prints tile, entity, and HUD information to stdout
- No Graphics2D drawing calls exist
- HUD elements use System.out.println instead of Swing drawing

### Current State
- Renderer.printTile() prints to stdout
- Renderer.printEntity() prints to stdout
- Renderer.printHUD() prints to stdout
- GamePanel.renderBuffer exists but never receives draw calls

### Implementation Needed
1. Convert Renderer to use Graphics2D for all rendering
2. Implement tile drawing for floor/wall/decorations
3. Implement entity drawing for player/enemies/projectiles
4. Implement HUD drawing using Swing components or buffered text
5. Integrate with Swing paintComponent lifecycle

### Progress
- [x] Analyze Renderer.render() method
- [ ] Implement Graphics2D tile rendering
- [ ] Implement Graphics2D entity rendering
- [ ] Implement Graphics2D HUD rendering
- [ ] Add color/font for HUD text
- [ ] Handle window resize events
- [ ] Test rendering in GameWindow

### Dependencies
- Renderer.java - Main class to update
- HUDElement.java - Needs graphics-aware callbacks
- GamePanel.java - Already has buffer setup
- GameWindow.java - JFrame integration complete

### Notes
- Use Graphics2D for all rendering operations
- Store HUD data in buffers or JLabel components
- Use java.awt.BasicStroke for visual effects
- Consider using ImageIO for sprite loading later

### Next Steps
1. Replace all stdout.println with Graphics2D.drawImage/g2d operations
2. Create simple colored rectangles for tiles
3. Add text rendering for HUD using Font/FontMetrics
4. Test with simple level
5. Fix any compilation errors
6. Commit to main branch

### TODO for Next Cycle
- Load textures from disk files
- Add sprite-based rendering
- Implement collision response visualization
- Integrate achievement system UI
- Add map editor GUI integration
