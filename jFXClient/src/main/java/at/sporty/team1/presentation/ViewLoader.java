package at.sporty.team1.presentation;

import at.sporty.team1.presentation.controllers.*;
import at.sporty.team1.presentation.controllers.core.IJfxController;
import at.sporty.team1.presentation.controllers.RichViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class which helps to manage JavaFX view and their controllers.
 * @param <T> controller class. Is required for controller instantiation and
 * identification of the required {@code fxml} file.
 */
public class ViewLoader<T extends IJfxController> {
	private static Map<Class<? extends IJfxController>, String> _viewPaths;

    /**
     * Static Map for all registered {@code fxml} files and their controllers.
     */
	static {
		_viewPaths = new HashMap<>();

		//All UI-Controller relations should be defined here.
		_viewPaths.put(MainViewController.class, "/at/sporty/team1/presentation/views/MainView.fxml");
        _viewPaths.put(RichViewController.class, "/at/sporty/team1/presentation/views/RichView.fxml");
        _viewPaths.put(MemberDataViewController.class, "/at/sporty/team1/presentation/views/MemberDataView.fxml");
        _viewPaths.put(TeamViewController.class, "/at/sporty/team1/presentation/views/TeamView.fxml");
        _viewPaths.put(TestViewController.class, "/at/sporty/team1/presentation/views/TestView.fxml");
        _viewPaths.put(CompetitionViewController.class, "/at/sporty/team1/presentation/views/CompetitionView.fxml");
        _viewPaths.put(LoginMaskViewController.class, "/at/sporty/team1/presentation/views/LoginMaskView.fxml");
        _viewPaths.put(MemberSearchViewController.class, "/at/sporty/team1/presentation/views/MemberSearchView.fxml");
        _viewPaths.put(TournamentSearchViewController.class, "/at/sporty/team1/presentation/views/TournamentSearchView.fxml");
    }

	private FXMLLoader _loader;

    /**
     * Default constructor for this utility class.
     * @param controllerClass controller class of the requested view.
     */
	private ViewLoader(Class<T> controllerClass) {
		String path = _viewPaths.get(controllerClass);
		assert(path != null);
		URL viewResource = getClass().getResource(path);
		assert(viewResource != null);

		_loader = new FXMLLoader(viewResource);
	}

    /**
     * Returns loaded view as a {@code Node}}.
     * @return loaded {@code Node}.
     */
	public Node loadNode() {
		try {
			return _loader.load();
		} catch (IOException e) {
			throw new ViewLoaderException(e);
		}
	}

    /**
     * Returns loaded controller class.
     * @return controller class.
     */
	public T getController() {
		return _loader.getController();
	}

    /**
     * Returns new ViewLoader instance. ViewLoader provides a functionality
     * to load the View by its controller class and the instance of the
     * controller class itself.
     * @param controllerClass controller class of the requested view.
     * @param <T> controller which is an instance of IJfxController.
     * @return new ViewLoader instance for requested controller class.
     */
	public static <T extends IJfxController> ViewLoader<T> loadView(Class<T> controllerClass) {
		return new ViewLoader<>(controllerClass);
	}

    /**
     * ViewLoaderException class which is used (returned) to indicate problem on
     * the view-loading stage.
     */
	private static class ViewLoaderException extends RuntimeException {
		private static final long serialVersionUID = 1L;

        /**
         * Constructor which wraps the reason exception.
         * @param e exception to e wrapped.
         */
		public ViewLoaderException(Exception e) {
			super(e);
		}
	}
}
