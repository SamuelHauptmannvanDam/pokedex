package com.example.paginglist2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.paginglist2.models.Pokemon;
import com.example.paginglist2.models.PokemonRequest;
import com.example.paginglist2.models.SpecificPokemon;
import com.example.paginglist2.models.Type;
import com.example.paginglist2.pokeapi.PokeapiService;
import com.example.paginglist2.pokeapi.PokeapiServiceSpecific;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PokemonActivity extends AppCompatActivity {

    private static final String TAG = "POKEDEX";
    private Retrofit retrofit;
    private TextView nameTextView;
    private TextView idTextView;
    private TextView abilitiesTextView;
    private TextView baseExperienceTextView;
    private TextView heightTextView;
    private TextView typeTextView;
    private TextView weightTextView;
    private TextView statsTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        nameTextView = findViewById(R.id.nameTextViewPokemonActivity); //Connect UI
        idTextView = findViewById(R.id.idTextViewPokemonActivity); //Connect UI
        abilitiesTextView = findViewById(R.id.abilitiesTextViewPokemonActivity); //Connect UI
        baseExperienceTextView = findViewById(R.id.baseExperienceTextViewPokemonActivity); //Connect UI
        heightTextView = findViewById(R.id.heightTextViewPokemonActivity); //Connect UI
        typeTextView = findViewById(R.id.typeTextViewPokemonActivity); //Connect UI
        weightTextView = findViewById(R.id.weightTextViewPokemonActivity); //Connect UI
        statsTextView = findViewById(R.id.statsTextViewPokemonActivity); //Connect UI

        Intent intent = getIntent();
        int pokemonNumber = intent.getIntExtra("pokemonNumberKey",0);

        addImage(pokemonNumber+1);
        int urlCount = pokemonNumber+1;

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/" )
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        obtainData(urlCount);

    }

    private void obtainData(int pokemonID){

        PokeapiServiceSpecific service = retrofit.create(PokeapiServiceSpecific.class);
        Call<SpecificPokemon> pokemonCall = service.getPokemons(pokemonID);

        pokemonCall.enqueue(new Callback<SpecificPokemon>() {
            @Override
            public void onResponse(Call<SpecificPokemon> call, Response<SpecificPokemon> response) {

                SpecificPokemon specificPokemons = response.body();

                nameTextView.setText(specificPokemons.getName());
                idTextView.setText("ID: " +specificPokemons.getId());

                String contentAbilities = "";
                for (int i = 0; i < specificPokemons.getTypes().size(); i++) {
                    contentAbilities += specificPokemons.getAbilities().get(i).getAbility().getName() + ", ";
                }
                abilitiesTextView.setText("Ability: " + contentAbilities);

                baseExperienceTextView.setText("Base Experience: " + specificPokemons.getBaseExperience().toString());
                heightTextView.setText("Height: " + specificPokemons.getHeight().toString());

                String contentType = "";
                for (int i = 0; i < specificPokemons.getTypes().size(); i++) {
                    contentType += specificPokemons.getTypes().get(i).getType().getName() + ", ";
                }
                typeTextView.setText("Type: " + contentType);
                weightTextView.setText("Weight: " + specificPokemons.getWeight().toString());

                String contentStat = "";
                for (int i = 0; i < specificPokemons.getStats().size(); i++) {
                    contentStat += specificPokemons.getStats().get(i).getStat().getName() + ": "+ specificPokemons.getStats().get(i).getBaseStat()+ "\n";
                }
                statsTextView.setText(contentStat);

            }

            @Override
            public void onFailure(Call<SpecificPokemon> call, Throwable t) {
                nameTextView.setText(t.getMessage() + "ERROR");
            }
        });
    }

    public void addImage(int pokemonNumber){
        final ImageView myImageView;
        myImageView = findViewById(R.id.imageViewInPokemonActivity);
        Glide.with(getApplicationContext())
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + pokemonNumber + ".png")
                .centerCrop()
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(myImageView);
    }

}
