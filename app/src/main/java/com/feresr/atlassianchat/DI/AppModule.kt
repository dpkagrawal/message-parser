package com.feresr.atlassianchat.DI

import com.feresr.atlassianchat.finder.EmoticonFinder
import com.feresr.atlassianchat.finder.HTMLTitleFinder
import com.feresr.atlassianchat.finder.LinkFinder
import com.feresr.atlassianchat.finder.MentionFinder
import com.feresr.atlassianchat.provider.LinkNodeProvider
import com.feresr.atlassianchat.provider.NodeProvider
import com.feresr.atlassianchat.provider.SimpleNodeProvider
import com.feresr.atlassianchat.utils.TitleRetriever
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by feresr on 26/6/17.
 */
@Module
class AppModule {

    @Provides
    @Singleton
    @Named("emoticon")
    fun provideEmoticonFinder(emoticonFinder: EmoticonFinder): NodeProvider {
        return SimpleNodeProvider(emoticonFinder, "emoticons")
    }

    @Provides
    @Singleton
    @Named("mention")
    fun provideMentionFinder(mentionFinder: MentionFinder): NodeProvider {
        return SimpleNodeProvider(mentionFinder, "mentions")
    }

    @Provides
    @Singleton
    @Named("link")
    fun provideLinkFinder(linkFinder: LinkFinder, titleRetriever: TitleRetriever): NodeProvider {
        return LinkNodeProvider(linkFinder, titleRetriever)
    }

    @Provides
    fun provideTitleRetriever(): TitleRetriever {
        return TitleRetriever(OkHttpClient(), HTMLTitleFinder())
    }

    @Provides
    fun provideParsers(@Named("emoticon") emoticonNodeProvider: NodeProvider,
                       @Named("mention") mentionNodeProvider: NodeProvider,
                       @Named("link") linkNodeProvider: NodeProvider): Array<NodeProvider> {
        return arrayOf(emoticonNodeProvider, mentionNodeProvider, linkNodeProvider)
    }
}