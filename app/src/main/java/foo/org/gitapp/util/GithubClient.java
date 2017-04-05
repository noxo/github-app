package foo.org.gitapp.util;

import android.content.Context;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * Created by enoks on 22.2.2017.
 */

public class GithubClient {

    public interface GithubClientHandler
    {
        void success(String data);
        void fail(Throwable error, String frienlyErrorMsg);
    }

    private static final String GITHUB_REPOSITORIES_URL = "https://api.github.com/users/%s/repos";
    private static final String GITHUB_COMMITS_URL = "https://api.github.com/repos/%s/%s/commits";

    private AsyncHttpClient client = new AsyncHttpClient();
    private static GithubClient instance;

    private static Header GITHUB_REQUEST_HEADERS[] =  {
        new BasicHeader("Accept", "application/vnd.github.v3+json")
    };

    private final String USER_AGENT = "GitApp";
    private final String MAX_ITEMS = "10";
    private final String STARTING_FROM_PAGE = "1";


    private void get(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        // 403 if not set
        // https://developer.github.com/v3/#user-agent-required
        client.setUserAgent(USER_AGENT);
        client.get(context, url, GITHUB_REQUEST_HEADERS, params, responseHandler);
    }

    public void getCommits(final Context context, final String githubUsername, final String githubRepositoryName, final GithubClientHandler handler) {

        String requestUrl = String.format(GITHUB_COMMITS_URL, githubUsername, githubRepositoryName);

        // https://developer.github.com/guides/traversing-with-pagination/
        RequestParams params = new RequestParams();
        params.add("per_page", MAX_ITEMS);
        params.add("page", STARTING_FROM_PAGE);

        get(context, requestUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                handler.success(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.fail(error, "error");
            }
        });
    }

    public void getRepositories(final Context context, final String githubUsername, final GithubClientHandler handler) {

        String requestUrl = String.format(GITHUB_REPOSITORIES_URL, githubUsername);

        get(context, requestUrl, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                handler.success(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.fail(error, "error");
            }
        });
    }

}
