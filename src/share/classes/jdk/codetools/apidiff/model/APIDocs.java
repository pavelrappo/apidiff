/*
 * Copyright (c) 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package jdk.codetools.apidiff.model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

import jdk.codetools.apidiff.Log;

/**
 * A class that encapsulates the descriptions generated by javadoc in a file
 * for a module, package or type.
 */
public class APIDocs {
    /**
     * Reads a file generated by javadoc, and extracts the descriptions for
     * the declarations contained therein.
     *
     * <p>If the file does not exist, an empty object will be returned,
     * which returns {@code null} for {@link #getDescription()} and {@link #getDescription(String)},
     * and an empty map for {@link #getMemberDescriptions()}.
     *
     * @param log  a log to which any errors will be reported
     * @param file the file to be read
     *
     * @return an instance of {@code APIDocs} that contains the descriptions
     *      found in the file.
     */
    public static APIDocs read(Log log, Path file) {
        return read(new APIReader(log), file);

    }

    /**
     * Reads a file generated by javadoc, and extracts the descriptions for
     * the declarations contained therein.
     *
     * <p>If the file does not exist, an empty object will be returned,
     * which returns {@code null} for {@link #getDescription()} and {@link #getDescription(String)},
     * and an empty map for {@link #getMemberDescriptions()}.
     *
     * @param r    an API reader to read the file
     * @param file the file to be read
     *
     * @return an instance of {@code APIDocs} that contains the descriptions
     *      found in the file.
     */
    public static APIDocs read(APIReader r, Path file) {
        if (!Files.exists(file)) {
            return EMPTY;
        }

        r.read(file);
        return new APIDocs(r.getDeclarationNames(), r.getDescription(), r.getMemberDescriptions());
    }

    private final Map<String, String> declNames;
    private final String description;
    private final Map<String, String> memberDescriptions;

    private static final APIDocs EMPTY = new APIDocs(Collections.emptyMap(), null, Collections.emptyMap());

    private APIDocs(Map<String, String> declNames, String description, Map<String, String> memberDescriptions) {
        this.declNames = declNames;
        this.description = description;
        this.memberDescriptions = memberDescriptions;
    }

    /**
     * Returns the parts of the names for the top-level element defined in the file.
     * The keys for the names are {@code module}, {@code package} and {@code class}.
     *
     * @return a map containing the parts of the names
     */
    public Map<String, String> getDeclarationNames() {
        return declNames;
    }

    /**
     * Returns the description for the primary element declared in the file:
     * the module, page or type.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the description for a member declared in the file, or null if the member is not found.
     *
     * Only types contain members; modules and packages do not.
     * Members are identified by their signature.
     * The signature is either the name of the member, or {@code <init>} for a constructor,
     * followed by a comma-separated list of argument types enclosed in parentheses if the member
     * is a constructor or method.
     *
     * @param memberSignature the signature of the member
     * @return the description of the member
     */
    public String getDescription(String memberSignature) {
        return memberDescriptions.get(memberSignature);
    }

    /**
     * Returns a map containing the descriptions of all the members found in the file.
     *
     * @return a map of descriptions, indexed by signature
     */
    public Map<String,String> getMemberDescriptions() {
        return memberDescriptions;
    }
}
