package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;


public class newPost extends MainActivity implements AdapterView.OnItemSelectedListener {
    Button  btnupload;
    ImageButton btnbrowse;
    EditText etitle,etoverview,etdes,etbenefit,etprecausion,ethowtodo,etproblem,etsolution;
    ImageView imgview;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference,mypostdatabaseReference,notifyreference,followerefernce;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_new_post);

        LinearLayout dynamicContent;
        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        View wizard = getLayoutInflater().inflate(R.layout.activity_new_post, null);
        dynamicContent.addView(wizard);

        // Spinner Drop down elements
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        // Spinner click listener

        InputValidatorHelper inputValidatorHelper = new InputValidatorHelper();
        StringBuilder errMsg = new StringBuilder("Unable to save. Please fix the following errors and try again.\n");
        // Spinner Drop down elements


        List<String> categories = new ArrayList<String>();
        categories.add("General");
        categories.add("Yoga and Health");
        categories.add("Disease Solution");



        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);



        storageReference = FirebaseStorage.getInstance().getReference("hPost");
        databaseReference = FirebaseDatabase.getInstance().getReference("hPost");
        mypostdatabaseReference = FirebaseDatabase.getInstance().getReference("mypost");
        notifyreference = FirebaseDatabase.getInstance().getReference("notification");
        followerefernce = FirebaseDatabase.getInstance().getReference("follower");

        btnbrowse = (ImageButton) findViewById(R.id.btnbrowse);
        btnupload= (Button)findViewById(R.id.btnPost);
        etitle=(EditText)findViewById(R.id.heading);
        etoverview=(EditText)findViewById(R.id.overview);
        etdes=(EditText)findViewById(R.id.description);
        etbenefit=(EditText)findViewById(R.id.benefits);
        ethowtodo=(EditText)findViewById(R.id.howtodo);
        etproblem=(EditText)findViewById(R.id.problem);
        etsolution=(EditText)findViewById(R.id.solution);
        etprecausion=(EditText)findViewById(R.id.precautions);
        // txtname = (EditText)findViewById(R.id.txtname);
        // txtcouse=(EditText)findViewById(R.id.txtcourse);
        //txtemail=(EditText)findViewById(R.id.txtemail);

        imgview = (ImageView)findViewById(R.id.imgview);
        progressDialog = new ProgressDialog(newPost.this);// context name as per your project name





        btnbrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

            }
        });
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title1=etitle.getText().toString();
                String overview1= etoverview.getText().toString().trim();
                String desc1=etdes.getText().toString().trim();
                String benefit1=etbenefit.getText().toString().trim();
                String howtodo1=ethowtodo.getText().toString().trim();
                String problem1=etproblem.getText().toString().trim();
                String solution1=etsolution.getText().toString().trim();
                String precaution1=etprecausion.getText().toString().trim();
                String description1=desc1+"\n"+benefit1+"\n"+howtodo1+"\n"+problem1+"\n"+solution1+"\n"+precaution1;

                if (inputValidatorHelper.isNullOrEmpty(title1)) {
                    errMsg.append(" Title should not be empty.\n");
                    Toast.makeText(getApplicationContext(),"Title should not be empty",Toast.LENGTH_SHORT).show();
                    //allowSave = false;
                }
                else if(inputValidatorHelper.isNullOrEmpty(overview1)) {
                    errMsg.append("Overview should not be empty.\n");
                    Toast.makeText(getApplicationContext(),"Overview should not be empty",Toast.LENGTH_SHORT).show();

                    // allowSave = false;
                }
                else if(inputValidatorHelper.isNullOrEmpty(description1)) {
                    errMsg.append("- Description should not be empty.\n");
                    Toast.makeText(getApplicationContext(),"Description should not be empty.",Toast.LENGTH_SHORT).show();

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

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgview.setImageBitmap(bitmap);
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

            progressDialog.setTitle("Image is Uploading...");
            progressDialog.show();
            StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri Uri) {
                                    String desc=null,benefit=null,howtodo=null,problem=null,solution=null,precaution=null;
                                    String uri=Uri.toString();
                                    String title=etitle.getText().toString().trim();
                                    String overview= etoverview.getText().toString().trim();
                                     desc=etdes.getText().toString().trim();
                                        benefit=etbenefit.getText().toString().trim();
                                        howtodo=ethowtodo.getText().toString().trim();
                                        problem=etproblem.getText().toString().trim();
                                       solution=etsolution.getText().toString().trim();
                                       precaution=etprecausion.getText().toString().trim();
                                       String description=desc+"\n"+benefit+"\n"+howtodo+"\n"+problem+"\n"+solution+"\n"+precaution;

                                    // String email=txtemail.getText().toString().trim();
                                    FirebaseUser myuser= FirebaseAuth.getInstance().getCurrentUser();
                                    String myuserida=myuser.getUid();
                                    String ImageUploadId = databaseReference.push().getKey();
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                                    @SuppressWarnings("VisibleForTests")
                                    modelGeneral imageUploadInfo = new modelGeneral(title,overview,uri,description,ImageUploadId,myuserida);

                                    //String mobno=databaseReference.Auythecation(mobno);

                                    databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                                    mypostdatabaseReference.child(myuserida).child(ImageUploadId).setValue(imageUploadInfo);

                                    followerefernce.child(myuserida).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            //  notifyreference.child("old").child(dataSnapshot.getKey()).child(ImageUploadId).setValue(true);
                                                notifyreference.child("new").child(dataSnapshot.getKey()).child(myuserida).child(ImageUploadId).setValue(true);

                                            }

                                           // adapter2.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });

                        }
                    });
        }
        else {

            Toast.makeText(newPost.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }

    int pos=0;

   // @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        pos=position;
        String item = parent.getItemAtPosition(position).toString();
        LinearLayout l1=findViewById(R.id.ldescription);
        LinearLayout l2=findViewById(R.id.lbenefits);
        LinearLayout l3=findViewById(R.id.lhow);
        LinearLayout l4=findViewById(R.id.lproblem);
        LinearLayout l5=findViewById(R.id.lsolution);
        LinearLayout l6=findViewById(R.id.lprecautions);
        if(position==0)
        {
            l1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.GONE);
            l3.setVisibility(View.GONE);
            l4.setVisibility(View.GONE);
            l5.setVisibility(View.GONE);
            l6.setVisibility(View.GONE);
        }
        else if(position==1)
        {
            l1.setVisibility(View.GONE);
            l2.setVisibility(View.VISIBLE);
            l3.setVisibility(View.VISIBLE);
            l6.setVisibility(View.VISIBLE);
            l4.setVisibility(View.GONE);
            l5.setVisibility(View.GONE);
        }
        else
        {
            l1.setVisibility(View.GONE);
            l2.setVisibility(View.GONE);
            l3.setVisibility(View.GONE);
            l4.setVisibility(View.VISIBLE);
            l5.setVisibility(View.VISIBLE);
            l6.setVisibility(View.VISIBLE);
        }
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}