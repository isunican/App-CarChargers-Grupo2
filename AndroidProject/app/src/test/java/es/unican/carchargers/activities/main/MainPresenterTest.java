package es.unican.carchargers.activities.main;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.activities.main.IMainContract;
import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.Connection;
import es.unican.carchargers.model.Operator;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repositories;
@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {
    @Mock
    IMainContract.View mockView;
    IMainContract.Presenter sut;
    IRepository repository;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new MainPresenter();
    }
    // Metodo realizado por Isaac Berrouet
    @Test
    public void onSortedClickedTest() {

        // Se crean las companhias
        Operator operatorA = new Operator();
        operatorA.title = "A";
        Operator operatorB = new Operator();
        operatorB.title = "B";

        // Se crean las conexiones
        Connection connection1 = new Connection();
        connection1.powerKW = 600.1;
        Connection connection2 = new Connection();
        connection2.powerKW = 600;
        Connection connection3 = new Connection();
        connection3.powerKW = 0;

        // Se crean los cargadores
        Charger charger1 = new Charger();
        charger1.operator = operatorA;
        charger1.connections.add(connection1);
        Charger charger2 = new Charger();
        charger2.operator = operatorA;
        charger2.connections.add(connection2);
        Charger charger3 = new Charger();
        charger3.operator = operatorB;
        charger3.connections.add(connection2);
        Charger charger4 = new Charger();
        charger4.operator = operatorB;
        charger4.connections.add(connection3);

        // Se crean las listas de cargadores
        List<Charger> chargers1 = new ArrayList<Charger>();
        chargers1.add(charger2);
        chargers1.add(charger1);
        chargers1.add(charger4);
        List<Charger> chargers2 = new ArrayList<Charger>();
        chargers2.add(charger4);
        chargers2.add(charger2);
        chargers2.add(charger1);
        List<Charger> chargers3 = new ArrayList<Charger>();
        chargers3.add(charger2);
        List<Charger> chargers4 = new ArrayList<Charger>();
        chargers4.add(charger1);
        chargers4.add(charger2);
        chargers4.add(charger3);
        chargers4.add(charger4);
        List<Charger> chargers5 = new ArrayList<Charger>();

        List<Charger> chargersResult1 = new ArrayList<Charger>();
        chargersResult1.add(charger4);
        chargersResult1.add(charger2);
        chargersResult1.add(charger1);
        List<Charger> chargersResult2 = new ArrayList<Charger>();
        chargersResult2.add(charger1);
        chargersResult2.add(charger2);
        chargersResult2.add(charger4);
        List<Charger> chargersResult3 = new ArrayList<Charger>();
        chargersResult3.add(charger4);
        chargersResult3.add(charger2);
        chargersResult3.add(charger3);
        chargersResult3.add(charger1);

        // Lista de 3 cargadores que se ordenan de forma ascendente
        repository = Repositories.getFake(chargers1);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onSortedClicked("POTENCIA", true);
        // Se muestra la lista ordenada
        verify(mockView).showChargers(chargersResult1);

        // Lista de 3 cargadores que se ordena de forma descendente
        sut.onSortedClicked("POTENCIA", false);
        // Se muestra la lista ordenada
        verify(mockView).showChargers(chargersResult2);

        // No se selecciona si ordenar ascendentemente ni ascendentemente
        sut.onSortedClicked("POTENCIA", null);
        verify(mockView).showChargers(chargers1);
        // Salta la alerta al usuario
        verify(mockView).showAscDescEmpty();

        // Lista de 3 cargadores ya ordenada
        repository = Repositories.getFake(chargers2);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onSortedClicked("POTENCIA", true);
        // Se vuelve a mostrar la misma lista
        verify(mockView, atLeast(2)).showChargers(chargers2);

        // Lista de un solo cargador
        repository = Repositories.getFake(chargers3);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onSortedClicked("POTENCIA", true);
        // Se vuelve a mostrar la misma lista
        verify(mockView, atLeast(2)).showChargers(chargers3);

        // Lista de 4 cargadores con 2 cargadores de misma potencia
        repository = Repositories.getFake(chargers4);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onSortedClicked("POTENCIA", true);
        // Se muestra la lista ordenada y en caso de empate se hace por orden alfabetico
        verify(mockView).showChargers(chargersResult3);

        // Lista sin cargadores
        repository = Repositories.getFake(chargers5);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onSortedClicked("POTENCIA", true);
        // Se muestra la lista sin cargadores de nuevo
        verify(mockView, atLeast(2)).showChargers(chargers5);
        // Se alerta al usuario
        verify(mockView).showSortedEmpty();

        // No se aplica un criterio de ordenacion
        repository = Repositories.getFake(chargers1);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onSortedClicked("NINGUNO", true);
        // Se muestra la misma lista de nuevo
        verify(mockView, atLeast(2)).showChargers(chargers1);
        // Se alerta al usuario
        verify(mockView).showRuleEmpty();
    }

    // Metodos realizados por Carlos Silva
    /*
    @Test
    public void onChargerClickedTest() {
        List<Charger> shownChargers = new ArrayList<>();
        Charger charger0 = new Charger();
        Charger charger1 = new Charger();
        shownChargers.add(charger0);
        shownChargers.add(charger1);

        sut.onChargerClicked(1);

        verify(mockView).showChargerDetails(shownChargers.get(1));
    }

    @Test
    public void onMenuInfoClickedTest() {
        List<Charger> shownChargers = new ArrayList<>();

        sut.onMenuInfoClicked();

        verify(mockView).showInfoActivity();
    }
    */
}
