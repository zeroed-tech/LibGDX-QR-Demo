package tech.zeroed.libgdxqrdemo;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
		VisUI.load();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		mainTable = new Table();
		final Table contentTable = new Table();

		tabbedPane = new TabbedPane();
		mainTable.add(new VisLabel("Welcome to the LibGDX-QR test app")).row();
		mainTable.add(tabbedPane.getTable()).pad(10, 10, 0, 10).growX().row();
		mainTable.add(contentTable).pad(0, 10, 0, 10).grow().row();
		tabbedPane.addListener(new TabbedPaneAdapter(){
			@Override
			public void switchedTab(Tab tab) {
				Table content = tab.getContentTable();

				contentTable.clearChildren();
				contentTable.add(content).expand().fill();
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

	}
}