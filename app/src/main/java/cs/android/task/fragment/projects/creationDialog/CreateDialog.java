package cs.android.task.fragment.projects.creationDialog;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.Chip;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import cs.android.task.R;

public class CreateDialog extends Fragment {

private List<Chip> chipsList = new ArrayList<>();
private ChipsInput chipsInput;
private Bundle bundle;

public static CreateDialog newInstance(@NonNull Bundle bundle) {
    CreateDialog dialog = new CreateDialog();
    dialog.setArguments(bundle);
    dialog.bundle = bundle;
    return dialog;
}

public CreateDialog () {}

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
}

@Override
public View onCreateView(
        LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.project_create_dialog, container, false);
    chipsInput = (ChipsInput) view.findViewById(R.id.select_members);

    ArrayList<String> names = bundle.getStringArrayList("names");
    ArrayList<String> phoneNums = bundle.getStringArrayList("phone_nums");

    for (int i = 0; i < names.size(); i++) {
        chipsList.add(new Chip(R.drawable.smile, names.get(i), phoneNums.get(i)));
    }
    chipsInput.setFilterableList(chipsList);

    ((MaterialButton)view.findViewById(R.id.ok)).setOnClickListener(v->{
        bundle.putBoolean("ok", true);
        this.getFragmentManager().popBackStack();
    });
    ((MaterialButton)view.findViewById(R.id.cancel)).setOnClickListener(v->{
        bundle.putBoolean("ok",false);
        this.getFragmentManager().popBackStack();
    });
    bundle.putString("name",(( EditText )view.findViewById(R.id.new_project_name)).getText().toString());
    return view;
}


}
