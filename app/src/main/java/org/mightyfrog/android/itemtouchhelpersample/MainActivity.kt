package org.mightyfrog.android.itemtouchhelpersample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * @author Shigehiro Soejima
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TestAdapter()
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            createItemTouchHelper().attachToRecyclerView(this)
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END

        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(dragFlags, swipeFlags) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                (recyclerView.adapter as TestAdapter).swap(from, to)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                (recyclerView.adapter as TestAdapter).remove(viewHolder.adapterPosition)
            }
        })
    }

    class TestAdapter : RecyclerView.Adapter<TestItemView>() {

        private val list: MutableList<Int> = (1..100).toMutableList()

        override fun getItemCount() = list.size

        override fun onBindViewHolder(holder: TestItemView, position: Int) {
            holder.tv.text = list[position].toString()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestItemView {
            return TestItemView(LayoutInflater.from(parent.context).inflate(R.layout.vh, parent, false))
        }

        fun swap(from: Int, to: Int) {
            Collections.swap(list, from, to)
            notifyItemMoved(from, to)
        }

        fun remove(pos: Int) {
            list.removeAt(pos)
            notifyItemRemoved(pos)
        }
    }

    class TestItemView(view: View) : RecyclerView.ViewHolder(view) {
        val tv = view.findViewById<TextView>(R.id.tv)!!
    }
}
