package cn.wxiach.ui.assets;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class FontAssets {

    private static final String path = "/fonts/LXGWWenKaiMonoScreen.ttf";

    public static final Font LXGWWenKaiMonoScreen;

    static {
        try {
            InputStream resource = FontAssets.class.getResourceAsStream(path);
            if (resource == null) {
                throw new RuntimeException(String.format("[%s] resource is not founded.", path));
            }
            LXGWWenKaiMonoScreen = Font.createFont(Font.TRUETYPE_FONT, resource).deriveFont(16f);

            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            graphicsEnvironment.registerFont(LXGWWenKaiMonoScreen);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setGlobalFont(Font font) {
        UIManager.put("Label.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("Menu.font", font);
        UIManager.put("MenuItem.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("CheckBox.font", font);
        UIManager.put("RadioButton.font", font);
        UIManager.put("ComboBox.font", font);
        UIManager.put("Table.font", font);
        UIManager.put("List.font", font);
        UIManager.put("ToggleButton.font", font);
    }

}
