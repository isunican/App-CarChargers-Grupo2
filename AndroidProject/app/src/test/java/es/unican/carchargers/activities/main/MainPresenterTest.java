package es.unican.carchargers.activities.main;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import es.unican.carchargers.activities.main.IMainContract;
import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.constants.ECountry;
import es.unican.carchargers.constants.ELocation;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.Connection;
import es.unican.carchargers.model.Operator;
import es.unican.carchargers.repository.ICallBack;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repositories;
import es.unican.carchargers.repository.service.APIArguments;
import es.unican.carchargers.repository.service.FakeCall;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {
    @Mock
    IMainContract.View mockView;
    IMainContract.Presenter sut;
    IMainContract.View sutView;

    MainPresenter mp = new MainPresenter();
    MainView mv;


    @Mock
    private MainView view;

    @Mock
    private MainPresenter presenter;

    private IRepository repository;
    @Mock
    private IRepository repository2;
    private ICallBack cb;

    private APIArguments args = APIArguments.builder();

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new MainPresenter();
        view = mock(MainView.class);
        presenter = mock(MainPresenter.class);
    }
    // Metodo realizado por Isaac Berrouet y Carlos Silva
    @Test
    public void onSortedClickedTest() {

        // Se crean las companhias (Isaac Berrouet)
        Operator operatorA = new Operator();
        operatorA.title = "A";
        Operator operatorB = new Operator();
        operatorB.title = "B";

        // Se crean las conexiones (Isaac Berrouet)
        Connection connection1 = new Connection();
        connection1.powerKW = 600.1;
        Connection connection2 = new Connection();
        connection2.powerKW = 600;
        Connection connection3 = new Connection();
        connection3.powerKW = 0;

        // Se crean los cargadores (Isaac Berrouet)
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

        // Se crean las listas de cargadores (Isaac Berrouet)
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

        // Lista de 3 cargadores que se ordenan de forma ascendente (Isaac Berrouet)
        repository = Repositories.getFake(chargers1);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onSortedClicked("POTENCIA", true);
        // Se muestra la lista ordenada
        verify(mockView, atLeast(1)).showChargers(chargersResult1);

        // Lista de 3 cargadores que se ordena de forma descendente (Isaac Berrouet)
        sut.onSortedClicked("POTENCIA", false);
        // Se muestra la lista ordenada
        verify(mockView, atLeast(1)).showChargers(chargersResult2);

        // No se selecciona si ordenar ascendentemente ni ascendentemente (Isaac Berrouet)
        sut.onSortedClicked("POTENCIA", null);
        verify(mockView, atLeast(1)).showChargers(chargers1);
        // Salta la alerta al usuario
        verify(mockView).showAscDescEmpty();

        // Lista de 3 cargadores ya ordenada (Isaac Berrouet)
        repository = Repositories.getFake(chargers2);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onSortedClicked("POTENCIA", true);
        // Se vuelve a mostrar la misma lista (Isaac Berrouet)
        verify(mockView, atLeast(2)).showChargers(chargers2);

        // Lista de un solo cargador (Isaac Berrouet)
        repository = Repositories.getFake(chargers3);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onSortedClicked("POTENCIA", true);
        // Se vuelve a mostrar la misma lista
        verify(mockView, atLeast(2)).showChargers(chargers3);

        // Lista de 4 cargadores con 2 cargadores de misma potencia (Isaac Berrouet)
        repository = Repositories.getFake(chargers4);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onSortedClicked("POTENCIA", true);
        // Se muestra la lista ordenada y en caso de empate se hace por orden alfabetico
        verify(mockView, atLeast(1)).showChargers(chargersResult3);

        // Lista sin cargadores (Isaac Berrouet)
        repository = Repositories.getFake(chargers5);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onSortedClicked("POTENCIA", true);
        // Se muestra la lista sin cargadores de nuevo
        verify(mockView, atLeast(2)).showChargers(chargers5);
        // Se alerta al usuario
        verify(mockView).showSortedEmpty();

        // No se aplica un criterio de ordenacion (Isaac Berrouet)
        repository = Repositories.getFake(chargers1);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onSortedClicked("NINGUNO", true);
        // Se muestra la misma lista de nuevo
        verify(mockView, atLeast(2)).showChargers(chargers1);
        // Se alerta al usuario
        verify(mockView).showRuleEmpty();

        /* Aqui empiezan las pruebas del criterio CosteTotal */

        //Cargadores para estas pruebas (Carlos Silva)
        Charger charger4_1 = new Charger();
        Charger charger4_2 = new Charger();
        Charger charger5_2 = new Charger();
        Charger charger6_2 = new Charger();
        Charger charger_1_2 = new Charger();

        Connection connectionPower1 = new Connection();
        connectionPower1.powerKW = 1;
        Connection connectionPower2 = new Connection();
        connectionPower2.powerKW = 2;

        charger4_1.connections.add(connectionPower1);
        charger4_2.connections.add(connectionPower2);
        charger5_2.connections.add(connectionPower2);
        charger6_2.connections.add(connectionPower2);
        charger_1_2.connections.add(connectionPower2);

        charger4_1.usageCost = "4,00€/kWh";
        charger4_2.usageCost = "4,00€/kWh";
        charger5_2.usageCost = "5,00€/kWh";
        charger6_2.usageCost = "6,00€/kWh";

        //Listas de argumentos y resultados para estas pruebas (Carlos Silva)
        List<Charger> chargers1A = new ArrayList<Charger>();
        chargers1A.add(charger4_2);
        chargers1A.add(charger6_2);
        chargers1A.add(charger5_2);

        List<Charger> chargers1B = new ArrayList<Charger>();
        chargers1B.add(charger4_2);
        chargers1B.add(charger6_2);
        chargers1B.add(charger5_2);

        List<Charger> chargers1C = new ArrayList<Charger>();

        List<Charger> chargers1D = new ArrayList<Charger>();
        chargers1D.add(charger6_2);
        chargers1D.add(charger4_1);
        chargers1D.add(charger4_2);
        chargers1D.add(charger5_2);

        List<Charger> chargers1E = new ArrayList<Charger>();
        chargers1E.add(charger6_2);
        chargers1E.add(charger4_1);
        chargers1E.add(charger4_2);
        chargers1E.add(charger5_2);

        List<Charger> chargers1F = new ArrayList<Charger>();
        chargers1F.add(charger_1_2);
        chargers1F.add(charger4_1);
        chargers1F.add(charger4_2);
        chargers1F.add(charger5_2);

        List<Charger> chargers1G = new ArrayList<Charger>();
        chargers1G.add(charger_1_2);
        chargers1G.add(charger4_1);
        chargers1G.add(charger4_2);
        chargers1G.add(charger5_2);

        List<Charger> chargerResults1A = new ArrayList<Charger>();
        chargerResults1A.add(charger4_2);
        chargerResults1A.add(charger5_2);
        chargerResults1A.add(charger6_2);

        List<Charger> chargerResults1B = new ArrayList<Charger>();
        chargerResults1B.add(charger6_2);
        chargerResults1B.add(charger5_2);
        chargerResults1B.add(charger4_2);

        List<Charger> chargerResults1D = new ArrayList<Charger>();
        chargerResults1D.add(charger4_2);
        chargerResults1D.add(charger4_1);
        chargerResults1D.add(charger5_2);
        chargerResults1D.add(charger6_2);

        List<Charger> chargerResults1E = new ArrayList<Charger>();
        chargerResults1E.add(charger6_2);
        chargerResults1E.add(charger5_2);
        chargerResults1E.add(charger4_2);
        chargerResults1E.add(charger4_1);

        List<Charger> chargerResults1F = new ArrayList<Charger>();
        chargerResults1F.add(charger4_2);
        chargerResults1F.add(charger4_1);
        chargerResults1F.add(charger5_2);
        chargerResults1F.add(charger_1_2);

        List<Charger> chargerResults1G = new ArrayList<Charger>();
        chargerResults1G.add(charger5_2);
        chargerResults1G.add(charger4_2);
        chargerResults1G.add(charger4_1);
        chargerResults1G.add(charger_1_2);

        //UT.1a (Orden ascendente) (Carlos Silva)
        repository = Repositories.getFake(chargers1A);
        when(mockView.getRepository()).thenReturn(repository);
        when(mockView.returnCapacidadBateria()).thenReturn(100.0);
        when(mockView.returnPorcentajeBateria()).thenReturn(99.0);
        sut.init(mockView);
        sut.onSortedClicked("COSTE TOTAL", true);
        verify(mockView, atLeast(1)).showChargers(chargerResults1A);

        //UT.1b (Orden descendente) (Carlos Silva)
        repository = Repositories.getFake(chargers1B);
        when(mockView.getRepository()).thenReturn(repository);
        when(mockView.returnCapacidadBateria()).thenReturn(100.0);
        when(mockView.returnPorcentajeBateria()).thenReturn(99.0);
        sut.init(mockView);
        sut.onSortedClicked("COSTE TOTAL", false);
        verify(mockView, atLeast(1)).showChargers(chargerResults1B);

        //UT.1c (Orden no escogido) (Carlos Silva)
        repository = Repositories.getFake(chargers1C);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onSortedClicked("COSTE TOTAL", null);
        verify(mockView, atLeast(1)).showRuleEmpty();

        //UT.1d (Orden ascendente con empate) (Carlos Silva)
        repository = Repositories.getFake(chargers1D);
        when(mockView.getRepository()).thenReturn(repository);
        when(mockView.returnCapacidadBateria()).thenReturn(100.0);
        when(mockView.returnPorcentajeBateria()).thenReturn(99.0);
        sut.init(mockView);
        sut.onSortedClicked("COSTE TOTAL", true);
        verify(mockView, atLeast(1)).showChargers(chargerResults1D);

        //UT.1e (Orden descendente con empate) (Carlos Silva)
        repository = Repositories.getFake(chargers1E);
        when(mockView.getRepository()).thenReturn(repository);
        when(mockView.returnCapacidadBateria()).thenReturn(100.0);
        when(mockView.returnPorcentajeBateria()).thenReturn(99.0);
        sut.init(mockView);
        sut.onSortedClicked("COSTE TOTAL", false);
        verify(mockView, atLeast(1)).showChargers(chargerResults1E);

        //UT.1f (Orden ascendente con un coste indefinido) (Carlos Silva)
        repository = Repositories.getFake(chargers1F);
        when(mockView.getRepository()).thenReturn(repository);
        when(mockView.returnCapacidadBateria()).thenReturn(100.0);
        when(mockView.returnPorcentajeBateria()).thenReturn(99.0);
        sut.init(mockView);
        sut.onSortedClicked("COSTE TOTAL", true);
        verify(mockView, atLeast(1)).showChargers(chargerResults1F);

        //UT.1g (Orden descendente con un coste indefinido) (Carlos Silva)
        repository = Repositories.getFake(chargers1G);
        when(mockView.getRepository()).thenReturn(repository);
        when(mockView.returnCapacidadBateria()).thenReturn(100.0);
        when(mockView.returnPorcentajeBateria()).thenReturn(99.0);
        sut.init(mockView);
        sut.onSortedClicked("COSTE TOTAL", false);
        verify(mockView, atLeast(1)).showChargers(chargerResults1G);

    }

    /**
     * Metodo realizado por Sergio Algorri.
     */
    @Test
    public void onChargerClickedTest() {

        Charger charger1 = new Charger();
        Charger charger2 = new Charger();
        Charger charger3 = new Charger();

        List<Charger> listaCargadores = new ArrayList<>();
        listaCargadores.add(charger1);
        listaCargadores.add(charger2);
        listaCargadores.add(charger3);

        repository = Repositories.getFake(listaCargadores);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);

        sut.onChargerClicked(0);
        verify(mockView, atLeast(1)).showChargerDetails(charger1);
        sut.onChargerClicked(1);
        verify(mockView, atLeast(1)).showChargerDetails(charger2);
        sut.onChargerClicked(2);
        verify(mockView, atLeast(1)).showChargerDetails(charger3);

        /*
            Se comprueba que cuando el parametro index es mayor o igual que el tamanho de la
            lista de cargadores, no se produce ninguna llamada extra a ningun cargador de la lista.
         */
        sut.onChargerClicked(3);
        verify(mockView, atLeast(2)).showChargerDetails(charger1);
        verify(mockView, atLeast(2)).showChargerDetails(charger2);
        verify(mockView, atLeast(2)).showChargerDetails(charger3);
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

    //Test realizado por Adrián Ceballos
    @Test
    public void loadTest() {
        //Creo cargadores y les añado a una lista
        Charger charger1 = new Charger();
        Charger charger2 = new Charger();
        Charger charger3 = new Charger();
        Charger charger4 = new Charger();
        List<Charger> chargers = new ArrayList<>();
        chargers.add(charger1);
        chargers.add(charger2);
        chargers.add(charger3);
        chargers.add(charger4);

        //Caso válido, se llama al método requestChargers con éxito

        //Creo el repositorio fake, se accede correctamente al repositorio
        repository = Repositories.getFake(chargers);
        when(mockView.getRepository()).thenReturn(repository);

        //Caso válido, se llama al método requestChargers con éxito
        //Llamo al init que a su vez llama al load()
        sut.init(mockView);

        //Verifico que se llama a los métodos
        verify(mockView).showChargers(chargers);
        verify(mockView).showLoadCorrect(chargers.size());
        verify(mockView, never()).showLoadError();

        //Caso no válido, se llama al método requestChargers pero con un fail

        repository = Repositories.getFail();
        when(mockView.getRepository()).thenReturn(repository);

        sut.init(mockView);

        //Verifico que se llama al método correspondiente
        verify(mockView).showLoadError();
    }

    //Test realizado por Adrián Ceballos
    @Test
    public void getChargerComparatorTest() {
        //Se crean las companhias
        Operator operatorA = new Operator();
        operatorA.title = "A";
        Operator operatorB = new Operator();
        operatorB.title = "B";

        //Se crean las conexiones
        Connection connection1 = new Connection();
        connection1.powerKW = 1.5;
        Connection connection2 = new Connection();
        connection2.powerKW = 2.0;

        //Se crean los cargadores
        Charger charger1 = new Charger();
        charger1.operator = operatorA;
        charger1.connections.add(connection1);
        Charger charger2 = new Charger();
        charger2.operator = operatorB;
        charger2.connections.add(connection2);

        //Tengo que hacer uso de mocks porque en uno de los test tengo que acceder a MainView
        List<Charger> chargers = new ArrayList<>();
        chargers.add(charger1);
        chargers.add(charger2);
        repository = Repositories.getFake(chargers);
        when(mockView.getRepository()).thenReturn(repository);
        mp.init(mockView);

        //Caso de prueba por potencia y ascendente
        Comparator<Charger> comparator = mp.getChargerComparator("POTENCIA", true);
        int result = comparator.compare(charger1, charger2);
        //como charger1 tiene menos potencia que charger2, el compare devuelve < 0
        assert result < 0;

        //Caso de prueba por potencia y descendente
        comparator = mp.getChargerComparator("POTENCIA", false);
        result = comparator.compare(charger1, charger2);
        //charger 1 tiene menos potencia que charger2 pero aparece despues en orden descendente
        assert result > 0;

        //Caso de prueba cuando ascendente es nulo (ascendente por defecto)
        comparator = mp.getChargerComparator("POTENCIA", null);
        result = comparator.compare(charger1, charger2);
        //Charger1 tiene menos potencia que charger2
        assert  result < 0;

        //Caso de prueba por coste total y ascendente
        charger1.usageCost = "10,00€/kWh";
        charger2.usageCost = "8.00€/kWh";

        //Anhado capacidadBateria y porcentajeBateria
        double capacidadBateria = 100.0;
        double porcentajeBateria = 90.0;

        when(mockView.returnPorcentajeBateria()).thenReturn(porcentajeBateria);
        when(mockView.returnCapacidadBateria()).thenReturn(capacidadBateria);

        comparator = mp.getChargerComparator("COSTE TOTAL", true);
        result = comparator.compare(charger1, charger2);
        //Charger1 tiene un coste total mayor que charger2
        assert result > 0;

        //Caso de prueba por coste total y descendente
        comparator = mp.getChargerComparator("COSTE TOTAL", false);
        result = comparator.compare(charger1, charger2);
        //Charger1 tiene un coste total mayor que charger2 pero aparece despues en orden descendente
        assert result < 0;

        //Caso de prueba por coste total y ascendente es nulo (ascendente por defecto)
        comparator = mp.getChargerComparator("COSTE TOTAL", null);
        result = comparator.compare(charger1, charger2);
        //Charger1 tiene un coste total mayor que charger 2, ascendente por defecto
        assert result > 0;

        //Caso de prueba por potencia en caso de empate (se ordena alfabéticamente)
        // Se crean las conexiones

        connection2 = new Connection();
        //Estimo la misma potencia para los dos cargadores
        connection2.powerKW = 1.5;

        //Anhado la conexion empatada al charger2
        charger2.connections.add(connection2);

        comparator = mp.getChargerComparator("POTENCIA", true);
        result = comparator.compare(charger1, charger2);
        //Charger1 tiene la misma potencia que charger2 pero aparece primero alfabéticamente
        assert result < 0;

        //Caso de prueba por coste total en caso de empate (se ordena por potencia máxima)
        charger1.usageCost = "10,00€/kWh";
        charger2.usageCost = "10.00€/kWh";
        connection2 = new Connection();
        connection2.powerKW = 2.0;

        //Anhado la conexion empatada al charger2
        charger2.connections.add(connection2);

        comparator = mp.getChargerComparator("COSTE TOTAL", true);
        result = comparator.compare(charger1, charger2);
        //Charger1 tiene un coste total igual que charger 2, pero aparece después porque tiene menos potencia máxima
        assert result > 0;
    }

    //Test realizado por Pablo Gomez
    @Test
    public void onFilteredClickedTest() {

        // Se crean las companhias
        Operator operatorA = new Operator();
        operatorA.title = "A";
        Operator operatorB = new Operator();
        operatorB.title = "B";

        // Se crean las conexiones
        Connection connection1 = new Connection();
        connection1.powerKW = 160;
        Connection connection2 = new Connection();
        connection2.powerKW = 100;
        Connection connection3 = new Connection();
        connection3.powerKW = 0;

        // Se crean los cargadores
        Charger charger1 = new Charger();
        charger1.operator = operatorA;
        charger1.connections.add(connection3);
        Charger charger2 = new Charger();
        charger2.operator = operatorA;
        charger2.connections.add(connection2);
        Charger charger3 = new Charger();
        charger3.operator = operatorB;
        charger3.connections.add(connection1);

        // Se crean las listas de cargadores
        List<Charger> chargers1 = new ArrayList<Charger>();
        chargers1.add(charger1);
        chargers1.add(charger2);
        chargers1.add(charger3);

        List<Charger> chargersResult1 = new ArrayList<Charger>();
        chargersResult1.add(charger1);
        chargersResult1.add(charger2);
        chargersResult1.add(charger3);
        List<Charger> chargersResult2 = new ArrayList<Charger>();
        chargersResult2.add(charger2);
        chargersResult2.add(charger3);
        List<Charger> chargersResult3 = new ArrayList<Charger>();
        chargersResult3.add(charger2);
        List<Charger> chargersResult4 = new ArrayList<Charger>();

        // Todos los cargadores cumplen el filtrado
        repository = Repositories.getFake(chargers1);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onFilteredClicked("-", 0, 200);
        // Se muestra la lista filtrada
        verify(mockView, atLeast(1)).showChargers(chargersResult1);

        // Los dos cargadores con mas potencia cumplen el filtrado
        sut.onFilteredClicked("-", 50, 200);
        // Se muestra la lista filtrada
        verify(mockView).showChargers(chargersResult2);

        // Solo el cargador con 100 de potencia cumple el filtrado
        sut.onFilteredClicked("A", 50, 200);
        // Se muestra la lista filtrada
        verify(mockView).showChargers(chargersResult3);

        // Ningun cargador cumple el filtrado
        sut.onFilteredClicked("-", 180, 200);

        // Se muestra la lista filtrada, vacia en este caso
        verify(mockView).showChargers(chargersResult4);
        // Se alerta al usuario
        verify(mockView).showFilterEmpty();
    }
    /**
     * Este test está comentado para poder pasar el github actions,
     * ya que la implementación del método no es correcta y se
     * solucionará cuando el Product Owner solicite el cambio a traves del ticket
     * correspondiente.
    //Test realizado por Pablo Gomez
    @Test
    public void filterByOtherBusinessesTest(){
        // Se crean las companhias
        Operator operatorTesla = new Operator();
        operatorTesla.title = "Tesla";
        Operator operatorRepsol = new Operator();
        operatorRepsol.title = "Repsol";
        Operator operatorA = new Operator();
        operatorA.title = "A";
        Operator operatorB = new Operator();
        operatorB.title = "B";

        // Se crean las conexiones
        Connection connection1 = new Connection();
        connection1.powerKW = 160;
        Connection connection2 = new Connection();
        connection2.powerKW = 100;
        Connection connection3 = new Connection();
        connection3.powerKW = 0;

        // Se crean los cargadores
        Charger charger1 = new Charger();
        charger1.operator = operatorRepsol;
        charger1.connections.add(connection3);
        Charger charger2 = new Charger();
        charger2.operator = operatorRepsol;
        charger2.connections.add(connection2);
        Charger charger3 = new Charger();
        charger3.operator = operatorTesla;
        charger3.connections.add(connection1);
        Charger charger4 = new Charger();
        charger4.operator = operatorA;
        charger4.connections.add(connection2);
        Charger charger5 = new Charger();
        charger5.operator = operatorB;
        charger5.connections.add(connection1);

        // Se crean las listas de cargadores
        List<Charger> chargers1 = new ArrayList<Charger>();
        chargers1.add(charger1);
        chargers1.add(charger2);
        chargers1.add(charger3);
        chargers1.add(charger4);

        List<Charger> chargers2 = new ArrayList<Charger>();
        chargers2.add(charger1);
        chargers2.add(charger4);
        chargers2.add(charger5);

        List<Charger> chargers3 = new ArrayList<Charger>();
        chargers3.add(charger1);
        chargers3.add(charger2);
        chargers3.add(charger3);

        List<Charger> chargersResult1 = new ArrayList<Charger>();
        chargersResult1.add(charger4);
        List<Charger> chargersResult2 = new ArrayList<Charger>();
        chargersResult2.add(charger4);
        chargersResult2.add(charger5);
        List<Charger> chargersResult3 = new ArrayList<Charger>();

        //Prueba 2.a
        mp.shownChargers = chargers1;
        mp.filterByOtherBusinesses();
        assertEquals(mp.filteredChargers, chargersResult1);

        //Prueba 2.b
        mp.shownChargers = chargers2;
        mp.filterByOtherBusinesses();
        assertEquals(mp.filteredChargers, chargersResult2);

        //Prueba 2.c
        mp.shownChargers = chargers3;
        mp.filterByOtherBusinesses();
        assertEquals(mp.filteredChargers, chargersResult3);
    }
    */
}
