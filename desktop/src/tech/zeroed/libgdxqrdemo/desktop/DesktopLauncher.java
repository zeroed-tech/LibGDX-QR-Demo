package tech.zeroed.libgdxqrdemo.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import tech.zeroed.libgdxqr.QRCode;
import tech.zeroed.libgdxqrdemo.LibGDXQRDemo;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int)(720*0.7f);
		config.height = (int)(1280*0.7f);
		config.resizable  = false;
		config.samples = 4;
		QRCode.init();
		new LwjglApplication(new LibGDXQRDemo(), config);
	}
}
