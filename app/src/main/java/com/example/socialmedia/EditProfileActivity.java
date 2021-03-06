package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.view.View.GONE;

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
    int ch=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        /*LinearLayout dynamicContent;
        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        View wizard = getLayoutInflater().inflate(R.layout.activity_edit_profile, null);
        dynamicContent.addView(wizard);*/
        storageReferenceProfile = FirebaseStorage.getInstance().getReference("profile");
        databaseReferenceProfile = FirebaseDatabase.getInstance().getReference("profile");
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String id=user.getUid();
        btnbrowseProfile = (ImageButton) findViewById(R.id.btnbrowseProfile);
        btnuploadProfile= (Button)findViewById(R.id.updateProfile);
        etNameProfile=(EditText)findViewById(R.id.usernameProfile);
        etcityProfile=(EditText)findViewById(R.id.cityProfile);
        etcountryProfile=(EditText)findViewById(R.id.countryProfile);
        btnNextPreference=findViewById(R.id.nexttoPreference);
        imgviewProfile = (ImageView)findViewById(R.id.imgviewProfile);
        aboutmeProfile=findViewById(R.id.aboutmeProfile);
        // txtname = (EditText)findViewById(R.id.txtname);
        // txtcouse=(EditText)findViewById(R.id.txtcourse);
        //txtemail=(EditText)findViewById(R.id.txtemail);
        InputValidatorHelper inputValidatorHelper = new InputValidatorHelper();
        StringBuilder errMsg = new StringBuilder("Unable to save. Please fix the following errors and try again.\n");

        databaseReferenceProfile.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                if(ch==0) {
                    if (snapshot.hasChild(id)) {
                        btnNextPreference.setVisibility(GONE);
                        modelProfile p=snapshot.child(id).getValue(modelProfile.class);
                        etNameProfile.setText(p.usernameP);
                        etcityProfile.setText(p.cityP);
                        etcountryProfile.setText(p.countryP);
                        aboutmeProfile.setText(p.getUserdetail());
                        Glide.with(getApplicationContext()).load(p.imgUrlP).into(imgviewProfile);
<<<<<<< HEAD:app/src/main/java/com/innovation/socialmedia/EditProfileActivity.java
                        FirebaseDatabase.getInstance().getReference("NotifCount").child(user.getUid()).setValue(0);
                        Toast.makeText(getApplicationContext(),"If You have updated the profile you can move to next page....\n",Toast.LENGTH_LONG).show();
=======
>>>>>>> parent of bcfbd8f (changes):app/src/main/java/com/example/socialmedia/EditProfileActivity.java

                    } else {
                        btnuploadProfile.setVisibility(GONE);
                    }
                    ch = 1;
                }
            }catch (Exception e){}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnNextPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameProfile1=etNameProfile.getText().toString().trim();
                String cityProfile1= etcityProfile.getText().toString().trim();
                String countryProfile1=etcountryProfile.getText().toString().trim();
                String aboutmeProfile1=aboutmeProfile.getText().toString().trim();




                if (inputValidatorHelper.isNullOrEmpty(userNameProfile1)) {

                    Toast.makeText(getApplicationContext(),"UserName should not be empty.\n",Toast.LENGTH_SHORT).show();

                    //allowSave = false;
                }
                else if(inputValidatorHelper.isNullOrEmpty(cityProfile1)) {
                    Toast.makeText(getApplicationContext(),"City name should not be empty.\n",Toast.LENGTH_SHORT).show();

                    // allowSave = false;
                }
                else if(inputValidatorHelper.isNullOrEmpty(countryProfile1)) {
                    Toast.makeText(getApplicationContext(),"Country name should not be empty.\n",Toast.LENGTH_SHORT).show();

                    //allowSave = false;
                }
                else if(inputValidatorHelper.isNullOrEmpty(aboutmeProfile1)) {
                    Toast.makeText(getApplicationContext(),"About me should not be empty.\n",Toast.LENGTH_SHORT).show();

                    //allowSave = false;
                }
                else {
                    UploadImage();


                }

            }
        });


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

                    Toast.makeText(getApplicationContext(),"UserName should not be empty.\n",Toast.LENGTH_SHORT).show();

                    //allowSave = false;
                }
                else if(inputValidatorHelper.isNullOrEmpty(cityProfile1)) {
                    Toast.makeText(getApplicationContext(),"City name should not be empty.\n",Toast.LENGTH_SHORT).show();

                    // allowSave = false;
                }
                else if(inputValidatorHelper.isNullOrEmpty(countryProfile1)) {
                    Toast.makeText(getApplicationContext(),"Country name should not be empty.\n",Toast.LENGTH_SHORT).show();

                    //allowSave = false;
                }
                else if(inputValidatorHelper.isNullOrEmpty(aboutmeProfile1)) {
                    Toast.makeText(getApplicationContext(),"About me should not be empty.\n",Toast.LENGTH_SHORT).show();

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
                                    Toast.makeText(getApplicationContext(), "Your Profile Updated Successfully ", Toast.LENGTH_LONG).show();
                                    @SuppressWarnings("VisibleForTests")
                                    String prevseenpost="Prevseen";

                                    Intent i = new Intent(EditProfileActivity.this, PreferenceActivity.class);
                                    startActivity(i);
                                    modelProfile modelp=new modelProfile(userNameProfile,cityProfile,countryProfile,uriP,myuseridaP,aboutme,userPreference,prevseenpost);
                                   String ImageUploadId = databaseReferenceProfile.push().getKey();
                                 //   String ImageUploadId = databaseReferenceProfile.push();
                                    //String mobno=databaseReference.Auythecation(mobno);
                                    databaseReferenceProfile.child(myuseridaP).setValue(modelp);
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
