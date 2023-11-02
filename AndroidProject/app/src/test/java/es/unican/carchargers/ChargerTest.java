package es.unican.carchargers;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.Connection;

public class ChargerTest {
    @Test
    public void maxPowerTest() {

        // Create the connections for the test
        Connection connection1 = new Connection();
        connection1.powerKW = 600.1;
        Connection connection2 = new Connection();
        connection2.powerKW = 600;
        Connection connection3 = new Connection();
        connection3.powerKW = 2;
        Connection connection4 = new Connection();
        connection4.powerKW = 0;
        Connection connection5 = new Connection();
        connection5.powerKW = 600;

        // Create the chargers with their connections
        Charger charger1 = new Charger();
        charger1.connections.add(connection1);
        charger1.connections.add(connection2);
        charger1.connections.add(connection4);
        Charger charger2 = new Charger();
        charger2.connections.add(connection2);
        charger2.connections.add(connection5);
        charger2.connections.add(connection4);
        Charger charger3 = new Charger();
        charger3.connections.add(connection3);
        Charger charger4 = new Charger();

        // Check a charger with multiple connections
        assertEquals(600.1, charger1.maxPower(), 0.001);

        // Check a charger with multiple connections and two connections are draw
        assertEquals(600, charger2.maxPower(), 0.001);

        // Check a charger with only one connection
        assertEquals(2, charger3.maxPower(), 0.001);

        // Check a charger without connections
        assertEquals(0, charger4.maxPower(), 0.001);
    }
}