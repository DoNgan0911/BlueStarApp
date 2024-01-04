package com.example.bluestarapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class EditProfile extends AppCompatActivity {

    EditText name, cccd;
    Button button;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    DocumentReference documentReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        name = findViewById(R.id.nameEditText);
        cccd = findViewById(R.id.cccdEditText);
        button = findViewById(R.id.button);

        retrieveCCCDFromFirestore(AppUtil.edtSignInEmail);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });


    }

    private void updateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            Log.d("EditProfile", "User ID: " + userId);  // Log the userId
            String newName = name.getText().toString();
            String newCCCD = cccd.getText().toString();

            // Update the user's information in the Firestore "CUSTOMER" collection
            db.collection("CUSTOMER")
                    .whereEqualTo("account_id", userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                    // Retrieve the document ID
                                    String documentId = document.getId();
                                    Log.d("EditProfile", "Document ID: " + documentId);

                                    // Update the "fullname" field
                                    db.collection("CUSTOMER").document(documentId)
                                            .update("fullname", newName , "num_id", newCCCD)

                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("EditProfile", "Full name updated successfully in Firestore");
                                                    // Show success notification
                                                    Toast.makeText(EditProfile.this, "Full name updated successfully", Toast.LENGTH_SHORT).show();

                                                    // Now, update the num_id in Realtime Database

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e("EditProfile", "Error updating full name in Firestore", e);
                                                    Toast.makeText(EditProfile.this, "Failed to update full name", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                Log.e("EditProfile", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    private void retrieveCCCDFromFirestore(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get reference to the "customer" collection
        db.collection("CUSTOMER")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                // Retrieve the CCCD from the document
                                String num_id = document.getString("num_id");

                                // Display the CCCD in the EditText
                                cccd.setText(num_id);

                                // You may also want to update other UI elements based on the retrieved data
                                // For example, update the fullname and points if they are stored in the document
                                String fullName = document.getString("fullname");

                                name.setText(fullName);
                            }
                        } else {
                            // Handle errors
                        }
                    }
                });
    }
}

