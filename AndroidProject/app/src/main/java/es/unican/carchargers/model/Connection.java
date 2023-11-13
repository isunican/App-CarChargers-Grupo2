package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Connection {

    @SerializedName("ID")                   public String id;
    @SerializedName("PowerKW")              public double powerKW;
    @SerializedName("Quantity")             public int quantity;
    @SerializedName("ConnectionType")       public ConnectionType connectionType;
    @SerializedName("StatusType")           public StatusType statusType;

    public Connection () {
        this.connectionType = new ConnectionType();
        this.statusType = new StatusType();
    }

}
