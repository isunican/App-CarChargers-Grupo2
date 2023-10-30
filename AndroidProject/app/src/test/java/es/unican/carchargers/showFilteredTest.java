package es.unican.carchargers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import es.unican.carchargers.activities.main.IMainContract;
import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.Operator;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repositories;

@RunWith(MockitoJUnitRunner.class)
public class showFilteredTest {

    @Mock
    IMainContract.View mockView;
    IMainContract.Presenter sut;
    IRepository repository;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        sut = new MainPresenter();

    }


    @Test
    public void showFilteredTestA() {
        String companhia = "ENDESA";
        List<Charger> mockChargers = new ArrayList<>();
        Charger a = new Charger();
        a.operator.title = "ENDESA";
        mockChargers.add(a);
        repository = Repositories.getFake(mockChargers);
        when(mockView.getRepository()).thenReturn(repository);

        sut.onFilteredClicked("ENDESA");

        verify(mockView).showChargers(mockChargers);
    }


    @Test
    public void test() {
        Charger charger = new Charger();
        charger.numberOfPoints = 5;
        assertEquals(charger.numberOfPoints, 5);
    }







}
