package cn.wxiach.ui.common.layout;

import javax.swing.*;
import java.awt.*;

public class Layouts {

    /**
     * @param axis       必须为 BoxLayout.X_AXIS 或 BoxLayout.Y_AXIS
     * @param components 需要添加到容器的组件
     * @param gap        组件之间的间隔
     * @return 返回包含所有组件的JPanel容器
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

            // 除最后一个组件外，在每个组件后添加间隔
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
