package com.example.paginglist2.models;

import java.util.ArrayList;

public class PokemonRequest {

    private ArrayList<Pokemon> results;
    private ArrayList<SpecificPokemon> specificPokemonResults;

    public ArrayList<SpecificPokemon> getSpecificPokemonResults() { return specificPokemonResults; }
    public ArrayList<Pokemon> getResults() {
        return results;
    }

}
