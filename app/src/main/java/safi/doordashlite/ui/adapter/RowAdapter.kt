package safi.doordashlite.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class RowAdapter<Model>(private val rowSpec: RowSpec<Model>) :
    RecyclerView.Adapter<RowAdapter<Model>.ViewHolder>() {

    private val items = ArrayList<Model>()

    private val clickEventSubject = PublishSubject.create<Model>()
    val clickEvent: Observable<Model> = clickEventSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val row = rowSpec.create()
        val view = row.inflate(inflater, parent)
        return ViewHolder(view, row)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = items[position]
        holder.bind(model, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun update(newItems: List<Model>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View, private val row: Row<Model>) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(rowData: Model, position: Int) {
            row.bind(rowData, { items[position] = it })
            itemView.setOnClickListener { clickEventSubject.onNext(rowData) }
        }
    }

    interface RowSpec<T> {
        fun create(): Row<T>
    }

    interface Row<T> {
        fun inflate(inflater: LayoutInflater, parent: ViewGroup): View

        fun bind(model: T, modelMutator: (T) -> Unit)
    }
}
