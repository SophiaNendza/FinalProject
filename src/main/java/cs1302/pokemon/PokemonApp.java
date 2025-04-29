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
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class PokemonApp extends Application {

    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private Stage stage;
    private Scene scene;
    private VBox root;
    private HBox titleBox;
    private Label title;
    private HBox searchBox;
    private TextField searchBar;
    private Button enter;
    private Button random;
    private ImageView imageView;
    private VBox imageViewBox;

    private String iurl = "https://coffective.com/wp-content/uploads/2018/06/default-featured-image.png.jpg";
    private Image DEFAULT = new Image(iurl);

    public PokemonApp() {
        this.stage = null;
        this.scene = null;
        this.root = new VBox();
        this.titleBox = new HBox();
        this.title = new Label("Pokemon Information!");
        this.searchBox = new HBox();
        this.searchBar = new TextField("Place holder");
        this.enter = new Button("Enter");
        this.random = new Button("Random");
        this.imageView = new ImageView();
        this.imageViewBox = new VBox();
    } // PokemonApp

    public void init() {
        System.out.println("running init");
        this.root.getChildren().addAll(titleBox,searchBox,imageViewBox);
        this.titleBox.getChildren().addAll(title);
        this.searchBox.getChildren().addAll(searchBar, enter, random);
        titleBox.setAlignment(Pos.CENTER);
        searchBox.setAlignment(Pos.CENTER);
        imageViewBox.setAlignment(Pos.CENTER);
        imageView.setImage(DEFAULT);
        imageViewBox.getChildren().addAll(imageView);

     } // init

    public void start(Stage stage) {
        System.out.println("running start");
        this.stage = stage;
        this.scene = new Scene(this.root, 1200, 720);
        this.stage.setOnCloseRequest(event -> Platform.exit());
        this.stage.setTitle("PokemonApp!");
        this.stage.setScene(this.scene);
        this.stage.sizeToScene();
        this.stage.show();
        stage.setMaxWidth(1270);
        stage.setMaxHeight(700);
        stage.setMinWidth(500);
        stage.setMinHeight(500);
        //for now
        EventHandler<ActionEvent> api = (ActionEvent ae) -> {
            setInfo();
        }; // api
        enter.setOnAction(api);
    } // start

    public void stop() {
        System.out.println("running stop");
    } // stop

    public Data apiRequest() {
        URI url = URI.create("https://api.pokemontcg.io/v2/cards?q=name:" +
                             searchBar.getText());

        HttpRequest request = HttpRequest.newBuilder()
            .header("X-Api-Key","c2566a44-fab6-4a73-8fbd-45cc9b93a2df")
            .uri(url).build();
        Data resp = null;
        try {
            HttpResponse<String> response = HTTP_CLIENT
                .<String>send(request, BodyHandlers.ofString());
            String json = response.body();
            resp = GSON.<Data>fromJson(json, Data.class);
        } catch (Exception e) {
            System.out.println(e);
        } // try
        int type = 0;
        System.out.println(resp.data[0].types[0]);
        if (resp.data[0].types[0].equalsIgnoreCase("normal")) {
            type = 1;
        } else if (resp.data[0].types[0].equalsIgnoreCase("fighting")) {
            type = 2;
        } else if(resp.data[0].types[0].equalsIgnoreCase("flying")) {
            type = 3;
        } else if (resp.data[0].types[0].equalsIgnoreCase("poison")) {
            type = 4;
        } else if (resp.data[0].types[0].equalsIgnoreCase("ground")) {
            type = 5;
        } else if (resp.data[0].types[0].equalsIgnoreCase("rock")) {
            type = 6;
        } else if (resp.data[0].types[0].equalsIgnoreCase("bug")) {
            type = 7;
        } else if (resp.data[0].types[0].equalsIgnoreCase("ghost")) {
            type = 8;
        } else if (resp.data[0].types[0].equalsIgnoreCase("steel")) {
            type = 9;
        } else if (resp.data[0].types[0].equalsIgnoreCase("fire")) {
            type = 10;
        } else if (resp.data[0].types[0].equalsIgnoreCase("water")) {
            type = 11;
        } else if (resp.data[0].types[0].equalsIgnoreCase("grass")) {
            type = 12;
        } else if (resp.data[0].types[0].equalsIgnoreCase("lightning")) {
            type = 13;
        } else if (resp.data[0].types[0].equalsIgnoreCase("psychic")) {
            type = 14;
        } else if (resp.data[0].types[0].equalsIgnoreCase("ice")) {
            type = 15;
        } else if (resp.data[0].types[0].equalsIgnoreCase("dragon")) {
            type = 16;
        } else if (resp.data[0].types[0].equalsIgnoreCase("dark")) {
            type = 17;
        } else if (resp.data[0].types[0].equalsIgnoreCase("fairy")) {
            type = 18;
        } else if (resp.data[0].types[0].equalsIgnoreCase("stellar")) {
            type = 19;
        } // if
        System.out.println("Type: " + type);
        URI url2 = URI.create("https://pokeapi.co/api/v2/type/" + type);
        return resp;
    } // apiRequest

    public void setInfo() {
        Data info = apiRequest();
        Image pokeImage = new Image(info.data[0].images.small);
        imageView.setImage(pokeImage);
    } // setInfo
} // PokemonApp
