package foo.org.gitapp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import foo.org.gitapp.R;
import foo.org.gitapp.models.Repository;

/**
 * Created by enoks on 22.2.2017.
 */

public class RepositoryListAdapter extends BaseAdapter {

    private List<Repository> repositories;
    private LayoutInflater inflater;
    private Context context;

    public RepositoryListAdapter(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.repositories = new ArrayList<Repository>();
    }

    @Override
    public int getCount() {
        return repositories.size();
    }

    @Override
    public Object getItem(int position) {
        return repositories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Repository repository = repositories.get(position);
        ViewHolder holder;

        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.repository_row, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.contentEditText.setText(repository.getName());
        return view;
    }

    public void clear()
    {
        this.repositories = new ArrayList<Repository>();
        this.notifyDataSetChanged();
    }

    public void update(@NonNull List<Repository> repositories) {
        this.repositories = repositories;
        this.notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.repositoryNameTextView)  TextView contentEditText;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
