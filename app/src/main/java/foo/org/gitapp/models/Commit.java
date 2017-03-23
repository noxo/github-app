package foo.org.gitapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by enoks on 22.2.2017.
 */

public class Commit {

    public class AuthorDetails {

        private String avatar_url;

        public String getAvatarUrl() {
            return avatar_url;
        }

        public void setAvatarUrl(String avatar_url) {
            this.avatar_url = avatar_url;
        }

    }

    public class Author
    {
        private String name;
        private String date;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public class CommitDetails
    {
        private String message;
        private Author author;

        public Author getAuthor() {
            return author;
        }

        public void setAuthor(Author author) {
            this.author = author;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

    private String sha;
    @SerializedName("commit")
    private CommitDetails commitDetails;
    @SerializedName("author")
    private AuthorDetails authorDetails;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public CommitDetails getCommitDetails() {
        return commitDetails;
    }

    public void setCommitDetails(CommitDetails commitDetails) {
        this.commitDetails = commitDetails;
    }

    public AuthorDetails getAuthorDetails() {
        return authorDetails;
    }

    public void setAuthorDetails(AuthorDetails authorDetails) {
        this.authorDetails = authorDetails;
    }
}
