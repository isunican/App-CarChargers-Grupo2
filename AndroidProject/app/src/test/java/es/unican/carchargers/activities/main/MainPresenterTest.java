package es.unican.carchargers.activities.main;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import es.unican.carchargers.model.Charger;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class MainPresenterTest {

    @Mock
    private MainView view;

    private MainPresenter presenter;

    @Test
    public void test() {
        Charger charger = new Charger();
        charger.numberOfPoints = 5;
        assertEquals(charger.numberOfPoints, 5);
    }

    @Before
    public void setup() {
    }

    @Test
    public void onChargerClickedTest() {
        List<Charger> shownChargers = new ArrayList<>();
        Charger charger0 = new Charger();
        Charger charger1 = new Charger();
        shownChargers.add(charger0);
        shownChargers.add(charger1);
        presenter = new MainPresenter(view, shownChargers);

        presenter.onChargerClicked(1);

        verify(view).showChargerDetails(shownChargers.get(1));
    }

    @Test
    public void onMenuInfoClickedTest() {
        List<Charger> shownChargers = new ArrayList<>();
        presenter = new MainPresenter(view, shownChargers);

        presenter.onMenuInfoClicked();

        verify(view).showInfoActivity();
    }



}