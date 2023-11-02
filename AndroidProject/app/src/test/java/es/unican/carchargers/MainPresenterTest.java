package es.unican.carchargers;

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
    @Test
    public void onSortedClickedTest() {

        Operator operatorA = new Operator();
        operatorA.title = "A";
        Operator operatorB = new Operator();
        operatorB.title = "B";

        Connection connection1 = new Connection();
        connection1.powerKW = 600.1;
        Connection connection2 = new Connection();
        connection2.powerKW = 600;
        Connection connection3 = new Connection();
        connection3.powerKW = 0;

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

        // List of 3 chargers ordered
        repository = Repositories.getFake(chargers1);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onSortedClicked("POTENCIA", true);
        verify(mockView).showChargers(chargersResult1);

        sut.onSortedClicked("POTENCIA", false);
        verify(mockView).showChargers(chargersResult2);


        sut.onSortedClicked("POTENCIA", Boolean.parseBoolean("A"));
        verify(mockView).showChargers(chargers1);

        repository = Repositories.getFake(chargers2);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onSortedClicked("POTENCIA", true);
        verify(mockView, atLeast(2)).showChargers(chargers2);


        repository = Repositories.getFake(chargers3);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onSortedClicked("POTENCIA", true);
        verify(mockView, atLeast(2)).showChargers(chargers3);

        repository = Repositories.getFake(chargers4);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onSortedClicked("POTENCIA", true);
        verify(mockView).showChargers(chargersResult3);

        repository = Repositories.getFake(chargers5);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onSortedClicked("POTENCIA", true);
        verify(mockView).showChargers(chargers5);

        repository = Repositories.getFake(chargers1);
        when(mockView.getRepository()).thenReturn(repository);
        sut.init(mockView);
        sut.onSortedClicked("NINGUNO", true);
        verify(mockView, atLeast(2)).showChargers(chargers1);
    }
}
