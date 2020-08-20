package tech.zeroed.libgdxqrdemo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import tech.zeroed.libgdxqr.CodeScanned;
import tech.zeroed.libgdxqr.QRCode;

import java.io.UnsupportedEncodingException;

public class ScanTab extends Tab implements Updatable{
    VisTable content;

    public ScanTab() {
        super(false, false);
        content = new VisTable();

        VisImageTextButton scan = new VisImageTextButton("Scan", new TextureRegionDrawable(QRCode.CreateGenerator().blockSize(1).generate("Zeored.tech")));
        final VisLabel statusLabel = new VisLabel("Scan Status: Waiting...");
        final VisLabel response = new VisLabel();
        VisScrollPane responsePane = new VisScrollPane(response);
        response.setWrap(true);

        content.add(scan).pad(20, 20, 0, 20);
        content.add(statusLabel).pad(20, 0, 0, 20).growX().row();

        content.add(responsePane).colspan(2).grow().pad(20).row();

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
                        public void OnCodeScanned(byte[] code) {
                            response.setText(HexDumpUtil.formatHexDump(code, 0, code.length));
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

final class HexDumpUtil {
    public static String formatHexDump(byte[] array, int offset, int length) {
        final int width = 16;

        StringBuilder builder = new StringBuilder();

        for (int rowOffset = offset; rowOffset < offset + length; rowOffset += width) {
            builder.append(String.format("%06d:  ", rowOffset));

            for (int index = 0; index < width; index++) {
                if (rowOffset + index < array.length) {
                    builder.append(String.format("%02x ", array[rowOffset + index]));
                } else {
                    builder.append("   ");
                }
            }

            if (rowOffset < array.length) {
                int asciiWidth = Math.min(width, array.length - rowOffset);
                builder.append("  |  ");
                try {
                    builder.append(new String(array, rowOffset, asciiWidth, "UTF-8").replaceAll("\r\n", " ").replaceAll("\n", " "));
                } catch (UnsupportedEncodingException ignored) {
                    //If UTF-8 isn't available as an encoding then what can we do?!
                }
            }

            builder.append(String.format("%n"));
        }

        return builder.toString();
    }
}
