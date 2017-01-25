import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * NetBeans generated GUI
 *
 * @author Kadir Ercin
 */
public class GoInterface extends JFrame {


	private static final long serialVersionUID = 1L;
	/**
     * Creates new form NewJFrame2
     */
    public GoInterface(int n) {
        initComponents(n);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    protected void initComponents(int n) {

        panel = new JPanel();
        whiteAvatar = new JLabel();
        blackAvatar = new JLabel();
        blackBar = new JProgressBar();
        whiteBar = new JProgressBar();
        blackScore = new JLabel();
        whiteScore = new JLabel();
        save = new JButton();
        menu = new JButton();
        pass = new JToggleButton();
        moveScroll = new JScrollPane();
        moves = new JTextArea();
        saveMoves = new JButton();
        movesLabel = new JLabel();
        previous = new JButton();
        next = new JButton();
        turns = new JLabel();
        twoTurns = new JButton();
        enemy = new JButton();
        delete = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        panel.setLayout(new GridLayout(n, n));

        whiteAvatar.setText("jLabel1");
        whiteAvatar.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));

        blackAvatar.setText("jLabel2");
        blackAvatar.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));

        blackScore.setText("Black Player");

        whiteScore.setText("White Player");

        save.setText("Save");

        menu.setText("Menu");

        pass.setText("Pass");

        moves.setColumns(20);
        moves.setRows(5);
        moveScroll.setViewportView(moves);

        saveMoves.setFont(new Font("Tahoma", 0, 10)); // NOI18N
        saveMoves.setText("Save Moves");

        movesLabel.setText("Moves");

        previous.setText("<-");

        next.setText("->");

        turns.setText("Turn X");

        twoTurns.setText("2 Turns");
        twoTurns.setMargin(new Insets(0, 0, 0, 0));

        enemy.setText("Play Enemy");
        enemy.setMargin(new Insets(0, 0, 0, 0));

        delete.setText("Delete");
        delete.setMargin(new Insets(0, 0, 0, 0));

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(panel, GroupLayout.PREFERRED_SIZE, 450, GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(movesLabel, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(moveScroll, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE))
                            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(saveMoves)
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(menu, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(save, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(delete, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(twoTurns, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(enemy, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(blackAvatar, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
                                .addGap(45, 45, 45))
                            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(blackBar, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34))))
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(blackScore, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(previous, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(next, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(pass, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
                            .addComponent(turns, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(whiteAvatar, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(whiteBar, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(whiteScore, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)))
                .addGap(54, 54, 54))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(blackAvatar, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(blackScore, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(blackBar, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(whiteAvatar, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(previous, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(next, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(turns, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(whiteScore)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(whiteBar, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pass)))))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(menu, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(save, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveMoves)
                        .addGap(18, 18, 18)
                        .addComponent(movesLabel, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(moveScroll, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE))
                    .addComponent(panel, GroupLayout.PREFERRED_SIZE, 450, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(twoTurns)
                    .addComponent(delete)
                    .addComponent(enemy))
                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>                        

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GoInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GoInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GoInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GoInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GoInterface(5).setVisible(true);
            }
        });
    }

    public void update(GoBoard board) {

            moves.setText(board.history);
            turns.setText("Turn "+board.turn);
            blackScore.setText("Black Player "+board.blackStones+"  "+board.blackCaptured);
            whiteScore.setText("White Player "+board.whiteStones+"  "+board.whiteCaptured);

        if(board.bTurn) {
            blackAvatar.setIcon(new ImageIcon("Go_Pictures/BlackStoneAv.png"));
            whiteAvatar.setIcon(new ImageIcon("Go_Pictures/WhiteStoneAvDark.png"));
        }
        else {
            blackAvatar.setIcon(new ImageIcon("Go_Pictures/BlackStoneAvDark.png"));
            whiteAvatar.setIcon(new ImageIcon("Go_Pictures/WhiteStoneAv.png"));
        }
    }

    // Variables declaration - do not modify                     
    protected JLabel blackAvatar;
    protected JProgressBar blackBar;
    protected JLabel blackScore;
    protected JButton delete;
    protected JButton enemy;
    protected JButton menu;
    protected JScrollPane moveScroll;
    protected JTextArea moves;
    protected JLabel movesLabel;
    protected JButton next;
    protected JPanel panel;
    protected JToggleButton pass;
    protected JButton previous;
    protected JButton save;
    protected JButton saveMoves;
    protected JLabel turns;
    protected JButton twoTurns;
    protected JLabel whiteAvatar;
    protected JProgressBar whiteBar;
    protected JLabel whiteScore;
    // End of variables declaration                   
}