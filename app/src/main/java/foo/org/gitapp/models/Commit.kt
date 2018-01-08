package foo.org.gitapp.models

import com.google.gson.annotations.SerializedName

/**
 * Created by enoks on 22.2.2017.
 */

class Commit {

    inner class AuthorDetails {

        var avatarUrl: String? = null

    }

    inner class Author {
        var name: String? = null
        var date: String? = null

    }

    inner class CommitDetails {
        var message: String? = null
        var author: Author? = null

    }

    var sha: String? = null
    @SerializedName("commit")
    var commitDetails: CommitDetails? = null
    @SerializedName("author")
    var authorDetails: AuthorDetails? = null
}
