package foo.org.gitapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import android.widget.ImageView
import android.widget.TextView
import java.util.ArrayList

import com.squareup.picasso.*

import butterknife.BindView
import butterknife.ButterKnife

import foo.org.gitapp.R
import foo.org.gitapp.models.Commit
import foo.org.gitapp.models.Repository

import com.github.siyamed.shapeimageview.CircularImageView

/**
 * Created by enoks on 22.2.2017.
 */

class CommitListAdapter(private val context: Context) : BaseAdapter() {

    private var commits: List<Commit>? = null
    private val inflater: LayoutInflater

    init {
        this.inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return commits?.size ?: 0;
    }

    override fun getItem(position: Int): Any? {
        return commits?.get(0) ?: null;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var view = view

        val commit = commits?.get(position)
        val holder: ViewHolder

        if (view != null) {
            holder = view.tag as ViewHolder
        } else {
            view = inflater.inflate(R.layout.commit_row, parent, false)
            holder = ViewHolder(view)
            view!!.tag = holder
        }

        commit?.authorDetails?.avatarUrl?.let {
            if (!it.isEmpty())
            {
                Picasso.with(context).load(it).into(holder.avatarImageView)
            }
        }

        holder.shaTextView?.text = commit?.sha
        holder.authorNameTextView?.text = commit?.commitDetails?.author?.name
        holder.authorizedTimestampTextView?.text = commit?.commitDetails?.author?.date
        holder.messageTextView?.text = commit?.commitDetails?.message
        return view
    }

    fun update(commits: List<Commit>) {
        this.commits = commits
        this.notifyDataSetChanged()
    }

    internal class ViewHolder(view: View) {
        @BindView(R.id.avatarImageView)
        lateinit var avatarImageView: ImageView;
        @BindView(R.id.shaTextView)
        lateinit var shaTextView: TextView
        @BindView(R.id.authorNameTextView)
        lateinit var authorNameTextView: TextView
        @BindView(R.id.authorizedTimestampTextView)
        lateinit var authorizedTimestampTextView: TextView
        @BindView(R.id.messageTextView)
        lateinit var messageTextView: TextView;

        init {
            ButterKnife.bind(this, view)
        }
    }

}
