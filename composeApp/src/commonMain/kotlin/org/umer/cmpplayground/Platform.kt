package org.umer.cmpplayground

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform