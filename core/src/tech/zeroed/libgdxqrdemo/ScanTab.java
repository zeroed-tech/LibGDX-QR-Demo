package tech.zeroed.libgdxqrdemo;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisImageTextButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextArea;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import tech.zeroed.libgdxqr.CodeScanned;
import tech.zeroed.libgdxqr.QRCode;

public class ScanTab extends Tab implements Updatable{
    VisTable content;

    public ScanTab() {
        super(false, false);
        content = new VisTable();

        VisImageTextButton scan = new VisImageTextButton("Scan", new TextureRegionDrawable(QRCode.CreateGenerator().blockSize(1).generate("Zeored.tech")));
        final VisLabel statusLabel = new VisLabel("Scan Status: Waiting...");
        final VisTextArea response = new VisTextArea();

        content.add(scan).pad(20, 20, 0, 20);
        content.add(statusLabel).pad(20, 0, 0, 20).growX().row();

        content.add(response).colspan(2).grow().pad(20).row();

        scan.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    QRCode.scanQRCode(new CodeScanned() {
                        @Override
                        public void OnCodeScanned(String code) {
                            response.setText(code);
                            statusLabel.setText("Scanned");
                        }

                        @Override
                        public void OnCodeScanError(String error) {
                            response.setText("Error scanning");
                            statusLabel.setText(error);
                        }
                    });
                }catch (IllegalStateException ignore){
                    response.setText("Error scanning");
                    statusLabel.setText("The current platform does not support scanning");
                }
            }
        });

    }

    @Override
    public String getTabTitle() {
        return "Scan";
    }

    @Override
    public Table getContentTable() {
        return content;
    }

    public void update(){

    }
}
