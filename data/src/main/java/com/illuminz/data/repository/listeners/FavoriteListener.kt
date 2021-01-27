package com.illuminz.data.repository.listeners

interface FavoriteListener {
    fun refreshFavoriteStatus(favoriteList: MutableList<FavoriteData>)
}