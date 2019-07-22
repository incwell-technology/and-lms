package com.incwelltechnology.lms.util

import java.io.IOException

class NoInternetException(message:String) : IOException(message)

class NoTokenException(message:String): IOException(message)
