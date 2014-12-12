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

import com.github.fge.filesystem.exceptions.ReadOnlyAttributeException;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("DesignForExtension")
@ParametersAreNonnullByDefault
public abstract class AclFileAttributesProvider
    extends FileAttributesProvider
    implements AclFileAttributeView
{
    protected AclFileAttributesProvider()
    {
        super("acl");
    }

    @Override
    public List<AclEntry> getAcl()
        throws IOException
    {
        return Collections.emptyList();
    }

    /*
     * read
     */

    /*
     * write
     */

    @Override
    public void setOwner(final UserPrincipal owner)
        throws IOException
    {
        throw new ReadOnlyAttributeException();
    }

    /*
     * by name
     */
    @Override
    public final void setAttributeByName(final String name, final Object value)
        throws IOException
    {
        Objects.requireNonNull(value);
        switch (Objects.requireNonNull(name)) {
            /* owner */
            case "owner":
                setOwner((UserPrincipal) value);
                break;
            /* acl */
            case "acl":
                //noinspection unchecked
                setAcl((List<AclEntry>) value);
                break;
            default:
                throw new IllegalStateException("how did I get there??");
        }
    }

    @Nullable
    @Override
    public final Object getAttributeByName(final String name)
        throws IOException
    {
        switch (Objects.requireNonNull(name)) {
            /* owner */
            case "owner":
                return getOwner();
            /* acl */
            case "acl":
                return getAcl();
            default:
                throw new IllegalStateException("how did I get there??");
        }
    }
}