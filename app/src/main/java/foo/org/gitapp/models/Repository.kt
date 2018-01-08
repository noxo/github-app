package foo.org.gitapp.models

/**
 * Created by enoks on 22.2.2017.
 */

class Repository {

    inner class Owner {

        var login: String? = null

    }

    var id: String? = null
    var name: String? = null
    var owner: Owner? = null
        private set

    fun seOwner(owner: Owner) {
        this.owner = owner
    }

}
