package com.example.exo4_tdm2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_dialog.view.*
import kotlinx.android.synthetic.main.post_view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    var position:Int=0


    var itemList : MutableList<post> = ArrayList()
    lateinit var list : MutableList<Int>
    lateinit var adapter: RecyclerAdapter
    val REQUEST_PERMESSION=1
    lateinit var intent2: Intent
    lateinit var layoutManager : LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        intent2 = Intent(this, Main2Activity::class.java)
        list = allNote(itemList)
        val add=findViewById<Button>(R.id.addTacheBtnView)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter(this)
        recyclerView.adapter = adapter
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.INTERNET),REQUEST_PERMESSION)
        }
        else {
            call()
        }

        

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==REQUEST_PERMESSION){
            call()


        }

    }
    fun call(){
        val queue = Volley.newRequestQueue(this)
        val url = "https://jsonplaceholder.typicode.com/"
        val req=JsonObjectRequest(Request.Method.GET,url,null,
            com.android.volley.Response.Listener {
                    response ->   Toast.makeText(this@MainActivity, "Successfully Added", Toast.LENGTH_SHORT).show()

                val jsonArray = response.getJSONArray("posts")
                for (res:Int in 0..response.length()  )
                {
                    val objects: JSONObject =jsonArray.getJSONObject(res)

                    val titre =  objects.getString("title")
                    val description =  objects.getString("body")

                    val posts = post(titre,description)

                    itemList.add(posts)

                    update()
                }

            },
            com.android.volley.Response.ErrorListener {
                Toast.makeText(this@MainActivity, "fail", Toast.LENGTH_SHORT).show()


            }
            )
        queue.add(req)


    }
    fun update(){

        list.clear()
        list =allNote(itemList)
        adapter.notifyDataSetChanged()

    }
    fun allNote(lists : MutableList<post>):MutableList<Int>{
        var list : MutableList<Int> = ArrayList()
        for (i in 1 until lists.size){
            list.add(i)
        }
        return list
    }
}
