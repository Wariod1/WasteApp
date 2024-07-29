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
    private boolean showAcceptButton;

    public interface OnItemClickListener {
        void onAcceptClick(Person person);
    }

    public PersonAdapter(Context context, ArrayList<Person> personList, OnItemClickListener listener, boolean showAcceptButton) {
        this.context = context;
        this.personList = personList;
        this.listener = listener;
        this.showAcceptButton = showAcceptButton;
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
        holder.textViewWasteType.setText(person.getWasteType());

        if (person.getAcceptedDateTime() != null) {
            holder.textViewAcceptedDateTime.setVisibility(View.VISIBLE);
            holder.textViewAcceptedDateTime.setText("Accepted: " + person.getAcceptedDateTime());
        } else {
            holder.textViewAcceptedDateTime.setVisibility(View.GONE);
        }

        if (showAcceptButton) {
            holder.buttonAccept.setVisibility(View.VISIBLE);
            holder.buttonAccept.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAcceptClick(person);
                }
            });
        } else {
            holder.buttonAccept.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFullName, textViewPhoneNumber, textViewLocation, textViewWasteType, textViewAcceptedDateTime;
        Button buttonAccept;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFullName = itemView.findViewById(R.id.textViewFullName);
            textViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            textViewWasteType = itemView.findViewById(R.id.textViewWasteType);
            textViewAcceptedDateTime = itemView.findViewById(R.id.textViewAcceptedDateTime);
            buttonAccept = itemView.findViewById(R.id.buttonAccept);
        }
    }
}