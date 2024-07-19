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

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {
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
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_person, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person person = personList.get(position);
        holder.textViewName.setText(person.getFullName());
        holder.textViewPhone.setText(person.getPhoneNumber());
        holder.textViewLocation.setText(person.getLocation());

        if (showAcceptButton) {
            holder.buttonAccept.setVisibility(View.VISIBLE);
            holder.buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPhone, textViewLocation;
        Button buttonAccept;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewFullName);
            textViewPhone = itemView.findViewById(R.id.textViewPhoneNumber);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            buttonAccept = itemView.findViewById(R.id.buttonAccept);
        }
    }
}
