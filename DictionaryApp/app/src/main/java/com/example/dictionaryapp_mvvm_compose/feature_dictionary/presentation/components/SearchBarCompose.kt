package com.example.dictionaryapp_mvvm_compose.feature_dictionary.presentation.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dictionaryapp_mvvm_compose.feature_dictionary.presentation.viewmodels.DictionaryListViewModel

@Composable
fun SearchTextField(text: String, onValueChange: (String) -> Unit, viewModel: DictionaryListViewModel) {
    TextField(
        value = text,
        onValueChange = {
            onValueChange(it)
            if(!viewModel.validate(word = it)){
                viewModel.isVisible.value=true
            }
        },
        trailingIcon = {
            if(!viewModel.validate(word = text) && viewModel.isVisible.value)
                Icon(Icons.Default.Warning,"error", tint = MaterialTheme.colors.error)
        },
        shape = RoundedCornerShape(16.dp),
        placeholder = {
            Text(text = "Search", style = MaterialTheme.typography.body1, color = Color.Gray)
        },
        colors = TextFieldDefaults.textFieldColors(

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun SearchBar(viewModel: DictionaryListViewModel,context:Context, onClick: (word:String)-> Unit){
    var text by remember{ mutableStateOf("") }
    Column(modifier =Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(horizontal = 10.dp)) {
            Box (modifier = Modifier.padding(end=10.dp).fillMaxWidth().weight(4f)){
                SearchTextField(text, { text = it }, viewModel)
            }
            Button(
                modifier = Modifier.fillMaxWidth().weight(1f),
                onClick = { onClick(text) },
                shape = RoundedCornerShape(16.dp),
            ) {
                Box(modifier = Modifier.padding(8.dp)) {
                    Icon(
                        Icons.Filled.Search,
                        "contentDescription",
                        tint = Color.White
                    )
                }
            }
        }
        if(!viewModel.validate(word = text)) {
            Log.d("errorMes2", viewModel.errorMessage.value.toString())
            if(viewModel.isVisible.value) {
                Text(modifier = Modifier
                    .padding(horizontal = 10.dp),
                    text = viewModel.errorMessage.value.toString(),
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
        }
    }
}
