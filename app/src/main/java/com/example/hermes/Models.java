package com.example.hermes;
import org.parceler.Parcel;

@Parcel
public class Models {

    private String nameID;
    private String messagingServicesID;

    public Models() {} ;

    public Models(String nameID, String messagingServicesID){
        this.nameID = nameID;
        this.messagingServicesID = messagingServicesID;
    }

    public String getNameID() {
        return nameID;
    }
    public void setNameID(String nameID) {
        this.nameID = nameID;
    }

    public String getMessagingServicesID() {
        return messagingServicesID;
    }
    public void setMessagingServicesID(String messagingServicesID) {
        this.messagingServicesID = messagingServicesID;
    }
}
