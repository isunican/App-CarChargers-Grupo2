package es.unican.carchargers;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.Connection;

public class SortTest {
    List<Charger> chargers;
    Charger charger1;
    Charger charger2;
    Charger charger3;
    Connection connection1;
    Connection connection2;
    Connection connection3;

    @Before
    public void initialize() {
        chargers = new ArrayList<>();
        charger1 = new Charger();
        connection1 = new Connection();
        connection2 = new Connection();
        connection3 = new Connection();
        connection1.powerKW = 600.1;
        connection2.powerKW = 600;
        connection3.powerKW = 0;
        charger1.connections.add(connection1);
        charger2.connections.add(connection2);
        charger3.connections.add(connection3);
    }
    @Test
    public void sortPower() {

    }
}
