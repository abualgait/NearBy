package com.abualgait.msayed.nearby.shared.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abualgait.msayed.nearby.R
import com.abualgait.msayed.nearby.shared.data.model.Venue
import com.abualgait.msayed.nearby.shared.interfaces.SimpleItemClickListener
import com.abualgait.msayed.nearby.shared.viewholders.PlacesVH
import com.abualgait.msayed.nearby.shared.viewholders.RecyclerViewHolder
import java.util.*

class CommonAdapter() : RecyclerView.Adapter<RecyclerViewHolder>() {


    private var mContext: Context? = null
    private lateinit var mList: ArrayList<Venue>


    private val PLACE: Int = 1

    private lateinit var itemClick: SimpleItemClickListener


    constructor(mContext: Context, mList: ArrayList<Venue>) : this() {
        this.mContext = mContext
        this.mList = mList

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        var viewHolder: RecyclerViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {


            PLACE -> {
                val view: View = inflater.inflate(R.layout.item_place, parent, false)
                viewHolder = PlacesVH(this.mContext!!, view, itemClick)

            }


        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.onBindView(mList[position], position)

    }

    override fun getItemCount(): Int {
        return mList.size


    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemViewType(position: Int): Int {
        return PLACE
    }

    fun setItemClick(itemClick: SimpleItemClickListener) {
        this.itemClick = itemClick
    }


}