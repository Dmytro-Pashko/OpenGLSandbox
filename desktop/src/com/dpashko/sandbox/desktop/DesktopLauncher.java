package com.dpashko.sandbox.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.dpashko.sandbox.Sandbox;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Sandbox");
        config.useOpenGL3(true, 3, 2);
        new Lwjgl3Application(new Sandbox(), config);
    }
}
