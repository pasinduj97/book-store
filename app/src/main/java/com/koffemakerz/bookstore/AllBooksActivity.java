package com.koffemakerz.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllBooksActivity extends AppCompatActivity {
    private RecyclerView mrecycleView;

    private FirebaseFirestore db;

    private MyAdapter adapter;
    private List<Model> list;
    EditText search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_books);

        search = findViewById(R.id.search);
        mrecycleView = findViewById(R.id.recyclerView);
        mrecycleView.setHasFixedSize(true);
        mrecycleView.setLayoutManager(new LinearLayoutManager(this));




        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                        filter(editable.toString());
            }
        });

        db = FirebaseFirestore.getInstance();

        list = new ArrayList<>();
        adapter = new MyAdapter(this, list,AllBooksActivity.this);

        mrecycleView.setAdapter(adapter);



        showData();
    }

    public  void filter(String text){
        ArrayList<Model> filteredlist = new ArrayList<>();
        for(Model model : list){
            if(model.getTitle().toLowerCase().contains(text.toLowerCase()) || model.getGenre().toLowerCase().contains(text.toLowerCase()) || model.getDescription().toLowerCase().contains(text.toLowerCase()) ){
                filteredlist.add(model);
            }
        }

        adapter.filterList(filteredlist);
    }

    public void showData(){
        db.collection("Documents").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        for(DocumentSnapshot snapshot : task.getResult()){
                            Model model = new Model(snapshot.getString("id"), snapshot.getString("title"),
                                    snapshot.getString("author"), snapshot.getString("description"),snapshot.getString("price"),snapshot.getString("genre"),snapshot.getString("image"),snapshot.getString("burrowed"));

                            list.add(model);

                        }

                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AllBooksActivity.this, "Data Not Loaded to the Application", Toast.LENGTH_SHORT).show();
            }
        });
    }


}