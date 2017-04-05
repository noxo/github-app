package foo.org.gitapp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.Arrays;
import java.util.List;


import com.google.gson.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import foo.org.gitapp.DI.DaggerGithubComponent;
import foo.org.gitapp.DI.GithubModule;
import foo.org.gitapp.R;
import foo.org.gitapp.models.Repository;
import foo.org.gitapp.ui.adapters.RepositoryListAdapter;
import foo.org.gitapp.util.GithubClient;

import com.orhanobut.logger.Logger;

import javax.inject.Inject;

/**
 * Created by enoks on 22.2.2017.
 */

public class RepositoriesActivity extends AppCompatActivity {

    @BindView(R.id.repositoriesListView)
    ListView repositoriesListView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.searchRepositoryButton)
    Button searchRepositoryButton;
    @BindView(R.id.progress)
    ProgressBar mProgressView;
    @BindView(R.id.searchRepositoryEditText)
    EditText searchRepositoryEditText;
    @BindView(R.id.mainLayout)
    CoordinatorLayout coordinatorLayout;

    private RepositoryListAdapter repositoryListAdapter;
    @Inject
    GithubClient githubClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.title_repository_activity);
        setContentView(R.layout.activity_repositories);
        ButterKnife.bind(this);
        DaggerGithubComponent.builder().githubModule(new GithubModule()).build().inject(this);

        repositoryListAdapter = new RepositoryListAdapter(this);
        repositoriesListView.setAdapter(repositoryListAdapter);
        repositoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Repository repository =  (Repository) repositoryListAdapter.getItem(position);
                Intent intent = new Intent(RepositoriesActivity.this, CommitsActivity.class);
                intent.putExtra("user", repository.getOwner().getLogin());
                intent.putExtra("repository", repository.getName());
                startActivity(intent);
            }
        });

        setSupportActionBar(toolbar);

    }

    @OnClick(R.id.searchRepositoryButton)
    void searchRepositories()
    {
        final String githubUsername = searchRepositoryEditText.getText().toString();

        showProgress(true);
        repositoryListAdapter.clear();

        githubClient.getRepositories(this, githubUsername,new GithubClient.GithubClientHandler() {
            @Override
            public void success(String jsonString) {
                Gson gson = new Gson();
                List<Repository> repositories = Arrays.asList(gson.fromJson(jsonString, Repository[].class));
                Logger.i("repositories pulled");
                showProgress(false);
                repositoryListAdapter.update(repositories);
            }

            @Override
            public void fail(Throwable error, String frienlyErrorMsg) {
                Logger.e(error, "failed pulling repositories");
                showError(frienlyErrorMsg);
                showProgress(false);
            }
        });
    }

    private void showError(String msg) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(Color.RED);
        snackbar.show();
    }

    private void showProgress(final boolean show) {
        searchRepositoryButton.setEnabled(show ? false : true);
        searchRepositoryEditText.setEnabled(show ? false : true);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
