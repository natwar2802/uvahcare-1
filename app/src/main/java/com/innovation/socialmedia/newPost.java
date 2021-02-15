package com.innovation.socialmedia;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.icu.text.CaseMap;
import android.os.Build;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//go to 112
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmedia.R;
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
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;


public class newPost extends MainActivity implements AdapterView.OnItemSelectedListener {
    Button  btnupload;
    ImageButton btnbrowse;
    Button suggestreminder;
    EditText etitle,etoverview,etdes,etbenefit,etprecausion,ethowtodo,etproblem,etsolution;
    ImageView imgview;
    Uri FilePathUri;
   // Uri FilePathUri1;
    StorageReference storageReference;
    DatabaseReference databaseReference,mypostdatabaseReference,notifyreference,followerefernce;
    int Image_Request_Code = 7;
    //int Image_Request_Code1 = 8;
    ProgressDialog progressDialog ;
    ArrayList<String> temp=new ArrayList<String>();
    Button select_text;
    ImageButton imgviewReminderBrowse;
    ImageView imgviewReminder;
    LinearLayout l,reminderLayout;
    EditText ReminderTitle,ReminderNotes;
    String preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_new_post);



        LinearLayout dynamicContent;
        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        View wizard = getLayoutInflater().inflate(R.layout.activity_new_post, null);
        dynamicContent.addView(wizard);
        l=(LinearLayout) findViewById(R.id.newdefense);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
   preference="";
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        appname.setVisibility(View.VISIBLE);
        search_.setVisibility(View.GONE);


        // Spinner Drop down elements
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        reminderLayout = findViewById(R.id.reminderLayout);
        suggestreminder= findViewById(R.id.suggestReminder);
        ReminderTitle = findViewById(R.id.titleReminder);
        ReminderNotes = findViewById(R.id.noteReminder);
        suggestreminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reminderLayout.getVisibility()==View.VISIBLE)
                    reminderLayout.setVisibility(View.GONE);
                else
                    reminderLayout.setVisibility(View.VISIBLE);
            }
        });
        /*
        imgviewReminderBrowse= findViewById(R.id.imgviewReminderBrowse);
        imgviewReminderBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code1);
            }
        });
        imgviewReminder = findViewById(R.id.imgviewReminder);*/
        //implement Reminder image select here and send to firebase the ReminderElement using Constructor

        select_text=findViewById(R.id.select_text);
        select_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(l.getVisibility()==View.GONE)
                    l.setVisibility(View.VISIBLE);
                else
                    l.setVisibility(View.GONE);
            }
        });
        // Spinner click listener
try{        for(int i=0;i<l.getChildCount();i++)
        {
            LinearLayout l1=(LinearLayout)(l.getChildAt(i));
            for(int j=0;j<l1.getChildCount();j++)
            {
                CardView c=(CardView)(l1.getChildAt(j));
                TextView t = ((TextView) c.getChildAt(0));
                t.setTextSize(15);
            }
        }}catch (Exception e){}

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
        progressDialog = new ProgressDialog(newPost.this);

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
                int position=spinner.getSelectedItemPosition();


                        // your code here

                String description1=desc1+"\n"+benefit1+"\n"+howtodo1+"\n"+problem1+"\n"+solution1+"\n"+precaution1;
                  if(preference.equals("")){
                     Toast.makeText(getApplicationContext(),"Please Select Tag",Toast.LENGTH_SHORT).show();

                 }
                 else if(inputValidatorHelper.isNullOrEmpty(title1)) {
                    errMsg.append(" Title should not be empty.\n");
                    Toast.makeText(getApplicationContext(),"Title should not be empty",Toast.LENGTH_SHORT).show();
                    //allowSave = false;
                }
                else if(inputValidatorHelper.isNullOrEmpty(overview1)) {
                    errMsg.append("Overview should not be empty.\n");
                    Toast.makeText(getApplicationContext(),"Overview should not be empty",Toast.LENGTH_SHORT).show();

                    // allowSave = false;
                }
               /** else if(reminderLayout.getVisibility()==View.VISIBLE&&inputValidatorHelper.isNullOrEmpty(Remindertitle));
                  {
                      Toast.makeText(getApplicationContext(),"Reminder Title Should not be Empty",Toast.LENGTH_SHORT).show();
                  }**/
                else if(inputValidatorHelper.isNullOrEmpty(description1)) {
                    errMsg.append("- Description should not be empty.\n");
                    Toast.makeText(getApplicationContext(),"Description should not be empty.",Toast.LENGTH_SHORT).show();

                    //allowSave = false;
                }
                else if(position==0){
                    if(inputValidatorHelper.isNullOrEmpty(desc1)) {
                        errMsg.append(" Discription should not be empty.\n");
                        Toast.makeText(getApplicationContext(),"Dicription should not be empty",Toast.LENGTH_SHORT).show();
                        //allowSave = false;
                    }
                    else{
                        UploadImage();
                    }
                }
                else if(position==1){
                    if(inputValidatorHelper.isNullOrEmpty(benefit1)) {
                        errMsg.append(" Benefit should not be empty.\n");
                        Toast.makeText(getApplicationContext(),"Benefit should not be empty",Toast.LENGTH_SHORT).show();
                        //allowSave = false;
                    }
                   else if(inputValidatorHelper.isNullOrEmpty(howtodo1)) {
                        errMsg.append("How to do should not be empty.\n");
                        Toast.makeText(getApplicationContext(),"How to do should not be empty",Toast.LENGTH_SHORT).show();
                        //allowSave = false;
                    }
                   else if(inputValidatorHelper.isNullOrEmpty(precaution1)) {
                        errMsg.append("Precation should not be empty.\n");
                        Toast.makeText(getApplicationContext(),"Precaution should not be empty",Toast.LENGTH_SHORT).show();
                        //allowSave = false;
                    }
                    else{
                        UploadImage();
                    }
                }
                else if(position==2){
                    if(inputValidatorHelper.isNullOrEmpty(problem1)) {
                        errMsg.append("Problem or Disease description should not be empty.\n");
                        Toast.makeText(getApplicationContext(),"Problem or Disease description should not be empty",Toast.LENGTH_SHORT).show();
                        //allowSave = false;
                    }
                   else if(inputValidatorHelper.isNullOrEmpty(solution1)) {
                        errMsg.append("Solution Description should not be empty.\n");
                        Toast.makeText(getApplicationContext(),"Solution Description  should not be empty",Toast.LENGTH_SHORT).show();
                        //allowSave = false;
                    }

                   else if(inputValidatorHelper.isNullOrEmpty(precaution1)) {
                        errMsg.append("Precation should not be empty.\n");
                        Toast.makeText(getApplicationContext(),"Precaution should not be empty",Toast.LENGTH_SHORT).show();
                        //allowSave = false;
                    }
                    else{
                        UploadImage();
                    }
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
               // Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                //byte[] data = baos.toByteArray();
                imgview.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
        /*else if(requestCode == Image_Request_Code1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            FilePathUri1 = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri1);
                // Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                //byte[] data = baos.toByteArray();
                imgviewReminder.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }*/
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


    public void UploadImage()  {

        if (FilePathUri != null) {

            progressDialog.setTitle("Post is Uploading...");
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
                                       String Remindertitle= ReminderTitle.getText().toString().trim();
                                       String Remindernotes= ReminderNotes.getText().toString().trim();
                                    Spinner spinner = (Spinner) findViewById(R.id.spinner);
                                    int position=spinner.getSelectedItemPosition();
                                       String description;
                                       if(position==0)
                                       {
                                           description=desc;
                                       }
                                       else if(position==1){
                                           description=benefit+"\n"+howtodo+"\n"+precaution;
                                       }
                                       else{
                                           description=problem+"\n"+solution+"\n"+precaution;
                                       }

                                    long data=System.currentTimeMillis();
                                    long ratesum=0;
                                    float rating=0;
                                    long claps=0;
                                    double postscore=0;
                                    long seencount=0;
                                    long postno=1;







                                    // String email=txtemail.getText().toString().trim();
                                    FirebaseUser myuser= FirebaseAuth.getInstance().getCurrentUser();
                                    String myuserida=myuser.getUid();
                                    String ImageUploadId = databaseReference.push().getKey();

                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Your Post Uploaded Successfully ", LENGTH_SHORT).show();
                                    @SuppressWarnings("VisibleForTests")

                                        modelGeneral imageUploadInfo = new modelGeneral(title,overview,uri,description,ImageUploadId,myuserida,preference,data,ratesum,rating,claps,postscore,seencount,postno);

                                    //String mobno=databaseReference.Auythecation(mobno);

                                    databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                                    databaseReference.child(ImageUploadId).child("preference").setValue(preference);
                                   // databaseReference.child(ImageUploadId).child("datetime").setValue(data);
                                    mypostdatabaseReference.child(myuserida).child(ImageUploadId).setValue(true);
                                    DatabaseReference reminderref=FirebaseDatabase.getInstance().getReference("Reminder");
                                    if(!Remindertitle.equals("")){
                                        reminderref.child(ImageUploadId).child("Title").setValue(Remindertitle);
                                        if(!Remindernotes.equals("")){
                                            reminderref.child(ImageUploadId).child("Notes").setValue(Remindernotes);
                                        }

                                    }

                                    final int[] cheker = {0};
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(cheker[0] ==0) {
                                                long postno = snapshot.getChildrenCount();
                                                databaseReference.child(ImageUploadId).child("postno").setValue(postno);
                                                cheker[0] =1;
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    final int[] noyifychecker = {0};
                                    followerefernce.child(myuserida).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                         //   if(noyifychecker[0] ==0){
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            //  notifyreference.child("old").child(dataSnapshot.getKey()).child(ImageUploadId).setValue(true);
                                                notifyreference.child("new").child(dataSnapshot.getKey()).child(myuserida).child(ImageUploadId).setValue(true);

                                            }
                                           // noyifychecker[0] =1;
                                           // }

                                           // adapter2.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    Intent i=new Intent(newPost.this,MainActivity.class);
                                    startActivity(i);
                                }
                            });

                        }
                    });
        }
        else {

            Toast.makeText(newPost.this, "Please Select Image or Add Image Name", LENGTH_SHORT).show();

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
    public void f(View view) {

        l=(LinearLayout) findViewById(R.id.newdefense);


        CardView c = (CardView) findViewById(view.getId());
        TextView t = ((TextView) c.getChildAt(0));
        String k = t.getText().toString();
        preference=k;

        for(int i=0;i<l.getChildCount();i++)
        {
            LinearLayout l1=(LinearLayout)(l.getChildAt(i));
            for(int j=0;j<l1.getChildCount();j++)
            {
                CardView c1=(CardView)(l1.getChildAt(j));
                c1.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }
        c.setCardBackgroundColor(Color.parseColor("#B388FF"));
    }
}