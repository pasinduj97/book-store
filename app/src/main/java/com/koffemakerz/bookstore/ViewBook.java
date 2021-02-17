package com.koffemakerz.bookstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewBook extends AppCompatActivity {


    TextView title,author, description, price,genre;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        title = findViewById(R.id.titlev);
        author = findViewById(R.id.author);
        description = findViewById(R.id.description);
        image = findViewById(R.id.img);
        genre = findViewById(R.id.genrev);
        price = findViewById(R.id.pricev);


        Intent intent = getIntent();

        String name = intent.getExtras().get("image").toString();
        int id = getResources().getIdentifier(name, "drawable", getPackageName());
        Drawable drawable = getResources().getDrawable(id,null);

        title.setText(intent.getExtras().get("title").toString());
        author.setText("By "+intent.getExtras().get("author").toString());
        description.setText(intent.getExtras().get("description").toString());
        image.setImageDrawable(drawable);
        genre.setText(intent.getExtras().get("genre").toString());
        price.setText("$"+intent.getExtras().get("price").toString());
    }
}