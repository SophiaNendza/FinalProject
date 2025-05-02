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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PokemonApp extends Application {

    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private Stage stage;
    private Scene scene;
    private VBox root;
    private VBox titleBox;
    private Label title;
    private Label genInstruct;
    private HBox searchBox;
    private TextField searchBar;
    private Button enter;
    private Button random;
    private ImageView imageView;
    private VBox imageViewBox;
    private Label typeInfoLabel;
    private Label cardAmount;
    private Button rightArrow;
    private Button leftArrow;
    private HBox buttonBox;
    private Data dataInfo;
    private TypeInfo typeInfo;
    private int currentPoke;
    private ProgressBar progress;
    private HBox imageMew;
    private ImageView mewIV;
    private ImageView mewIV2;
    private String mewUrl = "https://media4.giphy.com/media/IQebREsGFRXmo/200w.gif?cid=6c09b952iweixifp2x6fo3edlhfde53bz3x5lzqakr5q37zy&ep=v1_stickers_search&rid=200w.gif&ct=s";
    private Image MEW = new Image(mewUrl);
    private String iurl = "https://www.freeiconspng.com/thumbs/pokeball-png/pokeball-transparent-png-2.png";
    private Image DEFAULT = new Image(iurl);

    public PokemonApp() {
        this.stage = null;
        this.scene = null;
        this.root = new VBox();
        this.titleBox = new VBox();
        this.title = new Label("Pokemon Information!");
        this.searchBox = new HBox();
        this.searchBar = new TextField();
        this.enter = new Button("Enter");
        this.random = new Button("Random");
        this.imageView = new ImageView();
        this.imageViewBox = new VBox();
        this.typeInfoLabel = new Label("Enter a pokemon or press random to start searching!");
        this.genInstruct = new Label("Only generation 1 through generation 7 apply");
        this.cardAmount = new Label("0 / 0");
        this.rightArrow = new Button(">");
        this.leftArrow = new Button("<");
        this.buttonBox = new HBox();
        this.currentPoke = 0;
        this.progress = new ProgressBar();
        this.mewIV = new ImageView();
        this.mewIV2 = new ImageView();
        this.imageMew = new HBox();
    } // PokemonApp

    public void init() {
        this.root.getChildren().addAll(titleBox,searchBox,imageViewBox);
        this.root.setSpacing(7);
        this.titleBox.getChildren().addAll(title, genInstruct);
        this.titleBox.setSpacing(7);
        this.searchBox.getChildren().addAll(searchBar, enter, random);
        this.searchBox.setSpacing(5);
        titleBox.setAlignment(Pos.CENTER);
        searchBox.setAlignment(Pos.CENTER);
        imageViewBox.setAlignment(Pos.CENTER);
        imageView.setImage(DEFAULT);
        imageViewBox.getChildren().addAll(imageMew, cardAmount,
                                          typeInfoLabel, buttonBox, progress);
        this.imageViewBox.setSpacing(7);
        progress.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        progress.setVisible(false);
        progress.setPrefWidth(300);
        this.imageMew.getChildren().addAll(mewIV,imageView, mewIV2);
        imageMew.setAlignment(Pos.CENTER);
        imageMew.setSpacing(40);
        mewIV.setImage(MEW);
        mewIV2.setImage(MEW);
        mewIV.setScaleX(-1);
        buttonBox.getChildren().addAll(leftArrow, rightArrow);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(15);
        searchBar.setPromptText("Enter a Pokemon");
        leftArrow.setDisable(true);
        rightArrow.setDisable(true);
        root.setStyle("-fx-background-color: lightblue;");
     } // init

    public void start(Stage stage) {
        this.stage = stage;
        this.scene = new Scene(this.root, 1200, 720);
        this.stage.setOnCloseRequest(event -> Platform.exit());
        this.stage.setTitle("PokemonApp!");
        this.stage.setScene(this.scene);
        this.stage.sizeToScene();
        this.stage.show();
        stage.setMaxWidth(1270);
        stage.setMaxHeight(700);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        //for now
        EventHandler<ActionEvent> api = (ActionEvent ae) -> {
            runNow(() -> setInfo());
        }; // api
        enter.setOnAction(api);

        EventHandler<ActionEvent> right = (ActionEvent ae) -> {
            rightButton();
        }; //right
        rightArrow.setOnAction(right);

        EventHandler<ActionEvent> left = (ActionEvent ae) -> {
            leftButton();
        }; // left
        leftArrow.setOnAction(left);

        EventHandler<ActionEvent> randomEvent = (ActionEvent ae) -> {
            runNow(() -> randomRequest());

        }; // randomEvent
        random.setOnAction(randomEvent);
    } // start

    public void stop() {

    } // stop

    public Data apiRequest() {
        Platform.runLater(() -> progress.setVisible(true));
        currentPoke = 0;
        URI url = URI.create("https://api.pokemontcg.io/v2/cards?q=name:" +
                             searchBar.getText().replaceAll("\\s+", ""));

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
            alertPopUp(e);
        } // try
        Platform.runLater(() -> progress.setVisible(false));
        return resp;
    } // apiRequest

    public TypeInfo typeRequest() {
        URI url = URI.create("https://pokeapi.co/api/v2/type/" + typeSet());
        HttpRequest request = HttpRequest.newBuilder()
            .uri(url).build();
        TypeInfo typeResp = null;
        try {
            HttpResponse<String> response = HTTP_CLIENT
                .<String>send(request, BodyHandlers.ofString());
            String json = response.body();
            typeResp = GSON.<TypeInfo>fromJson(json, TypeInfo.class);
        } catch (Exception e) {
            alertPopUp(e);
        } // try
        return typeResp;
    } // typeRequest

    public void setInfo() {
        dataInfo = apiRequest();
        if (dataInfo.data == null || dataInfo.data.length == 0) {
            Platform.runLater(() -> alertPopUp(new NullPointerException()));
        } else {
        Image pokeImage = new Image(dataInfo.data[currentPoke].images.small);
        typeInfo = typeRequest();
        imageView.setImage(pokeImage);
        Platform.runLater(() -> typeInfoLabel.setText(typeInfo.names[7].name
                                                      + " in Japanese: " + typeInfo.names[0].name
                                                      + " in Korean: " + typeInfo.names[1].name
                                                      + " and in Chinese: "
                                                      + typeInfo.names[2].name));
        Platform.runLater(() -> cardAmount.setText("0 / " + (dataInfo.data.length - 1)));
         rightArrow.setDisable(false);
        } // if
    } // setInfo

    public void rightButton() {
        currentPoke += 1;
        if (dataInfo.data == null) {
            alertPopUp(new Exception());
            return;
        }
        if (currentPoke == dataInfo.data.length - 1) {
            rightArrow.setDisable(true);
        } // if
        if (leftArrow.isDisabled()) {
            leftArrow.setDisable(false);
        } // if
        setUpdated();
    } // rightButton

    public void leftButton() {
        currentPoke -= 1;
        if (currentPoke == 0) {
            leftArrow.setDisable(true);
        } // if
        if (rightArrow.isDisabled()) {
            rightArrow.setDisable(false);
        } // if
        setUpdated();
    } // leftButton


    private  void setUpdated() {
         Image pokeImage = new Image(dataInfo.data[currentPoke].images.small);
         imageView.setImage(pokeImage);
         cardAmount.setText(currentPoke + " / " + (dataInfo.data.length - 1));
    } // setUpdated

    public int typeSet() {
        int type = 0;
        if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("colorless")) {
            type = 1;
        } else if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("fighting")) {
            type = 2;
        } else if(dataInfo.data[currentPoke].types[0].equalsIgnoreCase("flying")) {
            type = 3;
        } else if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("poison")) {
            type = 4;
        } else if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("ground")) {
            type = 5;
        } else if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("rock")) {
            type = 6;
        } else if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("bug")) {
            type = 7;
        } else if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("ghost")) {
            type = 8;
        } else if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("metal")) {
            type = 9;
        } else if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("fire")) {
            type = 10;
        } else if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("water")) {
            type = 11;
        } else if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("grass")) {
            type = 12;
        } else if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("lightning")) {
            type = 13;
        } else if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("psychic")) {
            type = 14;
        } else if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("ice")) {
            type = 15;
        } else if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("dragon")) {
            type = 16;
        } else if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("darkness")) {
            type = 17;
        } else if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("fairy")) {
            type = 18;
        } else if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("stellar")) {
            type = 19;
        } // if
        return type;
    } // typeSet


    public Pokedex pokedexRequest() {
        currentPoke = 0;
        URI url = URI.create("https://pokeapi.co/api/v2/pokedex/1/");
        HttpRequest request = HttpRequest.newBuilder()
            .uri(url).build();
        Pokedex resp = null;
        try {
            HttpResponse<String> response = HTTP_CLIENT
                .<String>send(request, BodyHandlers.ofString());
            String json = response.body();
            resp = GSON.<Pokedex>fromJson(json, Pokedex.class);
        } catch (Exception e) {
            Platform.runLater(() -> alertPopUp(new Exception()));
        } // try
        return resp;
    } // apiRequest

    public void randomRequest() {
        leftArrow.setDisable(true);
        Pokedex pokedex = pokedexRequest();
        int randomNum = (int)(Math.random() * 600);
        String name = pokedex.pokemon_entries[randomNum].pokemon_species.name;
        searchBar.setText(name);
        setInfo();
        rightArrow.setDisable(false);
    } // randomRequest

    public void runNow(Runnable target) {
        Thread task = new Thread(target);
        task.setDaemon(true);
        task.start();
    } // runNow

    public void alertPopUp(Exception e) {
        Alert alert = new Alert(AlertType.ERROR);
        Platform.runLater(() -> progress.setVisible(false));
        alert.setTitle("Error");
        alert.setContentText(searchBar.getText() + " is not a valid pokemon. Please try again");
        String pokemonUrl = "https://static1.thegamerimages.com";
        String other = "/wordpress/wp-content/uploads/2020/08/Angry-Jigglypuff-Pokemon-Anime.jpeg";
        Image madPokemon = new Image(pokemonUrl + other);
        ImageView madiv = new ImageView();
        madiv.setImage(madPokemon);
        madiv.setFitHeight(150);
        madiv.setFitWidth(250);
        alert.setGraphic(madiv);
        alert.showAndWait();
    } // alertPopUp

} // PokemonApp
