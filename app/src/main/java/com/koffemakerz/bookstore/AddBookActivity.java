package com.koffemakerz.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.UUID;

public class AddBookActivity extends AppCompatActivity {

    private EditText title,author,description,price,burrowed,genre,image;
    private Button addNewBtn;
    int page = 1 , limit =10;
    private FirebaseFirestore db;
    private Dialog loader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        title = findViewById(R.id.titlef);
        author = findViewById(R.id.authorf);
        description = findViewById(R.id.descriptionf);
        price = findViewById(R.id.pricef);
        burrowed = findViewById(R.id.burrowf);
        genre = findViewById(R.id.genref);
        image = findViewById(R.id.imagef);


        loader = new Dialog(AddBookActivity.this);
        loader.setContentView(R.layout.progress_bar);
        loader.setCancelable(false);





        addNewBtn = findViewById(R.id.buttonAddRecord2);


        db = FirebaseFirestore.getInstance();



        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titles= title.getText().toString();
                String authors = author.getText().toString();
                String descriptions = description.getText().toString();
                String burroweds = burrowed.getText().toString();
                String images = image.getText().toString();
                String prices = price.getText().toString();
                String genres = genre.getText().toString();



                    String id = UUID.randomUUID().toString();
                    saveToFireStore(id,titles,authors,descriptions,burroweds,images,prices,genres);


            }
        });


    }



    private void saveToFireStore(String id, final String title, final String author, String desc, String burrowed, String img, String price, String genre){

        loader.show();

        if(!id.isEmpty() && !title.isEmpty() && !author.isEmpty() && !desc.isEmpty() && !burrowed.isEmpty() && !img.isEmpty() && !price.isEmpty() && !genre.isEmpty() ){
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("title", title);
            map.put("author", author);
            map.put("description", desc);
            map.put("burrowed", burrowed);
            map.put("image", img);
            map.put("price",price);
            map.put("genre", genre);


            db.collection("Documents").document(id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AddBookActivity.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();

                                loader.dismiss();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddBookActivity.this, "Data Not Added", Toast.LENGTH_SHORT).show();
                    loader.dismiss();
                }
            });

        }else {
            loader.dismiss();
            Toast.makeText(this, "The Main Fields Cannot be Null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddBookActivity.this, MainMenuActivity.class));
    }

}