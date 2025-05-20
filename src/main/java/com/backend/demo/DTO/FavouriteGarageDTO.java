package com.backend.demo.DTO;

public class FavouriteGarageDTO {
    private String userId;
    private String garageId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGarageId() {
        return garageId;
    }

    public void setGarageId(String garageId) {
        this.garageId = garageId;
    }

    public String toString() {
        return "Garage Id: " + this.getGarageId() + " UseId: "+ this.getUserId();
    }
}
