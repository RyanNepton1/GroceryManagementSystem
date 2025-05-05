package ca.mcgill.ecse.grocerymanagementsystem.view;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryManagementSystemController;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GroceryStoreView extends Application {

	public static final EventType<Event> REFRESH = new EventType<>("REFRESH");
	private static Set<Node> refreshableNodes = new HashSet<>();
	/**
	 * Register a new node as refreshable so that it will receive an event when the page needs to be refreshed.
	 */
	public static void registerRefreshableNode(Node n) {
		refreshableNodes.add(n);
	}

	/**
	 * Refresh all nodes that have been registered as refreshable.
	 */
	public static void refresh() {
		for (Node n : refreshableNodes) {
			n.fireEvent(new Event(REFRESH));
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		 // https://github.com/openjfx/samples/blob/master/HelloFX/Gradle/hellofx/src/main/java/HelloFX.java
		//String message = "Hello there!";
		//Scene scene = new Scene(new StackPane(new Label(message)), 640, 480);

		GroceryManagementSystemController.initializeTestData();

		//URL mainPageUrl = getClass().getResource("/views/MainPage.fxml");
		//TitledPane mainPage = FXMLLoader.load(mainPageUrl);
		URL mainPageUrl = getClass().getResource("/views/LandingPage.fxml");
		Parent mainPage = FXMLLoader.load(mainPageUrl);
		Scene scene = new Scene(mainPage, 640, 480);

		stage.setScene(scene);
		stage.setTitle("Pantry Pal");
		stage.show();
	}
}
