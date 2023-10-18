package es.unican.carchargers.activities.details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import es.unican.carchargers.R;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.Favourite;

/**
 * Charging station details view. Shows the basic information of a charging station.
 */
public class DetailsView extends AppCompatActivity {

    public static final String INTENT_CHARGER = "INTENT_CHARGER";

    boolean isFavourite;

    Charger charger = new Charger();

    Favourite favourite = new Favourite();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);

        // Link to view elements
        ImageView ivLogo = findViewById(R.id.ivLogo);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvId = findViewById(R.id.tvId);
        ImageView ivFavoritos = findViewById(R.id.ivFavoritos);

        // Get Charger from the intent that triggered this activity
        charger = Parcels.unwrap(getIntent().getExtras().getParcelable(INTENT_CHARGER));

        // Set logo
        int resourceId = EOperator.fromId(charger.operator.id).logo;
        ivLogo.setImageResource(resourceId);

        // Set Infos
        tvTitle.setText(charger.operator.title);
        tvId.setText(charger.id);

        // SharedPreferences
        SharedPreferences sharedPref = this.getSharedPreferences("favoritos",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Favoritos
        // Se comprueba si el cargador esta en favoritos
        isFavourite = sharedPref.getBoolean(charger.id, false);
        if (isFavourite) {
            ivFavoritos.setImageResource(R.drawable.favoritoactivo);
        } else {
            ivFavoritos.setImageResource(R.drawable.favoritosnoactivo);
        }

        ivFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                charger.isFavourite = !charger.isFavourite;
                if (charger.isFavourite) {
                    ivFavoritos.setImageResource(R.drawable.favoritoactivo);
                    editor.putBoolean(charger.id, true);
                    editor.apply();
                    favourite.addCharger(charger.id);
                    Toast.makeText(getApplicationContext(), "Anhadido correctamente a favoritos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}