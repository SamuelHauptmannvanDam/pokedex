package com.example.paginglist2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.paginglist2.models.Pokemon;
import com.example.paginglist2.models.PokemonRequest;
import com.example.paginglist2.pokeapi.PokeapiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "POKEDEX";

    private Retrofit retrofit;

    private RecyclerView recyclerView;
    private ListPokemonAdapter listPokemonAdapter;

    private int offset;
    private boolean loadMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        listPokemonAdapter = new ListPokemonAdapter(this);
        recyclerView.setAdapter(listPokemonAdapter);

        listPokemonAdapter.setOnItemClickListener(new ListPokemonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(getApplicationContext(), PokemonActivity.class);
                intent.putExtra("pokemonNumberKey", position);
                startActivity(intent);

            }
        });

        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (dy > 0){
                Log.d(TAG, "dy: "+String.valueOf(dy));
                int visibleCount = layoutManager.getChildCount();
                Log.d(TAG, "visibleCount: "+String.valueOf(visibleCount));

                int totalItemCount = layoutManager.getItemCount();
                Log.d(TAG, "totalItemCount: "+String.valueOf(totalItemCount));

                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                Log.d(TAG, "pastVisibleItems: "+String.valueOf(pastVisibleItems));

                if (loadMore){
                    if ((visibleCount + pastVisibleItems) >= totalItemCount){
                        Log.i(TAG, "We reached the end");

                        loadMore = false;
                        offset+=20;
                        obtainData(offset);
                    }
                }
            }
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        loadMore = true;
        offset = 0;
        obtainData(offset);
    }

    private void obtainData(int offset){
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<PokemonRequest> pokemonRequestCall = service.optainListPokemon(20, offset);

        pokemonRequestCall.enqueue(new Callback<PokemonRequest>() {
            @Override
            public void onResponse(Call<PokemonRequest> call, Response<PokemonRequest> response) {
                loadMore = true;
                if (response.isSuccessful()) {

                    PokemonRequest pokemonRequest = response.body();
                    ArrayList<Pokemon> listPokemon = pokemonRequest.getResults();

                    listPokemonAdapter.addListPokemon(listPokemon);

                } else {
                    Log.e(TAG, " onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokemonRequest> call, Throwable t) {
                loadMore = true;
                Log.e(TAG, " onfailure: " + t.getMessage());
            }
        });
    }
}
