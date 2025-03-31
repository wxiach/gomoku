package cn.wxiach.ui;

import cn.wxiach.ui.components.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ControlPanel extends JPanel {

    private static final int PANEL_WIDTH = 200;

    private final PieceRadio pieceRadio;
    private final DifficultRadio difficultRadio;

    public ControlPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(24, 16, 24, 16));

        add(new TimeBoardPanel());
        add(Box.createVerticalStrut(24));

        add(new StartGameButton());
        add(Box.createVerticalStrut(24));

        add( new SurrenderButton());
        add(Box.createVerticalStrut(24));

        add( new RevertButton());
        add(Box.createVerticalStrut(48));

        add(ComponentUtils.createHorizontalSeparator());

        add(Box.createVerticalStrut(24));
        pieceRadio = new PieceRadio();
        add(pieceRadio);
        add(Box.createVerticalStrut(24));

        difficultRadio = new DifficultRadio();
        add(difficultRadio);
    }

    public PieceRadio getPieceColorSelectButton() {
        return this.pieceRadio;
    }

    public DifficultRadio getDifficultRadio() {
        return this.difficultRadio;
    }

}
