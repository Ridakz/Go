import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a go board.It uses the Intersection enum for every to represent the
 * state of every intersections of the grid.This class also implements functions that allow
 * to manage a go game,such as detecting surrounded stones,legal/illegal moves and counting
 * the area for the score of the game.
 *
 *@version 30/05/2016
 *@author Kadir Ercin
 */
public class GoBoard implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * the size of the board
     */
    final int size;

    /**
     * a grid of Intersection
     */
    private Intersection inter[][];

    /**
     * if it is black player's turn
     */
     boolean bTurn = true;


    /**
     * the number of black stones
     */
     int blackStones;

    /**
     * the number of white stones
     */
     int whiteStones;


    /**
     * the game's turn
     */
     int turn ;

    /**
     * the number of removed stones at each turn
     */
     int numberRemoved = 0;


    /**
     * number of stone captured by the black player
     */
     int blackCaptured = 0;

    /**
     * number of stone captured by the white player
     */
     int whiteCaptured = 0;

    /**
     * the history of the game
     */
     String history = new String("");

    /**
     * the last circled stone's position
     */
    private Vector lastCircled = new Vector(-1, -1);

    /**
     * the last position where Ko occured
     */
    Vector lastKo = new Vector(-1, -1);

    /**
     * stores the lastest played position and its adjacents position
     */
    private Vector played[] = new Vector[5];

    /**
     * a list of played position, used for review
     */
    List<Vector[]> playedReview = new ArrayList<>();

    /**
     * a list of intersection states, used for review
     */
    List<Intersection> interReview = new ArrayList<>();

    /**
     * if doing a Ko can be possible
     */
    boolean canKo = false;

    /**
     * if a player passed
     */
    boolean passed = false;

    /**
     * Constructor of the class GoBoard
     *
     * @param inte the intersection state that every intersection will have in the new GoBoard
     * @param N    the size of the board
     */
    public GoBoard(Intersection inte, int N) {
        this.size = N;
        this.inter = new Intersection[N][N];
        for (int i = 0; i < N; i++) {
            for (int k = 0; k < N; k++) {
                this.inter[i][k] = inte;
            }
        }
    }

    /**
     * This method returns the state of the given position
     *
     * @param pos a Vector representing a position of grid
     * @return the intersection of the given position
     */
    public Intersection getInter(Vector pos) {
        if (pos.getX() >= 0 && pos.getX() < this.size && pos.getY() >= 0 && pos.getY() < this.size)
            return this.inter[pos.getX()][pos.getY()];
        else
            return Intersection.OutOfBounds;
    }

    /**
     * This method manages the play of a stone at a given position
     *
     * @param position the position in the grid
     */
    public  void play(Vector position) {
        if (getInter(position) == Intersection.Nothing && !isKo(position, canKo, bTurn, lastCircled)) {
            if (bTurn) {
                setInter(position, Intersection.BlackStone);
                blackStones++;
            } else {
                setInter(position, Intersection.WhiteStone);
                whiteStones++;
            }
            if (surrounds(position)) {
                updateReview();
                history = new String(history + "Turn " + turn + "  " + (size - position.getY()) + (char) ('A' + position.getX()) + "\n");

                if(bTurn)
                    GoManager.goGame.setViewInter(position,Intersection.BlackStone);
                else
                    GoManager.goGame.setViewInter(position,Intersection.WhiteStone);


                //counts the number of removed stones
                numberRemoved = replaceAdj(position, Intersection.Surrounded, Intersection.Surrounded2);
                if (numberRemoved == 1)
                    //gets the last circled stone postion from the current played stone position,needed for Ko rule
                    lastCircled = getCircled(position);
                setPlayed(position);
                playedReview.add(copyPlayed());
                if (bTurn) {
                    blackCaptured += numberRemoved;
                    whiteStones -= numberRemoved;
                    interReview.add(Intersection.BlackStone);
                } else {
                    whiteCaptured += numberRemoved;
                    blackStones -= numberRemoved;
                    interReview.add(Intersection.WhiteStone);
                }
                turn++;

                numberRemoved = GoManager.goGame.remAdjacentStones(position, Intersection.Surrounded2, Intersection.Nothing);
                bTurn = !bTurn;

                canKo = numberRemoved == 1;
                if (canKo)
                    lastKo = position;


            } else if (isSurrounded(position)) {
                System.err.println("SURROUNDED");
                if (bTurn) {
                    rep(position, Intersection.Surrounded, Intersection.BlackStone);
                    blackStones--;
                } else {
                    rep(position, Intersection.Surrounded, Intersection.WhiteStone);
                    whiteStones--;
                }
                setInter(position, Intersection.Nothing);
                numberRemoved = 0;
                GoManager.goGame.setViewInter(position,Intersection.Surrounded);


                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                            GoManager.goGame.setViewInter(position,Intersection.Nothing);
                    }
                });
            } else {
                setPlayed(position);
                updateReview();
                playedReview.add(copyPlayed());//to copy played position
                if (bTurn)
                    interReview.add(Intersection.BlackStone);
                else
                    interReview.add(Intersection.WhiteStone);
                turn++;
                history = new String(history + "Turn " + turn + "  " + (size - position.getY()) + (char) ('A' + position.getX()) + "\n");
                numberRemoved = 0;

                if(bTurn)
                    GoManager.goGame.setViewInter(position,Intersection.BlackStone);
                else
                    GoManager.goGame.setViewInter(position,Intersection.WhiteStone);

                bTurn = !bTurn;

                canKo = numberRemoved == 1;
                if (canKo)
                    lastKo = position;
            }
        } else {

            System.err.println("CANNOT PLAY : "+getInter(position));
            if (getInter(position) == Intersection.Nothing) {
                GoManager.goGame.setViewInter(position,Intersection.Surrounded);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                            GoManager.goGame.setViewInter(position,Intersection.Nothing);
                    }

                });
            }
        }
        GoManager.goGUI.update(this);

    }

    /**
     * This method sets the state of an intersection
     *
     * @param pos          a Vector representing a position of grid
     * @param intersection an intersection state
     */
    public void setInter(Vector pos, Intersection intersection) {
        if (pos.getX() >= 0 && pos.getX() < this.size && pos.getY() >= 0 && pos.getY() < this.size)
            this.inter[pos.getX()][pos.getY()] = intersection;
        else
            this.inter[pos.getX()][pos.getY()] = Intersection.OutOfBounds;
    }

    /**
     * This method checks if the last played stone circles the other colored stones next to it,
     * it changes the state of the circled stones next to it to Surrounded if they are.
     *
     * @param pos the position of the last played move
     * @return if the played stone circles other stones
     */
    public boolean surrounds(Vector pos) {
        if (this.getInter(pos).equals(Intersection.BlackStone)) {
			
					/* checks if the adjacent position are circled */
            if (this.getInter(new Vector(pos.getX() + 1, pos.getY())).equals(Intersection.WhiteStone))
                this.isWSurrounded(new Vector(pos.getX() + 1, pos.getY()));
            if (this.getInter(new Vector(pos.getX(), pos.getY() - 1)).equals(Intersection.WhiteStone))
                this.isWSurrounded(new Vector(pos.getX(), pos.getY() - 1));
            if (this.getInter(new Vector(pos.getX() - 1, pos.getY())).equals(Intersection.WhiteStone))
                this.isWSurrounded(new Vector(pos.getX() - 1, pos.getY()));
            if (this.getInter(new Vector(pos.getX(), pos.getY() + 1)).equals(Intersection.WhiteStone))
                this.isWSurrounded(new Vector(pos.getX(), pos.getY() + 1));
        } else {
            if (this.getInter(new Vector(pos.getX() + 1, pos.getY())).equals(Intersection.BlackStone))
                this.isBSurrounded(new Vector(pos.getX() + 1, pos.getY()));
            if (this.getInter(new Vector(pos.getX(), pos.getY() - 1)).equals(Intersection.BlackStone))
                this.isBSurrounded(new Vector(pos.getX(), pos.getY() - 1));
            if (this.getInter(new Vector(pos.getX() - 1, pos.getY())).equals(Intersection.BlackStone))
                this.isBSurrounded(new Vector(pos.getX() - 1, pos.getY()));
            if (this.getInter(new Vector(pos.getX(), pos.getY() + 1)).equals(Intersection.BlackStone))
                this.isBSurrounded(new Vector(pos.getX(), pos.getY() + 1));
        }
        //return if one of the adjacent position is circled
        return this.getInter(new Vector(pos.getX() + 1, pos.getY())) == Intersection.Surrounded
                || this.getInter(new Vector(pos.getX(), pos.getY() + 1)) == Intersection.Surrounded
                || this.getInter(new Vector(pos.getX(), pos.getY() - 1)) == Intersection.Surrounded
                || this.getInter(new Vector(pos.getX() - 1, pos.getY())) == Intersection.Surrounded;
    }

    /**
     * checks if the last played move is surrounded or not
     *
     * @param pos the position of the last played move
     * @return if the played move is circled
     */
    public boolean isSurrounded(Vector pos) {
        if (this.getInter(pos) == Intersection.BlackStone)
            return isBSurrounded(pos);
        else
            return isWSurrounded(pos);
    }

    /**
     * checks if a black stone is circled or not
     *
     * @param pos the position of the black stone
     * @return if the black stone is circled
     */
    public boolean isBSurrounded(Vector pos) {
        // checks if the current position is out of the board or a white stone
        if (this.getInter(pos).equals(Intersection.WhiteStone) || this.getInter(pos).equals(Intersection.OutOfBounds) || this.getInter(pos).equals(Intersection.Surrounded))
            return true;
            // checks if there is an empty position next to the current one
        else if (this.getInter(new Vector(pos.getX() + 1, pos.getY())) == Intersection.Nothing || this.getInter(new Vector(pos.getX() - 1, pos.getY())) == Intersection.Nothing || this.getInter(new Vector(pos.getX(), pos.getY() + 1)) == Intersection.Nothing || this.getInter(new Vector(pos.getX(), pos.getY() - 1)) == Intersection.Nothing) {
            setInter(pos, Intersection.Surrounded);
            // sets the intersection to their former state if the black stones arent circled
            this.rep(pos, Intersection.Surrounded, Intersection.BlackStone);
            return false;
        }
        // case if the current position is a black stone
        else {
            //sets the intersection to Surrounded, to not check the same position endlessly
            setInter(pos, Intersection.Surrounded);
            // look the state of the adjacent positions
            return isBSurrounded(new Vector(pos.getX() + 1, pos.getY()))
                    && isBSurrounded(new Vector(pos.getX(), pos.getY() - 1))
                    && isBSurrounded(new Vector(pos.getX() - 1, pos.getY()))
                    && isBSurrounded(new Vector(pos.getX(), pos.getY() + 1));
        }
    }

    /**
     * checks if a white stone is circled or not
     *
     * @param pos the position of the white stone
     * @return if the white stone is circled
     */
    public boolean isWSurrounded(Vector pos) {
        if (this.getInter(pos) == Intersection.BlackStone || this.getInter(pos) == Intersection.OutOfBounds || this.getInter(pos) == Intersection.Surrounded)
            return true;
        else if (this.getInter(new Vector(pos.getX() + 1, pos.getY())) == Intersection.Nothing || this.getInter(new Vector(pos.getX() - 1, pos.getY())) == Intersection.Nothing || this.getInter(new Vector(pos.getX(), pos.getY() + 1)) == Intersection.Nothing || this.getInter(new Vector(pos.getX(), pos.getY() - 1)) == Intersection.Nothing) {
            setInter(pos, Intersection.Surrounded);
            this.rep(pos, Intersection.Surrounded, Intersection.WhiteStone);
            return false;
        } else {
            setInter(pos, Intersection.Surrounded);
            return isWSurrounded(new Vector(pos.getX() + 1, pos.getY()))
                    && isWSurrounded(new Vector(pos.getX(), pos.getY() - 1))
                    && isWSurrounded(new Vector(pos.getX() - 1, pos.getY()))
                    && isWSurrounded(new Vector(pos.getX(), pos.getY() + 1));
        }
    }

    /**
     * This method recursively replaces the state of a group of intersections
     * at given position from state a to state b if they were at state a
     *
     * @param pos the position of an intersection
     * @param a   the state of an intersection
     * @param b   the new state of the group
     * @return the number of changed state
     */
    public int replace(Vector pos, Intersection a, Intersection b) {
        if (this.getInter(pos).equals(a)) {
            this.setInter(pos, b);
            return 1
                    + replace(new Vector(pos.getX() + 1, pos.getY()), a, b)
                    + replace(new Vector(pos.getX(), pos.getY() - 1), a, b)
                    + replace(new Vector(pos.getX() - 1, pos.getY()), a, b)
                    + replace(new Vector(pos.getX(), pos.getY() + 1), a, b);
        }
        return 0;
    }

    /**
     * This method recursively replaces the state of the groups of intersections
     * that are adjacent to the  given position from state a to state b  only
     * if they were at state a
     *
     * @param pos the position of an intersection
     * @param a   the state of an intersection
     * @param b   the new state of the group
     * @return the number of changed state
     */
    public int replaceAdj(Vector pos, Intersection a, Intersection b) {
        return replace(new Vector(pos.getX() + 1, pos.getY()), a, b)
                + replace(new Vector(pos.getX(), pos.getY() - 1), a, b)
                + replace(new Vector(pos.getX() - 1, pos.getY()), a, b)
                + replace(new Vector(pos.getX(), pos.getY() + 1), a, b);
    }

    /**
     * Same as replace but doesn't count the changes
     */
    public void rep(Vector pos, Intersection a, Intersection b) {
        if (this.getInter(pos) == a) {
            this.setInter(pos, b);
            rep(new Vector(pos.getX() + 1, pos.getY()), a, b);
            rep(new Vector(pos.getX(), pos.getY() - 1), a, b);
            rep(new Vector(pos.getX() - 1, pos.getY()), a, b);
            rep(new Vector(pos.getX(), pos.getY() + 1), a, b);
        }
    }

    /**
     * Same as replaceAdj but doesn't count the changes
     */
    public void repAdj(Vector pos, Intersection a, Intersection b) {
        rep(new Vector(pos.getX() + 1, pos.getY()), a, b);
        rep(new Vector(pos.getX(), pos.getY() - 1), a, b);
        rep(new Vector(pos.getX() - 1, pos.getY()), a, b);
        rep(new Vector(pos.getX(), pos.getY() + 1), a, b);
    }

    /**
     * This methods returns the position of one of the last circled position,useful for
     * checking ko rule
     *
     * @param pos last played position
     * @return a position that was circled
     */
    public Vector getCircled(Vector pos) {
        if (this.getInter(new Vector(pos.getX() + 1, pos.getY())) == Intersection.Surrounded2)
            return new Vector(pos.getX() + 1, pos.getY());
        else if (this.getInter(new Vector(pos.getX() - 1, pos.getY())) == Intersection.Surrounded2)
            return new Vector(pos.getX() - 1, pos.getY());
        else if (this.getInter(new Vector(pos.getX(), pos.getY() + 1)) == Intersection.Surrounded2)
            return new Vector(pos.getX(), pos.getY() + 1);
        else if (this.getInter(new Vector(pos.getX(), pos.getY() - 1)) == Intersection.Surrounded2)
            return new Vector(pos.getX(), pos.getY() - 1);
        else
            return new Vector(-1, -1);
    }

    /**
     * This method checks if the position given makes it a Ko move.
     *
     * @param pos   the position of the played move
     * @param canKo if it is possible to do a Ko
     * @param bTurn if it is the turn of the black player
     * @param cir   the last circled position
     * @return if this move is a Ko
     */
    public boolean isKo(Vector pos, boolean canKo, boolean bTurn, Vector cir) {
        if (bTurn)
            this.setInter(pos, Intersection.BlackStone);
        else
            this.setInter(pos, Intersection.WhiteStone);
        //sets to circled the circled position next to pos
        this.surrounds(pos);

        // if he can ko,he plays the same former positon,and the gets only one stone by doing that
        if (this.replaceAdj(pos, Intersection.Surrounded, Intersection.Surrounded2) == 1 && canKo && cir.equals(pos)) {
            if (bTurn)
                this.repAdj(pos, Intersection.Surrounded2, Intersection.WhiteStone);
            else
                this.repAdj(pos, Intersection.Surrounded2, Intersection.BlackStone);
            this.setInter(pos, Intersection.Nothing);
            return true;
        } else {
            if (bTurn)
                this.repAdj(pos, Intersection.Surrounded2, Intersection.WhiteStone);
            else
                this.repAdj(pos, Intersection.Surrounded2, Intersection.BlackStone);
            this.setInter(pos, Intersection.Nothing);
            return false;
        }

    }

    /**
     * This method checks if the given position  belongs to the black territory
     *
     * @param pos a given position
     * @return if the position belongs to the black territory
     */
    public boolean bTerritory(Vector pos) {
        if (this.getInter(pos) == Intersection.Nothing) {
            this.setInter(pos, Intersection.Surrounded2);
            return bTerritory(new Vector(pos.getX() + 1, pos.getY()))
                    && bTerritory(new Vector(pos.getX(), pos.getY() - 1))
                    && bTerritory(new Vector(pos.getX() - 1, pos.getY()))
                    && bTerritory(new Vector(pos.getX(), pos.getY() + 1));
        } else if (this.getInter(pos) == Intersection.WhiteStone) {
            this.repAdj(pos, Intersection.Surrounded2, Intersection.Nothing);
            return false;
        } else
            return true;
    }

    /**
     * This method checks if the given position  belongs to the white territory
     *
     * @param pos a given position
     * @return if the position belongs to the white territory
     */
    public boolean wTerritory(Vector pos) {
        if (this.getInter(pos) == Intersection.Nothing) {
            this.setInter(pos, Intersection.Surrounded2);
            return wTerritory(new Vector(pos.getX() + 1, pos.getY()))
                    && wTerritory(new Vector(pos.getX(), pos.getY() - 1))
                    && wTerritory(new Vector(pos.getX() - 1, pos.getY()))
                    && wTerritory(new Vector(pos.getX(), pos.getY() + 1));
        } else if (this.getInter(pos) == Intersection.BlackStone) {
            this.repAdj(pos, Intersection.Surrounded2, Intersection.Nothing);
            return false;
        } else
            return true;
    }

    /**
     * This methods counts all the territory of the black and the white player.
     *
     * @return the vector containing the territory of the black and the white player (x for black player)
     */
    public Vector area() {
        Vector area = new Vector();

        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                if (this.bTerritory(new Vector(i, k)))
                    area.setX(area.getX() + this.replace(new Vector(i, k), Intersection.Surrounded2, Intersection.BlackTerritory));
                if (this.wTerritory(new Vector(i, k)))
                    area.setY(area.getY() + this.replace(new Vector(i, k), Intersection.Surrounded2, Intersection.WhiteTerritory));
            }
        }
        return area;
    }

    /**
     * stores the last played position and the adjacent positions
     * the adjacent positions gets (-1,-1) if they weren't circled position
     * used for reviewing
     *
     * @param pos the last played position
     */
    public void setPlayed(Vector pos) {
        played[0] = pos;

        if (getInter(new Vector(pos.getX() + 1, pos.getY())) == Intersection.Surrounded2)
            played[1] = new Vector(pos.getX() + 1, pos.getY());
        else
            played[1] = new Vector(-1, -1);

        if (getInter(new Vector(pos.getX() - 1, pos.getY())) == Intersection.Surrounded2)
            played[2] = new Vector(pos.getX() - 1, pos.getY());
        else
            played[2] = new Vector(-1, -1);

        if (getInter(new Vector(pos.getX(), pos.getY() + 1)) == Intersection.Surrounded2)
            played[3] = new Vector(pos.getX(), pos.getY() + 1);
        else
            played[3] = new Vector(-1, -1);

        if (getInter(new Vector(pos.getX(), pos.getY() - 1)) == Intersection.Surrounded2)
            played[4] = new Vector(pos.getX(), pos.getY() - 1);
        else
            played[4] = new Vector(-1, -1);

    }

    /**
     * Updates the game review when playing
     */
    public void updateReview() {
        if (this.turn < this.playedReview.size()) {
            for (int i = this.playedReview.size() - 1; i > this.turn - 1; i--) {
                this.playedReview.remove(i);
                this.interReview.remove(i);
            }
        }
    }

    /**
     * copies the played positions (with adjacent positions)
     *
     * @return the played positions
     */
    public Vector[] copyPlayed() {
        Vector[] cp = new Vector[5];
        for (int i = 0; i < 5; i++)
            cp[i] = new Vector(this.played[i]);
        return cp;
    }
}