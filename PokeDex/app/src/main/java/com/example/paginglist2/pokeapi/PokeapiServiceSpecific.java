package com.example.paginglist2.pokeapi;

import com.example.paginglist2.models.SpecificPokemon;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PokeapiServiceSpecific {

    @GET("pokemon/{id}")
    Call<SpecificPokemon> getPokemons(@Path("id") int pokemonId);

}


