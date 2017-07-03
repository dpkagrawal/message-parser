package com.feresr.atlassianchat.DI

import com.feresr.atlassianchat.BuildConfig
import com.feresr.atlassianchat.finder.EmoticonFinder
import com.feresr.atlassianchat.finder.LinkFinder
import com.feresr.atlassianchat.finder.MentionFinder
import com.feresr.atlassianchat.networking.GoogleSearchEndpoints
import com.feresr.atlassianchat.networking.GoogleSearchInterceptor
import com.feresr.atlassianchat.parser.LinkParser
import com.feresr.atlassianchat.parser.Parser
import com.feresr.atlassianchat.parser.SimpleParser
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
class AppModule {

    @Provides
    fun provideGoogleSearchEndpoints(interceptor: GoogleSearchInterceptor): GoogleSearchEndpoints {
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .baseUrl(BuildConfig.BASE_URL)
                .build()

        return retrofit.create<GoogleSearchEndpoints>(GoogleSearchEndpoints::class.java)
    }

    @Provides
    @Singleton
    @Named("emoticon")
    fun provideEmoticonParser(emoticonFinder: EmoticonFinder): Parser {
        return SimpleParser(emoticonFinder, "emoticons")
    }

    @Provides
    @Singleton
    @Named("mention")
    fun provideMentionParser(mentionFinder: MentionFinder): Parser {
        return SimpleParser(mentionFinder, "mentions")
    }

    @Provides
    @Singleton
    @Named("link")
    fun provideLinkParser(linkFinder: LinkFinder, googleSearchEndpoints: GoogleSearchEndpoints): Parser {
        return LinkParser(linkFinder, googleSearchEndpoints)
    }

    @Provides
    fun provideParsers(@Named("emoticon") emoticonParser: Parser,
                       @Named("mention") mentionParser: Parser,
                       @Named("link") linkParser: Parser): ArrayList<Parser> {
        return arrayListOf(emoticonParser, mentionParser, linkParser)
    }
}