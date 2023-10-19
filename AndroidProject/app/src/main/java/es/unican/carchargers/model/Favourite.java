package es.unican.carchargers.model;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Favourite {

    public List<String> listaFavoritos = new ArrayList<>();

    public Favourite(){}

    public void addCharger(String id) {
        listaFavoritos.add(id);
    }
}
