package com.mambo.mywaterpay

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

object Supabase {
    private const val SUPABASE_URL = "https://ccmqolurywherjxqwxpn.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_OjW81GtlfgYqblgxuWteNw_3TtY2RcU"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Auth)
        install(Postgrest)
    }
}
