package safi.doordashlite.api.dagger

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import safi.doordashlite.api.DiscoverRestaurantsApi
import javax.inject.Singleton


@Module
class ApiModule {

    companion object {
        private const val BASE_URL = "https://api.doordash.com"
    }

    @Provides
    @Singleton
    internal fun provideDiscoverRestaurantsApi(): DiscoverRestaurantsApi {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val okHttpClient = OkHttpClient()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.create(DiscoverRestaurantsApi::class.java)
    }
}