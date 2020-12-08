package com.example.shop_app.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.shop_app.MainActivity;
import com.example.shop_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.Query;

import java.util.ArrayList;
import java.util.Arrays;

public class NotificationsFragment extends Fragment {
    FirebaseFirestore store;
    FirebaseAuth authorization;
    String userId;
    Button bt;
    ListView lv;
    ArrayList<String> arrayList = new ArrayList<String>();;
    ArrayAdapter arrayAdapter;

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        lv = (ListView)root.findViewById(R.id.list_name);
        //bt = (Button)root.findViewById(R.id.button7);
        authorization = (FirebaseAuth) FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();
        userId = authorization.getCurrentUser().getUid();
        store.collection("users").document(userId).collection("note")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String id_list;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Query query = store.collection("users").document(userId).collection("note").whereEqualTo("title")
                                id_list = document.getString("title");

                                arrayList.add(id_list);

                            }
                        } else {
                            Log.d("utowrzono", "Error getting documents: ", task.getException());
                        }

                        lv.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrayList));
                    }

                });


       //System.out.println(arrayList);

        //final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
             //   textView.setText(s);
            }
        });
        return root;
    }
}