package com.feresr.atlassianchat.DI

import com.feresr.atlassianchat.finder.EmoticonFinder
import com.feresr.atlassianchat.finder.HTMLTitleFinder
import com.feresr.atlassianchat.finder.LinkFinder
import com.feresr.atlassianchat.finder.MentionFinder
import com.feresr.atlassianchat.parser.LinkParser
import com.feresr.atlassianchat.parser.Parser
import com.feresr.atlassianchat.parser.SimpleParser
import com.feresr.atlassianchat.utils.TitleRetriever
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

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
    fun provideLinkParser(linkFinder: LinkFinder, titleRetriever: TitleRetriever): Parser {
        return LinkParser(linkFinder, titleRetriever)
    }

    @Provides
    fun provideTitleRetriever(): TitleRetriever {
        return TitleRetriever(OkHttpClient(), HTMLTitleFinder())
    }

    @Provides
    fun provideParsers(@Named("emoticon") emoticonParser: Parser,
                       @Named("mention") mentionParser: Parser,
                       @Named("link") linkParser: Parser): Array<Parser> {
        return arrayOf(emoticonParser, mentionParser, linkParser)
    }
}