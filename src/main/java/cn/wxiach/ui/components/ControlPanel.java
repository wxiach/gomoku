package cn.wxiach.ui.components;

import cn.wxiach.ui.components.buttons.StartGameButton;
import cn.wxiach.ui.components.buttons.PieceColorSelectButton;
import cn.wxiach.ui.components.buttons.RevertChessButton;
import cn.wxiach.ui.components.buttons.SurrenderButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ControlPanel extends JPanel {

    private static final int PANEL_WIDTH = 200;

    private final StartGameButton startGameButton;
    private final SurrenderButton surrenderButton;
    private final RevertChessButton revertChessButton;
    private final PieceColorSelectButton pieceColorSelectButton;

    public ControlPanel() {

        setPreferredSize(new Dimension(PANEL_WIDTH, 0));

        setBorder(new EmptyBorder(24, 16, 24, 16));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        startGameButton = new StartGameButton();
        add(startGameButton);
        add(Box.createVerticalStrut(24));

        surrenderButton = new SurrenderButton();
        add(surrenderButton);
        add(Box.createVerticalStrut(24));

        revertChessButton = new RevertChessButton();
        add(revertChessButton);
        add(Box.createVerticalStrut(24));

        pieceColorSelectButton = new PieceColorSelectButton();
        add(pieceColorSelectButton);
    }

    public PieceColorSelectButton getPieceColorSelectButton() {
        return this.pieceColorSelectButton;
    }

}
