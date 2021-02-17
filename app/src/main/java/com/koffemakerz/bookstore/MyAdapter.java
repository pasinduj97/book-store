package com.koffemakerz.bookstore;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private AllBooksActivity activity;
    private List<Model> mList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Dialog loader;
    private Context context;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;



    public MyAdapter(AllBooksActivity activity, List<Model> mList, Context context){
        this.activity = activity;
        this.mList = mList;
        this.context = context;
        loader = new Dialog(context);
        loader.setContentView(R.layout.progress_bar);
        loader.setCancelable(false);

    }

//    public void updateData(int position){
//        Model item = mList.get(position);
//        Bundle bundle = new Bundle();
//
//        bundle.putString("uId", item.getId());
//        bundle.putString("uFirstName", item.getFirstName());
//        bundle.putString("uLastName", item.getLastName());
//        bundle.putString("uPostalAddressNo", item.getPostalAddresNo());
//        bundle.putString("uPostalAddress1", item.getPostalAddressl1());
//        bundle.putString("uPostalAddress2", item.getPostalAddresl2());
//        bundle.putString("uEmail", item.getEmail());
//        bundle.putString("uMobileOne", item.getMobileOne());
//        bundle.putString("uMobileTwo", item.getMobileTwo());
//        bundle.putString("uMobileThree", item.getMobileThree());
//        bundle.putString("uDOB", item.getDob());
//        bundle.putString("uAnniversary", item.getAnniversary());
//        bundle.putString("uInitials", item.getInitials());
//        bundle.putString("uCountry", item.getCountry());
//        bundle.putString("uPostalCode", item.getPostalC());
//        bundle.putString("uType", item.getType());
//
//        Intent intent = new Intent(activity, AddBookActivity.class);
//        intent.putExtras(bundle);
//        activity.startActivity(intent);
//    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.itrm, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.title.setText(mList.get(position).getTitle());
//        holder.author.setText(mList.get(position).getAuthor());

        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        DocumentReference userRef = db.collection("users").document(userid);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    if(mList.get(position).getBurrowed().equals("true") && mList.get(position).getId().equals(task.getResult().getString("burrowed"))){
                        holder.returnButton.setVisibility(View.VISIBLE);
                    }else{
                        holder.returnButton.setVisibility(View.GONE);
                    }


                }
            }
        });



        if(mList.get(position).getBurrowed().equals("true")){
            holder.author.setText("Not Available");
        }else{
            holder.author.setText("Available");
        }






        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ViewBook.class);
                intent.putExtra("author",mList.get(position).getAuthor());
                intent.putExtra("title",mList.get(position).getTitle());
                intent.putExtra("genre",mList.get(position).getGenre());
                intent.putExtra("price",mList.get(position).getPrice());
                intent.putExtra("description",mList.get(position).getDescription());
                intent.putExtra("image",mList.get(position).getImage());

                view.getContext().startActivity(intent);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

               burrow(position);


                return false;
            }
        });

        holder.returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> update = new ArrayMap<>();
                update.put("burrowed","null");
                db.collection("users").document(userid).update(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(activity, "Book Returned", Toast.LENGTH_SHORT).show();
                    }
                });

                Map<String,Object> bookUpdate = new ArrayMap<>();
                bookUpdate.put("burrowed","null");

                db.collection("Documents").document(mList.get(position).getId()).update(bookUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(context, MainMenuActivity.class);
                        context.startActivity(intent);
                    }
                });
            }

        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title,author;
        CardView cardView;
        ImageView returnButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleitem);
            author = itemView.findViewById(R.id.authoritem);
            returnButton = itemView.findViewById(R.id.ret);

            cardView = itemView.findViewById(R.id.cardview);

        }
    }

    public void burrow(final int position){

        dialogBuilder = new AlertDialog.Builder(context);
        final View popup = LayoutInflater.from(context).inflate(R.layout.activity_burrow_popup, null);
        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        dialog.show();

        Button no = popup.findViewById(R.id.no);
        Button yes = popup.findViewById(R.id.yes);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                burrowBook(position);

            }
        });

    }

    public void burrowBook(final int position){
        Model item = mList.get(position);
        dialog.dismiss();

        loader.show();

        Map<String,Object> book = new ArrayMap<>();
        book.put("burrowed","true");

        db.collection("Documents").document(item.getId()).update(book)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            updateUser(position);
                            Toast.makeText(activity, "Book Burrowed Successfully", Toast.LENGTH_SHORT).show();

                            loader.dismiss();
                        }else{
                            Toast.makeText(activity, "Not Successfull" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            loader.dismiss();
                        }
                    }
                });
    }

    private void updateUser(int position){
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String id = mList.get(position).getId();
        Map<String,Object> book = new ArrayMap<>();
        book.put("burrowed", id);

        db.collection("users").document(userid).update(book).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){

                   Intent intent = new Intent(context, MainMenuActivity.class);
                   context.startActivity(intent);

               }else {
                   Toast.makeText(context,"failed",Toast.LENGTH_SHORT).show();
               }
            }
        });
    }


    public void filterList(ArrayList<Model> filterdList){
        mList = filterdList;
        notifyDataSetChanged();

    }

}

