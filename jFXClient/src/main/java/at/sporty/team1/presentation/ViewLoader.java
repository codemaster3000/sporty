package at.sporty.team1.presentation;

import at.sporty.team1.presentation.controllers.IJfxController;
import at.sporty.team1.presentation.controllers.MainViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ViewLoader<T extends IJfxController> {
	private static Map<Class<? extends IJfxController>, String> _viewPaths;

	static {
		_viewPaths = new HashMap<>();

		// All UI-Controller relations should be defined here.
		_viewPaths.put(MainViewController.class, "/at/sporty/team1/presentation/views/MainView.fxml");
	}

	private FXMLLoader _loader;

	private ViewLoader(Class<T> controllerClass) {
		String path = _viewPaths.get(controllerClass);
		assert(path != null);
		URL viewResource = getClass().getResource(path);
		assert(viewResource != null);

		_loader = new FXMLLoader(viewResource);
		
		// TODO i18n RB
	}

	public Node loadNode() {
		try {
			return _loader.load();
		} catch (IOException e) {
			throw new ViewLoaderException(e);
		}
	}

	public T getController() {
		return _loader.getController();
	}

	public static <T extends IJfxController> ViewLoader<T> loadView(Class<T> controllerClass) {
		return new ViewLoader<>(controllerClass);
	}

	private static class ViewLoaderException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public ViewLoaderException(Exception e) {
			super(e);
		}
	}
}
