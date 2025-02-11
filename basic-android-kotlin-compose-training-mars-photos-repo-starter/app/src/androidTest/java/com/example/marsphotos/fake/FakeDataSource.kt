package com.example.marsphotos.fake

import com.example.marsphotos.model.divisas

object FakeDataSource {

    const val idOne = "img1"
    const val idTwo = "img2"
    const val imgOne = "url.1"
    const val imgTwo = "url.2"
    val photosList = listOf(
        divisas(
            id = idOne,
            imgSrc = imgOne
        ),
        divisas(
            id = idTwo,
            imgSrc = imgTwo
        )
    )
}
