package com.eahm.drinkwaterapp.Utils;

import com.eahm.drinkwaterapp.Models.UserModel;
import com.pixplicity.easyprefs.library.Prefs;

public class UserManagement {

    public UserModel getCustomerData() {
        UserModel user = new UserModel();
        user.name= Prefs.getString(Constants.C_NAME, "");
        user.lastName= Prefs.getString(Constants.C_LAST_NAME, "");
        user.phone = Prefs.getString(Constants.C_PHONE, "");
        user.type = Prefs.getString(Constants.C_TYPE,  "");
        user.profileImageUrl = Prefs.getString(Constants.C_PROFILE_URL,  "");
        user.currentDevice = Prefs.getString(Constants.C_CURRENT_DEVICE,  "");
        return user;
    }


    public void setCustomerData(UserModel user) {
        Prefs.putString(Constants.C_NAME, user.name);
        Prefs.putString(Constants.C_LAST_NAME, user.lastName);
        Prefs.putString(Constants.C_PHONE, user.phone);
        Prefs.putString(Constants.C_TYPE, user.type);
        Prefs.putString(Constants.C_PROFILE_URL, user.profileImageUrl);
        Prefs.putString(Constants.C_CURRENT_DEVICE, user.currentDevice);
    }

    public void removeCustomerData(){
        UserModel cleanUser = new UserModel("","","","","","","","",null);
        setCustomerData(cleanUser);
    }

    public boolean isCustomerDataEmpty(){
        UserModel user = getCustomerData();
        return (user != null &&
                user.getName() != null && user.getName().isEmpty() &&
                user.getLastName() != null && user.getLastName().isEmpty() &&
                user.getPhone() != null && user.getPhone().isEmpty() &&
                user.getType() != null && user.getType().isEmpty() &&
                user.getProfileImageUrl() != null && user.getProfileImageUrl().isEmpty());
    }

}
