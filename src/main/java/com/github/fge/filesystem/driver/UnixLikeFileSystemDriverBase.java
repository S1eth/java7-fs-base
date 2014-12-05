/*
 * Copyright (c) 2014, Francis Galiegue (fgaliegue@gmail.com)
 *
 * This software is dual-licensed under:
 *
 * - the Lesser General Public License (LGPL) version 3.0 or, at your option, any
 *   later version;
 * - the Apache Software License (ASL) version 2.0.
 *
 * The text of both licenses is available under the src/resources/ directory of
 * this project (under the names LGPL-3.0.txt and ASL-2.0.txt respectively).
 *
 * Direct link to the sources:
 *
 * - LGPL 3.0: https://www.gnu.org/licenses/lgpl-3.0.txt
 * - ASL 2.0: http://www.apache.org/licenses/LICENSE-2.0.txt
 */

package com.github.fge.filesystem.driver;

import com.github.fge.filesystem.path.UnixPathElementsFactory;
import com.github.fge.filesystem.path.matchers.PathMatcherProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.net.URI;
import java.nio.file.FileStore;
import java.nio.file.PathMatcher;

/**
 * A base abstract implementation of a {@link FileSystemDriver} for Unix-like
 * filesystems
 *
 * <p>In addition to the defaults defined in {@link FileSystemDriverBase}, this
 * abstract class defaults to a {@link UnixPathElementsFactory} for building
 * paths and a default {@link PathMatcherProvider} to provide {@link
 * PathMatcher} instances -- therefore bringing support for both {@code "glob"}
 * and {@code "regex"} path matchers.</p>
 */
@ParametersAreNonnullByDefault
public abstract class UnixLikeFileSystemDriverBase
    extends FileSystemDriverBase
{
    protected UnixLikeFileSystemDriverBase(final URI uri,
        final FileStore fileStore)
    {
        super(uri, new UnixPathElementsFactory(), fileStore,
            new PathMatcherProvider());
    }
}