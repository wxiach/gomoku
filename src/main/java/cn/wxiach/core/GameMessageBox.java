package cn.wxiach.core;

import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.GameExitEvent;
import cn.wxiach.model.Piece;

import javax.swing.*;

public class GameMessageBox {

    public static Piece.Color showPieceSelectionDialog(JFrame parent) {
        Object[] options = {"Black", "White"};
        int option = JOptionPane.showOptionDialog(parent, "Please select your piece .", "Piece Selection",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (option == JOptionPane.CLOSED_OPTION) {
            GomokuEventBus.getInstance().publish(new GameExitEvent(parent));
        }

        return option == 0 ? Piece.Color.BLACK : Piece.Color.WHITE;
    }

    public static boolean showGameOverOptionDialog(JFrame parent, Piece.Color winner) {
        String winnerStr = winner == Piece.Color.BLACK ? "Black" : "White";
        int option = JOptionPane.showConfirmDialog(parent, String.format("%s win. Want to go for another round?", winnerStr),
                        "确认", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            return true;
        } else {
            GomokuEventBus.getInstance().publish(new GameExitEvent(parent));
            return false;
        }
    }
}
