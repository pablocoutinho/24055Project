package com.kharedji.onlineshopping.domain.models

import java.io.Serializable

class UsersData(
    var name: String? = "",
    var phoneNumber: String = "",
    var email: String = "",
    var address: String = "",
    var id:String=""
):Serializable