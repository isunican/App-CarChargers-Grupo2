package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A charging station according to the OpenChargeMap API
 * Documentation: https://openchargemap.org/site/develop/api#/operations/get-poi
 *
 * Currently it only includes a sub-set of the complete model returned by OpenChargeMap
 */
@Parcel
public class Charger {
    
    @SerializedName("ID")                   public String id;
    @SerializedName("NumberOfPoints")       public int numberOfPoints;
    @SerializedName("UsageCost")            public String usageCost;
    @SerializedName("OperatorInfo")         public Operator operator;
    @SerializedName("AddressInfo")          public Address address;
    @SerializedName("Connections")          public List<Connection> connections;

    public boolean isFavourite = false;
    public double costeTotal = -1;

    public Charger() {
        this.connections = new ArrayList<Connection>();
        this.operator = new Operator();
        this.address = new Address();
    }

    public double maxPower() {
        double potenciaMax = 0;
        double pontenciaActual;
        for (int i = 0; i < connections.size(); i++) {
            pontenciaActual = connections.get(i).powerKW;
            if (pontenciaActual > potenciaMax) {
                potenciaMax = pontenciaActual;
            }
        }
        return potenciaMax;
    }

    public double costeTotalCarga(double capacidadBateria, double porcentajeBateriaActual) {

        if (capacidadBateria <= 0 || porcentajeBateriaActual < 0 || porcentajeBateriaActual > 100) {
            this.costeTotal = -1;
        } else {

            double bateriaRestante = capacidadBateria * ((100 - porcentajeBateriaActual)/100);

            if (this.usageCost == null || this.usageCost.equals("")) {
                this.costeTotal = -1;
            } else {
                double coste = obtenerMenorPrecio(this.usageCost);
                this.costeTotal = bateriaRestante * coste;
            }
        }

        return this.costeTotal;
    }

    // Es public para poder realizar su prueba unitaria.
    public double obtenerMenorPrecio(String cadena) {
        String patron = "(\\d+[,.]\\d{1,2})€/kWh";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(cadena);
        List<Double> precios = new ArrayList<>();

        while (matcher.find()) {
            String precioStr = matcher.group(1);
            double precio = Double.parseDouble(precioStr.replace(",", "."));
            precios.add(precio);
        }

        double precioMinimo = 0.0;
        if (!precios.isEmpty()) {
            precioMinimo = Collections.min(precios);
        }

        return precioMinimo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numberOfPoints, usageCost, operator, address, connections, isFavourite, costeTotal);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Charger charger = (Charger) obj;
        return Objects.equals(id, charger.id);
    }
}
