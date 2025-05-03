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

/**
 * The class that creates the pokemon application.
 */

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
    private String mewUrl = "https://media4.giphy.com/media/" +
        "IQebREsGFRXmo/200w.gif?cid=6c09b952iweixifp2x6fo3edlhfde53b" +
        "z3x5lzqakr5q37zy&ep=v1_stickers_search&rid=200w.gif&ct=s";
    private final Image mew = new Image(mewUrl);
    private String iurl = "https://www.freeiconspng.com/thumbs" +
        "/pokeball-png/pokeball-transparent-png-2.png";
    private final Image defaultImage = new Image(iurl);

    /**
     * Constructor which creates all the instance variables.
     */

    public PokemonApp() {
        System.out.println(iurl);
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

    /**
     * Method that initializes all the instance variables.
     * Adds all the nodes to their respective positions.
     */

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
        imageView.setImage(defaultImage);
        imageViewBox.getChildren().addAll(imageMew, cardAmount,
                                          typeInfoLabel, buttonBox, progress);
        this.imageViewBox.setSpacing(7);
        progress.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        progress.setVisible(false);
        progress.setPrefWidth(300);
        this.imageMew.getChildren().addAll(mewIV,imageView, mewIV2);
        imageMew.setAlignment(Pos.CENTER);
        imageMew.setSpacing(40);
        mewIV.setImage(mew);
        mewIV2.setImage(mew);
        mewIV.setScaleX(-1);
        buttonBox.getChildren().addAll(leftArrow, rightArrow);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(15);
        searchBar.setPromptText("Enter a Pokemon");
        leftArrow.setDisable(true);
        rightArrow.setDisable(true);
        root.setStyle("-fx-background-color: lightblue;");
    } // init

    /**
     * Method that creates the scene and stage in order to run the application.
     * Includes event handlers for each button.
     *
     * @param stage Stage object that is used to show the scene.
     */

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

    /**
     * Runs after the application is stopped.
     */

    public void stop() {

    } // stop

    /**
     * Sends are recieves an api request to the first api which returns pokemond card data.
     *
     * @return Data returns the formated class from the json file.
     */

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

    /**
     * Sends and recieves an api request for the type information relating
     * to the type recieved from the first api.
     *
     * @return TypeInfo returns the formatted class of the json file.
     */

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

    /**
     * Sets the information on the screen after the enter button is pressed.
     */

    public void setInfo() {
        dataInfo = apiRequest();
        if (dataInfo.data == null || dataInfo.data.length == 0) {
            Platform.runLater(() -> alertPopUp(new NullPointerException()));
        } else {
            Image pokeImage = new Image(dataInfo.data[currentPoke].images.small);
            typeInfo = typeRequest();
            imageView.setImage(pokeImage);
            Platform.runLater(() -> typeInfoLabel.setText(typeInfo.names[7].name
                                                          + " in Japanese: "
                                                          + typeInfo.names[0].name
                                                          + " in Korean: " + typeInfo.names[1].name
                                                          + " and in Chinese: "
                                                          + typeInfo.names[2].name));
            Platform.runLater(() -> cardAmount.setText("0 / " + (dataInfo.data.length - 1)));
            rightArrow.setDisable(false);
        } // if
    } // setInfo

    /**
     * Shows the next pokemon in the array and updates the information.
     */

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

    /**
     *  Shows the pokemon behind the current one in the array and updates the information.
     */

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


    /**
     * Updates the information after the arrow buttons are pressed.
     */

    private void setUpdated() {
        Image pokeImage = new Image(dataInfo.data[currentPoke].images.small);
        imageView.setImage(pokeImage);
        cardAmount.setText(currentPoke + " / " + (dataInfo.data.length - 1));
    } // setUpdated


    /**
     * Takes the type given from the first api and translates it into an
     * integer which can be used to send a request to the second api.
     *
     * @return int The type that will be used in the api request.
     */

    public int typeSet() {
        int type = 0;
        if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("colorless")) {
            type = 1;
        } else if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("fighting")) {
            type = 2;
        } else if (dataInfo.data[currentPoke].types[0].equalsIgnoreCase("flying")) {
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

    /**
     * Sends and recieves a request to the second api to recieve the pokedex (all the pokemon).
     *
     * @return Pokedex returns a formatted object of the json file containing
     * all the pokemon.
     */

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

    /**
     *  Uses the second api call to send a request to the first api for the random pokemon feature.
     */

    public void randomRequest() {
        leftArrow.setDisable(true);
        Pokedex pokedex = pokedexRequest();
        int randomNum = (int)(Math.random() * 600);
        String name = pokedex.pokemonEntries[randomNum].pokemonSpecies.name;
        searchBar.setText(name);
        setInfo();
        rightArrow.setDisable(false);
    } // randomRequest

    /**
     * Creates a thread for long running operations.
     *
     * @param target A runnable object that is the task of the thread.
     */

    public void runNow(Runnable target) {
        Thread task = new Thread(target);
        task.setDaemon(true);
        task.start();
    } // runNow

    /**
     * Creates an alert object and shows it.
     *
     * @param e The exception that caused the error.
     */

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
