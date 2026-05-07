package org.opengamestudio

import java.io.File

fun masterClone(dir: String, url: String): Boolean {
    try {
        println("Master clone: dir='$dir' url='$url'")
        return true
    } catch (e: Exception) {
        println("Master clone failed: '$e'")
        return false
    }
}
