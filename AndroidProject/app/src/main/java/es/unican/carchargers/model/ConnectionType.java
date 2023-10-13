package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class ConnectionType {

    @SerializedName("ID")                   public String id;
    @SerializedName("FormalName")           public String formalName;

}
