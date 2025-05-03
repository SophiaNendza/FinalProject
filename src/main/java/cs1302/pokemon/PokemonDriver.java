package cs1302.pokemon;

import javafx.application.Application;

/**
 * The dirver class that sends the stage to the start method.
 */

public class PokemonDriver {

    public static void main(String[] args) {
        try {
            Application.launch(PokemonApp.class, args);
        }  catch (UnsupportedOperationException e) {
            System.out.println(e);
            System.exit(1);
        } // try
    } // main
} // PokemonDriver
