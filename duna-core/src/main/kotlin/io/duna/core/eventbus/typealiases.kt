package io.duna.core.eventbus

import org.eclipse.collections.api.multimap.Multimap
import java.util.function.Function

typealias Headers = Multimap<String, String>
typealias Pipe<V, R> = Function<Message<V>, Message<R>>
