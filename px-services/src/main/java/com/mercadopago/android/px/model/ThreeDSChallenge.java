package com.mercadopago.android.px.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ThreeDSChallenge implements Serializable {
    @SerializedName("acsSignedContent")
    public String acsSignedContent;
    public String response;
    @SerializedName("threeDSServerTransID")
    public String threeDSServerTransID;
    @SerializedName("acsReferenceNumber")
    public String acsReferenceNumber;
    public String eci;
    @SerializedName("dsTransID")
    public String dsTransID;
    @SerializedName("acsTransID")
    public String acsTransID;
}
