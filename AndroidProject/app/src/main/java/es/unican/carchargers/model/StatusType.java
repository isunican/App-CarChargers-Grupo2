package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class StatusType {

    @SerializedName("ID")                   public String id;
    @SerializedName("IsOperational")        public boolean isOperational;

}
