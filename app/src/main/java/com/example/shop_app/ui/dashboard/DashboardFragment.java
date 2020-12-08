package com.example.shop_app.ui.dashboard;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.shop_app.R;
import com.example.shop_app.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    EditText item,count_item,name_list;
    Button add_product,add_list;
    ListView product_list;
    FirebaseFirestore store;
    String userId;
    FirebaseAuth authorization;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //final TextView textView = root.findViewById(R.id.text_dashboard);
        item = (EditText) root.findViewById(R.id.item);
        name_list = (EditText) root.findViewById(R.id.editTextTextPersonName);
        count_item = (EditText) root.findViewById(R.id.count_item);
        add_product = (Button) root.findViewById(R.id.add_to_has_map);
        add_list = (Button) root.findViewById(R.id.add_to_db);
        authorization = (FirebaseAuth) FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();
        //product_list =(ListView) root.findViewById((R.id.database_list_view)) ;
        final Map<String,Object> item_list = new HashMap<>();
        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i = item.getText().toString().trim();
                String ci = count_item.getText().toString().trim();

              //  Map<String,Object> note = new HashMap<>();
                if(TextUtils.isEmpty(i)){
                    item.setError("musisz wpisać produkt");
                    return;
                }
                if(TextUtils.isEmpty(ci)){
                    count_item.setError("musisz wpisać ilosc");
                    return;
                }
                item_list.put(i,ci);
                item.setText("");
                count_item.setText("");


            }
        });
        add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x=0;
                String name = name_list.getText().toString().trim();
                if(item_list.size()<1){
                    item.setError("musisz dodać jakis produkt");
                    return;
                }
                String shop_list = name_list.getText().toString().trim();

                if(TextUtils.isEmpty(shop_list)){
                    name_list.setError("musisz podać nazwe listy");
                    return;
                }
                userId = authorization.getCurrentUser().getUid();
                DocumentReference create_colection = store.collection("users").document(userId).collection("note").document();
                item_list.put("title",name);
                create_colection.set(item_list, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        item_list.clear();
                    }
                });



            }
        });

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {



            }
        });
        return root;
    }

}