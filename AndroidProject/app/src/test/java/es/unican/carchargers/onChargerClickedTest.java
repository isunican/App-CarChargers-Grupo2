package es.unican.carchargers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.activities.main.IMainContract;
import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repositories;

public class onChargerClickedTest {

    @Mock
    IMainContract.View contractView;
    IMainContract.Presenter p;
    ArgumentCaptor<Charger> capturaCharger;

    //declaro los cargadores
    Charger charger1;
    Charger charger2;

    public void setup() {
        MockitoAnnotations.openMocks(this); // Creación de los mocks definidos anteriormente con @Mock
        capturaCharger = ArgumentCaptor.forClass(Charger.class);
        p = new MainPresenter();
        charger1 = new Charger();
        charger2 = new Charger();
    }

    @Test
    public void testOnChargerClickedValidoTest() {
        setup();
        List<Charger> chargers = new ArrayList<Charger>();
        chargers.add(charger1);
        chargers.add(charger2);

        IRepository repository = Repositories.getFake(chargers);
        when(contractView.getRepository()).thenReturn(repository);

        p.init(contractView);
        p.onChargerClicked(1);
        verify(contractView).showChargerDetails(capturaCharger.capture());
    }
}
