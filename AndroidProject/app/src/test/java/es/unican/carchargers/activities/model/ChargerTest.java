package es.unican.carchargers.activities.model;

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

    /**
     * Prueba unitaria realizada por Sergio Algorri.
     * En esta prueba unitaria no se comprueba el paso correcto del parámetro
     * debido a que esta llamada tiene como precondición que el String que se pasa como
     * párametro es distinto de nulo y no es vacío. Dicha comprobación se realiza en el
     * método costeTotalCarga.
     */
    @Test
    public void obtenerMenorPrecioTest() {
        // create the chargers with their usage cost for the test
        // one usage cost
        Charger charger1 = new Charger();
        charger1.usageCost = "0,39€/kWh";

        // two usage costs
        Charger charger2 = new Charger();
        charger2.usageCost = "0,39€/kWh DC - 0,29€/kWh AC";

        // multiple usage costs
        Charger charger3 = new Charger();
        charger3.usageCost = "0,39€/kWh DC - 0,19€/kWh AC - 0,29€/kWh";

        // usage cost without cost
        Charger charger4 = new Charger();
        charger4.usageCost = "parking fee";

        assertEquals(0.39, charger1.obtenerMenorPrecio(charger1.usageCost), 0.001);
        assertEquals(0.29, charger2.obtenerMenorPrecio(charger2.usageCost), 0.001);
        assertEquals(0.19, charger3.obtenerMenorPrecio(charger3.usageCost), 0.001);
        assertEquals(0.0, charger4.obtenerMenorPrecio(charger4.usageCost), 0.001);
    }

    /**
     * Prueba unitaria realizada por Carlos Silva.
     */
    @Test
    public void costeTotalCargaTest() {
        Charger chargerConCoste = new Charger();
        chargerConCoste.usageCost = "4,00€/kWh";
        Charger chargerCosteNulo = new Charger();
        Charger chargerCosteCero = new Charger();
        chargerCosteCero.usageCost = "0,00€/kWh";
        Charger chargerCosteNegativo = new Charger();
        chargerCosteNegativo.usageCost = "-1,00€/kWh";

        //UT.2a (Valores normales)
        assertEquals(312, chargerConCoste.costeTotalCarga(150.0,48.0), 0.001);

        //UT.2b (Capacidad de batería a 0)
        assertEquals(-1, chargerConCoste.costeTotalCarga(0.0,48.0), 0.001);

        //UT.2c (Porcentaje de batería negativo)
        assertEquals(-1, chargerConCoste.costeTotalCarga(150.0,-1.0), 0.001);

        //UT.2d (Cargador sin coste definido)
        assertEquals(-1, chargerCosteNulo.costeTotalCarga(150.0,48.0), 0.001);

        //UT.2e (Porcentaje de batería mayor que 100)
        assertEquals(-1, chargerConCoste.costeTotalCarga(150.0,101.0), 0.001);

        //UT.2f (Cargador con coste cero)
        assertEquals(0, chargerCosteCero.costeTotalCarga(150.0,48.0), 0.001);
    }
}