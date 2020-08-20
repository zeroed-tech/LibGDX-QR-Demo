package tech.zeroed.libgdxqrdemo;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import org.robovm.apple.uikit.UIInterfaceOrientationMask;
import org.robovm.apple.uikit.UIWindow;
import tech.zeroed.libgdxqr.IOSQRCodeNativeInterface;
import tech.zeroed.libgdxqr.QRCode;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        QRCode.init(new IOSQRCodeNativeInterface());
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new LibGDXQRDemo(), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    public UIInterfaceOrientationMask getSupportedInterfaceOrientations(UIApplication application, UIWindow window) {
        return UIInterfaceOrientationMask.All;
    }




}