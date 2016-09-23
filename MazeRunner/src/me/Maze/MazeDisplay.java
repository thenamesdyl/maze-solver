package me.Maze;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

public class MazeDisplay extends Canvas {
    private Maze myMaze;
    private JFrame myWindow;
    private int mazeRows, mazeCols;
    private BufferStrategy buffer;
    private int wallWidth;

    private int tileSize;
    private Color unvisitedColor;
    private Color visitedColor;
    private Color abandonedColor;
    private Color wallColor;

    public MazeDisplay(Maze aMaze, int r, int c) {
        myMaze = aMaze;
        mazeRows = r;
        mazeCols = c;

        // default visual set.
        tileSize = 10;
        unvisitedColor = Color.BLUE;
        visitedColor = Color.GREEN;
        abandonedColor = Color.RED;
        wallColor = Color.BLACK;

        // This is not optional
        wallWidth = (tileSize / 10 >= 1 ? tileSize / 10 : 1);
    }

    public int getTileSize() {
        return tileSize;
    }

    public void setTileSize(int s) {
        tileSize = s;
        wallWidth = (tileSize / 10 >= 1 ? tileSize / 10 : 1);
    }

    public Color getUnvisitedColor() {
        return unvisitedColor;
    }

    public Color getVisitedColor() {
        return visitedColor;
    }

    public Color getAbandonedColor() {
        return abandonedColor;
    }

    public Color getWallColor() {
        return wallColor;
    }

    public void setUnvisitedColor(Color c) {
        unvisitedColor = c;
    }

    public void setVisitedColor(Color c) {
        visitedColor = c;
    }

    public void setAbandonedColor(Color c) {
        abandonedColor = c;
    }

    public void setWallColor(Color c) {
        wallColor = c;
    }

    public void showMaze() {
        myWindow = new JFrame("Maze Display : (" + mazeRows + " x " + mazeCols + ")");
        myWindow.setSize(mazeCols * tileSize + tileSize * 2, mazeRows * tileSize + tileSize * 4);
        myWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container cp = myWindow.getContentPane();
        cp.setLayout(new FlowLayout());

        this.setBounds(tileSize, tileSize, mazeCols * tileSize + tileSize, mazeRows * tileSize + tileSize);
        cp.add(this);

        //myWindow.setResizable(false);
        myWindow.pack();
        myWindow.setVisible(true);

        this.createBufferStrategy(2);
        buffer = this.getBufferStrategy();

        requestFocus();
        drawMaze();
    }

    public void destroyMaze() {
        myWindow.setVisible(false);
    }

    public void paint(Graphics g) {
        drawMaze();
    }

    private void drawSquare(MazeSquare sq) {
        Graphics2D g2d = (Graphics2D) (buffer.getDrawGraphics());
        int pad = wallWidth / 2;
        int j = sq.getPosition().getCol();
        int i = sq.getPosition().getRow();

        colorSquare(g2d, sq);

        g2d.fill(new Rectangle((tileSize * j + wallWidth),
                (tileSize * i + wallWidth),
                tileSize,
                tileSize)
        );

        g2d.setStroke(new BasicStroke(wallWidth));
        g2d.setColor(wallColor);

        if (sq.getWall(Direction.NORTH)) {
            g2d.draw(new Line2D.Float((tileSize * j),
                    (tileSize * i + wallWidth / 2),
                    (tileSize * (j + 1)),
                    (tileSize * i + wallWidth / 2))
            );
        }
        if (sq.getWall(Direction.SOUTH)) {
            g2d.draw(new Line2D.Float((tileSize * j),
                    (tileSize * (i + 1) + wallWidth / 2),
                    (tileSize * (j + 1)),
                    (tileSize * (i + 1) + wallWidth / 2))
            );
        }
        if (sq.getWall(Direction.EAST)) {
            g2d.draw(new Line2D.Float((tileSize * (j + 1) + wallWidth / 2),
                    (tileSize * i),
                    (tileSize * (j + 1) + wallWidth / 2),
                    (tileSize * (i + 1)))
            );
        }
        if (sq.getWall(Direction.WEST)) {
            g2d.draw(new Line2D.Float((tileSize * j + wallWidth / 2),
                    (tileSize * i),
                    (tileSize * j + wallWidth / 2),
                    (tileSize * (i + 1)))
            );
        }

        // regardless we need to make a wallWidth size wallColor
        // rectangle in the bottom right corner of the tile to keep
        // the background color from wiping out walls
        g2d.draw(new Rectangle(tileSize * (j + 1),
                tileSize * (i + 1),
                wallWidth / 4,
                wallWidth / 4)
        );

        g2d.dispose();
        buffer.show();
    }



    // drawMaze re-renders the entire maze.  It generates the color
    // map and then renders the walls over the top of them.  drawMaze
    // is executed by the MazeDisplay constructor when a new
    // MazeDisplay is first instantiated.

    public void drawMaze() {
        if (this.buffer == null) {
            this.createBufferStrategy(2);
            buffer = this.getBufferStrategy();
        }

        Graphics2D g2d = (Graphics2D) (buffer.getDrawGraphics());

        for (int i = 0; i < mazeRows; i++) {
            for (int j = 0; j < mazeCols; j++) {
                MazeSquare sq = myMaze.squareAt(new Coordinate(i, j));
                colorSquare(g2d, sq);
                g2d.fill(new Rectangle(tileSize * j,
                        tileSize * i,
                        tileSize,
                        tileSize)
                );
            }
        }

        g2d.setColor(wallColor);
        g2d.setStroke(new BasicStroke(wallWidth));

        for (int i = 0; i < mazeRows; i++) {
            for (int j = 0; j < mazeCols; j++) {
                MazeSquare sq = myMaze.squareAt(new Coordinate(i, j));
                if (sq.getWall(Direction.NORTH)) {
                    g2d.draw(new Line2D.Float((tileSize * j),
                            (tileSize * i + wallWidth / 2),
                            (tileSize * (j + 1)),
                            (tileSize * i + wallWidth / 2))
                    );
                }
                if (sq.getWall(Direction.SOUTH)) {
                    g2d.draw(new Line2D.Float((tileSize * j),
                            (tileSize * (i + 1) + wallWidth / 2),
                            (tileSize * (j + 1)),
                            (tileSize * (i + 1) + wallWidth / 2))
                    );
                }
                if (sq.getWall(Direction.EAST)) {
                    g2d.draw(new Line2D.Float((tileSize * (j + 1) + wallWidth / 2),
                            (tileSize * i),
                            (tileSize * (j + 1) + wallWidth / 2),
                            (tileSize * (i + 1)))
                    );
                }
                if (sq.getWall(Direction.WEST)) {
                    g2d.draw(new Line2D.Float((tileSize * j + wallWidth / 2),
                            (tileSize * i),
                            (tileSize * j + wallWidth / 2),
                            (tileSize * (i + 1)))
                    );
                }
            }
        }

        g2d.dispose();
        buffer.show();
    }

    private void colorSquare(Graphics2D g2d, MazeSquare sq) {
        if (sq.isAbandoned()) {
            g2d.setColor(abandonedColor);
        } else if (sq.isVisited()) {
            g2d.setColor(visitedColor);
        } else {
            g2d.setColor(unvisitedColor);
        }
    }

    // update triggers a repaint of the squareAt(p).  Update should be
    // used to perform updates to the view when the maze is altered.
    public void update(Coordinate p) {
        drawSquare(myMaze.squareAt(p));
    }
}