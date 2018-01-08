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

import java.util.Arrays


import com.google.gson.*

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import foo.org.gitapp.DI.DaggerGithubComponent
import foo.org.gitapp.DI.GithubModule
import foo.org.gitapp.R
import foo.org.gitapp.models.Repository
import foo.org.gitapp.ui.adapters.RepositoryListAdapter
import foo.org.gitapp.util.GithubClient

import com.orhanobut.logger.Logger
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
    lateinit var githubClient: GithubClient
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

        showProgress(true)
        repositoryListAdapter.clear()

        githubClient.getRepositories(this, githubUsername, object : GithubClient.GithubClientHandler {
            override fun success(jsonString: String) {
                val gson = Gson()
                val repositories = Arrays.asList(*gson.fromJson<Array<Repository>>(jsonString, Array<Repository>::class.java!!))
                Logger.i("repositories pulled")
                showProgress(false)
                repositoryListAdapter.update(repositories)
            }

            override fun fail(error: Throwable, frienlyErrorMsg: String) {
                Logger.e(error, "failed pulling repositories")
                showError(frienlyErrorMsg)
                showProgress(false)
            }
        })
    }

    private fun showError(msg: String) {
        val snackbar = Snackbar.make(coordinatorLayout!!, msg, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(Color.RED)
        snackbar.show()
    }

    private fun showProgress(show: Boolean) {
        searchRepositoryButton.isEnabled = if (show) false else true
        searchRepositoryEditText.isEnabled = if (show) false else true
        mProgressView.visibility = if (show) View.VISIBLE else View.GONE
    }
}
