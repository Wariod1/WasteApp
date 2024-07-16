package com.example.wasteapp.person;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wasteapp.R;
import java.util.ArrayList;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Person> personList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onAcceptClick(Person person);
    }

    public PersonAdapter(Context context, ArrayList<Person> personList, OnItemClickListener listener) {
        this.context = context;
        this.personList = personList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_person, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Person person = personList.get(position);
        holder.textViewFullName.setText(person.getFullName());
        holder.textViewPhoneNumber.setText(person.getPhoneNumber());
        holder.textViewLocation.setText(person.getLocation());

        holder.buttonAccept.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAcceptClick(person);
            }
        });
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFullName, textViewPhoneNumber, textViewLocation;
        Button buttonAccept;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFullName = itemView.findViewById(R.id.textViewFullName);
            textViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            buttonAccept = itemView.findViewById(R.id.buttonAccept);
        }
    }
}