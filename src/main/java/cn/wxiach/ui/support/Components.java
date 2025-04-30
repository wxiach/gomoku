package cn.wxiach.ui.support;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Components {

    public static JPanel createHorizontalContainer(Component... components) {
        JPanel container = new JPanel(new FlowLayout());
        Arrays.stream(components).forEach(container::add);
        return container;
    }

    public static JSeparator createHorizontalSeparator() {
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 4));
        return separator;
    }


    public static <T extends JComponent> T createComponentWithMargin(
            JComponent parent, T component, int topMargin, int bottomMargin) {
        parent.add(Box.createVerticalStrut(topMargin));
        parent.add(component);
        parent.add(Box.createVerticalStrut(bottomMargin));
        return component;
    }
}
