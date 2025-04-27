package cs1302.pokemon;

import java.net.http.HttpClient;
import java.net.URI;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class PokemonApp extends Application {

    public static final HttpClient HtTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private Stage stage;
    private Scene scene;
    private VBox root;
    private Label title;

    public PokemonApp() {
        this.stage = null;
        this.scene = null;
        this.root = new VBox();
        this.title = new Label("Pokemon Information!");
    } // PokemonApp

    public void init() {
        System.out.println("running init");
        this.root.getChildren().addAll(title);
     } // init

    public void start(Stage stage) {
        System.out.println("running start");
        this.stage = stage;
        this.scene = new Scene(this.root);
        this.stage.setOnCloseRequest(event -> Platform.exit());
        this.stage.setTitle("PokemonApp!");
        this.stage.setScene(this.scene);
        this.stage.sizeToScene();
        this.stage.show();
    } // start

    public void stop() {
        System.out.println("running stop");
    } // stop
} // PokemonApp
