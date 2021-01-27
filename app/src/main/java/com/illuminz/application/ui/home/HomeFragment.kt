package com.illuminz.application.ui.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import com.core.extensions.setCustomAnimations
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.AnimationDirection
import com.core.utils.GlideApp
import com.illuminz.application.R
import com.illuminz.application.ui.home.bar.DrinksFragment
import com.illuminz.application.ui.home.bookTable.BookTableFragment
import com.illuminz.application.ui.home.food.FoodListFragment
import com.illuminz.application.ui.home.items.*
import com.illuminz.application.ui.home.laundry.LaundryFragment
import com.illuminz.application.ui.home.massage.MassageListFragment
import com.illuminz.application.ui.home.nearbyplaces.NearbyFragment
import com.illuminz.application.ui.home.roomcleaning.RoomCleaningFragment
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.dialog_transport.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_food.view.*


class HomeFragment : DaggerBaseFragment() {

    companion object {
        const val TAG = "HomeFragment"
        private const val ORDERFOOD = "ORDERFOOD"
        private const val LAUNDRY = "LAUNDRY"
        private const val MASSAGE = "MASSAGE"
        private const val ROOMCLEANING = "ROOMCLEANING"
        private const val BAR = "BAR"
        private const val BOOKTABLE = "BOOKTABLE"
        private const val NEARBY = "NEARBY"
        private const val TRANSPORT = "TRANSPORT"
        private const val GYM = "GYM"
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }

    }

    private lateinit var adapter: GroupAdapter<GroupieViewHolder>

    override fun getLayoutResId(): Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setListener()
    }

    private fun initialise() {

        tvTimeValue.text = "15"
        tvTimeUnit.text = "min"
        tvDeliveryLabel.text = "It will be delivered at 4:30pm"

        adapter = GroupAdapter()
        val layoutManager = (rvHome.layoutManager as GridLayoutManager)
        layoutManager.spanSizeLookup = adapter.spanSizeLookup
        rvHome.adapter = adapter

//        adapter.add(BookingDetailItem())
//        adapter.add(RoomServiceItem())
        adapter.add(ServiceTitleItem(getString(R.string.services)))

        val image1 = "https://www.howtogeek.com/wp-content/uploads/2020/03/delivery-food.jpg"
        val image2 =
            "https://cleanlaundry.com/wp-content/uploads/2016/02/9-Tips-to-Make-Ironing-Your-Clothes-a-Piece-of-Cake1-300x200.jpg"
        val image3 =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTtVQMlP4qORDhewsl_ovMA2MVn9zKSgfalBg&usqp=CAU"
        val image4 =
            "https://ehlersdanlosnews.com/wp-content/uploads/2020/01/shutterstock_547127785-760x475@2x.jpg"
        val image5 =
            "https://images.unsplash.com/photo-1588710406418-bb90c807db72?ixid=MXwxMjA3fDB8MHxzZWFyY2h8Mnx8cmVzdGF1cmFudCUyMHRhYmxlfGVufDB8fDB8&ixlib=rb-1.2.1&w=1000&q=80"
        val image6 =
            "https://www.designmantic.com/blog/wp-content/uploads/2020/04/Spa-and-Massage-Logos.jpg"

        val image7 =
            "https://cdn.britannica.com/95/94195-050-FCBF777E/Golden-Gate-Bridge-San-Francisco.jpg"

        val image8 =
            "https://www.thoughtco.com/thmb/2-kR4cU4EOrY8bQ1M8msDinZgG8=/1926x1280/filters:fill(auto,1)/GettyImages-556712867-58e85b223df78c51620400d4.jpg"

        val image9 =
            "https://s9i4v6w2.rocketcdn.me/wordpress/wp-content/uploads/2020/10/Pullman-Accor-Power-Fitness-Gym-Design-1-400x284.jpg"

        val services =
            listOf(
                ServicesItem(
                    image = image1,
                    name = "Order Food",
                    description = "North indian, Continental, Chinese and more",
                    type = 1,
                    tag = ORDERFOOD
                ),
                ServicesItem(
                    image = image2,
                    name = "Laundry/Ironing",
                    description = "Book Laundry and ironing for your clothes",
                    type = 1,
                    tag = LAUNDRY
                ),
                ServicesItem(
                    image = image3,
                    name = "Room Cleaning",
                    description = "Ask or schedule for your room cleaning",
                    type = 1,
                    tag = ROOMCLEANING
                ),
                ServicesItem(
                    image = image4,
                    name = "Cheer-up Bar",
                    description = "Bar menu",
                    type = 1,
                    tag = BAR
                ),
                ServicesItem(
                    image = image5,
                    name = "Book A Table",
                    description = "Book A Table",
                    type = 1,
                    tag = BOOKTABLE
                ),
                ServicesItem(
                    image = image6,
                    name = "Spa & Massage",
                    description = "Book Laundry and ironing for your clothes",
                    type = 1,
                    tag = MASSAGE
                )
            )
        adapter.addAll(services)
        adapter.add(ServiceTitleItem("MORE SERVICES"))

        val item5 =
            listOf(
                MoreServicesItem(
                    image = image7,
                    name = "Nearby Places",
                    description = "Places to visit, Things to do, and more in Mumbai",
                    type = 2,
                    tag = NEARBY
                ),
                MoreServicesItem(
                    image = image8,
                    name = "Transportation",
                    description = "Local sight-seeing and airport transfers",
                    type = 2,
                    tag = TRANSPORT
                ),
                MoreServicesItem(
                    image = image9,
                    name = "Gym Timing",
                    description = "Morning - 5 AM to 10 AM\n" +
                            "Evening - 4 PM to 8 PM",
                    type = 2,
                    tag = GYM
                )
            )
        adapter.addAll(item5)
    }


    private fun setListener() {

        btnFeedback.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        }

        btnWifi.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        }
            adapter.setOnItemClickListener { item, _ ->

                if (item is ServicesItem) {
                    when (item.tag) {
                        ORDERFOOD -> {
                                val fragment = FoodListFragment()
                                openFragment(fragment, FoodListFragment.TAG)
                        }

                        LAUNDRY -> {
                                val fragment = LaundryFragment()
                                openFragment(fragment, LaundryFragment.TAG)
                        }

                        MASSAGE -> {
                                val fragment = MassageListFragment()
                                openFragment(fragment, MassageListFragment.TAG)
                        }

                        ROOMCLEANING -> {
                                val fragment = RoomCleaningFragment()
                                openFragment(fragment, RoomCleaningFragment.TAG)
                        }

                        BAR -> {
                                val fragment = DrinksFragment()
                                openFragment(fragment, DrinksFragment.TAG)
                        }
                        BOOKTABLE -> {
                                val fragment = BookTableFragment()
                                openFragment(fragment, BookTableFragment.TAG)
                        }

                    }
                } else if (item is MoreServicesItem) {
                    when (item.tag) {
                        NEARBY -> {
                                val fragment = NearbyFragment.newInstance(
                                    getString(R.string.nearby_places),
                                    getString(R.string.places_to_visit), nearbyList()
                                )
                                openFragment(fragment, NearbyFragment.TAG)
                        }

                        TRANSPORT -> {
                            val url = "https://images.newindianexpress.com/uploads/user/imagelibrary/2019/10/20/w900X450/North_DMC.jpg"
                            val title = "New Delhi Railway Station"
                            val price = CurrencyFormatter.format(amount = 1000.00, currencyCode = "INR")
                            val distance = "220km"
                            val priceDistance = "$price | $distance"
                            showTransportDialog(url,title,priceDistance)
                        }

                        GYM -> {
                                val fragment = NearbyFragment.newInstance(
                                    getString(R.string.gym_features),
                                    getString(R.string.gym_subtitle), gymList()
                                )
                                openFragment(fragment, NearbyFragment.TAG)
                        }
                    }
                }

            }
    }

    private fun openFragment(fragment: DaggerBaseFragment, tag: String) {
        if (parentFragmentManager.findFragmentByTag(tag) == null) {
                parentFragmentManager.beginTransaction()
                .setCustomAnimations(AnimationDirection.End)
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(tag)
                .commit()
        }
    }

    private fun nearbyList(): ArrayList<String> {
        val imageList = arrayListOf(
            "https://2.bp.blogspot.com/-kIlA_BjfIqQ/T-CQ8WuaC7I/AAAAAAAAAkE/dTo_8UZxBBU/s1600/Mumbai+322.JPG",
            "https://www.visittnt.com/blog/wp-content/uploads/2018/05/Marine-drive.jpg",
            "https://lp-cms-production.imgix.net/2019-06/99ad019a84817e4bb5248d47c70dd1d2-chhatrapati-shivaji-terminus.jpg",
            "https://static.toiimg.com/photo/61984337.cms",
            "https://static.toiimg.com/photo/54599572.cms",
            "https://www.fabhotels.com/blog/wp-content/uploads/2019/06/Haji-Ali-Dargah_600-1280x720.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/6/62/Mumbai_Dhobi_Ghat_Laundry_District.JPG",
            "https://www.indianholiday.com/images/travel-destination/delhi.jpg",
            "https://www.holidify.com/images/tooltipImages/MUNNAR.jpg"
        )

        return imageList
    }

    private fun gymList(): ArrayList<String> {
        val imageList = arrayListOf(
            "https://img.webmd.com/dtmcms/live/webmd/consumer_assets/site_images/article_thumbnails/blog_posts/webmd-doctors/1800x1200_empty-gym.jpg",
            "https://www.telegraph.co.uk/content/dam/luxury/2017/03/21/Gym_2_trans_NvBQzQNjv4BqgsaO8O78rhmZrDxTlQBjdGcv5yZLmao6LolmWYJrXns.jpg",
            "https://blog.nasm.org/hubfs/cleangym%20%281%29.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQm1YU1rJ6caLIsbCAvfqLCcarmMyvFZISZZw&usqp=CAU",
            "https://www.gannett-cdn.com/presto/2020/04/21/USAT/6d4af31f-c7f7-4ade-b570-515252b9c81e-XXX_fitness_center.jpg"
        )

        return imageList
    }

    private fun showTransportDialog(image: String,title:String,price:String) {
        val dialog = context?.let { Dialog(it) }

        dialog?.run {

            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.dialog_transport)

            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setGravity(Gravity.CENTER)
            }



            tvTransportName.text = title
            tvDistance.text = price

            GlideApp.with(context)
                .load(image)
                .placeholder(R.color.colorPrimary)
                .error(R.color.black)
                .centerCrop()
                .into(ivTransport)

            btRequest.setOnClickListener {
                dismiss()
            }
            show()
        }
    }
}