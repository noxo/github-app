package foo.org.gitapp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.*;

import butterknife.BindView;
import butterknife.ButterKnife;

import foo.org.gitapp.R;
import foo.org.gitapp.models.Commit;
import foo.org.gitapp.models.Repository;

import com.github.siyamed.shapeimageview.CircularImageView;

/**
 * Created by enoks on 22.2.2017.
 */

public class CommitListAdapter extends BaseAdapter {

    private List<Commit> commits;
    private LayoutInflater inflater;
    private Context context;

    public CommitListAdapter(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.commits = new ArrayList<Commit>();
    }

    @Override
    public int getCount() {
        return commits.size();
    }

    @Override
    public Object getItem(int position) {
        return commits.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Commit commit = commits.get(position);
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.commit_row, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        if (commit.getAuthorDetails() != null)
        {
            if (!commit.getAuthorDetails().getAvatarUrl().isEmpty()) {
                String avatarPictureUrl = commit.getAuthorDetails().getAvatarUrl();
                Picasso.with(context).load(avatarPictureUrl).into(holder.avatarImageView);
            }
        }

        holder.shaTextView.setText(commit.getSha());
        holder.authorNameTextView.setText(commit.getCommitDetails().getAuthor().getName());
        holder.authorizedTimestampTextView.setText(commit.getCommitDetails().getAuthor().getDate());
        holder.messageTextView.setText(commit.getCommitDetails().getMessage());
        return view;
    }

    public void clear()
    {
        this.commits = new ArrayList<Commit>();
        this.notifyDataSetChanged();
    }
    public void update(@NonNull List<Commit> commits) {
        this.commits = commits;
        this.notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.avatarImageView) ImageView avatarImageView;
        @BindView(R.id.shaTextView)  TextView shaTextView;
        @BindView(R.id.authorNameTextView)  TextView authorNameTextView;
        @BindView(R.id.authorizedTimestampTextView)  TextView authorizedTimestampTextView;
        @BindView(R.id.messageTextView)  TextView messageTextView;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
