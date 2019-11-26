package cs.android.task.fragment.projects.details.files;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cs.android.task.R;
import cs.android.task.entity.File;

public class FilesDetails extends Fragment {

private Menu menu;
private RecyclerView fileListView;
private List<File> files = new LinkedList<>();

public FilesDetails() { }

public static FilesDetails newInstance() {
    FilesDetails frag =  new FilesDetails();
    // add args here
    return  frag;
}

@Override
public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
}
@Nullable
@Override
public View onCreateView (@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.files_card, container, false);
    Toolbar fileToolbar = view.findViewById(R.id.file_toolbar);
    fileToolbar.inflateMenu(R.menu.file_menu);
    fileToolbar.setOnMenuItemClickListener( item -> {
        item.setChecked(!item.isChecked());
        return true;
        }
    );
    menu = fileToolbar.getMenu();
    fileListView = view.findViewById(R.id.file_list);
    fileListView.setHasFixedSize(true);
    initFile();
    fileListView.setAdapter(new FileDetailsAdapter(this.files));
    fileListView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    return view;
}

  /**
   * Add file boolean.
   *
   * @param file the file
   * @return the boolean
   *         if file's already existed, return false, else true
   */
  public boolean addFile(File file) {
    if (this.files.stream().anyMatch(f->f.getName().equals(file.getName()))) {
        return false;
    } else {
        files.add(file);
        return true;
    }
}

// For Test
private void initFile() {
      for (int i = 0; i < 5; i++) {
          File file = new File();
          file.setName("Image " + i + ".jpg");
          file.setLastUpdate(new Date());
          file.setOwner("LMS");
          this.addFile(file);
      }

      for (int i = 0; i < 5; i++) {
          File file = new File();
          file.setName("Text " + i + ".txt");
          file.setLastUpdate(new Date());
          file.setOwner("LMS");
          this.addFile(file);
      }

    for (int i = 0; i < 5; i++) {
        File file = new File();
        file.setName("Audio " + i + ".mp3");
        file.setLastUpdate(new Date());
        file.setOwner("LMS");
        this.addFile(file);
    }
}

@Override
public void onCreateOptionsMenu (@NonNull Menu menu,@NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu,inflater);
    inflater.inflate(R.menu.main_menu, menu);
}

}
