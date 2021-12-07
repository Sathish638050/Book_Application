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
        val listView = findViewById<ListView>(R.id.listView)

        filterBtn.setOnClickListener{
            var filtername  = name.editText?.text?.toString()
            var filtercountry = country.editText?.text?.toString()
            var filterData :MutableList<String> = ArrayList()

            if(postList != null){
                for(Book in postList)
                    if((!filtername.isNullOrEmpty()) && (!filtercountry.isNullOrEmpty())){
                        if((filtername.toString() in Book.author.toString()) && (filtercountry.toString() in Book.country.toString())){
                            filterData.add("Result: " + Book.title.toString())
                        }
                    }else if((filtername.isNullOrEmpty())){
                        if(!(filtercountry.isNullOrEmpty())){
                            if(filtercountry.toString() in Book.country.toString()){
                                filterData.add("Result: "+Book.title.toString())
                            }
                        }
                    }else if((filtercountry.isNullOrEmpty())){
                        if(!(filtername.isNullOrEmpty())){
                            if(filtername.toString() in Book.author.toString()){
                                filterData.add("Result: "+Book.title.toString())
                            }
                        }
                    }
            }
            if(filterData.size == 0){
                resultCount.visibility = View.VISIBLE
                resultCount.text = "Please enter author name and country"
                listView.visibility = View.INVISIBLE
            }else{
                var adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,filterData.take(3))
                listView.visibility = View.VISIBLE
                listView.adapter = adapter
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