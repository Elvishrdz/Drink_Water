package com.eahm.drinkwaterapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eahm.drinkwaterapp.Models.UserModel;
import com.eahm.drinkwaterapp.Utils.UserManagement;
import com.eahm.drinkwaterapp.Utils.Utils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.auth.api.credentials.CredentialRequestResponse;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.credentials.IdentityProviders;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@SuppressLint("Registered") // Remover si se añade al Manifest
public class LoginActivity extends AppCompatActivity {

    //region VARIABLES
    RelativeLayout layoutAuth;
    LoginButton buttonFacebookLogin;
    SignInButton buttonGoogleSignIn;
    Button buttonSignUpForm;
    Button buttonSignInForm;

    LinearLayout layoutSignUpForm;
    TextInputLayout inputSignUpUserEmail;
    TextInputLayout inputSignUpUserPassword;
    CheckBox checkBoxTerms;
    ProgressBar progressBarSignUpForm;
    Button buttonJoin;
    Button buttonSignUpFormReturn;

    LinearLayout layoutResetPassword;
    TextInputLayout inputResetPasswordUserEmail;
    Button buttonSendInstructions;
    Button buttonResetPasswordReturn;

    LinearLayout layoutLoginForm;
    TextInputLayout inputLoginUserEmail;
    TextInputLayout inputLoginUserPassword;
    Button buttonLogIn;
    Button buttonForgotPassword;
    Button buttonSignUp;
    Button buttonLoginFormReturn;
    ProgressBar progressBarLogIn;

    static final int CODE_GOOGLE_SIGN_IN = 123;
    static final int CODE_GOOGLE_SELECT_ACCOUNT = 777;
    static final int CODE_SIGN_IN_HINT = 834;

    FirebaseAuth mAuth;

    //Facebook
    CallbackManager callbackManager;

    //Google Sign In
    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;

    //Smart Lock
    CredentialsClient mCredentialClient;
    CredentialRequest mCredentialRequest;
    //endregion VARIABLES

    @Override
    public void onBackPressed() {

        if(progressBarSignUpForm.getVisibility() == View.VISIBLE){
           return;
        }

        if(layoutSignUpForm.getVisibility() == View.VISIBLE){
            setLayoutView(1);
        }
        else if(layoutResetPassword.getVisibility() == View.VISIBLE){
            setLayoutView(2);
        }
        else if(layoutLoginForm.getVisibility() == View.VISIBLE){
            setLayoutView(1);
        }
        else if(layoutAuth.getVisibility() == View.VISIBLE){
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Locale.setDefault(Locale.ENGLISH);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        //region Find View By Ids
        layoutAuth = findViewById(R.id.layoutAuth);
        buttonFacebookLogin = findViewById(R.id.buttonFacebookLogin);
        buttonGoogleSignIn = findViewById(R.id.buttonGoogleLogin);
        buttonSignInForm = findViewById(R.id.buttonSignInForm);
        buttonSignUpForm = findViewById(R.id.buttonSignUpForm);

        layoutSignUpForm = findViewById(R.id.layoutSignUpForm);
        inputSignUpUserEmail = findViewById(R.id.inputSignUpUserEmail);
        inputSignUpUserPassword = findViewById(R.id.inputSignUpUserPassword);
        checkBoxTerms = findViewById(R.id.checkBoxTerms);
        progressBarSignUpForm = findViewById(R.id.progressBarSignUpForm);
        buttonJoin= findViewById(R.id.buttonJoin);
        buttonSignUpFormReturn = findViewById(R.id.buttonSignUpFormReturn);

        layoutResetPassword = findViewById(R.id.layoutResetPassword);
        inputResetPasswordUserEmail = findViewById(R.id.inputResetPasswordUserEmail);
        buttonSendInstructions = findViewById(R.id.buttonSendInstructions);
        buttonResetPasswordReturn = findViewById(R.id.buttonResetPasswordReturn);

        layoutLoginForm = findViewById(R.id.layoutLoginForm);
        inputLoginUserEmail = findViewById(R.id.inputLoginUserEmail);
        inputLoginUserPassword = findViewById(R.id.inputLoginUserPassword);
        buttonLogIn = findViewById(R.id.buttonLogIn);
        buttonForgotPassword = findViewById(R.id.buttonForgotPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonLoginFormReturn = findViewById(R.id.buttonLoginFormReturn);
        progressBarLogIn = findViewById(R.id.progressBarLogIn);
        //endregion Find View By Ids

        //region FACEBOOK
        callbackManager = CallbackManager.Factory.create();
        buttonFacebookLogin.setReadPermissions("email", "public_profile", "user_friends");
        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        // If using in a fragment
        // buttonFacebookLogin.setFragment(this);
        //endregion FACEBOOK

        //region GOOGLE
        gso = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this )
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "Fallo la conexion ApiClient", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //endregion GOOGLE

        //region SMART LOCK
        mCredentialClient = Credentials.getClient(this);

        mCredentialRequest = new CredentialRequest.Builder()
                .setPasswordLoginSupported(true)
                .setAccountTypes(IdentityProviders.FACEBOOK, IdentityProviders.GOOGLE, IdentityProviders.TWITTER)
                .build();

        requestCredentials();

        //endregion SMART LOCK

        //region LISTENERS
        buttonFacebookLogin.setOnClickListener(v->buttonInteractions(false));
        buttonFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Toast.makeText(LoginActivity.this, "ON SUCCESS ", Toast.LENGTH_SHORT).show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code

                buttonInteractions(true);
                Toast.makeText(LoginActivity.this, "ON CANCEL ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                buttonInteractions(true);
                Toast.makeText(LoginActivity.this, "ON ERROR " + exception.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        buttonGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonInteractions(false);
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, CODE_GOOGLE_SIGN_IN);
            }
        });
        buttonSignUpForm.setOnClickListener(v -> setLayoutView(4));
        buttonSignInForm.setOnClickListener(v -> setLayoutView(2));

        buttonJoin.setOnClickListener(v ->{
            register();
        });
        buttonSignUpFormReturn.setOnClickListener(v -> setLayoutView(1));

        buttonSendInstructions.setOnClickListener(v -> {
            Toast.makeText(this, "Send Instructions...", Toast.LENGTH_SHORT).show();
        });
        buttonResetPasswordReturn.setOnClickListener(v -> setLoginFormView());

        buttonLogIn.setOnClickListener(v->{
            Toast.makeText(this, "Iniciar Sesion...", Toast.LENGTH_SHORT).show();
            login();
        });
        buttonForgotPassword.setOnClickListener(v -> {
            EditText loginEmail = inputLoginUserEmail.getEditText();
            EditText resetEmail = inputResetPasswordUserEmail.getEditText();

            if(loginEmail != null && resetEmail != null){
                String email = loginEmail.getText().toString();

                if(new Utils().validateEmail(email, this).equalsIgnoreCase("TRUE")){
                    resetEmail.setText(email);
                }
                else {
                    resetEmail.setText("");
                }
            }
            setLayoutView(3);
        });
        buttonSignUp.setOnClickListener(v -> setLayoutView(4));
        buttonLoginFormReturn.setOnClickListener(v -> setLayoutView(1));
        //endregion LISTENERS

        buttonInteractions(true);
        setLayoutView(1);
    }

    boolean isLogin = false;
    private void login() {
        final String email = inputLoginUserEmail.getEditText().getText().toString();
        //final String password = //inputLoginUserPassword.getEditText().getText().toString();

        Utils utils = new Utils();

        String validateEmail = utils.validateEmail(email, this);
        if (!validateEmail.equalsIgnoreCase("true")){
            Toast.makeText(getApplicationContext(), validateEmail, Toast.LENGTH_SHORT).show();
            return;
        }

        String validatePassword = utils.validatePassword(inputLoginUserPassword.getEditText().getText().toString(), this);
        if (!validatePassword.equalsIgnoreCase("true")){
            Toast.makeText(getApplicationContext(), validatePassword, Toast.LENGTH_SHORT).show();
            return;
        }

        configView(1,false,"","");

        Utils.hideKeyboard(LoginActivity.this);

        isLogin = true;

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, inputLoginUserPassword.getEditText().getText().toString()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //region SUCCESSFUL
                    inputLoginUserPassword.getEditText().setText("");

                    if (FirebaseAuth.getInstance().getCurrentUser() == null){
                        configView(1,true,
                                "No se pudo obtener el usuario, intentalo de nuevo",
                                "login_fail");
                        return;
                    }
                    else if(!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                        //region VERIFY EMAIL

                        //String toastMessage =  "Aun no has verificado tu cuenta, revisa tu correo";

                        configView(1,true, "", "");
                        isLogin = false;

                        AlertDialog.Builder message = new AlertDialog.Builder(LoginActivity.this);

                        message.setMessage("Tu correo no esta verificado! \nSolicita el mensaje y activa tu cuenta para continuar...");

                        message.setNegativeButton("Enviar verificación", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        String message = "No se pudo enviar el mensaje intenta de nuevo";

                                        if(task.isSuccessful()) message = "Revisa tu correo electronico";

                                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                                        if(FirebaseAuth.getInstance() != null) FirebaseAuth.getInstance().signOut();

                                    }
                                });
                            }
                        });

                        message.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(FirebaseAuth.getInstance() != null) FirebaseAuth.getInstance().signOut();
                            }
                        });

                        message.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                if(FirebaseAuth.getInstance() != null) FirebaseAuth.getInstance().signOut();
                            }
                        });

                        message.create().show();
                        return;

                        //endregion VERIFY EMAIL
                    }

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference userInfo = FirebaseDatabase.getInstance().getReference().child("users").child("customer").child(userId);

                    userInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){

                                String displayName = dataSnapshot.hasChild("name") ? dataSnapshot.child("name").getValue(true).toString() : "";
                                String displaySecondName = dataSnapshot.hasChild("secondName") ? dataSnapshot.child("secondName").getValue(true).toString() : "";
                                String displayLastName = dataSnapshot.hasChild("lastName") ? dataSnapshot.child("lastName").getValue(true).toString() : "";
                                String displaySecondLastName = dataSnapshot.hasChild("secondLastName") ? dataSnapshot.child("secondLastName").getValue(true).toString() : "";
                                String displayPhone = dataSnapshot.hasChild("phone") ? dataSnapshot.child("phone").getValue(true).toString() : "";
                                String displayType = dataSnapshot.hasChild("type") ? dataSnapshot.child("type").getValue(true).toString() : "";
                                String profileImageUrl = dataSnapshot.hasChild("profileImageUrl") ? dataSnapshot.child("profileImageUrl").getValue(true).toString() : "";
                                //Map<String,String> displayLastConnection= dataSnapshot.hasChild("lastConnection") ? (Map< >)dataSnapshot.child("lastConnection").getValue(true) : null;
                                Map<String,String> displayLastConnection= null;

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(displayName)
                                        .setPhotoUri(Uri.parse(profileImageUrl))
                                        .build();

                                if (FirebaseAuth.getInstance().getCurrentUser() == null){
                                    configView(1,true,
                                            "Conexion con usuario perdida al autenticar! intentalo de nuevo",
                                            "login_fail");
                                    return;
                                }

                                FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //GUARDAR DISPOSITIVO EN FIREBASE
                                        HashMap map = new HashMap();
                                        String finalType = "customer";

                                        map.put("type", finalType);
                                        map.put("currentDevice", utils.getDeviceName(LoginActivity.this));
                                        map.put("lastConnection", ServerValue.TIMESTAMP);

                                        userInfo.getRef().updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if(task.isSuccessful()){
                                                    UserManagement userManager = new UserManagement();

                                                    UserModel customerModel = new UserModel(
                                                            displayName,
                                                            displaySecondName,
                                                            displayLastName,
                                                            displaySecondLastName,
                                                            displayPhone,
                                                            finalType != null ? finalType : displayType,
                                                            profileImageUrl,
                                                            utils.getDeviceName(LoginActivity.this),
                                                            displayLastConnection);

                                                    userManager.setCustomerData(customerModel);

                                                    Intent intent = null;

                                                    progressBarLogIn.setVisibility(View.GONE);

                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                    Credential myCredential = new Credential.Builder(user.getEmail())
                                                            .setName(user.getDisplayName())
                                                            .setProfilePictureUri(user.getPhotoUrl())
                                                            .build();


                                                    // MOSTRAR CUADRO DE DIALOGO DE SMART LOCK
                                                    mCredentialClient.save(myCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if(!task.isComplete() && task.getException() != null){
                                                                if (task.getException() instanceof ResolvableApiException) {
                                                                    ResolvableApiException rae = (ResolvableApiException) task.getException();
                                                                    try {
                                                                        rae.startResolutionForResult(LoginActivity.this, 888);
                                                                    } catch (IntentSender.SendIntentException ex) {
                                                                        Toast.makeText(LoginActivity.this, "Save failed", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } else {
                                                                    // Request has no resolution
                                                                    Toast.makeText(LoginActivity.this, "Save failed", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                            //AUTENTICADO
                                                            //Intent intent = new Intent(LoginActivity.this, PrevWaterActivity.class);
                                                            Intent intent = new Intent(LoginActivity.this, WaterDrinkActivity.class);
                                                            isLogin = false;
                                                            //startActivityPassList(intent,true);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });

                                                }
                                                else {
                                                    configView(1,true,
                                                            "No se pudo completar el inicio, intenta de nuevo!",
                                                            "login_fail");
                                                }
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        configView(1,true,
                                                "Fallo al actualizar tus datos: " + e.toString() + " intenta de nuevo!",
                                                "login_fail");
                                    }
                                });
                            }
                            else {
                                //esta registrado, pero no hay datos en la DB,
                                //podriamos enviarlo a settings para crear sus datos (option)
                                configView(1,true,
                                        "No se encuentran tus datos, ponte en contacto con nosotros!",
                                        "login_fail");
                                //Este usuario puede pertenecer a otro servicio tus datos fueron borrados
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            configView(1,true,
                                    "Fallo al leer tus datos: " + databaseError.toString() + " Intenta de nuevo!",
                                    "login_fail");
                        }
                    });

                    //endregion SUCCESSFUL
                }
                else {
                    configView(1,true,
                            "Credenciales Invalidas, Intenta de nuevo!",
                            "login_fail");
                }
            }
        });
    }

    private void configView(int view, boolean allEnabled, String toastMessage, String codeActions){

        if(view == 1){// login

            if(allEnabled) progressBarLogIn.setVisibility(View.GONE);
            else progressBarLogIn.setVisibility(View.VISIBLE);


            inputLoginUserEmail.setEnabled(allEnabled);
            inputLoginUserPassword.setEnabled(allEnabled);
            buttonLogIn.setEnabled(allEnabled);
            buttonForgotPassword.setEnabled(allEnabled);
            buttonSignUp.setEnabled(allEnabled);
            buttonLoginFormReturn.setEnabled(allEnabled);

            if(!toastMessage.isEmpty()){
                Toast.makeText(LoginActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
            }

            if(codeActions.equals("login_fail")){
                if(FirebaseAuth.getInstance() != null) FirebaseAuth.getInstance().signOut();
                new UserManagement().removeCustomerData();
                isLogin = false;
            }
        }



    }

    //region CHANGING VIEWS
    private void setLayoutView(int codeView){

        if(codeView == 1) layoutAuth.setVisibility(View.VISIBLE);
        else layoutAuth.setVisibility(View.GONE);

        if(codeView == 2) layoutLoginForm.setVisibility(View.VISIBLE);
        else layoutLoginForm.setVisibility(View.GONE);

        if(codeView == 3) layoutResetPassword.setVisibility(View.VISIBLE);
        else layoutResetPassword.setVisibility(View.GONE);

        if(codeView == 4) layoutSignUpForm.setVisibility(View.VISIBLE);
        else layoutSignUpForm.setVisibility(View.GONE);

    }

    private void setLoginFormView(){
        setLayoutView(2);

        //region SIGN-IN HINT
        HintRequest hintRequest = new HintRequest.Builder()
                .setHintPickerConfig(new CredentialPickerConfig.Builder()
                        .setShowCancelButton(true)
                        .build())
                .setEmailAddressIdentifierSupported(true)
                .setAccountTypes(IdentityProviders.GOOGLE, IdentityProviders.FACEBOOK, IdentityProviders.TWITTER)
                .build();


        PendingIntent intent = mCredentialClient.getHintPickerIntent(hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), CODE_SIGN_IN_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            //Toast.makeText(this, "Could not start hint picker Intent", Toast.LENGTH_SHORT).show();
            Log.e(LoginActivity.this.getLocalClassName(), "Could not start hint picker Intent: " + e.toString(), e);
        }

        //endregion SIGN-IN HINT

    }
    //endregion CHANGING VIEWS

    private void buttonInteractions(boolean enabled){
        buttonFacebookLogin.setEnabled(enabled);
        buttonGoogleSignIn.setEnabled(enabled);
        buttonSignUpForm.setEnabled(enabled);
        buttonSignInForm.setEnabled(enabled);

        buttonJoin.setEnabled(enabled);
        buttonSignUpFormReturn.setEnabled(enabled);

        buttonSendInstructions.setEnabled(enabled);
        buttonResetPasswordReturn.setEnabled(enabled);

        buttonLogIn.setEnabled(enabled);
        buttonForgotPassword.setEnabled(enabled);
        buttonSignUp.setEnabled(enabled);
        buttonLoginFormReturn.setEnabled(enabled);
    }

    private void requestCredentials(){
        //Request stored credentials
        mCredentialClient.request(mCredentialRequest).addOnSuccessListener(new OnSuccessListener<CredentialRequestResponse>() {
            @Override
            public void onSuccess(CredentialRequestResponse credentialRequestResponse) {
                onCredentialRetrieved(credentialRequestResponse.getCredential());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // This is most likely the case where the user has multiple saved
                    // credentials and needs to pick one. This requires showing UI to
                    // resolve the read request.
                    ResolvableApiException rae = (ResolvableApiException) e;
                    try {
                        rae.startResolutionForResult(LoginActivity.this, CODE_GOOGLE_SELECT_ACCOUNT);
                    } catch (IntentSender.SendIntentException ex) {
                        //hideProgress();
                        Toast.makeText(LoginActivity.this, "Error: " + ex.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else if (e instanceof ApiException) {
                    // The user must create an account or sign in manually.
                    ApiException ae = (ApiException) e;
                    int code = ae.getStatusCode();
                    Toast.makeText(LoginActivity.this, "Create an account: " + code, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Something went wrong: " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CODE_SIGN_IN_HINT){
            if (resultCode == RESULT_OK && data != null) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);

                String email = credential.getId();
                Objects.requireNonNull(inputLoginUserEmail.getEditText()).setText(email);
                inputLoginUserPassword.requestFocus();

                // Optionally, you can also check if the Credential object contains an ID token that has a verified email address. If so, you can skip your app's email verification step, since the email address has already been verified by Google.

            } else {
                Log.e("SIGN_IN_HINT", "Hint Read Failed or canceled");
                //Toast.makeText(this, "Hint Read Failed", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == CODE_GOOGLE_SELECT_ACCOUNT){
            if(resultCode == RESULT_OK){
                buttonInteractions(false);
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                onCredentialRetrieved(credential);
            }
            else {
                Toast.makeText(this, "Credential Read Failed", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == 888){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "Credential Saved", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(this, "Save: Canceled by user", Toast.LENGTH_SHORT).show();
            }
        }
        
        else if(requestCode == CODE_GOOGLE_SIGN_IN){
            //GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //handleSignInResult(result);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }
            catch (ApiException e){
                Toast.makeText(this, "No se Pudo obtener acceso con google " + e.toString(), Toast.LENGTH_SHORT).show();
                buttonInteractions(true);
            }
        }

    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            Toast.makeText(this, "Sin Usuario", Toast.LENGTH_SHORT).show();
           /* username.setText("USER NAME");
            info.setText("INFO");
            imageViewProfile.setImageResource(R.drawable.btn_google_dark_focus_xxxhdpi);*/
        }
        else {
           /* username.setText(currentUser.getDisplayName());
            info.setText(currentUser.getEmail() + " - " + currentUser.getPhoneNumber());

            Glide.with(this)
                    .load(currentUser.getPhotoUrl())
                    .into(imageViewProfile);*/
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user != null) {
                               updateDatabaseValues(user);
                            }
                            else {
                                // WE DONT HAVE USER... LOG OUT!
                            }
                        } else {
                            buttonInteractions(true);
                            Toast.makeText(LoginActivity.this, "fallo aquiii", Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                           // Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    private void updateDatabaseValues(FirebaseUser user) {
        Map map = new HashMap();
        map.put("name", user.getDisplayName());
        map.put("photoUrl",user.getPhotoUrl().toString());
        map.put("currentDevice", new Utils().getDeviceName(LoginActivity.this));
        map.put("lastLogin", ServerValue.TIMESTAMP);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child("customer").child(user.getUid());

        ref.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //AUTENTICADO
                //Intent intent = new Intent(LoginActivity.this, PrevWaterActivity.class);
                Intent intent = new Intent(LoginActivity.this, WaterDrinkActivity.class);
                startActivity(intent);
                finish();
               // updateUI(user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //LOG.OUT TOOOOO
                buttonInteractions(true);
                Toast.makeText(LoginActivity.this, "Algo Fallo!" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onCredentialRetrieved(Credential credential) {

        String accountType = credential.getAccountType();

        Toast.makeText(this, "Credential Read: " + accountType, Toast.LENGTH_SHORT).show();

        if(accountType == null){
            // Sign the user in with information from the Credential.

            //signInWithPassword(credential.getId(), credential.getPassword());
        }
        else if (accountType.equals(IdentityProviders.GOOGLE)){
            signInWithGoogle(credential);
        }
        else if (accountType.equals(IdentityProviders.FACEBOOK)){

        }
        else if (accountType.equals(IdentityProviders.TWITTER)){

        }

    }

    private void signInWithGoogle(Credential credential) {
      /*gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();*/

        GoogleSignInClient signInClient = GoogleSignIn.getClient(this, gso);
        Task<GoogleSignInAccount> task = signInClient.silentSignIn();

        task.addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
            @Override
            public void onSuccess(GoogleSignInAccount gsa) {

                Credential _credential = new Credential.Builder(gsa.getEmail())
                        .setAccountType(IdentityProviders.GOOGLE)
                        .setName(gsa.getDisplayName())
                        .setProfilePictureUri(gsa.getPhotoUrl())
                        .build();

                firebaseAuthWithGoogle(gsa);
                saveSmartLockCredentials(_credential);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                buttonInteractions(true);
                Toast.makeText(LoginActivity.this, "Something Fail: " + e.toString(), Toast.LENGTH_SHORT).show();
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException rae = (ResolvableApiException) e;
                    try {
                        rae.startResolutionForResult(LoginActivity.this, 888);
                    } catch (IntentSender.SendIntentException ex) {
                        Toast.makeText(LoginActivity.this, "Save failed", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(e instanceof ApiException){
                    Toast.makeText(LoginActivity.this, "Save failed Api", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Request has no resolution
                    Toast.makeText(LoginActivity.this, "Save failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveSmartLockCredentials(Credential credential) {

        mCredentialClient.save(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(LoginActivity.this, "Credential Saved", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                ResolvableApiException rae = (ResolvableApiException) e;
                try {
                    rae.startResolutionForResult(LoginActivity.this, 888);
                } catch (IntentSender.SendIntentException ex) {
                    Toast.makeText(LoginActivity.this, "Save failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Request has no resolution
                Toast.makeText(LoginActivity.this, "Save failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user != null) {
                                updateDatabaseValues(user);
                            }
                            else {
                                // WE DONT HAVE USER... LOG OUT!
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    //LINK DIFERENTS PROVIDER ACCOUNTS TO A UNIQUE USER


    private void linkWithCredential(AuthCredential credential){

         /*
        AuthCredential credential = GoogleAuthProvider.getCredential(googleIdToken, null);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        */

        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //"linkWithCredential:success"
                            FirebaseUser user = task.getResult().getUser();
                            updateUI(user);
                        } else {
                            //"linkWithCredential:failure", task.getException()
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }


    private void unlinkWithCredential(AuthCredential credential){

        /*
        AuthCredential credential = GoogleAuthProvider.getCredential(googleIdToken, null);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        */

        FirebaseUser prevUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser currentUser = task.getResult().getUser();
                        // Merge prevUser and currentUser accounts and data
                        // ...
                    }
                });
    }




    private void register(){

        //region INPUT VALIDATIONS
        EditText editTextEmail = inputSignUpUserEmail.getEditText();
        EditText editTextPass = inputSignUpUserPassword.getEditText();

        if(editTextEmail == null) return;
        if(editTextPass == null) return;

        String email = editTextEmail.getText().toString();
        String responseEmail = new Utils().validateEmail(email, this);

        if(!responseEmail.equalsIgnoreCase("TRUE")){
            Toast.makeText(this, responseEmail, Toast.LENGTH_SHORT).show();
            return;
        };

        String pass = editTextPass.getText().toString();
        String responsePass = new Utils().validatePassword(pass, this);

        if(!responsePass.equalsIgnoreCase("TRUE")){
            Toast.makeText(this, responsePass, Toast.LENGTH_SHORT).show();
            return;
        };

        if(!checkBoxTerms.isChecked()){
            Toast.makeText(this, "No has Aceptado los terminos y condiciones", Toast.LENGTH_SHORT).show();
            return;
        }
        //endregion INPUT VALIDATIONS

        buttonInteractions(false);
        progressBarSignUpForm.setVisibility(View.VISIBLE);
        Utils.hideKeyboard(LoginActivity.this);

        mAuth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                final FirebaseUser user = authResult.getUser();
                final String userUid = user.getUid();

                DatabaseReference customerDataRef = FirebaseDatabase.getInstance().getReference().child("users").child("customer").child(userUid);

                UserModel currentUserModel = new UserModel();
                currentUserModel.setName("");
                currentUserModel.setSecondName("");
                currentUserModel.setLastName("");
                currentUserModel.setSecondLastName("");
                currentUserModel.setPhone("");
                currentUserModel.setType("customer");
                currentUserModel.setCurrentDevice(new Utils().getDeviceName(LoginActivity.this));
                currentUserModel.setProfileImageUrl("");
                currentUserModel.setLastConnection(ServerValue.TIMESTAMP);

                customerDataRef.setValue(currentUserModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isComplete() && task.getException() != null){
                            Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName("")
                                .setPhotoUri(null)
                                .build();

                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(!task.isComplete() && task.getException() != null){
                                    Toast.makeText(LoginActivity.this, "Updating Profile Fail: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }

                                UserManagement userManager = new UserManagement();
                                userManager.setCustomerData(currentUserModel);

                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(!task.isComplete() && task.getException() != null){
                                            Toast.makeText(LoginActivity.this, "Sending Verification Email Failed: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        progressBarSignUpForm.setVisibility(View.INVISIBLE);


                                        Credential myCredential = new Credential.Builder(user.getEmail())
                                                .setName(user.getDisplayName())
                                                .setProfilePictureUri(user.getPhotoUrl())
                                                .build();


                                        // MOSTRAR CUADRO DE DIALOGO DE SMART LOCK
                                        mCredentialClient.save(myCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(!task.isComplete() && task.getException() != null){
                                                    if (task.getException() instanceof ResolvableApiException) {
                                                        ResolvableApiException rae = (ResolvableApiException) task.getException();
                                                        try {
                                                            rae.startResolutionForResult(LoginActivity.this, 888);
                                                        } catch (IntentSender.SendIntentException ex) {
                                                            Toast.makeText(LoginActivity.this, "Save failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        // Request has no resolution
                                                        Toast.makeText(LoginActivity.this, "Save failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                //AUTENTICADO
                                                ///Intent intent = new Intent(LoginActivity.this, PrevWaterActivity.class);
                                                Intent intent = new Intent(LoginActivity.this, WaterDrinkActivity.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }

        }).addOnFailureListener(e ->{

            buttonInteractions(true);
            progressBarSignUpForm.setVisibility(View.INVISIBLE);

            if (e instanceof FirebaseAuthWeakPasswordException) {
                // if user enters wrong email.
                Log.d("REGISTER", "onComplete: weak_password");
                Toast.makeText(LoginActivity.this, "La contraseña es muy debil, escribe otra.", Toast.LENGTH_SHORT).show();
            }
            else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // if user enters wrong password.
                Log.d("REGISTER", "onComplete: malformed_email");
                Toast.makeText(LoginActivity.this, "Correo invalido, corrigelo.", Toast.LENGTH_SHORT).show();
            }
            else if (e instanceof FirebaseAuthUserCollisionException) {
                Log.d("REGISTER", "onComplete: exist_email");
                Toast.makeText(LoginActivity.this, "Este correo ya existe!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(LoginActivity.this, "Error de inicio de sesion: " + e.toString() , Toast.LENGTH_SHORT).show();
                Log.d("REGISTER", "onComplete: " + e.getMessage());
            }
        });
    }


}
