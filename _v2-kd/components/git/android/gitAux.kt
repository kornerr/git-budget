package org.opengamestudio

import org.eclipse.jgit.transport.*

fun gitCreds(): UsernamePasswordCredentialsProvider {
    return UsernamePasswordCredentialsProvider(
        GIT_STUB_USERNAME,
        hash
    )
}
