package com.abualgait.msayed.nearby.shared.viewholders


import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.abualgait.msayed.nearby.R
import com.abualgait.msayed.nearby.shared.data.model.Venue
import com.abualgait.msayed.nearby.shared.interfaces.SimpleItemClickListener
import com.abualgait.msayed.nearby.shared.util.io.ImageSetter


class PlacesVH(private var mContext: Context, itemView: View?, private var itemClick: SimpleItemClickListener) :
    RecyclerViewHolder(itemView!!) {


    private var placeImage: ImageView? = null
    private var placeName: TextView? = null
    private var placeAddress: TextView? = null


    private var obj: Any? = null


    init {
        placeImage = itemView?.findViewById(R.id.placeImage)
        placeName = itemView?.findViewById(R.id.placeName)
        placeAddress = itemView?.findViewById(R.id.placeAddress)
        itemView!!.setOnClickListener { itemClick.onItemClick(this.obj!!) }

    }

    override fun onBindView(`object`: Any, position: Int) {
        obj = `object`
        if (`object` is Venue) {
            val info = `object`
            if (info.image != null) {
                ImageSetter.loadImage(info.image!!, placeImage)
            }
            placeName?.text = if (!TextUtils.isEmpty(info.name)) info.name else ""
            placeAddress?.text = if (!TextUtils.isEmpty(info.location.address)) info.location.address else ""

        }


    }


}