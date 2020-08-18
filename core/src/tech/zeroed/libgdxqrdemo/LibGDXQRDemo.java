package tech.zeroed.libgdxqrdemo;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;

public class LibGDXQRDemo implements ApplicationListener {

	Stage stage;
	Table mainTable;
	private TabbedPane tabbedPane;

	@Override
	public void create() {
		VisUI.load(loadSkin());
		stage = new Stage(new FitViewport(1920, 1080));
		Gdx.input.setInputProcessor(stage);
		mainTable = new Table();
		final Table contentTable = new Table();

		tabbedPane = new TabbedPane();
		mainTable.add(new VisLabel("Welcome to the LibGDX-QR test app", "Large")).row();
		mainTable.add(tabbedPane.getTable()).pad(10, 10, 0, 10).growX().row();
		mainTable.add(contentTable).pad(0, 10, 0, 10).grow().row();
		tabbedPane.addListener(new TabbedPaneAdapter(){
			@Override
			public void switchedTab(Tab tab) {
				Table content = tab.getContentTable();

				contentTable.clearChildren();
				contentTable.add(content).grow();
			}
		});
		tabbedPane.add(new GenerateTab(stage));
		tabbedPane.add(new ScanTab());
		tabbedPane.switchTab(0);
		mainTable.setFillParent(true);
		stage.addActor(mainTable);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0.7f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		((Updatable)tabbedPane.getActiveTab()).update();

		stage.act();
		stage.draw();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	private Skin loadSkin(){
		Skin skin = new Skin(Gdx.files.internal("Skin.json")) {
			//Override json loader to process FreeType fonts from skin JSON
			@Override
			protected Json getJsonLoader(final FileHandle skinFile) {
				Json json = super.getJsonLoader(skinFile);
				final Skin skin = this;

				json.setSerializer(FreeTypeFontGenerator.class, new Json.ReadOnlySerializer<FreeTypeFontGenerator>() {
					@Override
					public FreeTypeFontGenerator read(Json json, JsonValue jsonData, Class type) {
						String path = json.readValue("font", String.class, jsonData);
						jsonData.remove("font");

						FreeTypeFontGenerator.Hinting hinting = FreeTypeFontGenerator.Hinting.valueOf(json.readValue("hinting", String.class, "AutoMedium", jsonData));
						jsonData.remove("hinting");

						Texture.TextureFilter minFilter = Texture.TextureFilter.valueOf(json.readValue("minFilter", String.class, "Nearest", jsonData));
						jsonData.remove("minFilter");

						Texture.TextureFilter magFilter = Texture.TextureFilter.valueOf(json.readValue("magFilter", String.class, "Nearest", jsonData));
						jsonData.remove("magFilter");

						FreeTypeFontGenerator.FreeTypeFontParameter parameter = json.readValue(FreeTypeFontGenerator.FreeTypeFontParameter.class, jsonData);
						parameter.hinting = hinting;
						parameter.minFilter = minFilter;
						parameter.magFilter = magFilter;
						FreeTypeFontGenerator generator = new FreeTypeFontGenerator(skinFile.parent().child(path));
						BitmapFont font = generator.generateFont(parameter);
						skin.add(jsonData.name, font);
						if (parameter.incremental) {
							generator.dispose();
							return null;
						} else {
							return generator;
						}
					}
				});

				return json;
			}
		};
		return skin;
	}
}