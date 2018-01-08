package foo.org.gitapp.util

import android.content.Context

import com.loopj.android.http.*

import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.message.BasicHeader

/**
 * Created by enoks on 22.2.2017.
 */

class GithubClient {

    interface GithubClientHandler {
        fun success(data: String)
        fun fail(error: Throwable, frienlyErrorMsg: String)
    }

    private val client = AsyncHttpClient()

    private val USER_AGENT = "GitApp"
    private val MAX_ITEMS = "10"
    private val STARTING_FROM_PAGE = "1"

    private operator fun get(context: Context, url: String, params: RequestParams?, responseHandler: AsyncHttpResponseHandler) {
        // 403 if not set
        // https://developer.github.com/v3/#user-agent-required
        client.setUserAgent(USER_AGENT)
        client.get(context, url, GITHUB_REQUEST_HEADERS, params, responseHandler)
    }

    fun getCommits(context: Context, githubUsername: String, githubRepositoryName: String, handler: GithubClientHandler) {

        val requestUrl = String.format(GITHUB_COMMITS_URL, githubUsername, githubRepositoryName)

        // https://developer.github.com/guides/traversing-with-pagination/
        val params = RequestParams()
        params.add("per_page", MAX_ITEMS)
        params.add("page", STARTING_FROM_PAGE)

        get(context, requestUrl, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                handler.success(String(responseBody))
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                handler.fail(error, "error")
            }
        })
    }

    fun getRepositories(context: Context, githubUsername: String, handler: GithubClientHandler) {

        val requestUrl = String.format(GITHUB_REPOSITORIES_URL, githubUsername)

        get(context, requestUrl, null, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                handler.success(String(responseBody))
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                handler.fail(error, "error")
            }
        })
    }

    companion object {

        private val GITHUB_REPOSITORIES_URL = "https://api.github.com/users/%s/repos"
        private val GITHUB_COMMITS_URL = "https://api.github.com/repos/%s/%s/commits"
        private val instance: GithubClient? = null

        private val GITHUB_REQUEST_HEADERS = arrayOf<Header>(BasicHeader("Accept", "application/vnd.github.v3+json"))
    }

}
