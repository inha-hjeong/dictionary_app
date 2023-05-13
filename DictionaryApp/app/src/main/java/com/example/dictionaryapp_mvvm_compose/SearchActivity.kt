package com.example.dictionaryapp_mvvm_compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.dictionaryapp_mvvm_compose.common.util.TinyDB
import com.example.dictionaryapp_mvvm_compose.feature_dictionary.presentation.components.RecentSearchResult
import com.example.dictionaryapp_mvvm_compose.feature_dictionary.presentation.components.SearchBar
import com.example.dictionaryapp_mvvm_compose.feature_dictionary.presentation.viewmodels.DictionaryListViewModel
import com.example.dictionaryapp_mvvm_compose.ui.theme.DictionaryApp_MVVM_ComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DictionaryApp_MVVM_ComposeTheme {
                var dictionaryListViewModel =
                    ViewModelProvider(this@SearchActivity)[DictionaryListViewModel::class.java]
                BuildSearchActivity(
                    context = this@SearchActivity,
                    dictionaryListViewModel = dictionaryListViewModel
                )
            }
        }
    }
    
}
@Composable
fun BuildSearchActivity(context: Context,dictionaryListViewModel:DictionaryListViewModel){
    val loading = remember { mutableStateOf(true) }
    var recentList = remember { mutableStateListOf<String>() }
    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Dictionary",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                SearchBar(
                    viewModel = dictionaryListViewModel,
                    context = context
                ){word->
                    if(loading.value){
                        loading.value=false
                        loading.value=true
                    }else{
                        loading.value=true
                    }
                    Log.d("searchedItem Bool:",loading.value.toString())
                    Log.d("searchedItem","onclicked !")
                    Log.d("searchedItem",word)

                    if(dictionaryListViewModel.validate(word = word)){
                        var tinyDB = TinyDB(context)
                        var list = tinyDB.getListString("recentSearch")
                        list.add(word)
                        tinyDB.putListString("recentSearch", list)
                        val intent = Intent(context, MainActivity::class.java)
                        intent.putExtra("word", word)
                        context.startActivity(intent)
                    }else{
                        dictionaryListViewModel.isVisible.value=true
                    }
                }
                Row(modifier = Modifier
                    .padding(10.dp)){
                    Box (modifier = Modifier.padding(end=10.dp).fillMaxWidth().weight(4f)){Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Recent",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )}
                    Button(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                             TinyDB(context).clear()
                            recentList.clear()
                        }
                    ) {
                        Box(modifier = Modifier.padding(8.dp)) {
                            Icon(
                                Icons.Filled.Clear,
                                "contentDescription",
                                tint = Color.White
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth()
                        .padding(10.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if(loading.value) {
                        var tinyDB = TinyDB(context)
                        recentList = tinyDB.getListString("recentSearch").toMutableStateList()
                        recentList.reverse()
                        Log.d("searchedItem", recentList.toString())
                        if (recentList.size >= 6) {
                            recentList.subList(0, 5).iterator().forEach {
                                RecentSearchResult(
                                    word = it,
                                    context = context
                                )
                            }
                        }else{
                            recentList.iterator().forEach {
                                RecentSearchResult(
                                    word = it,
                                    context = context
                                )
                            }
                        }
                    }

                }
            }
        }
    )
}

