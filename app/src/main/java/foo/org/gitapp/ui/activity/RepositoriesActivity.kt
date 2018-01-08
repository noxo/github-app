package foo.org.gitapp.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import android.support.v7.widget.Toolbar

import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.ProgressBar

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import foo.org.gitapp.DI.DaggerGithubComponent
import foo.org.gitapp.DI.GithubModule
import foo.org.gitapp.R
import foo.org.gitapp.models.Repository
import foo.org.gitapp.ui.adapters.RepositoryListAdapter


import foo.org.gitapp.util.GithubRetroFitClient
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject

/**
 * Created by enoks on 22.2.2017.
 */

class RepositoriesActivity : AppCompatActivity() {

    @BindView(R.id.repositoriesListView)
    lateinit var repositoriesListView: ListView;
    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar
    @BindView(R.id.searchRepositoryButton)
    lateinit var searchRepositoryButton: Button
    @BindView(R.id.progress)
    lateinit var mProgressView: ProgressBar
    @BindView(R.id.searchRepositoryEditText)
    lateinit var searchRepositoryEditText: EditText
    @BindView(R.id.mainLayout)
    lateinit var coordinatorLayout: CoordinatorLayout
    @Inject
    lateinit var githubRetroClient: GithubRetroFitClient

    lateinit var repositoryListAdapter: RepositoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.title_repository_activity)
        setContentView(R.layout.activity_repositories)
        ButterKnife.bind(this)

        DaggerGithubComponent.builder().githubModule(GithubModule()).build().inject(this)

        repositoryListAdapter = RepositoryListAdapter(this)
        repositoriesListView.adapter = repositoryListAdapter
        repositoriesListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val repository = repositoryListAdapter.getItem(position) as Repository
            val intent = Intent(this@RepositoriesActivity, CommitsActivity::class.java)
            intent.putExtra("user", repository.owner!!.login)
            intent.putExtra("repository", repository.name)
            startActivity(intent)
        }

        setSupportActionBar(toolbar)

    }

    @OnClick(R.id.searchRepositoryButton)
    internal fun searchRepositories() {

        val githubUsername = searchRepositoryEditText.text.toString()
        showProgress(true);
        githubRetroClient.getRepositories(githubUsername)
                .subscribeOn(Schedulers.computation())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe ({
                    result ->
                    showProgress(false)
                    repositoryListAdapter.update(result)
                }, { error ->
                    showProgress(false)
                    showError(error.toString())
                })

    }


    private fun showError(msg: String) {
        val snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(Color.RED)
        snackbar.show()
    }

    private fun showProgress(show: Boolean) {
        searchRepositoryButton.isEnabled = if (show) false else true
        searchRepositoryEditText.isEnabled = if (show) false else true
        mProgressView.visibility = if (show) View.VISIBLE else View.GONE
    }
}
