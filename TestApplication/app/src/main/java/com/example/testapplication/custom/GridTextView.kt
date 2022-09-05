package com.example.testapplication.custom

import android.app.Service
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.R
import java.lang.IllegalStateException


class GridTextView:RecyclerView {

    private var row:Int=7
    private var column:Int=7
    private var spaceSize=10
    private lateinit var recyclerViewAdapter:Adapter
    private lateinit var inputEditText: EditText

    constructor(context: Context):super(context){
        init()
    }



    constructor(context: Context,attrs:AttributeSet?):super(context, attrs){
        attrs?.let {
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.GridTextView)
            row=typeArray.getInt(R.styleable.GridTextView_row,7)
            column=typeArray.getInt(R.styleable.GridTextView_column,7)
            spaceSize=typeArray.getDimensionPixelSize(R.styleable.GridTextView_line_width,10)
            typeArray.recycle()
        }
        init()
    }


    private fun init(){
        recyclerViewAdapter=Adapter(row*column)
        this.adapter=recyclerViewAdapter
        this.layoutManager=GridLayoutManager(context,column)
        this.addItemDecoration(MarginItemDecoration(spaceSize,column))
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
        inputEditText= EditText(context)
        setEditText()





    }




    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        val cellWidth=(this.width-(column+1)*spaceSize)/column
        val cellHeight=(this.height-(row+1)*spaceSize)/row
        recyclerViewAdapter.setViewHolderHeight(cellHeight)
        recyclerViewAdapter.setViewHolderWidth(cellWidth)
    }

    fun setEditText(editText: EditText){
        inputEditText=editText
        setEditText()
    }


    private fun setEditText(){
        inputEditText.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputEditText.setSelection(inputEditText.length())
                val con=p0.toString()
                var result=""
                var x=0
                var y=0
                val gridTextList=Array(row){Array(column){" "} }
                run breaker@{
                    con.forEach { c ->

                        //엔터시 다음줄로
                        if(c=='\n'){
                            x=0
                            y+=1
                            return@forEach
                        }

                        //글자 가로 수 넘어갈 시 다음 줄로
                        if(x>=column){
                            x=0
                            y+=1
                        }

                        //
                        if(y>=row){
                            Log.d("testing","out of index")
                            inputEditText.setText(con.substring(0 until con.length-1))
                            return@breaker
                        }

                        gridTextList[y][x]=c.toString()
                        x+=1
                    }
                }



                gridTextList.forEach{
                    result+=it.joinToString("")
                }


                recyclerViewAdapter.setString(result)
                recyclerViewAdapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    inner class Adapter(private val itemNum:Int): RecyclerView.Adapter<ViewHolder>() {
        private var string=""
        private val viewHolderList= mutableListOf<ViewHolder>()
        private var viewHolderIndex=0
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val viewHolder=ViewHolder(TextView(context))
            viewHolderList.add(viewHolder)
            viewHolderIndex+=1
            viewHolder.view.setOnClickListener {
                Log.d("testing","clicked")
                inputEditText.isFocusableInTouchMode=true
                inputEditText.requestFocus()
                inputEditText.postDelayed({

                    val manager=context.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
                    manager.showSoftInput(inputEditText,0)
                },200)

            }
            return viewHolder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Log.d("testing","bind")
            if(position>=string.length){
                return
            }
            val oneLetter=string[position].toString()

            holder.view.text=oneLetter

        }

        override fun getItemCount(): Int {
            return itemNum
        }

        fun setViewHolderWidth(width: Int){
            viewHolderList.forEach{
                it.setWidth(width)
            }
        }

        fun setViewHolderHeight(height: Int){
            viewHolderList.forEach {
                it.setHeight(height)
            }
        }

        fun setString(text: String){
            string=text
            Log.d("testing set string","$string")
        }



    }


    inner class ViewHolder(val view: TextView) :RecyclerView.ViewHolder(view){
        init {
            view.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
            view.gravity=Gravity.CENTER
            view.maxLines=1
            view.inputType= InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
            view.imeOptions=EditorInfo.IME_ACTION_DONE
        }

        fun setWidth(width:Int){
            view.width=width
        }
        fun setHeight(height:Int){
            view.height=height
        }
    }

}