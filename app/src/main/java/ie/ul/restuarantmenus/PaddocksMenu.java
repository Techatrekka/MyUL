package ie.ul.restuarantmenus;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaddocksMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaddocksMenu extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView imageview;
    private Spinner spinner;

    public PaddocksMenu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PaddocksMenu.
     */
    // TODO: Rename and change types and number of parameters
    public static PaddocksMenu newInstance(String param1, String param2) {
        PaddocksMenu fragment = new PaddocksMenu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_scholars_menu, container, false);
        spinner = rootView.findViewById(R.id.spinner2);
        imageview = rootView.findViewById(R.id.imageView2);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
             //  R.array.paddocks_menu, android.R.layout.simple_spinner_item);
        String[] values = {"breakfast", "lunch", "dinner"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        imageview.setImageResource(R.drawable.paddocks_breakfast);
                        break;
                    case 1:
                        imageview.setImageResource(R.drawable.paddocks_lunch);
                        break;
                    case 2:
                        imageview.setImageResource(R.drawable.paddocks_dinner);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return rootView;    }
}