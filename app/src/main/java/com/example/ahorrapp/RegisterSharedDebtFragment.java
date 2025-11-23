package com.example.ahorrapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ahorrapp.databinding.FragmentRegisterSharedDebtBinding;
import com.example.ahorrapp.lib.DebtShared;
import com.example.ahorrapp.lib.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegisterSharedDebtFragment extends Fragment {

    private FragmentRegisterSharedDebtBinding binding;
    private List<View> participantViews = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private List<User> userList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterSharedDebtBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        binding.addParticipantButton.setOnClickListener(v -> addParticipantView());
        binding.saveSharedDebtButton.setOnClickListener(v -> saveSharedDebt());

        loadUsers();
    }

    private void loadUsers() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String uid = userSnapshot.getKey();
                    String name = userSnapshot.child("name").getValue(String.class);
                    if (uid != null && name != null) {
                        userList.add(new User(uid, name));
                    }
                }
                binding.addParticipantButton.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addParticipantView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View participantView = inflater.inflate(R.layout.item_participant, binding.participantsContainer, false);

        Spinner userSpinner = participantView.findViewById(R.id.userSpinner);
        ArrayAdapter<User> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, userList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(adapter);

        participantViews.add(participantView);
        binding.participantsContainer.addView(participantView);
    }

    private void saveSharedDebt() {
        // ... (misma lógica que antes)
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
