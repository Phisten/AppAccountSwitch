package org.phisten.appaccountswitch

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.os.Environment
import java.io.File
import android.Manifest.permission
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import java.util.*


class MainActivity : AppCompatActivity()  {
    private val REQUEST_EXTERNAL_STORAGE: Int = 0

    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val lv : ListView = findViewById(org.phisten.appaccountswitch.R.id.list)
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.dst2)
                lv.adapter = ListExampleAdapter(this)
                //toast("app分身")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.dst1)
                lv.adapter = ListAdapter_AppConfig(this)
                //toast("app主要")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                lv.adapter = null
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        // 無權限，向使用者請求
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE), REQUEST_EXTERNAL_STORAGE)
        }


        val lv : ListView = findViewById(org.phisten.appaccountswitch.R.id.list)
        lv.adapter = ListExampleAdapter(this)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }


    private class ListExampleAdapter(context: Context) : BaseAdapter() {
        val mainContext : Context = context
        val sourceDirPath = "/storage/emulated/0/Download/Local/"
        val destinationDirPath = "/storage/emulated/999/Android/data/com.aniplex.kirarafantasia/files/"
        val copyList : List<String> = listOf("a.d","a.d2")
        val sourceDir = File(sourceDirPath)
        val savedataList = sourceDir.listFiles()
        internal var sList = savedataList

        private val mInflator: LayoutInflater
        init {
            this.mInflator = LayoutInflater.from(context)
        }

        override fun getCount(): Int {
            return sList.size
        }

        override fun getItem(position: Int): Any {
            return sList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            val view: View?
            val vh: ListRowHolder
            if (convertView == null) {
                view = this.mInflator.inflate(R.layout.list_row, parent, false)
                vh = ListRowHolder(view,sList[position].absolutePath + "/", destinationDirPath, copyList, mainContext)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as ListRowHolder
            }
            vh.label.text = sList[position].name
            return view
        }
    }
    private class ListRowHolder(row: View?,sourceDirPath: String,destinationDirPath: String,copyList: List<String>,context: Context) {
        public val label: TextView
        init {
            this.label = row?.findViewById<TextView>(R.id.label) as TextView
            row.setOnClickListener(View.OnClickListener {
                try {
                    println(sourceDirPath)
                    println(destinationDirPath)
                    println(copyList)
                    val sourceDir = File(sourceDirPath)
                    if (sourceDir.exists() && sourceDir.isDirectory) {
                        copyList.forEach {
                            fileName ->
                            val sourceFile = File(sourceDir.absolutePath + "/" + fileName)

                            //check srcFile Exists
                            if (sourceFile.exists()) {
                                println(sourceFile.absolutePath + "  :src Exists")

                                val destinationFile : File
                                if(destinationDirPath.substring(destinationDirPath.length-1) != "/")
                                {
                                    destinationFile = File(destinationDirPath + "/" +  fileName)
                                }else
                                {
                                    destinationFile = File(destinationDirPath + fileName)
                                }

                                //Delete destinationFile
                                if (destinationFile.exists()) {
                                    println(destinationFile.absolutePath + "  :dst Exists")
                                    val result = destinationFile.delete()
                                    println(result)
                                }

                                //Copy file
                                val copyResult = sourceFile.copyTo(destinationFile)
                                println(copyResult)
                            }
                        }
                        Toast.makeText(context,"OK", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    println(e)
                }
            })
        }
    }



    private class ListAdapter_AppConfig(context: Context) : BaseAdapter() {
        val mainContext : Context = context
        val sourceDirPath = "/storage/emulated/0/Download/Local/"
        val destinationDirPath = "/storage/emulated/0/Android/data/com.aniplex.kirarafantasia/files/"
        val copyList : List<String> = listOf("a.d","a.d2")

        val sourceDir = File(sourceDirPath)
        val savedataList = sourceDir.listFiles()
        internal var sList = savedataList

        private val mInflator: LayoutInflater
        init {
            this.mInflator = LayoutInflater.from(context)
        }

        override fun getCount(): Int {
            return sList.size
        }

        override fun getItem(position: Int): Any {
            return sList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            val view: View?
            val vh: ListRowHolder
            if (convertView == null) {
                view = this.mInflator.inflate(R.layout.list_row, parent, false)
                vh = ListRowHolder(view,sList[position].absolutePath + "/", destinationDirPath,copyList,mainContext)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as ListRowHolder
            }
            vh.label.text = sList[position].name
            //vh.label.text = sList[position].name
            return view
        }
    }
}