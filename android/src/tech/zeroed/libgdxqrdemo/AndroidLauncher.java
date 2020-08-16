package tech.zeroed.libgdxqrdemo;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import tech.zeroed.libgdxqr.AndroidQRCodeNativeInterface;
import tech.zeroed.libgdxqr.QRCode;
import tech.zeroed.libgdxqrdemo.LibGDXQRDemo;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		QRCode.init(new AndroidQRCodeNativeInterface(this));
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new LibGDXQRDemo(), config);
	}
}
