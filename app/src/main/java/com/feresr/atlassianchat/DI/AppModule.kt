package com.feresr.atlassianchat.DI

import com.feresr.atlassianchat.LinkMapper
import com.feresr.atlassianchat.finders.EmoticonFinder
import com.feresr.atlassianchat.finders.LinkFinder
import com.feresr.atlassianchat.finders.MentionFinder
import com.feresr.atlassianchat.networking.GoogleSearchEndpoints
import com.feresr.atlassianchat.networking.GoogleSearchInterceptor
import com.feresr.parser.MessageParser
import com.feresr.parser.Parser
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideMessageParser(): MessageParser {
        return MessageParser()
    }

    @Provides
    fun provideGoogleSearchEndpoints(interceptor: GoogleSearchInterceptor): GoogleSearchEndpoints {
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(GoogleSearchEndpoints.BASE_URL)
                .build()

        return retrofit.create<GoogleSearchEndpoints>(GoogleSearchEndpoints::class.java)
    }

    @Provides
    @Singleton
    @Named("emoticon")
    fun provideEmoticonParser(emoticonFinder: EmoticonFinder): Parser {
        return Parser(emoticonFinder, "emoticons")
    }

    @Provides
    @Singleton
    @Named("mention")
    fun provideMentionParser(mentionFinder: MentionFinder): Parser {
        return Parser(mentionFinder, "mentions")
    }

    @Provides
    @Singleton
    @Named("link")
    fun provideLinkParser(linkFinder: LinkFinder, linkMapper: LinkMapper): Parser {
        return Parser(linkFinder, "links", linkMapper)
    }
}