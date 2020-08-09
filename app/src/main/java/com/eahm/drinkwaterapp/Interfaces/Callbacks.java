package com.eahm.drinkwaterapp.Interfaces;

public interface Callbacks  {

    void onReturnCallback(boolean status, String message, int code);

    //TODO: CALLBACKS FOR DIALOG
    void onYesResponse(String code, String message);
    void onNoResponse(String code, String message);

}
