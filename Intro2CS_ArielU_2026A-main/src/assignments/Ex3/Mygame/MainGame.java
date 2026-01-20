package assignments.Ex3.Mygame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import assignments.Ex3.Map2D;

public class MainGame {
    public static void main(String[] args) throws InterruptedException {
        String mapFile = "Intro2CS_ArielU_2026A-main/src/assignments/Ex3/Mygame/GameMap.txt";
        int[][] rawMap = MapLoader.loadMap(mapFile);
        List<Ghost> ghostList = new ArrayList<>();


        Map2D mapObj = new assignments.Ex3.Map(rawMap);
        Board board = new Board(rawMap);

        int startRow = 5;
        int startCol = 5;
        if (mapObj.getPixel(startCol, startRow) == 1) {
            outer:
            for (int r = 0; r < mapObj.getHeight(); r++) {
                for (int c = 0; c < mapObj.getWidth(); c++) {
                    if (mapObj.getPixel(c, r) != 1) {
                        startCol = c;
                        startRow = r;
                        break outer;
                    }
                }
            }
        }

        PacmanRenderer pacman = new PacmanRenderer(startRow, startCol);
        Ghost ghost1 = new Ghost(1, 1, Color.RED, "אוכלים");
        Ghost ghost2 = new Ghost(2, 2, Color.PINK, "אותי");
        Ghost ghost3 = new Ghost(3, 3, Color.green, "עכשיו");
        ghostList.add(ghost1);
        ghostList.add(ghost2);
        ghostList.add(ghost3);
        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        StdDraw.enableDoubleBuffering();

        while (true) {
            if (board.isGameOver(mapObj)) {
                StdDraw.clear(Color.BLACK);
                StdDraw.setPenColor(Color.GREEN);
                Font font = new Font("Arial", Font.BOLD, 30);
                StdDraw.setFont(font);
                StdDraw.text(0.5, 0.5, "YOU WON");
                StdDraw.show();
                Thread.sleep(3000);
                System.exit(0);
            }
            for (Ghost g : ghostList) {
                g.move(board);
            }
            pacman.move(board, mapObj, ghostList);

            StdDraw.clear(Color.BLACK);
            board.draw();
            pacman.draw(board);
            for (Ghost g : ghostList) {
                g.draw(board.getWidth(), board.getHeight());
            }

            StdDraw.show();
            Thread.sleep(150);
        }
    }
}
