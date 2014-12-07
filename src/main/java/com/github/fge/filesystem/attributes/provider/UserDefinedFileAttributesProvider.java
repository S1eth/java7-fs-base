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

package com.github.fge.filesystem.attributes.provider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Objects;

@ParametersAreNonnullByDefault
public abstract class UserDefinedFileAttributesProvider<V extends UserDefinedFileAttributeView>
    implements FileAttributesProvider<V, Void>
{
    private final V view;

    protected UserDefinedFileAttributesProvider(final V view)
    {
        this.view = Objects.requireNonNull(view);
    }

    @Nonnull
    @Override
    public final V getAttributeView()
    {
        return view;
    }

    @Nullable
    @Override
    public final Void getAttributes()
        throws IOException
    {
        return null;
    }

    @Nullable
    @Override
    public final Object getAttributeByName(final String name)
        throws IOException
    {
        if (!view.list().contains(name))
            throw new IllegalStateException("how did I get there??");

        final ByteBuffer buf = ByteBuffer.allocate(view.size(name));
        view.read(name, buf);
        return buf.array();
    }

    @Override
    public final void setAttributeByName(final String name,
        @Nullable final Object value)
        throws IOException
    {
        if (!view.list().contains(name))
            throw new IllegalStateException("how did I get there??");

        if (value == null)
            throw new IllegalArgumentException("null value not allowed here");

        final Class<?> c = value.getClass();

        /*
         * See javadoc for UserDefinedFileAttributeView; argument can either
         * be a ByteBuffer or a byte array
         */
        // TODO: normally this is OK; ClassCastException is defined as thrown
        // in Files.setAttribute*()
        final ByteBuffer buf = ByteBuffer.class.isAssignableFrom(c)
            ? (ByteBuffer) value
            : ByteBuffer.wrap((byte[]) value);

        // TODO: check wrt .size(); normally, size is not fixed
        view.write(name, buf);
    }
}
