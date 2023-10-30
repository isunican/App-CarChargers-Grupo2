package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;


import java.util.ArrayList;
import java.util.List;

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
    private boolean ascendente = false;

    public Charger() {
        this.connections = new ArrayList<Connection>();
        this.operator = new Operator();
        this.address = new Address();
    }

    public double maxPower() {
        double potenciaMax = 0;
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).powerKW > potenciaMax) {
                potenciaMax = connections.get(i).powerKW;
            }
        }
        return potenciaMax;
    }
}
