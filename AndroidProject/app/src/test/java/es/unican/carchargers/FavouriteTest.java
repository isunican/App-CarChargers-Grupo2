package es.unican.carchargers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.Favourite;

public class FavouriteTest {

    private Favourite favorito;
    private List<Charger> listaCargadores;

    @Before
    public void inicializa() {
        favorito = new Favourite();
        listaCargadores = new ArrayList<>();
    }
    @Test
    public void addChargerTest() {
        Charger charger1 = new Charger();
        Charger charger2 = new Charger();
        Charger charger3 = new Charger();

        charger1.id = "1";
        charger2.id = "2";
        charger3.id = "3";

        listaCargadores.add(charger1);
        listaCargadores.add(charger2);
        listaCargadores.add(charger3);

        // Probamos sin cargadores favoritos
        assertEquals(favorito.listaFavoritos.size(), 0);

        // Anhadir a favoritos el cargador 2 y 3
        favorito.addCharger(charger2.id);
        favorito.addCharger(charger3.id);

        // Probamos que se han anahdido correctamente
        assertEquals(favorito.listaFavoritos.size(), 2);
        assertEquals(favorito.listaFavoritos.get(0), charger2.id);
        assertEquals(favorito.listaFavoritos.get(1), charger3.id);

        // Buscamos un cargador que no esta en favoritos
        assertFalse(favorito.listaFavoritos.contains(charger1));

        // Probamos a anhadir a favoritos un cargador ya en favoritos
        assertEquals(favorito.addCharger(charger2.id), "REPETIDO");

        // Verificamos que al anahdir un cargador a favoritos devuelve el id de dicho cargador
        assertEquals(favorito.addCharger(charger1.id), "1");
    }
}