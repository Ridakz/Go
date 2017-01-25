import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;

/**
 * This class uses GoBoard and the  GUI and makes the link between them
 * to make a playable go game. It adds listener to the Swing component in the GUI
 * to take user input and uses a GoBoard to store data and manage the rules of
 * game with GoBoard's methods.
 * 
 * It adds options for the users, like reviewing the game, playing with powers or saving the game ...
 * 
 * The management looks like the MVC model, GoManager would be the controller,the GUI the view
 * and GoBoard the model.
 *
 *
 *@version 30/05/2016
 *@author Kadir Ercin
 */
public class GoManager implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** the board of the game*/
	private	 GoBoard board = new GoBoard(Intersection.Nothing, 10);
	
	/** Labels representing each intersection of the GUI*/
	private	 JLabel grid[][];

	/** size of the GUI grid in pixel,used for rescaling*/
	private final int gridPixel = 450;


	static GoManager goGame;
	static GoInterface goGUI;

	/**
	 * Creates a new GoManager
	 * 
	 * @param N the size of the board
	 */
	public GoManager(int N) {
		board = new GoBoard(Intersection.Nothing, N);
		grid = new JLabel[N][N];
	}
     

	
	/** 
	 * Saves the game with the help of the Serializable interface, and creates the file "save.ser"
	 */
	public void saveGame() {
		try {
			FileOutputStream fileOut = new FileOutputStream("save.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	/** 
	 * Loads the game if there is a save
	 * 
	 * @return the GoManager matching the save
	 */
	public static GoManager loadGame() {
		GoManager load = null;
		
		try {
			FileInputStream fileIn = new FileInputStream("save.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			load = (GoManager) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
			return null;
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			return null;
		}
		return load;
	}



	/**
	 * retrieves the captured stones,used when reviewing backward.
	 */
	public void retrieve() {
		for (int i = 1; i < 5; i++) {
			// if != 1 they were circled , and we check wich color was the last played to retrieve stones
			if (this.board.playedReview.get(this.board.turn)[i].getX() != -1 && this.board.getInter(this.board.playedReview.get(this.board.turn)[0])==Intersection.WhiteStone)
				this.board.blackStones += this.remStones(this.board.playedReview.get(this.board.turn)[i], Intersection.Nothing,Intersection.BlackStone);
			else
				this.board.whiteStones += this.remStones(this.board.playedReview.get(this.board.turn)[i], Intersection.Nothing,Intersection.WhiteStone);
		}
		// for removed power, need to retrieve played[0] last to replace stones correctly
		if (this.board.interReview.get(this.board.turn) == Intersection.BlackTerritory) {
			this.board.setInter(this.board.playedReview.get(this.board.turn)[0], Intersection.BlackStone);
			this.board.blackStones++;
		} else if (this.board.interReview.get(this.board.turn) == Intersection.WhiteTerritory) {
			this.board.setInter(this.board.playedReview.get(this.board.turn)[0], Intersection.WhiteStone);
			this.board.whiteStones++;
		} else
			this.board.setInter(this.board.playedReview.get(this.board.turn)[0], Intersection.Nothing);
		ImageIcon icon = new ImageIcon(this.getIcon(this.board.playedReview.get(this.board.turn)[0]).getImage().getScaledInstance(this.gridPixel / this.board.size, this.gridPixel / this.board.size, Image.SCALE_SMOOTH));
		this.grid[this.board.playedReview.get(this.board.turn)[0].getX()][this.board.playedReview.get(this.board.turn)[0].getY()].setIcon(icon);
	}



	/**
	 * launches the menu GUI and adds listener to it
	 * 
	 * @param gi a reference to a GoInterface,needed when a player goes to the menu but want to continue the game
	 */
	public static void menu(GoInterface gi) {
		GoMenu menu = new GoMenu();
		JPanel menuPanel =(JPanel) menu.getContentPane();
		menu.setTitle("Menu");
		menu.setLocationRelativeTo(null);
		menu.setIconImage(new ImageIcon("Go_Pictures/go.png").getImage());
		menu.setResizable(false);
		menu.setVisible(true);
		
		menu.newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menu.setContentPane(menu.panel);
				menu.setSize(175, 300);
				if(gi!=null)
					gi.dispose();
			}
		});
		
		menu.loadGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GoManager loaded = null;
				loaded = loadGame();
				menu.dispose();
				game(loaded.board.size, loaded,false);
			}
		});
		
		menu.continueGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(gi!=null) {
					menu.dispose();
					gi.setVisible(true);
				}
			}
		});

		menu.start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menu.setVisible(false);
				menu.dispose();
				game(menu.sizes.getSelectedIndex() + 6, null,menu.power.isSelected());
			}
		});

		menu.handicap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menu.dispose();
				handicap(menu.sizes.getSelectedIndex() + 6,menu.power.isSelected());
			}
		});
		
		menu.menu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menu.setContentPane(menuPanel);
				menu.setSize(178, 270);
			}
		});
	}

	/**
	 * Gives  the picture fitting a position in the GUI grid
	 *
	 * @param pos the position of the intersection
	 *
	 * @return the picture fitting the intersection
	 */
	public ImageIcon getIcon(Vector pos) {
		String inter = new String();
		String position = new String();

		if (board.getInter(pos).equals(Intersection.BlackStone))
			inter = new String("Black");
		else if (board.getInter(pos).equals(Intersection.WhiteStone))
			inter = new String("White");
		else if (board.getInter(pos).equals(Intersection.Nothing)) {
            if (isHoshi(pos))
                return new ImageIcon("Go_Pictures/Hoshi.png");
            inter = new String("Nothing");
        }
		else if (board.getInter(pos).equals(Intersection.BlackTerritory))
			return new ImageIcon("Go_Pictures/BlackTerritory.png");
		else if (board.getInter(pos).equals(Intersection.WhiteTerritory))
			return new ImageIcon("Go_Pictures/WhiteTerritory.png");
		else
			return new ImageIcon("Go_Pictures/TEST.png");

		if (pos.getX() == 0 && pos.getY() == 0)
			position = new String("_TopLeft");
		else if (pos.getX() == 0 && pos.getY() == this.board.size - 1)
			position = new String("_BotLeft");
		else if (pos.getX() == this.board.size - 1 && pos.getY() == this.board.size - 1)
			position = new String("_BotRight");
		else if (pos.getX() == this.board.size - 1 && pos.getY() == 0)
			position = new String("_TopRight");
		else if (pos.getX() == 0)
			position = new String("_Left");
		else if (pos.getX() == this.board.size - 1)
			position = new String("_Right");
		else if (pos.getY() == 0)
			position = new String("_Top");
		else if (pos.getY() == this.board.size - 1)
			position = new String("_Bot");
		else
			position = new String("");

		return new ImageIcon("Go_Pictures/"+inter + position + ".png");
	}

    /**
     *
     * @param pos the position in the board
     * @return if the position is a hoshi (star,intersection with a dot) for 9,13,19 board sizes
     */
    private boolean isHoshi(Vector pos) {
        if(board.size == 9) {
            return (pos.getX() == 2 || pos.getX() == 6 ) && (pos.getY() == 2 || pos.getY() == 6);
        }
        if(board.size == 13) {
            return (pos.getX() == 3 || pos.getX() == 9 || pos.getX() == 6 ) && (pos.getY() == 3 || pos.getY() == 9 || pos.getY() ==6 );
        }
        if(board .size == 19) {
            return (pos.getX() == 3 || pos.getX() == 15 || pos.getX() == 9 ) && (pos.getY() == 3 || pos.getY() == 15 || pos.getY() ==9 );
        }
        return  false;
    }

    /**
	 * Gives  the picture fitting a position in the GUI grid
	 *
	 * @param pos the position of the intersection
     * @param inters the intersection state
	 *
	 * @return the picture fitting the intersection
	 */
    public ImageIcon getIcon(Vector pos,Intersection inters) {
        String inter = new String();
        String position = new String();


        if (inters.equals(Intersection.BlackStone))
            inter = new String("Black");
        else if (inters.equals(Intersection.WhiteStone))
            inter = new String("White");
        else if (inters.equals(Intersection.Nothing))
            inter = new String("Nothing");
        else if (inters.equals(Intersection.BlackTerritory))
            return new ImageIcon("Go_Pictures/BlackTerritory.png");
        else if (inters.equals(Intersection.WhiteTerritory))
            return new ImageIcon("Go_Pictures/WhiteTerritory.png");
        else if(inters.equals(Intersection.Surrounded))
            return new ImageIcon("Go_Pictures/Surrounded.png");
        else
            return new ImageIcon("Go_Pictures/TEST.png");

        if (pos.getX() == 0 && pos.getY() == 0)
            position = new String("_TopLeft");
        else if (pos.getX() == 0 && pos.getY() == this.board.size - 1)
            position = new String("_BotLeft");
        else if (pos.getX() == this.board.size - 1 && pos.getY() == this.board.size - 1)
            position = new String("_BotRight");
        else if (pos.getX() == this.board.size - 1 && pos.getY() == 0)
            position = new String("_TopRight");
        else if (pos.getX() == 0)
            position = new String("_Left");
        else if (pos.getX() == this.board.size - 1)
            position = new String("_Right");
        else if (pos.getY() == 0)
            position = new String("_Top");
        else if (pos.getY() == this.board.size - 1)
            position = new String("_Bot");
        else
            position = new String("");

        return new ImageIcon("Go_Pictures/"+inter + position + ".png");
    }
	
	/**
	 * Similar to replace in the GoBoard,this one updates the GUI pictures recursively too
	 * when removing stones.
	 * 
	 * @param pos the position of an intersection
	 * @param a the state of an intersection
	 * @param b the new state of the group
	 * 
	 * @return the number of changed intersection
	 */
	public int remStones(Vector pos, Intersection a, Intersection b) {
		if (this.board.getInter(pos) == a) {
			this.board.setInter(pos, b);
			this.grid[pos.getX()][pos.getY()].setIcon(new ImageIcon(this.getIcon(new Vector(pos.getX(), pos.getY())).getImage().getScaledInstance(this.gridPixel / board.size, this.gridPixel / board.size, Image.SCALE_SMOOTH)));
			return 1 + remStones(new Vector(pos.getX() + 1, pos.getY()), a, b)
					+ remStones(new Vector(pos.getX(), pos.getY() - 1), a, b)
					+ remStones(new Vector(pos.getX() - 1, pos.getY()), a, b)
					+ remStones(new Vector(pos.getX(), pos.getY() + 1), a, b);
		}
		return 0;
	}

	/**
	 * same as replaceAdj in GoBoard
	 */
	public int remAdjacentStones(Vector pos, Intersection a, Intersection b) {
		return  remStones(new Vector(pos.getX() + 1, pos.getY()), a, b)
				+ remStones(new Vector(pos.getX(), pos.getY() - 1), a, b)
				+ remStones(new Vector(pos.getX() - 1, pos.getY()), a, b)
				+ remStones(new Vector(pos.getX(), pos.getY() + 1), a, b);
	}

	/** 
	 * Launches the game, adds listener to the GoInterface GUI and uses
	 * GoBoard methods to manage the rules of the go/legal moves
	 * of the player
	 * 
	 * @param size		the size of the board
	 * @param handicap  the GoManager of the handicap != null if there is a handicap
	 * @param withPower if the game is played with powers
	 */
	public static void game(int size, GoManager handicap,boolean withPower) {
		GoManager temp = null;
		
		if(handicap==null)
			temp = new GoManager(size);
		else
			temp=handicap;
		
		 goGame = temp;
		 goGUI = new GoInterface(size);

		
		goGUI.setTitle("Go Game");
		goGUI.setLocationRelativeTo(null);
		goGUI.setResizable(false);
		goGUI.setIconImage(new ImageIcon("Go_Pictures/go.png").getImage());

        /*  Powers not supported anymore */
        goGUI.blackBar.setVisible(false);
        goGUI.whiteBar.setVisible(false);
        goGUI.twoTurns.setVisible(false);
        goGUI.enemy.setVisible(false);
        goGUI.delete.setVisible(false);
        goGUI.setSize(goGUI.getWidth(),goGUI.getHeight()-60);


		goGUI.update(goGame.board);

		goGUI.save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goGame.saveGame();
			}
		});
		
		goGUI.menu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goGUI.setVisible(false);
				menu(goGUI);
			}
		});
		
		goGUI.saveMoves.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PrintWriter writer =null;
				try {
					writer = new PrintWriter("Moves_History.txt");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				if(goGame.board.history.charAt(0)!='S') {
					goGame.board.history="Size : "+goGame.board.size+"\n"+goGame.board.history;
				}	
				int length=goGame.board.history.length();
				
				for(int i=0;i<length;i++) {
					if(goGame.board.history.charAt(i)!='\n')
						writer.append(goGame.board.history.charAt(i));
					else 	
						writer.println(""); 	//Changes line
				}
				writer.close();
			}
		});

		goGUI.previous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                if (goGame.board.turn > 0 ) {
					int blackStoneCount = goGame.board.blackStones;
					int whiteStoneCount = goGame.board.whiteStones;
					
					if(goGame.board.passed) {
						goGame.board.passed=false;
						goGUI.pass.setSelected(false);
					}
					goGame.board.turn--;
					
					goGame.retrieve();		// retrieves former captured stones

                    if (goGame.board.interReview.get(goGame.board.turn) == Intersection.BlackStone) {
						goGame.board.blackStones--;
						goGame.board.blackCaptured-=goGame.board.whiteStones - whiteStoneCount;
						goGame.board.bTurn = true;
					} else if (goGame.board.interReview.get(goGame.board.turn) == Intersection.WhiteStone) {
						goGame.board.whiteStones--;
						goGame.board.whiteCaptured-=goGame.board.blackStones - blackStoneCount;
						goGame.board.bTurn = false;
					} 


					if (goGame.board.turn - 1 >= 0 && goGame.board.lastKo.equals(goGame.board.playedReview.get(goGame.board.turn - 1)[0]))
						goGame.board.canKo = true;
					goGame.board.history = new String(goGame.board.history.substring(0, 1+goGame.board.history.lastIndexOf('\n', goGame.board.history.lastIndexOf('n')-1)));

                }
				goGUI.update(goGame.board);
			}
		});
   
		goGUI.next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (goGame.board.turn < goGame.board.playedReview.size() ) {
						int blackStoneCount = goGame.board.blackStones;
						int whiteStoneCount = goGame.board.whiteStones;
						Vector lastPlayed = new Vector(goGame.board.playedReview.get(goGame.board.turn)[0]);
						
						if(goGame.board.passed) {
							goGame.board.passed=false;
							goGUI.pass.setSelected(false);
						}
						goGame.board.turn++;
						goGame.board.setInter(lastPlayed, goGame.board.interReview.get(goGame.board.turn - 1));
						
						//for removed power, instead of putting a stone we remove one
						if(goGame.board.getInter(lastPlayed)!= Intersection.BlackStone && goGame.board.getInter(lastPlayed)!= Intersection.WhiteStone)
							goGame.board.setInter(lastPlayed, Intersection.Nothing);
						
						ImageIcon icon = new ImageIcon(goGame.getIcon(lastPlayed).getImage().getScaledInstance(goGame.gridPixel / size, goGame.gridPixel / size, Image.SCALE_SMOOTH));
						goGame.grid[lastPlayed.getX()][lastPlayed.getY()].setIcon(icon);
	
						if (goGame.board.surrounds(lastPlayed)) {
							if(goGame.board.getInter(lastPlayed)==Intersection.BlackStone)
								goGame.board.whiteStones -= goGame.remAdjacentStones(lastPlayed, Intersection.Surrounded,Intersection.Nothing);
							else
								goGame.board.blackStones -= goGame.remAdjacentStones(lastPlayed, Intersection.Surrounded,Intersection.Nothing);
						}
						
						if (goGame.board.interReview.get(goGame.board.turn-1) == Intersection.BlackStone)
								goGame.board.bTurn = false;
						else if (goGame.board.interReview.get(goGame.board.turn - 1) == Intersection.WhiteStone)
							goGame.board.bTurn = true;
						else 
							goGame.board.bTurn = !goGame.board.bTurn;

						goGame.board.history = new String(goGame.board.history + "Turn " + goGame.board.turn + "  "+ (goGame.board.size - lastPlayed.getY()) + (char) ('A' + lastPlayed.getX()) + "\n");
						goGame.board.blackCaptured-=goGame.board.whiteStones - whiteStoneCount;
						goGame.board.whiteCaptured-= goGame.board.blackStones -blackStoneCount;
	
						switch (goGame.board.interReview.get(goGame.board.turn - 1)) {
						case BlackStone:
							goGame.board.blackStones++;
							break;
							
						case WhiteStone:
							goGame.board.whiteStones++;
							break;
							
						case BlackTerritory:
							goGame.board.blackStones--;
							goGame.board.setInter(lastPlayed, Intersection.Nothing);
							break;
							
						default:
							goGame.board.whiteStones--;
							goGame.board.setInter(lastPlayed, Intersection.Nothing);
							break;
						}
						goGUI.update(goGame.board);

				}
			}
		});

		for (int i = 0; i < size * size; i++) {
			
			final int index = i;
			final Vector position = new Vector(index-(index/size)*size,index/size);
			
			// rescales the icon to fit
			ImageIcon icon = new ImageIcon(goGame.getIcon(position).getImage().getScaledInstance(goGame.gridPixel / size, goGame.gridPixel / size, Image.SCALE_SMOOTH));
			// adds the icon to the grid
			goGame.grid[position.getX()][position.getY()] = new JLabel(icon);
			
			MouseAdapter ml = new MouseAdapter() {
				public void mousePressed(MouseEvent me) {


				    goGame.board.play(position);

                    goGame.board.passed = false;
                    goGUI.pass.setSelected(false);

				    if(index>=0)
				        return;
				}
				
				public void mouseEntered(MouseEvent e) {
						if(goGame.board.getInter(position)==Intersection.Nothing) {
							ImageIcon icon =null;
							if(goGame.board.bTurn) {
								icon = new ImageIcon(new ImageIcon("Go_Pictures/BlackBlur2.png").getImage().getScaledInstance(
								goGame.gridPixel / size, goGame.gridPixel / size, Image.SCALE_SMOOTH));
								} else {
									 icon = new ImageIcon(new ImageIcon("Go_Pictures/WhiteBlur2.png").getImage().getScaledInstance(
									 goGame.gridPixel / size, goGame.gridPixel / size, Image.SCALE_SMOOTH));
								}
								goGame.grid[position.getX()][position.getY()].setIcon(icon);
						}
				}
				
				public void mouseExited(MouseEvent e) {
						if(goGame.board.getInter(position)==Intersection.Nothing) {
							ImageIcon icon = new ImageIcon(goGame.getIcon(position).getImage().getScaledInstance(
							goGame.gridPixel / size, goGame.gridPixel / size, Image.SCALE_SMOOTH));
							goGame.grid[position.getX()][position.getY()].setIcon(icon);
						}
					}
			};
			
			//Adds the mouse listener to every label in the grid
			goGame.grid[position.getX()][position.getY()].addMouseListener(ml);
			//Adds every label to the gridPanel
			goGUI.panel.add(goGame.grid[position.getX()][position.getY()]);
		}
		
		goGUI.pass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					if (!goGame.board.passed) {
						goGame.board.passed = true;
						goGame.board.bTurn = !goGame.board.bTurn;
						if(goGame.board.bTurn) {
				    		goGUI.blackAvatar.setIcon(new ImageIcon("Go_Pictures/BlackStoneAv.png"));
					        goGUI.whiteAvatar.setIcon(new ImageIcon("Go_Pictures/WhiteStoneAvDark.png"));
				    	} else {
				    		goGUI.blackAvatar.setIcon(new ImageIcon("Go_Pictures/BlackStoneAvDark.png"));
				    		goGUI.whiteAvatar.setIcon(new ImageIcon("Go_Pictures/WhiteStoneAv.png"));
				    	}
						goGame.board.history+="Passed ";
					} else {
						Vector area = new Vector();
						area = goGame.board.area();
						
						for (int i = 0; i < size; i++) {
							for (int k = 0; k < size; k++) {
								ImageIcon icon = new ImageIcon(goGame.getIcon(new Vector(i, k)).getImage().getScaledInstance(goGame.gridPixel / size, goGame.gridPixel / size, Image.SCALE_SMOOTH));
								goGame.grid[i][k].setIcon(icon);
							}
						}
						
						String score = new String("");
						if (area.getX() + goGame.board.blackStones > area.getY() + goGame.board.whiteStones +6 )
							JOptionPane.showMessageDialog(goGUI, score+="Black player won with an area of "+ (area.getX() + goGame.board.blackStones) + " vs " + (area.getY() + goGame.board.whiteStones)+"+"+6.5+"(Komi)");
						else
							JOptionPane.showMessageDialog(goGUI, score+="White player won with an area of "+ (area.getY() + goGame.board.whiteStones)+"+"+6.5+"(Komi)" + " vs " + (area.getX() + goGame.board.blackStones));
						
						for(int i=0;i<goGame.board.size;i++) {
							for(int k=0;k<goGame.board.size;k++)
								//Makes it impossible for the user to click after the end of the game
								goGame.board.setInter(new Vector(i,k),Intersection.Surrounded);
						}
						goGame.board.history+=score;
						goGUI.pass.setVisible(false);
						goGUI.previous.setVisible(false);
						goGUI.save.setVisible(false);
						goGUI.next.setVisible(false);
					}

		}
	 });
		goGUI.setVisible(true);
	}

	public static void handicap(int size,boolean withpower) {
		Handicap handicapInterface = new Handicap(size);
		GoManager handicap = new GoManager(size);
		
		handicapInterface.setLocationRelativeTo(null);
		handicapInterface.setTitle("Handicap setting");
		handicapInterface.setIconImage(new ImageIcon("Go_Pictures/go.png").getImage());
		handicapInterface.setResizable(false);

		for (int i = 0; i < size * size; i++) {
			final int index = i;
			int y = index / size;
			int x = index - y * size;
			
			Vector pos = new Vector(x, y);
			ImageIcon icon = new ImageIcon(handicap.getIcon(pos).getImage().getScaledInstance(450 / size, 450 / size, Image.SCALE_SMOOTH));
			JLabel inter = new JLabel(icon);
			
			inter.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent me) {
					ImageIcon icon = new ImageIcon();
					if (handicap.board.getInter(pos) != Intersection.Nothing) {
						if (handicap.board.getInter(pos) == Intersection.BlackStone)
							handicap.board.blackStones--;
						else
							handicap.board.whiteStones--;
						handicap.board.setInter(pos, Intersection.Nothing);
						icon = new ImageIcon(handicap.getIcon(pos).getImage().getScaledInstance(450 / size, 450 / size,Image.SCALE_SMOOTH));
						inter.setIcon(icon);
					} else if (SwingUtilities.isLeftMouseButton(me)) {
						handicap.board.setInter(pos, Intersection.BlackStone);
						handicap.board.blackStones++;
						icon = new ImageIcon(handicap.getIcon(pos).getImage().getScaledInstance(450 / size, 450 / size,Image.SCALE_SMOOTH));
						inter.setIcon(icon);

					} else if (SwingUtilities.isRightMouseButton(me)) {
						handicap.board.setInter(pos, Intersection.WhiteStone);
						handicap.board.whiteStones++;
						icon = new ImageIcon(handicap.getIcon(pos).getImage().getScaledInstance(450 / size, 450 / size,Image.SCALE_SMOOTH));
						inter.setIcon(icon);
					}
				}

			});
			handicapInterface.panel.add(inter);
		}
		handicapInterface.setVisible(true);

		handicapInterface.handicap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handicap.board.history= new String("Handicaps :\n");
				for(int i =0;i<handicap.board.size;i++) {
					for(int k=0;k<handicap.board.size;k++) {
						if(handicap.board.getInter(new Vector(i,k))==Intersection.BlackStone)
							handicap.board.history+="Black : "+(handicap.board.size - k) + (char) ('A' + i )+ "\n";
						else if(handicap.board.getInter(new Vector(i,k))==Intersection.WhiteStone)
							handicap.board.history+="White : "+(handicap.board.size - k )+ (char) ('A' + i )+ "\n";
					}
				}
				handicap.board.history+="------------------\n";
				handicapInterface.setVisible(false);
				handicapInterface.dispose();
				game(size, handicap,withpower);
			}
		});
	}
	
	/**
	 * launches the menu
	 * 
	 * @param args main args
	 */
	public static void main(String[] args) {
		menu(null);
	}

    public void setViewInter(Vector position , Intersection inter) {
        ImageIcon icon = new ImageIcon(getIcon(position,inter).getImage().getScaledInstance(gridPixel / board.size, gridPixel / board.size, Image.SCALE_SMOOTH));
        grid[position.getX()][position.getY()].setIcon(icon);
    }
}