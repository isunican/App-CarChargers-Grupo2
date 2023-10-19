package es.unican.carchargers.model;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.activities.details.DetailsView;

public class Favourite {

    public List<String> listaFavoritos = new ArrayList<>();

    public Favourite(){}

    public String addCharger(String id) {
        if (listaFavoritos.contains(id)) {
            return "REPETIDO";
        }

        listaFavoritos.add(id);
        return id;
    }
}
