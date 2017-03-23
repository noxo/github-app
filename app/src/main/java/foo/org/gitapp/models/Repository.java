package foo.org.gitapp.models;

/**
 * Created by enoks on 22.2.2017.
 */

public class Repository {

    public class Owner {

        private String login;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

    }

    private String id;
    private String name;
    private Owner owner;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Owner getOwner() {
        return owner;
    }

    public void seOwner(Owner owner) {
        this.owner = owner;
    }

}
