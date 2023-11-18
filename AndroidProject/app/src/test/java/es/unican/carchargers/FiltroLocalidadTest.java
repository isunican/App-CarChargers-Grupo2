package es.unican.carchargers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static es.unican.carchargers.activities.main.MainPresenter.mappingProvinces;

import android.view.MenuItem;
import android.webkit.WebView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.unican.carchargers.activities.main.IMainContract;
import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.activities.main.MainView;
import es.unican.carchargers.model.Charger;

public class FiltroLocalidadTest {

    private Charger charger1 = new Charger();
    private Charger charger2 = new Charger();
    private Charger charger3 = new Charger();
    @Mock
    private MainView mockView;
    private IMainContract.Presenter Presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
            //Se le da valore a los cargadores
        charger1.address.province = "Cantabria";
        charger1.address.town = "Santander";
        charger2.address.province = "Cantabria";
        charger2.address.town = "Maliaño";
        charger3.address.province = "Comunidad de Madrid";
        charger3.address.town = "Aranjuez";

    }

    @Test
    public void mappingProvincesTest() {
        //Lista cargadores vacia devuelve mapa vacio
        Map<String, Set<String>> Provincias = new HashMap<>();
        List<Charger> cargadores = new ArrayList<Charger>();
        assertEquals(Provincias, MainPresenter.mappingProvinces(cargadores));

        //Lista cargadors devuelve un mapa

            //Se crean los set de las provincias
        Set<String> Cantabria = new HashSet<>();
        Set<String> Madrid = new HashSet<>();

            //Se añaden los valores a los Set
        Cantabria.add("Santander");
        Cantabria.add("Maliaño");
        Madrid.add("Aranjuez");

            //Se añaden los set al Mapa de provincias
        Provincias.put("Cantabria",  Cantabria);
        Provincias.put("Comunidad de Madrid", Madrid);

            //Se añaden los cargadores
        cargadores.add(charger1);
        cargadores.add(charger2);
        cargadores.add(charger3);

        assertEquals(Provincias, MainPresenter.mappingProvinces(cargadores));
    }

    /*
    @Test public void onOptionsItemSelectedTest() {
        WebView item = new WebView(null);
        mockView.onOptionsItemSelected(item);
        item.getId()
    }
     */

}