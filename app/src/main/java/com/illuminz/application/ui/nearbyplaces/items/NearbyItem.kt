package com.illuminz.application.ui.nearbyplaces.items

import com.core.utils.GlideApp
import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_nearby.view.*

class NearbyItem(
     val image: String,
     val title: String
): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {

            GlideApp.with(this)
                 .load(image)
                 .centerCrop()
                 .into(ivImage)

            tvTitle.text = context.getString(R.string.places_to_visit)

            val list = arrayListOf("https://static.toiimg.com/photo/67977105/4.jpg?width=748&resize=4",
                                    "https://cdn.cnn.com/cnnnext/dam/assets/181010131059-australia-best-beaches-cossies-beach-cocos3.jpg",
                                   "https://static.toiimg.com/photo/32791811.cms",
                                    "https://www.history.com/.image/t_share/MTU3ODc5MDg3NTA5MDg3NTYx/taj-mahal-2.jpg",
                                    "https://i1.wp.com/www.omanobserver.om/wp-content/uploads/2019/11/Sea-coast.jpg?fit=1500%2C840&ssl=1",
                                    "https://api.timeforkids.com/wp-content/uploads/2017/08/170227012793_hero.jpg",
                                    "https://static.toiimg.com/photo/67977105/4.jpg?width=748&resize=4",
                                    "https://static.toiimg.com/photo/67977105/4.jpg?width=748&resize=4",
                                    "https://cdn.cnn.com/cnnnext/dam/assets/181010131059-australia-best-beaches-cossies-beach-cocos3.jpg",
                                    "https://static.toiimg.com/photo/32791811.cms",
                                    "https://www.history.com/.image/t_share/MTU3ODc5MDg3NTA5MDg3NTYx/taj-mahal-2.jpg",
                                    "https://i1.wp.com/www.omanobserver.om/wp-content/uploads/2019/11/Sea-coast.jpg?fit=1500%2C840&ssl=1",
                                    "https://api.timeforkids.com/wp-content/uploads/2017/08/170227012793_hero.jpg",
                                    "https://static.toiimg.com/photo/67977105/4.jpg?width=748&resize=4")

//            rlNearby.setOnClickListener {
//                ImageDialog.newInstance(title,context.getString(R.string.ten_km_away),list)
//                    .show(fragmentContainer,ImageDialog.TAG)
//            }

            cdNearbyItem.setOnClickListener {
                rlNearby.callOnClick()
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_nearby

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return 3
    }
}