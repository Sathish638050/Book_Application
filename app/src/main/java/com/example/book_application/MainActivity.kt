package com.example.book_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.text.StringBuilder

class MainActivity : AppCompatActivity() {

    var postList : List<BookDetail> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GetData();
        val name = findViewById<TextInputLayout>(R.id.nameFilter)
        val country = findViewById<TextInputLayout>(R.id.locationFilter)
        val filterBtn = findViewById<Button>(R.id.filterBtn)
        val resultCount = findViewById<TextView>(R.id.resultCount)
        val listView = findViewById<TextView>(R.id.listView)

        filterBtn.setOnClickListener{
            var filtername  = name.editText?.text?.toString()
            var filtercountry = country.editText?.text?.toString()
            var filterData : ArrayList<BookDetail> = ArrayList()
            if(postList != null){
                for(Book in postList){
                    if ((filtername != null) && (filtercountry != null)) {
                        resultCount.visibility = View.VISIBLE
                        if((filtername.isNotEmpty()) && (filtercountry.isNotEmpty())){
                            filterData = postList.filter { it.author.toString() in filtername.toString() } as ArrayList<BookDetail>
                        }else if(filtername.isNotEmpty()){
                            filterData = postList.filter { it.author.toString() in filtername.toString() } as ArrayList<BookDetail>
                        }else if(filtercountry.isNotEmpty()){
                            filterData = postList.filter { filtercountry.toString() in it.country.toString() } as ArrayList<BookDetail>
                        }
                    }
                }
            }
            if(filterData.size == 0){
                if((filtername != null) &&(filtercountry != null)){
                    if((filtername.isNotEmpty())&&(filtercountry.isNotEmpty())){
                        resultCount.visibility = View.VISIBLE
                        resultCount.text = "Not Found"
                        listView.visibility = View.INVISIBLE
                    }else{
                        resultCount.visibility = View.VISIBLE
                        resultCount.text = "Please enter author name and country"
                        listView.visibility = View.INVISIBLE
                    }
                }
            }else{
                listView.visibility = View.VISIBLE
                var result = ""
                val sb = StringBuilder()
                for(book in filterData){
                    sb.append("Result: "+book.title+"\n")
                }
                listView.setText(sb)
                resultCount.visibility = View.VISIBLE
                resultCount.text = "Result : " + filterData.size.toString()
            }
        }
    }

    private fun GetData() {

        var rf = Retrofit.Builder()
            .baseUrl(BookInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()

        var API =rf.create(BookInterface::class.java)
        var call =API.posts

        call?.enqueue(object:Callback<List<BookDetail?>?>{
            override fun onResponse(
                call: Call<List<BookDetail?>?>,
                response: Response<List<BookDetail?>?>
            ) {
                postList = response.body() as List<BookDetail>
            }

            override fun onFailure(call: Call<List<BookDetail?>?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}