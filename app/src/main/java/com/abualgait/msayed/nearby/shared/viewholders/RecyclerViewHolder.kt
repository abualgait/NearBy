package com.abualgait.msayed.nearby.shared.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView


abstract class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBindView(`object`: Any, position: Int)


}