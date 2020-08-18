package tech.zeroed.libgdxqrdemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.color.BasicColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerListener;
import com.kotcrab.vis.ui.widget.color.ExtendedColorPicker;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import tech.zeroed.libgdxqr.QRCode;
import tech.zeroed.libgdxqr.QRGenerator;

public class GenerateTab extends Tab implements Updatable {
    VisTable content;

    VisImage displayedCode;
    TextureRegion displayCodeTexture;

    private VisTextField input;
    private VisSlider blockSize;
    private VisLabel lblBlockSize;
    private VisLabel lblBorderSize;
    private VisSlider borderSize;

    VisSelectBox<QRGenerator.Shape> eyeOuterShape;
    VisSelectBox<QRGenerator.Shape> eyeInnerShape;
    VisSelectBox<QRGenerator.Shape> squareShape;

    private Color primaryColor;
    private Color secondaryColor;
    private boolean regenerateQRCode;
    private Stage stage;

    public GenerateTab(Stage stage) {
        super(false, false);
        this.stage = stage;
        content = new VisTable();

        VisTable left = new VisTable();
        VisTable right = new VisTable();

        TextureRegion question = new TextureRegion(new Texture("question.png"));

        input = new VisTextField();
        displayCodeTexture = QRCode.CreateGenerator().blockSize(12).generate("Zeroed.tech");
        displayedCode = new VisImage(displayCodeTexture);
        displayedCode.setScaling(Scaling.fillX);

        blockSize = new VisSlider(1, 30, 1, false);
        borderSize = new VisSlider(1, 5, 1, false);

        eyeOuterShape = new VisSelectBox<>();
        eyeOuterShape.setItems(QRGenerator.Shape.SQUARE, QRGenerator.Shape.ARC, QRGenerator.Shape.CIRCLE);
        eyeOuterShape.setSelected(QRGenerator.Shape.SQUARE);

        eyeInnerShape = new VisSelectBox<>();
        eyeInnerShape.setItems(QRGenerator.Shape.SQUARE, QRGenerator.Shape.CIRCLE);
        eyeInnerShape.setSelected(QRGenerator.Shape.SQUARE);

        squareShape = new VisSelectBox<>();
        squareShape.setItems(QRGenerator.Shape.SQUARE, QRGenerator.Shape.CIRCLE);
        squareShape.setSelected(QRGenerator.Shape.SQUARE);

        left.add(new VisLabel("QR Text: ")).fillX().pad(5);
        left.add(input).growX();
        VisImageButton questionButton = new VisImageButton(new TextureRegionDrawable(question));
        questionButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showHelp("Text to Encode", "Enter some text to encode into a QR code");
            }
        });
        left.add(questionButton).size(40,40).pad(10).row();

        // Block Size
        left.add(new VisLabel("Block Size: ")).fillX().pad(5);
        VisTable blockTable = new VisTable();
        blockTable.add(blockSize).growX();
        lblBlockSize = new VisLabel(""+blockSize.getValue());
        blockTable.add(lblBlockSize);
        left.add(blockTable).growX();
        questionButton = new VisImageButton(new TextureRegionDrawable(question));
        questionButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showHelp("Block Size", "Select the size of each square in pixels");
            }
        });
        left.add(questionButton).size(40,40).pad(10).row();

        left.add(new VisLabel("Border Size: ")).fillX().pad(5);
        VisTable borderTable = new VisTable();
        borderTable.add(borderSize).growX();
        lblBorderSize = new VisLabel(""+borderSize.getValue());
        borderTable.add(lblBorderSize);
        left.add(borderTable).growX();
        questionButton = new VisImageButton(new TextureRegionDrawable(question));
        questionButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showHelp("Border Size", "Select the size of the border\nborderpixels = bordersize * blocksize");
            }
        });
        left.add(questionButton).size(40,40).pad(10).row();

        left.add(new VisLabel("Primary Color: ")).fillX().pad(5);
        BasicColorPicker primaryPicker = new BasicColorPicker();
        primaryPicker.setListener(new ColorPickerListener() {
            @Override
            public void canceled(Color oldColor) {
                primaryColor = oldColor;
                change();
            }

            @Override
            public void changed(Color newColor) {
                primaryColor = newColor;
                change();
            }

            @Override
            public void reset(Color previousColor, Color newColor) {
                primaryColor = previousColor;
                change();
            }

            @Override
            public void finished(Color newColor) {
                primaryColor = newColor;
                change();
            }
        });
        primaryPicker.setAllowAlphaEdit(false);
        primaryPicker.setShowHexFields(false);
        left.add(primaryPicker).padLeft(50).padRight(50).growX();
        questionButton = new VisImageButton(new TextureRegionDrawable(question));
        questionButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showHelp("Primary Color", "Select the color for each square (typically black");
            }
        });
        left.add(questionButton).size(40,40).pad(10).row();

        left.add(new VisLabel("Secondary Color: ")).fillX().pad(5);

        BasicColorPicker secondaryPicker = new BasicColorPicker();
        secondaryPicker.setListener(new ColorPickerListener() {
            @Override
            public void canceled(Color oldColor) {
                secondaryColor = oldColor;
                change();
            }

            @Override
            public void changed(Color newColor) {
                secondaryColor = newColor;
                change();
            }

            @Override
            public void reset(Color previousColor, Color newColor) {
                secondaryColor = previousColor;
                change();
            }

            @Override
            public void finished(Color newColor) {
                secondaryColor = newColor;
                change();
            }
        });

        secondaryPicker.setShowHexFields(false);
        left.add(secondaryPicker).padLeft(50).padRight(50).growX();
        questionButton = new VisImageButton(new TextureRegionDrawable(question));
        questionButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showHelp("Secondary Color", "Select the color for the background (typically white");
            }
        });
        left.add(questionButton).size(40,40).pad(10).row();

        left.add(new VisLabel("Eye Outer Shape: ")).fillX().pad(5);
        left.add(eyeOuterShape).padLeft(50).padRight(50).growX();
        questionButton = new VisImageButton(new TextureRegionDrawable(question));
        questionButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showHelp("Eye Outer Shape", "Select the shape to use for the outer eye (typically square");
            }
        });
        left.add(questionButton).size(40,40).pad(10).row();

        left.add(new VisLabel("Eye Inner Shape: ")).fillX().pad(5);
        left.add(eyeInnerShape).padLeft(50).padRight(50).growX();
        questionButton = new VisImageButton(new TextureRegionDrawable(question));
        questionButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showHelp("Eye Inner Shape", "Select the shape to use for the inner eye (typically square");
            }
        });
        left.add(questionButton).size(40,40).pad(10).row();

        left.add(new VisLabel("Inner Shape: ")).fillX().pad(5);
        left.add(squareShape).padLeft(50).padRight(50).growX();
        questionButton = new VisImageButton(new TextureRegionDrawable(question));
        questionButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showHelp("Inner Shape", "Select the shape to use for the inner blocks (typically square");
            }
        });
        left.add(questionButton).size(40,40).pad(10).row();
        left.add().grow();

        right.add(displayedCode);

        //left.add(displayedCode).colspan(3).maxSize(Gdx.graphics.getWidth(),Gdx.graphics.getWidth()).padBottom(20).row();

        content.add(left).pad(10).grow();
        content.add(right).width(850).expand();

        content.setFillParent(true);

        for(Actor actor : new Actor[]{input, blockSize, borderSize, eyeOuterShape, eyeInnerShape, squareShape}){
            actor.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    change();
                }
            });
        }

        primaryColor = Color.BLACK;
        secondaryColor = Color.WHITE;
        blockSize.setValue(20);
        borderSize.setValue(1);
    }

    private void showHelp(String title, String details){
        VisDialog help = new VisDialog(title);
        help.getContentTable().add(new VisLabel(details));
        help.addCloseButton();
        help.setCenterOnAdd(true);
        help.setWidth(Gdx.graphics.getWidth()*0.75f);
        stage.addActor(help);
        help.fadeIn();
    }

    private void change(){
        regenerateQRCode = true;
    }

    @Override
    public String getTabTitle() {
        return "Generate";
    }

    @Override
    public Table getContentTable() {
        return content;
    }

    public void update(){
        if(regenerateQRCode){
            regenerateQRCode = false;
            // Clean up the old texture
            displayCodeTexture.getTexture().dispose();

            QRGenerator generator = QRCode.CreateGenerator();
            generator.blockSize((int) blockSize.getValue());
            generator.borderSize((int) borderSize.getValue());

            generator.primaryColor(primaryColor);
            generator.secondaryColor(secondaryColor);

            generator.setEyeBorderShape(eyeOuterShape.getSelected());
            generator.setEyeInnerShape(eyeInnerShape.getSelected());
            generator.setInnerShape(squareShape.getSelected());

            lblBlockSize.setText(""+(int)blockSize.getValue());
            lblBorderSize.setText(""+(int)borderSize.getValue());

            displayCodeTexture = generator.generate(input.getText().length() > 0 ? input.getText().length() > 2900 ? input.getText().substring(0,2900) : input.getText() : "Zeroed.tech");
            displayedCode.setDrawable(displayCodeTexture.getTexture());
        }
    }
}
