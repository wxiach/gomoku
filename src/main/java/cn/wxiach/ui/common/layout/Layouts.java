package cn.wxiach.ui.common.layout;

import javax.swing.*;
import java.awt.*;

public class Layouts {

    /**
     * @param axis       Should be one of BoxLayout.X_AXIS or BoxLayout.Y_AXIS
     * @param components Components to be added to the container
     * @param gap        Gap between components
     * @return
     */
    public static JPanel container(int axis, int gap, JComponent... components) {
        if (axis != BoxLayout.X_AXIS && axis != BoxLayout.Y_AXIS) {
            throw new IllegalArgumentException("Invalid axis: " + axis);
        }

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, axis));

        for (int i = 0; i < components.length; i++) {
            JComponent comp = components[i];

            if (axis == BoxLayout.Y_AXIS) {
                comp.setAlignmentX(JComponent.LEFT_ALIGNMENT);
                comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, comp.getPreferredSize().height));
            } else {
                comp.setAlignmentY(JComponent.TOP_ALIGNMENT);
            }

            container.add(comp);

            // Add gap after each component except the last one
            if (gap > 0 && i < components.length - 1) {
                if (axis == BoxLayout.Y_AXIS) {
                    container.add(Box.createVerticalStrut(gap));
                } else {
                    container.add(Box.createHorizontalStrut(gap));
                }
            }
        }

        return container;
    }
}
