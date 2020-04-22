package com.mundocrativo.javier.fontdownloader.ui.main

import android.graphics.Typeface
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.provider.FontRequest
import androidx.core.provider.FontsContractCompat
import com.koolio.library.DownloadableFontList
import com.koolio.library.FontList
import com.mundocrativo.javier.fontdownloader.R
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var handler: Handler? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

        iniciaHandler()
        fontRequestTest()

        traeFontList()

    }

    //-- explicacion de tener la lista de las fonts que se pueden bajar


    //-- expicacion del manejo de las downloables fonts
    //---https://www.raywenderlich.com/213-custom-and-downloadable-fonts-on-android

    fun fontRequestTest(){

        val query = "name=Creepster"

        val request = FontRequest(
            "com.google.android.gms.fonts",
            "com.google.android.gms",
            query,
            R.array.com_google_android_gms_fonts_certs
        )


        FontsContractCompat.requestFont(
            context!!,
            request,
            object : FontsContractCompat.FontRequestCallback() {
                override fun onTypefaceRetrieved(typeface: Typeface?) {
                    Log.v("msg","Letra encontrada")
                    message.typeface = typeface
                    message.setText("Font Creepster: Is Awesome")
                }

                override fun onTypefaceRequestFailed(reason: Int) {
                    Log.e("msg","Error: reason=$reason")
                }
            },
            handler!!
        )


    }

    //--- exlicacion del manejo de handlerTread y handler

    fun iniciaHandler(){
        val handlerThread = HandlerThread("fonatsHandler")
        handlerThread.start()
        handler = Handler(handlerThread.looper)
    }

    //-- hay que usar un developer APIkey que se baja en https://developers.google.com/fonts/docs/developer_api
    //-- se restringio a solo android apps de la font library  Importante. Hay que restringirlo enn un futuro solo a las
    //-- aplicaciones que se van a usar


    //-- como hacer una libreria en github
    //--- https://medium.com/@sgkantamani/how-to-create-and-publish-an-android-library-f37bf715932


    fun traeFontList(){

        DownloadableFontList.requestDownloadableFontList(calbackFont, "xxxxxxxxxxxxxxxxxxxxx")



    }

    val calbackFont = object  : DownloadableFontList.FontListCallback{
        override fun onFontListRetrieved(fontList: FontList?) {

            //-- tenemos la font list
            val listaNames =fontList!!.fontArrayList
            listaNames.forEach {
                Log.v("msg","query=${it.getQueryString()} ")
            }
            Log.v("msg","total fonts=${listaNames.size}")


        }

        override fun onTypefaceRequestFailed(p0: Int) {

        }
    }


}
