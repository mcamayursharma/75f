package com.assignment.canvas.models;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.kinvey.java.model.KinveyReference;

/**
 * Created by mayursharma on 4/4/17.
 */

public class Floor extends GenericJson {

    @Key
    private String name;
    @Key("filename")
    private String fileName;
    @Key("geo_orientation")
    private String geoOrientation;
    @Key
    private KinveyReference data;

    public Floor(){

    }

    public void initReference(FloorData data){
        KinveyReference reference = new KinveyReference("FloorData", data.get("_id").toString());
        this.data = reference;
    }



}
