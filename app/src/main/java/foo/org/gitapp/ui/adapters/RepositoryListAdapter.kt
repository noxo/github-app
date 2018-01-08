package foo.org.gitapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import foo.org.gitapp.R
import foo.org.gitapp.models.Repository

/**
 * Created by enoks on 22.2.2017.
 */

class RepositoryListAdapter(private val context: Context) : BaseAdapter() {

    private var repositories: List<Repository>? = null
    private val inflater: LayoutInflater

    init {
        this.inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return repositories?.count() ?: 0;
    }

    override fun getItem(position: Int): Any? {
        repositories?.let {
            return it[position]
        }//
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View? {

        repositories?.let {

            var view = view

            val repository = it.get(position)
            val holder: ViewHolder

            if (view != null) {
                holder = view.tag as ViewHolder
            } else {
                view = inflater.inflate(R.layout.repository_row, parent, false)
                holder = ViewHolder(view)
                view?.tag = holder
            }

            holder.contentEditText?.text = repository.name
            return view
        }

        return view;
    }

    fun clear() {
        this.repositories = ArrayList()
        this.notifyDataSetChanged()
    }

    fun update(repositories: List<Repository>) {
        this.repositories = repositories
        this.notifyDataSetChanged()
    }

    internal class ViewHolder(view: View) {
        @BindView(R.id.repositoryNameTextView)
        lateinit var contentEditText: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }

}
