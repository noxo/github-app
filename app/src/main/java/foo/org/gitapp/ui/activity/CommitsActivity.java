package foo.org.gitapp.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import foo.org.gitapp.R;
import foo.org.gitapp.models.Commit;
import foo.org.gitapp.ui.adapters.CommitListAdapter;
import foo.org.gitapp.util.GithubClient;

/**
 * Created by enoks on 22.2.2017.
 */

public class CommitsActivity extends AppCompatActivity {

    @BindView(R.id.commitsListView)
    ListView chatMessagesList;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress)
    ProgressBar mProgressView;
    @BindView(R.id.mainLayout)
    CoordinatorLayout coordinatorLayout;

    private CommitListAdapter commitListAdapter;
    private GithubClient githubClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_commits);
        ButterKnife.bind(this);

        commitListAdapter = new CommitListAdapter(this);
        chatMessagesList.setAdapter(commitListAdapter);
        githubClient = GithubClient.getInstance();

        setSupportActionBar(toolbar);
        pullCommits();

    }

    private void pullCommits() {

        String githubUsername = getIntent().getStringExtra("user");
        String githubRepositoryName = getIntent().getStringExtra("repository");

        showProgress(true);

        githubClient.getCommits(this, githubUsername, githubRepositoryName,new GithubClient.GithubClientHandler() {
            @Override
            public void success(String jsonString) {
                Gson gson = new Gson();
                List<Commit> commits = Arrays.asList(gson.fromJson(jsonString, Commit[].class));
                commitListAdapter.update(commits);
                Logger.i("messages pulled");
                showProgress(false);
            }

            @Override
            public void fail(Throwable error, String frienlyErrorMsg) {
                Logger.e(error, "failed pulling messages");
                showProgress(false);
                showError(frienlyErrorMsg);
            }
        });

    }

    private void showError(String msg) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(Color.RED);
        snackbar.show();
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
