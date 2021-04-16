package com.example.paginglist2.pokeapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import com.example.paginglist2.models.PokemonRequest;

public interface PokeapiService {

    @GET("pokemon")
    Call<PokemonRequest> optainListPokemon(@Query("limit") int limit, @Query("offset") int offset);

}


