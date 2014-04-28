package com.comtrade.android.fragments;

import android.app.ListFragment;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.comtrade.android.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * <p></p>
 */
public class ListFilesFragment extends ListFragment {
    private static final Logger log = Logger.getLogger(ListFilesFragment.class.getSimpleName());

    private FilesListAdapter adapter;

    private File currentFolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.list_files, container, false);
        adapter = new FilesListAdapter(inflater);
        setListAdapter(adapter);
        openFolder(Environment.getExternalStorageDirectory());
        return inflate;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        File file = (File) adapter.getItem(position);
        if (file.isDirectory()) {
            openFolder(file);
            adapter.notifyDataSetChanged();
        } else {
            openFile(file);
        }
    }

    private void openFile(File file) {
        log.info(String.format("openFile(%s)", file));
        ((BrowseImagesActivity) getActivity()).openFile(file);
    }

    public void openFolder(File folder) {
        log.info(String.format("Opening folder %s", folder.getAbsolutePath()));
        currentFolder = folder;
        adapter.setFiles(folder.listFiles());
    }

    public boolean goBack() {
        File parent = currentFolder.getParentFile();
        if (parent != null) {
            openFolder(parent);
            return true;
        }
        return false;
    }

    private class FilesListAdapter implements ListAdapter {
        private final LayoutInflater inflater;
        private List<File> files;
        List<DataSetObserver> observers;

        public FilesListAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
            files = new ArrayList<File>();
            observers = new ArrayList<DataSetObserver>();
        }

        public void setFiles(File[] files) {
            Arrays.sort(files);
            this.files.clear();
            Collections.addAll(this.files, files);
            notifyDataSetChanged();
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            observers.add(observer);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            observers.remove(observer);
        }

        @Override
        public int getCount() {
            return files.size();
        }

        @Override
        public Object getItem(int position) {
            return files.get(position);
        }

        @Override
        public long getItemId(int position) {
            return files.get(position).hashCode();
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView == null ? inflater.inflate(R.layout.list_file_element, parent, false) : convertView;

            TextView textView = (TextView) view.findViewById(R.id.lbl_file_name);
            File file = files.get(position);
            textView.setText(file.getName());
            if (file.isDirectory()) {
                textView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_view_image), null, null, null);
            }

            return view;
        }

        @Override
        public int getItemViewType(int position) {
            return files.get(position).isDirectory() ? 1 : 0;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public boolean isEmpty() {
            return files == null || files.size() == 0;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        public void notifyDataSetChanged() {
            for (DataSetObserver observer : observers) {
                observer.onChanged();
            }
        }
    }
}