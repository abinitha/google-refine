/*

Copyright 2011, Google Inc.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

    * Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
copyright notice, this list of conditions and the following disclaimer
in the documentation and/or other materials provided with the
distribution.
    * Neither the name of Google Inc. nor the names of its
contributors may be used to endorse or promote products derived from
this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,           
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY           
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/

package com.google.refine.importers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.json.JSONObject;

import com.google.refine.ProjectMetadata;
import com.google.refine.importing.ImportingJob;
import com.google.refine.importing.ImportingParser;
import com.google.refine.importing.ImportingUtilities;
import com.google.refine.model.Project;

abstract public class ImportingParserBase implements ImportingParser {
    final protected boolean useInputStream;
    
    protected ImportingParserBase(boolean useInputStream) {
        this.useInputStream = useInputStream;
    }
    
    @Override
    public void parse(Project project, ProjectMetadata metadata,
            ImportingJob job, List<JSONObject> fileRecords, String format,
            int limit, JSONObject options, List<Exception> exceptions) {
        
        for (JSONObject fileRecord : fileRecords) {
            try {
                parseOneFile(project, metadata, job, fileRecord, limit, options, exceptions);
            } catch (IOException e) {
                exceptions.add(e);
            }
            
            if (limit > 0 && project.rows.size() >= limit) {
                break;
            }
        }
    }
    
    public void parseOneFile(
        Project project,
        ProjectMetadata metadata,
        ImportingJob job,
        JSONObject fileRecord,
        int limit,
        JSONObject options,
        List<Exception> exceptions
    ) throws IOException {
        File file = ImportingUtilities.getFile(job, fileRecord);
        String fileSource = ImportingUtilities.getFileSource(fileRecord);
        if (useInputStream) {
            InputStream inputStream = new FileInputStream(file);
            try {
                parseOneFile(project, metadata, job, fileSource, inputStream, limit, options, exceptions);
            } finally {
                inputStream.close();
            }
        } else {
            Reader reader = ImportingUtilities.getFileReader(file, fileRecord);
            try {
                parseOneFile(project, metadata, job, fileSource, reader, limit, options, exceptions);
            } finally {
                reader.close();
            }
        }
    }
    
    public void parseOneFile(
        Project project,
        ProjectMetadata metadata,
        ImportingJob job,
        String fileSource,
        Reader reader,
        int limit,
        JSONObject options,
        List<Exception> exceptions
    ) {
        throw new NotImplementedException();
    }
    
    public void parseOneFile(
        Project project,
        ProjectMetadata metadata,
        ImportingJob job,
        String fileSource,
        InputStream inputStream,
        int limit,
        JSONObject options,
        List<Exception> exceptions
    ) {
        throw new NotImplementedException();
    }
}
