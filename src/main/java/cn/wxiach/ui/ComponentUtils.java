package cn.wxiach.ui;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class ComponentUtils {

    public static JPanel createHorizontalContainer(Component...components) {
        JPanel container = new JPanel(new FlowLayout());
        Arrays.stream(components).forEach(container::add);
        return container;
    }

    public static JSeparator createHorizontalSeparator() {
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 4));
        return separator;
    }
}
