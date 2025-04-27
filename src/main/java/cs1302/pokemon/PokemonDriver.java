package cs1302.pokemon;

import javafx.application.Application;

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
