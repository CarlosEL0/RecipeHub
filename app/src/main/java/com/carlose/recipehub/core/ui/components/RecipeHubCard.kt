package com.carlose.recipehub.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.carlose.recipehub.core.model.Recipe

@Composable
fun RecipeCard(
    recipe: Recipe,
    onRecipeClick: (Int) -> Unit,
    onFavoriteClick: () -> Unit = {onRecipeClick(recipe.id)},
    onCommentClick: () -> Unit = {onRecipeClick(recipe.id)}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onRecipeClick(recipe.id) },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "https://i.pravatar.cc/150?u=${recipe.authorId}",
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = recipe.authorName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {  }) {
                    Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.White)
                }
            }

            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = recipe.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color.DarkGray),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (recipe.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (recipe.isFavorite) Color.Red else Color.White
                    )
                }
                IconButton(onClick = onCommentClick) {
                    Icon(Icons.Outlined.Comment, contentDescription = null, tint = Color.White)
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Outlined.Share, contentDescription = null, tint = Color.White)
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${recipe.preparationTimeMinutes} min • ${recipe.portions} porciones",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = recipe.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = recipe.description,
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}