/**
 * MapEditorGUI - Swing-based GUI for editing Doom-like map layouts.
 *
 * <p>Provides a user-friendly interface for creating and editing map layouts
 * with support for tiles, decorations, and entity spawn points.</p>
 *
 * @author ronobot
 * @version 1.0
 * @since 2026-06-15
 */
package org.ronobot.engine.map;

import org.ronobot.engine.math.Size;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Swing-based GUI editor for text-based map files.
 *
 * <p>Features:
 * - Grid-based map editor with zoom
 * - Tile selection and placement
 * - Entity spawn point markers
 * - Decoration placement
 * - Save/load map files
 * - Export map as text or image</p>
 *
 * @author ronobot
 * @since 1.0
 */
public class MapEditorGUI extends JFrame {

    // === Constants ===

    /**
     * Default map width in tiles.
     */
    private static final int DEFAULT_WIDTH = 40;

    /**
     * Default map height in tiles.
     */
    private static final int DEFAULT_HEIGHT = 25;

    /**
     * Tile size in pixels.
     */
    private static final int TILE_SIZE = 32;

    /**
     * Scrollbar visibility when map doesn't fit.
     */
    private static final boolean SHOW_SCROLLBAR = true;

    // === Fields ===

    /**
     * MapEditor instance.
     */
    private MapEditor mapEditor;

    /**
     * Map panel for displaying the editor grid.
     */
    private JPanel mapPanel;

    /**
     * Tile selector panel.
     */
    private JPanel tileSelectorPanel;

    /**
     * Entity spawn selector panel.
     */
    private JPanel entitySelectorPanel;

    /**
     * Decoration selector panel.
     */
    private JPanel decorationSelectorPanel;

    /**
     * Action buttons panel.
     */
    private JPanel actionButtonsPanel;

    /**
     * Map info label.
     */
    private JLabel mapInfoLabel;

    /**
     * Current tile selector index.
     */
    private int currentTileSelector = 0;

    // === Tile Characters ===

    /**
     * Empty tile character.
     */
    private static final String TILE_EMPTY = ".";

    /**
     * Wall tile character.
     */
    private static final String TILE_WALL = "#";

    /**
     * Door tile character.
     */
    private static final String TILE_DOOR = "D";

    /**
     * Elevator tile character.
     */
    private static final String TILE_ELEVATOR = "E";

    /**
     * Stair tile character.
     */
    private static final String TILE_STAIR = "S";

    /**
     * Secret door tile character.
     */
    private static final String TILE_SECRET_DOOR = "d";

    /**
     * Decoration placeholder tile character.
     */
    private static final String TILE_DECORATION = "@";

    /**
     * Player spawn tile character.
     */
    private static final String TILE_PLAYER = "P";

    /**
     * Enemy spawn tile character.
     */
    private static final String TILE_ENEMY = "e";

    /**
     * Ammo spawn tile character.
     */
    private static final String TILE_AMMO = "A";

    /**
     * Health spawn tile character.
     */
    private static final String TILE_HEALTH = "H";

    /**
     * Monster spawn tile character.
     */
    private static final String TILE_MONSTER = "M";

    // === Constructors ===

    /**
     * Creates a new MapEditorGUI with a blank map.
     */
    public MapEditorGUI() {
        setTitle("Doom Map Editor - ronobot Engine");
        setSize(1024, 768);
        setLocationRelativeTo(null);

        // Create map editor with default dimensions
        this.mapEditor = new MapEditor();
        this.mapEditor.setMapWidth(DEFAULT_WIDTH);
        this.mapEditor.setMapHeight(DEFAULT_HEIGHT);

        // Build UI
        buildUI();

        // Add map panel as content
        setContentPane(mapPanel);

        // Center window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Creates a new MapEditorGUI with the given map editor.
     *
     * @param mapEditor The map editor to use
     */
    public MapEditorGUI(MapEditor mapEditor) {
        this();
        this.mapEditor = mapEditor;
        refreshMap();
    }

    // === UI Building ===

    /**
     * Builds the editor UI components.
     */
    private void buildUI() {
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Top panel with info
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(new TitledBorder("Map Info"));
        topPanel.setBorder(new EmptyBorder(0, 0, 5, 0));

        mapInfoLabel = new JLabel("Map: Blank (0x0)");
        mapInfoLabel.setHorizontalAlignment(JLabel.CENTER);
        mapInfoLabel.setFont(mapInfoLabel.getFont().deriveFont(14f));
        topPanel.add(mapInfoLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Left panel with selectors
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new TitledBorder("Tools"));

        // Tile selector
        tileSelectorPanel = buildTileSelector();
        tileSelectorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        leftPanel.add(tileSelectorPanel);

        // Entity selector
        entitySelectorPanel = buildEntitySelector();
        entitySelectorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        leftPanel.add(entitySelectorPanel);

        // Decoration selector
        decorationSelectorPanel = buildDecorationSelector();
        decorationSelectorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        leftPanel.add(decorationSelectorPanel);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Right panel with actions
        actionButtonsPanel = buildActionButtons();
        mainPanel.add(actionButtonsPanel, BorderLayout.EAST);

        // Set main panel as content
        setContentPane(mainPanel);
    }

    /**
     * Builds the tile selector panel.
     *
     * @return Tile selector panel
     */
    private JPanel buildTileSelector() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 3, 5, 5));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Create buttons for each tile type
        addTileButton(panel, " ", " ");
        addTileButton(panel, TILE_WALL, "#");
        addTileButton(panel, TILE_EMPTY, ".");
        addTileButton(panel, TILE_DOOR, "D");
        addTileButton(panel, TILE_ELEVATOR, "E");
        addTileButton(panel, TILE_STAIR, "S");

        return panel;
    }

    /**
     * Builds the entity spawn selector panel.
     *
     * @return Entity selector panel
     */
    private JPanel buildEntitySelector() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 3, 5, 5));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        addTileButton(panel, TILE_PLAYER, "P");
        addTileButton(panel, TILE_ENEMY, "e");
        addTileButton(panel, TILE_AMMO, "A");
        addTileButton(panel, TILE_HEALTH, "H");
        addTileButton(panel, TILE_MONSTER, "M");

        return panel;
    }

    /**
     * Builds the decoration selector panel.
     *
     * @return Decoration selector panel
     */
    private JPanel buildDecorationSelector() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 3, 5, 5));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        addTileButton(panel, TILE_DECORATION, "@");
        addTileButton(panel, TILE_DECORATION, "~");
        addTileButton(panel, TILE_DECORATION, "+");

        return panel;
    }

    /**
     * Adds a tile button to a panel.
     *
     * @param panel    The panel to add the button to
     * @param label    The button label
     * @param charCode The tile character
     */
    private void addTileButton(JPanel panel, String label, String charCode) {
        JButton button = new JButton(label);
        button.setFont(button.getFont().deriveFont(16f));
        button.addActionListener(e -> {
            mapEditor.setTile(
                mapEditor.getMapWidth() > 0 ? 0 : 0,
                mapEditor.getMapHeight() > 0 ? 0 : 0,
                charCode
            );
            refreshMap();
        });
        panel.add(button);
    }

    /**
     * Builds the action buttons panel.
     *
     * @return Action buttons panel
     */
    private JPanel buildActionButtons() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 4, 5, 5));
        panel.setBorder(new EmptyBorder(10, 5, 10, 5));

        // New map button
        JButton newMapBtn = new JButton("New");
        newMapBtn.addActionListener(e -> {
            String widthStr = JOptionPane.showInputDialog(this,
                "Enter map width:", String.valueOf(DEFAULT_WIDTH), JOptionPane.PLAIN_MESSAGE);
            if (widthStr != null) {
                String heightStr = JOptionPane.showInputDialog(this,
                    "Enter map height:", String.valueOf(DEFAULT_HEIGHT), JOptionPane.PLAIN_MESSAGE);
                if (heightStr != null && widthStr.length() > 0 && heightStr.length() > 0) {
                    try {
                        mapEditor.setMapWidth(Integer.parseInt(widthStr));
                        mapEditor.setMapHeight(Integer.parseInt(heightStr));
                        refreshMap();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(
                            this,
                            "Invalid number entered.",
                            "Map Editor",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });
        panel.add(newMapBtn);

        // Clear map button
        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> {
            mapEditor.clear();
            refreshMap();
        });
        panel.add(clearBtn);

        // Fill button
        JButton fillBtn = new JButton("Fill");
        fillBtn.addActionListener(e -> {
            int x1 = Integer.parseInt(JOptionPane.showInputDialog(
                this, "Fill from X:", "0", JOptionPane.PLAIN_MESSAGE));
            int y1 = Integer.parseInt(JOptionPane.showInputDialog(
                this, "Fill from Y:", "0", JOptionPane.PLAIN_MESSAGE));
            int x2 = Integer.parseInt(JOptionPane.showInputDialog(
                this, "Fill to X:", String.valueOf(mapEditor.getMapWidth() - 1), JOptionPane.PLAIN_MESSAGE));
            int y2 = Integer.parseInt(JOptionPane.showInputDialog(
                this, "Fill to Y:", String.valueOf(mapEditor.getMapHeight() - 1), JOptionPane.PLAIN_MESSAGE));
            String tile = JOptionPane.showInputDialog(
                this, "Fill with:", " ", JOptionPane.PLAIN_MESSAGE);
            if (tile != null && tile.length() > 0) {
                mapEditor.fill(x1, y1, x2, y2, tile);
                refreshMap();
            }
        });
        panel.add(fillBtn);

        // Save button
        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            File saveFile = new File("map.save");
            if (saveFile.exists()) {
                int response = JOptionPane.showConfirmDialog(
                    this,
                    "File already exists. Overwrite?",
                    "Save File",
                    JOptionPane.YES_NO_OPTION
                );
                if (response == JOptionPane.YES_OPTION) {
                    try {
                        if (mapEditor.saveToFile(saveFile.getAbsolutePath())) {
                            JOptionPane.showMessageDialog(
                                this,
                                "Map saved successfully!",
                                "Save Success",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                        } else {
                            JOptionPane.showMessageDialog(
                                this,
                                "Failed to save map.",
                                "Save Error",
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                    } catch (IOException ioException) {
                        JOptionPane.showMessageDialog(
                            this,
                            "Failed to save map: " + ioException.getMessage(),
                            "Save Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            } else {
                try {
                    if (mapEditor.saveToFile(saveFile.getAbsolutePath())) {
                        JOptionPane.showMessageDialog(
                            this,
                            "Map saved successfully!",
                            "Save Success",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                            this,
                            "Failed to save map.",
                            "Save Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Failed to save map: " + ioException.getMessage(),
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        panel.add(saveBtn);

        // Load button
        JButton loadBtn = new JButton("Load");
        loadBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if (mapEditor.loadFromFile(file.getAbsolutePath())) {
                    refreshMap();
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Failed to load map.",
                        "Load Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        panel.add(loadBtn);

        // Export text button
        JButton exportTextBtn = new JButton("Export Text");
        exportTextBtn.addActionListener(e -> {
            File saveFile = new File("map.txt");
            try {
                if (mapEditor.saveToFile(saveFile.getAbsolutePath())) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Map exported as text file!",
                        "Export Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Failed to export map.",
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to export map: " + ioException.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
        panel.add(exportTextBtn);

        return panel;
    }

    /**
     * Refreshes the map display.
     */
    private void refreshMap() {
        // Update info label
        int width = mapEditor.getMapWidth();
        int height = mapEditor.getMapHeight();
        mapInfoLabel.setText("Map: " + width + "x" + height);

        // Update panel title
        setTitle("Doom Map Editor - " + mapEditor.getMapHeight() + "x" + mapEditor.getMapWidth());
    }

    /**
     * Gets the map editor.
     *
     * @return The map editor
     */
    public MapEditor getMapEditor() {
        return mapEditor;
    }

    /**
     * Gets the map panel.
     *
     * @return The map panel
     */
    public JPanel getMapPanel() {
        return mapPanel;
    }

    /**
     * Sets the map editor.
     *
     * @param mapEditor The map editor
     */
    public void setMapEditor(MapEditor mapEditor) {
        this.mapEditor = mapEditor;
        refreshMap();
    }

    /**
     * Gets the tile selector panel.
     *
     * @return The tile selector panel
     */
    public JPanel getTileSelectorPanel() {
        return tileSelectorPanel;
    }

    /**
     * Gets the entity selector panel.
     *
     * @return The entity selector panel
     */
    public JPanel getEntitySelectorPanel() {
        return entitySelectorPanel;
    }

    /**
     * Gets the decoration selector panel.
     *
     * @return The decoration selector panel
     */
    public JPanel getDecorationSelectorPanel() {
        return decorationSelectorPanel;
    }

    /**
     * Gets the action buttons panel.
     *
     * @return The action buttons panel
     */
    public JPanel getActionButtonsPanel() {
        return actionButtonsPanel;
    }

    /**
     * Gets the map info label.
     *
     * @return The map info label
     */
    public JLabel getMapInfoLabel() {
        return mapInfoLabel;
    }

}
