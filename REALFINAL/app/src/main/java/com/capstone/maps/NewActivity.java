package com.capstone.maps;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewActivity extends MapActivity {
    Object view;
    ImageView imageView;
    Button button, edit, submit, btUpload;
    EditText editText;
    TextView textView;
    Double latitude = 0.0, latitudeP;
    Double longitude = 0.0, longitudeP;
    String content;
    String key;
    private Uri filePath;
    private DatabaseReference mReference;
    private FirebaseDatabase mDatabase;
    private ChildEventListener mChild;
    private static final String TAG = "googlemap_example";
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Intent iintent = getIntent();
        latitudeP = iintent.getDoubleExtra("latitude", 0.0);
        longitudeP = iintent.getDoubleExtra("longitude", 0.0);
        imageView = (ImageView) findViewById(R.id.image);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        edit = (Button) findViewById(R.id.edit);
        submit = (Button) findViewById(R.id.submit);
        btUpload = (Button) findViewById(R.id.bt_upload);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "???????????? ???????????????."), 0);
                } catch (IOError e) {
                }
            }
        });
        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //?????????
                uploadFile();

            }
        });


        mReference = FirebaseDatabase.getInstance().getReference();
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        MarkerOptions mOptions = new MarkerOptions();
                        // ?????? ?????????
                        mOptions.getTitle();
                        Integer i = 0;
                        for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                            String cc = dataSnapshot3.getKey();
                            if (i == 0) {  // longitude
                                content = dataSnapshot3.getValue().toString();
                                i++;
                            } else if (i == 1) {
                                latitude = Double.parseDouble(dataSnapshot3.getValue().toString());
                                i++;

                            } else {
                                longitude = Double.parseDouble(dataSnapshot3.getValue().toString());
                                if (latitudeP.toString().equals(longitude.toString()) && longitudeP.toString().equals(latitude.toString())) {
                                    if (content.equals("")) {
                                        key = dataSnapshot2.getKey();

                                        textView.setText(content);
                                        textView.setVisibility(View.VISIBLE);
                                        break;
                                    } else {
                                        key = dataSnapshot2.getKey();
                                        edit.setVisibility(View.INVISIBLE);
                                        editText.setVisibility(View.INVISIBLE);
                                        textView.setText(content);
                                        textView.setVisibility(View.VISIBLE);
                                        break;
                                    }
                                }
                                i = 0;

                            }


                        }

                    }

//

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textView.setText(editText.getText());
                editText.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.VISIBLE);
                edit.setVisibility(View.INVISIBLE);
                String buff = editText.getText().toString();
                LocationHelper helper = new LocationHelper(latitudeP, longitudeP, buff);

                FirebaseDatabase.getInstance().getReference("MarkerLocation").child(key)
                        .setValue(helper).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(NewActivity.this, "Location Saved", Toast.LENGTH_SHORT);
                        } else {
                            Toast.makeText(NewActivity.this, "Location Not Saved", Toast.LENGTH_SHORT);
                        }
                    }
                });


            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setVisibility(View.VISIBLE);
                textView.setVisibility(View.INVISIBLE);
                editText.setText(textView.getText());
                edit.setVisibility(View.VISIBLE);
            }
        });
        mReference = FirebaseDatabase.getInstance().getReference();
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        MarkerOptions mOptions = new MarkerOptions();
                        // ?????? ?????????
                        mOptions.getTitle();
                        Integer i = 0;
                        for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                            String cc = dataSnapshot3.getKey();
                            if (i == 0) {  // longitude
                                content = dataSnapshot3.getValue().toString();
                                i++;
                            } else if (i == 1) {
                                latitude = Double.parseDouble(dataSnapshot3.getValue().toString());
                                i++;

                            } else {
                                longitude = Double.parseDouble(dataSnapshot3.getValue().toString());

                                if (latitudeP.toString().equals(longitude.toString()) && longitudeP.toString().equals(latitude.toString())) {

                                    key = dataSnapshot2.getKey();
                                    break;
                                }
                                i = 0;

                            }


                        }

                    }

//

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    private void uploadFile() {
        //???????????? ????????? ????????? ??????
        try {
            if (filePath != null) {
                //????????? ?????? Dialog ?????????
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("????????????...");
                progressDialog.show();

                //storage
                FirebaseStorage storage = FirebaseStorage.getInstance();

                //Unique??? ???????????? ?????????.
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
                Date now = new Date();
                String filename = formatter.format(now) + ".png";
                //storage ????????? ?????? ???????????? ????????? ??????.
                StorageReference storageRef = storage.getReferenceFromUrl("gs://test-1f40a.appspot.com").child("images/" + filename);
                //???????????????...
                storageRef.putFile(filePath)
                        //?????????
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss(); //????????? ?????? Dialog ?????? ??????
                                Toast.makeText(getApplicationContext(), "????????? ??????!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        //?????????
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "????????? ??????!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        //?????????
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                @SuppressWarnings("VisibleForTests") //?????? ?????? ?????? ???????????? ????????? ????????????. ??? ??????????
                                        double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                //dialog??? ???????????? ???????????? ????????? ??????
                                progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOError E) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request????????? 0?????? OK??? ???????????? data??? ????????? ?????? ?????????
        if (requestCode == 0 && resultCode == RESULT_OK) {
            filePath = data.getData();
            Log.d(TAG, "uri:" + String.valueOf(filePath));
            try {
                //Uri ????????? Bitmap?????? ???????????? ImageView??? ?????? ?????????.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
