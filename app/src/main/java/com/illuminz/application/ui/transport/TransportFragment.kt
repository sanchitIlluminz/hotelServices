package com.illuminz.application.ui.transport

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.GridLayoutManager
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.GlideApp
import com.illuminz.application.R
import com.illuminz.application.ui.transport.items.TransportTitleItem
import com.illuminz.application.ui.transport.items.TransportationItem
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.dialog_transport.*
import kotlinx.android.synthetic.main.fragment_transport.*


class TransportFragment : DaggerBaseFragment() {

    companion object {
        const val TAG = "TransportFragment"
        fun newInstance() : TransportFragment{
            return TransportFragment()
        }
    }

    private lateinit var adapter:GroupAdapter<GroupieViewHolder>

    override fun getLayoutResId(): Int = R.layout.fragment_transport

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
        setListeners()
    }

    private fun initialise() {
        adapter = GroupAdapter()

        val layoutManager = rvTransport.layoutManager as GridLayoutManager
        layoutManager.spanSizeLookup = adapter.spanSizeLookup
        rvTransport.adapter = adapter

        val item = TransportTitleItem("Please check places where you want to go or Click Other Place")
        adapter.add(item)

        val imageList= getImageList()
        val nameList = getNameList()

        for (i in imageList.indices){
            val item = TransportationItem(image = imageList[i], name = nameList[i],price = 1000.00, distance = 220.00 )
            adapter.add(item)
        }

    }



    private fun setListeners() {

        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        adapter.setOnItemClickListener { item, view ->
            if (item is TransportationItem){
                val price = CurrencyFormatter.format(amount = item.price, currencyCode = "INR")
                val distance = item.distance.toInt()
                val fare = "$price | $distance km"
                showTransportDialog(item.image,item.name,fare)

            }
        }
    }

    private fun getImageList():List<String>{
        return listOf(  "https://i1.wp.com/www.newdelhitimes.com/wp-content/uploads/2020/11/27-1.jpg?fit=1024%2C551&ssl=1",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSsysc_AeL_7zQ2tvaj6tmlhq1WMtjzuht6-g&usqp=CAU",
                        "https://images.newindianexpress.com/uploads/user/imagelibrary/2019/10/20/w900X450/North_DMC.jpg",
                        "https://www.holidify.com/images/cmsuploads/compressed/5621259188_e74d63cb05_b_20180302140149.jpg",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTPN24tOl0lMKEPR4zUT-Bq50GUnoIHcbym1Q&usqp=CAU",
                        "https://akm-img-a-in.tosshub.com/indiatoday/images/story/201704/647_042717101905.jpg?size=1200:675",
                        "https://i.pinimg.com/originals/14/71/9c/14719c601adb8096217055490cec3340.jpg",
                        "https://media.tacdn.com/media/attractions-splice-spp-674x446/07/bb/de/e9.jpg",
                        "https://www.nativeplanet.com/img/2015/10/09-1444387927-lotustemple1e.jpg",
                        "https://i.pinimg.com/originals/cd/c4/af/cdc4af4fcfa2d5ec3329d6c86cc13211.jpg",
                        "https://imgstaticcontent.lbb.in/lbbnew/wp-content/uploads/sites/1/2018/02/02182853/020218_RashtrapatiBhavan_01.jpg")
    }

    private fun getNameList(): List<String>{
        return listOf("Delhi Bus Terminal","Delhi Airport","New Delhi Railway Station",
                        "India Gate","Humayun's Tomb","Qutab Minar","Red Fort",
                        "Akshardham Temple","Bahai (Lotus) Temple","Jama Masjid","Rashtrapati Bhawan")
    }

    private fun showTransportDialog(image: String,title:String,price:String) {
        val dialog = context?.let { Dialog(it) }

        dialog?.run {

            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.dialog_transport)

            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setGravity(Gravity.CENTER)
            }

            tvTransportName.text = title
            tvDistancePerKm.text = price

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