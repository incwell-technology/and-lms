package com.incwelltechnology.lms.data.storage

import com.incwelltechnology.lms.data.model.User
import com.orhanobut.hawk.Hawk

interface SharedPref {
    companion object {
        fun save(key: String, credential: User) {
            Hawk.put(key, credential)
        }

        fun delete(key: String){
            Hawk.delete(key)
        }

        fun check(key: String): Boolean {
            return Hawk.contains(key)
        }

        fun get(key: String): User {
            return Hawk.get(key)
        }
    }
}