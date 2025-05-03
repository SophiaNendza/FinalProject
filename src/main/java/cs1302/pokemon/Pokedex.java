package cs1302.pokemon;

import com.google.gson.annotations.SerializedName;
/**
 * The class for the whole json object for the second api call.
 */

public class Pokedex {
    @SerializedName("pokemon_entries")
    PokemonSpecies[] pokemonEntries;
} // pokedex
