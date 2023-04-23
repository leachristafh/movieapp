package com.example.movieapp.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.movieapp.models.Movie
import com.example.movieapp.screens.defaultMovie

@Composable
fun MovieCard(
    movie: Movie = defaultMovie,
    onFavoriteClick: (Movie) -> Unit = {},
    onItemClick: (String) -> Unit = {},
    onDeleteClick: (Movie) -> Unit = {},
    /*
 Die Funktion übernimmt vier Parameter

movie of type Movie which is a data class representing a movie with its properties like title, director, year of release, plot summary, and images.
onFavoriteClick of type (Movie) -> Unit which is a lambda function that will be called when the favorite icon is clicked.
onItemClick of type (String) -> Unit which is a lambda function that will be called when the movie card is clicked.
onDeleteClick of type (Movie) -> Unit which is a lambda function that will be called when the delete icon is clicked.
 This code defines a @Composable function called MovieCard that takes in several parameters, including a Movie object, three callback functions
  (onFavoriteClick, onItemClick, and onDeleteClick), and default values for each parameter. */
) {
    var expandedState by remember {
        mutableStateOf(false)
    }
    val showDetails = remember { mutableStateOf(false)
    }
    val iconRotation = animateFloatAsState(
        targetValue = if (showDetails.value) 180f else 0f,
        animationSpec = tween(durationMillis = 300)
    )
    var deleteState by remember {
        mutableStateOf(false)
    }
/*
Inside the function, it declares a few variables using
remember and mutableStateOf functions.
These variables are used to maintain the state of the composable function.
 expandedState and showDetails are boolean variables, while iconRotation is a Float
 variable that is updated based on the value of showDetails.
deleteState is also a boolean variable that is initialized to false and is used to control whether the delete icon is displayed or not
 */
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                onItemClick(movie.id.toString())
            },
        shape = RoundedCornerShape(corner = CornerSize(15.dp)),
        elevation = 5.dp // add a shadow to the Card and give it a sense of depth.


    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth() //damit es immer ganz ausfüllt
            ) {
                val painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = movie.images[0])
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                        }).build()
                )
                val painterState = painter.state
                if (painterState is AsyncImagePainter.State.Loading) {
                    CircularProgressIndicator()
                }
                var favoriteState by remember {
                    mutableStateOf(movie.isFavorite)
                }
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painter,
                    contentDescription = "Movie Poster",
                    contentScale = ContentScale.Crop,
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Icon(
                            tint = MaterialTheme.colors.secondary,
                            imageVector = if (favoriteState) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Add to favorites",
                            modifier = Modifier
                                .clickable {
                                    favoriteState = !favoriteState
                                    onFavoriteClick(movie)
                                }
                        )
                        Icon(
                            tint = Color.White,
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Movie",
                            modifier = Modifier
                                .clickable {
                                    deleteState = !deleteState
                                    onDeleteClick(movie)
                                }
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(movie.title, style = MaterialTheme.typography.h6)
                Icon(
                    imageVector = if (expandedState) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                    contentDescription = "Show details",
                    modifier = Modifier
                        .rotate(iconRotation.value).size(36.dp)
                        .clickable(onClick = {
                            expandedState = !expandedState
                        })
                )
            }

            AnimatedVisibility(visible = expandedState) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(MaterialTheme.colors.surface)
                ) {
                    Text("Director: ", fontWeight = FontWeight.Bold)
                    Text(movie.director)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Release Date: ", fontWeight = FontWeight.Bold)
                    Text(movie.year)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Summary: ", fontWeight = FontWeight.Bold)
                    Text(movie.plot)
                }
            }
        }
    }
}
