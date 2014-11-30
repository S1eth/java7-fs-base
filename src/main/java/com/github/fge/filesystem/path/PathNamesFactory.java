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

package com.github.fge.filesystem.path;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.InvalidPathException;
import java.util.Arrays;

@ParametersAreNonnullByDefault
public abstract class PathNamesFactory
{
    protected static final String[] NO_NAMES = new String[0];

    private final String rootSeparator;
    private final String separator;

    protected PathNamesFactory(final String rootSeparator,
        final String separator)
    {
        this.rootSeparator = rootSeparator;
        this.separator = separator;
    }

    protected abstract String[] rootAndNames(final String path);

    protected abstract String[] splitNames(final String namesOnly);

    protected abstract boolean isValidName(final String name);

    protected abstract boolean isSelf(final String name);

    protected abstract boolean isParent(final String name);

    protected abstract boolean isAbsolute(final PathNames pathNames);

    @Nonnull
    protected final PathNames toPathNames(final String path)
    {
        final String[] rootAndNames = rootAndNames(path);
        final String root = rootAndNames[0];
        final String namesOnly = rootAndNames[1];

        final String[] names = splitNames(namesOnly);

        for (final String name: names)
            if (!isValidName(name))
                throw new InvalidPathException(path,
                    "invalid path element: " + name);

        return new PathNames(root, names);
    }

    @Nonnull
    protected final PathNames normalize(final PathNames pathNames)
    {
        final String[] names = pathNames.names;
        final String[] newNames = new String[names.length];

        int index = 0;
        for (final String name: names) {
            if (isParent(name)) {
                if (index > 0)
                    index--;
                continue;
            }
            if (!isSelf(name))
                newNames[index++] = name;
        }

        return new PathNames(pathNames.root,
            index == 0 ? NO_NAMES : Arrays.copyOf(newNames, index));
    }

    /*
     * NOTE: throws OperationNotSupportedException if second is not absolute but
     * has a root
     */
    @Nonnull
    protected final PathNames resolve(final PathNames first,
        final PathNames second)
    {
        if (isAbsolute(second))
            return second;

        //noinspection VariableNotUsedInsideIf
        if (second.root != null)
            throw new UnsupportedOperationException();

        final String[] firstNames = first.names;
        final String[] secondNames = second.names;
        final int firstLen = firstNames.length;
        final int secondLen = secondNames.length;

        if (secondLen == 0)
            return first;

        final String[] newNames
            = Arrays.copyOf(firstNames, firstLen + secondLen);
        System.arraycopy(secondNames, 0, newNames, firstLen, secondLen);

        return new PathNames(first.root, newNames);
    }

    @Nonnull
    protected final PathNames resolveSibling(final PathNames first,
        final PathNames second)
    {
        final PathNames firstParent = first.parent();
        return firstParent == null ? second : resolve(firstParent, second);
    }

    @Nonnull
    protected final String toString(final PathNames pathNames)
    {
        final StringBuilder sb = new StringBuilder();
        final boolean hasRoot = pathNames.root != null;
        if (hasRoot)
            sb.append(pathNames.root);

        final String[] names = pathNames.names;
        final int len = names.length;
        if (len == 0)
            return sb.toString();

        if (hasRoot)
            sb.append(rootSeparator);
        sb.append(names[0]);

        for (int i = 1; i < len; i++)
            sb.append(separator).append(names[i]);

        return sb.toString();
    }
}
