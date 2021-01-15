package com.example.socialmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class EditProfileActivity extends  AppCompatActivity{

    Button  btnuploadProfile,btnNextPreference;
    EditText etNameProfile,etcityProfile,etcountryProfile,aboutmeProfile;
    ImageView imgviewProfile;
    ImageButton btnbrowseProfile;
    Uri FilePathUri;
    StorageReference storageReferenceProfile;
    DatabaseReference databaseReferenceProfile;
    int Image_Request_Code_Profile = 7;
    ProgressDialog progressDialogProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        /*LinearLayout dynamicContent;
        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        View wizard = getLayoutInflater().inflate(R.layout.activity_edit_profile, null);
        dynamicContent.addView(wizard);*/
        storageReferenceProfile = FirebaseStorage.getInstance().getReference("profile");
        databaseReferenceProfile = FirebaseDatabase.getInstance().getReference("profile");
        btnbrowseProfile = (ImageButton) findViewById(R.id.btnbrowseProfile);
        btnuploadProfile= (Button)findViewById(R.id.updateProfile);
        etNameProfile=(EditText)findViewById(R.id.usernameProfile);
        etcityProfile=(EditText)findViewById(R.id.cityProfile);
        etcountryProfile=(EditText)findViewById(R.id.countryProfile);
        btnNextPreference=findViewById(R.id.nexttoPreference);
        aboutmeProfile=findViewById(R.id.aboutmeProfile);
        // txtname = (EditText)findViewById(R.id.txtname);
        // txtcouse=(EditText)findViewById(R.id.txtcourse);
        //txtemail=(EditText)findViewById(R.id.txtemail);
        InputValidatorHelper inputValidatorHelper = new InputValidatorHelper();
        StringBuilder errMsg = new StringBuilder("Unable to save. Please fix the following errors and try again.\n");



        btnNextPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfileActivity.this, PreferenceActivity.class);
                startActivity(i);
            }
        });

        imgviewProfile = (ImageView)findViewById(R.id.imgviewProfile);
        progressDialogProfile = new ProgressDialog(EditProfileActivity.this);// context name as per your project name


        btnbrowseProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code_Profile);

            }
        });
        btnuploadProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userNameProfile1=etNameProfile.getText().toString().trim();
                String cityProfile1= etcityProfile.getText().toString().trim();
                String countryProfile1=etcountryProfile.getText().toString().trim();
                String aboutmeProfile1=aboutmeProfile.getText().toString().trim();




                if (inputValidatorHelper.isNullOrEmpty(userNameProfile1)) {
                    errMsg.append(" UserName should not be empty.\n");
                    //allowSave = false;
                }
                 else if(inputValidatorHelper.isNullOrEmpty(cityProfile1)) {
                    errMsg.append("City name should not be empty.\n");
                   // allowSave = false;
                }
                else if(inputValidatorHelper.isNullOrEmpty(countryProfile1)) {
                    errMsg.append("- Country name should not be empty.\n");
                    //allowSave = false;
                }
                else if(inputValidatorHelper.isNullOrEmpty(aboutmeProfile1)) {
                    errMsg.append("- About me should not be empty.\n");
                    //allowSave = false;
                }
                else {
                    UploadImage();
                }

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code_Profile && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                imgviewProfile.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


    public void UploadImage() {

        if (FilePathUri != null) {

            progressDialogProfile.setTitle("Image is Uploading...");
            progressDialogProfile.show();
            StorageReference storageReference2Profile = storageReferenceProfile.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2Profile.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference2Profile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri Uri) {
                                 //   String postkey = taskSnapshot.getKey();
                                    String uriP=Uri.toString();
                                    String userNameProfile=etNameProfile.getText().toString().trim();
                                    String cityProfile= etcityProfile.getText().toString().trim();
                                    String countryProfile=etcountryProfile.getText().toString().trim();
                                    String aboutme=aboutmeProfile.getText().toString().trim();
                                    ArrayList<String> userPreference = new ArrayList<String>();
                                    userPreference.add("userPreference");

                                    FirebaseUser myuserP= FirebaseAuth.getInstance().getCurrentUser();
                                    String myuseridaP=myuserP.getUid();


                                    // String email=txtemail.getText().toString().trim();
                                    progressDialogProfile.dismiss();
                                    Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                                    @SuppressWarnings("VisibleForTests")
                                    modelProfile imageUploadInfo = new modelProfile(userNameProfile,cityProfile,countryProfile,uriP,myuseridaP,aboutme,userPreference);
                                   String ImageUploadId = databaseReferenceProfile.push().getKey();
                                 //   String ImageUploadId = databaseReferenceProfile.push();
                                    //String mobno=databaseReference.Auythecation(mobno);
                                    databaseReferenceProfile.child(myuseridaP).setValue(imageUploadInfo);
                                }
                            });

                        }
                    });
        }
        else {

            Toast.makeText(EditProfileActivity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }
    }
