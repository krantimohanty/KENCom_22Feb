package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranti on 10/26/2016.
 */

public class GetAllHubResponse {
    @SerializedName("hub_location")
    @Expose
    private List<HubLocation> hubLocation = new ArrayList<HubLocation>();

    /**
     *
     * @return
     * The hubLocation
     */
    public List<HubLocation> getHubLocation() {
        return hubLocation;
    }

    /**
     *
     * @param hubLocation
     * The hub_location
     */
    public void setHubLocation(List<HubLocation> hubLocation) {
        this.hubLocation = hubLocation;
    }
}
